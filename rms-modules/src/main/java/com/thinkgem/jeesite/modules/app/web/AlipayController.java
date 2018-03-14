package com.thinkgem.jeesite.modules.app.web;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipayEncrypt;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StreamUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.app.entity.*;
import com.thinkgem.jeesite.modules.app.enums.AccountTypeEnum;
import com.thinkgem.jeesite.modules.app.enums.BookStatusEnum;
import com.thinkgem.jeesite.modules.app.enums.BuildingTypeEnum;
import com.thinkgem.jeesite.modules.app.enums.UpEnum;
import com.thinkgem.jeesite.modules.app.service.CustBindInfoService;
import com.thinkgem.jeesite.modules.app.util.JsonUtil;
import com.thinkgem.jeesite.modules.common.service.SmsService;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.entity.PhoneRecord;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.service.ContractBookService;
import com.thinkgem.jeesite.modules.contract.service.PhoneRecordService;
import com.thinkgem.jeesite.modules.contract.web.CommonBusinessController;
import com.thinkgem.jeesite.modules.entity.User;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.enums.HouseStatusEnum;
import com.thinkgem.jeesite.modules.inventory.enums.RoomStatusEnum;
import com.thinkgem.jeesite.modules.person.entity.Customer;
import com.thinkgem.jeesite.modules.person.service.CustomerService;
import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 支付宝租房同步Controller
 *
 * @author xiao
 * @version 2018-01-02
 */
@Controller
@RequestMapping(value = "${adminPath}/app/alipay")
public class AlipayController extends CommonBusinessController {

    private static final Logger logger = LoggerFactory.getLogger(AlipayController.class);

    @Autowired
    private PhoneRecordService phoneRecordService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustBindInfoService custBindInfoService;

    @Autowired
    private ContractBookService contractBookService;

    @Autowired
    private SmsService smsService;

    private AlipayClient alipayClient;
    public static String RESERVATION_URL;//预约看房SPI地址
    public static String AFFIRM_URL;//确认租约SPI地址
    public static String RECORD_URL;//拨号记录SPI地址
    public static String KA_CODE;
    public static String SPI_PRIVATE_KEY;//手机号解密
    public static String TP_PRIVATEKEY;//私钥
    public static String TP_OPENAPI_URL;//网关
    public static String TP_APPID;
    public static String COMPANY_NAME;//公司名称
    public static String FILE_ACCESS_DOMAN;//附件
    public static String ALIPAY_PUBLIC_KEY;//支付宝验签公钥
    public static String ALIPAY_ROOM_STORE_NO;//支付宝店铺编号

    @PostConstruct
    public void initParams() {
        Global global = Global.getInstance();
        COMPANY_NAME = global.getConfig("alipay.company.name");
        RESERVATION_URL = global.getConfig("alipay.reservation.url");
        AFFIRM_URL = global.getConfig("alipay.affirm.url");
        RECORD_URL = global.getConfig("alipay.phone.record.url");
        KA_CODE = global.getConfig("alipay.ka.code");
        SPI_PRIVATE_KEY = global.getConfig("alipay.spi.privatekey");
        TP_PRIVATEKEY = global.getConfig("alipay.private.signkey");
        TP_OPENAPI_URL = global.getConfig("alipay.open.api");
        TP_APPID = global.getConfig("alipay.app.id");
        FILE_ACCESS_DOMAN = global.getConfig("file.access.domain");
        ALIPAY_PUBLIC_KEY = global.getConfig("alipay.public.key");
        ALIPAY_ROOM_STORE_NO = global.getConfig("alipay.room.store.no");
        alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
    }

    /**
     * 基础信息维护
     */
    @RequiresPermissions("alipay:baseinfo:sync")
    @RequestMapping(value = "baseInfoSync")
    @ResponseBody
    public String baseInfoSync() {
        AlipayEcoRenthouseKaBaseinfoSyncRequest request = new AlipayEcoRenthouseKaBaseinfoSyncRequest();
        request.setBizContent("{\"ka_name\": \"" + COMPANY_NAME + "\"}");//第一次维护
        //        request.setBizContent("{\"ka_name\": \"" + COMPANY_NAME + "\"," +
        //                "\"ka_code\": \"" + KA_CODE + "\"}");//以后修改
        AlipayEcoRenthouseKaBaseinfoSyncResponse response = new AlipayEcoRenthouseKaBaseinfoSyncResponse();
        try {
            logger.info("AlipayEcoRenthouseKaBaseinfoSyncRequest is:{}", JSON.toJSONString(request));
            response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseKaBaseinfoSyncResponse is:{}", JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("baseInfoSync execute errors!", e);
        }
        if (response.isSuccess()) {
            return response.getKaCode();
        } else {
            return response.getMsg();
        }
    }

    /**
     * 基础信息获取
     */
    @RequiresPermissions("alipay:baseinfo:sync")
    @RequestMapping(value = "baseInfoQuery/{kaCode}")
    @ResponseBody
    public String baseInfoQuery(@PathVariable("kaCode") String kaCode) {
        AlipayEcoRenthouseKaBaseinfoQueryRequest request = new AlipayEcoRenthouseKaBaseinfoQueryRequest();
        request.setBizContent("{" + "\"ka_code\": \"" + kaCode + "\"" + "}");
        AlipayEcoRenthouseKaBaseinfoQueryResponse response = new AlipayEcoRenthouseKaBaseinfoQueryResponse();
        try {
            logger.info("AlipayEcoRenthouseKaBaseinfoQueryRequest is:{}", JSON.toJSONString(request));
            response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseKaBaseinfoQueryResponse is:{}", JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("baseInfoQuery execute errors!", e);
        }
        if (response.isSuccess()) {
            return response.getValid();
        } else {
            return response.getMsg();
        }
    }

    /**
     * 公寓运营商服务地址注册
     */
    @RequiresPermissions("alipay:baseinfo:sync")
    @RequestMapping(value = "serviceCreate/{type}")
    @ResponseBody
    public String serviceCreate(@PathVariable("type") String type) {
        String url = "";
        if ("1".equals(type)) {
            url = RESERVATION_URL;
        } else if ("2".equals(type)) {
            url = AFFIRM_URL;
        } else if ("3".equals(type)) {
            url = RECORD_URL;
        }
        AlipayEcoRenthouseKaServiceCreateRequest request = new AlipayEcoRenthouseKaServiceCreateRequest();
        request.setBizContent("{" +
                "\"address\": \"" + url + "\"," +
                "\"ka_code\": \"" + KA_CODE + "\"," +
                "\"type\": " + type +
                "}");
        AlipayEcoRenthouseKaServiceCreateResponse response = new AlipayEcoRenthouseKaServiceCreateResponse();
        try {
            logger.info("AlipayEcoRenthouseKaServiceCreateRequest is:{}", JSON.toJSONString(request));
            response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseKaServiceCreateResponse is:{}", JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("serviceCreate execute errors!", e);
        }
        if (response.isSuccess()) {
            return "success";
        } else {
            return response.getMsg();
        }
    }

    /**
     * 房间同步（集中式/分散式）
     */
    @RequiresPermissions("alipay:houseAndRoom:sync")
    @RequestMapping(value = "syncRoom/{roomId}")
    public String syncRoom(@PathVariable("roomId") String roomId, RedirectAttributes redirectAttributes) {
        Room room = roomService.get(roomId);
        House house = houseService.getHouseById(room.getHouse().getId());
        house.setPropertyProject(propertyProjectService.get(house.getPropertyProject().getId()));
        room.setHouse(house);
        boolean result;
        try {
            result = syncPropertyProject(house.getPropertyProject());
        } catch (AlipayApiException e) {
            logger.error("syncRoom property project errors {}, {}", house.getPropertyProject().getId(), e);
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋所属小区同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
        }

        //小区同步过或同步失败
        if (!result) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间所属小区同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
        }
        if (StringUtils.isBlank(room.getOrientation())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间没有选择朝向，请设置后再同步！");
            return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
        }
        if (StringUtils.isBlank(room.getAttachmentPath())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间没有上传图片，请上传后再同步！");
            return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
        }

        List<String> imageUrls;
        try {
            imageUrls = syncPictures(room.getAttachmentPath());
        } catch (Exception e) {
            logger.error("sync pictures error {}, {}", roomId, e);
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间图片同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
        }
        boolean flag = false;
        if (BuildingTypeEnum.DISPERSION.getValue().equals(house.getBuilding().getType())) {
            try {
                flag = syncDispersionRoom(room, imageUrls);
            } catch (AlipayApiException e) {
                addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, e.getMessage());
                return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
            }
        } else if (BuildingTypeEnum.CONCENTRATION.getValue().equals(house.getBuilding().getType())) {
            try {
                flag = syncConcentrationRoom(room, imageUrls);
            } catch (AlipayApiException e) {
                addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, e.getMessage());
                return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
            }
        }
        if (flag) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "房间同步支付宝成功！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间同步支付宝失败，请联系管理员！");
        }
        return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
    }

    /**
     * 房间的分散式同步
     */
    private boolean syncDispersionRoom(Room room, List<String> imageUrls) throws AlipayApiException {
        House house = room.getHouse();
        AlipayEcoRenthouseRoomDispersionSyncRequest request = new AlipayEcoRenthouseRoomDispersionSyncRequest();
        DispersedSynchronizeHousingModel model = new DispersedSynchronizeHousingModel();
        setCommonHouseRoomModel(model, room, house, buildingService.get(house.getBuilding().getId()), imageUrls);
        request.setBizContent(JSON.toJSONString(model));
        logger.info("AlipayEcoRenthouseRoomDispersionSyncRequest is:{}", JSON.toJSONString(request));
        AlipayEcoRenthouseRoomDispersionSyncResponse response = alipayClient.execute(request);
        logger.info("AlipayEcoRenthouseRoomDispersionSyncResponse is:{}", JSON.toJSONString(response));
        return processRoomSyncResult(response, room);
    }

    /**
     * 房间的集中式同步
     */
    private boolean syncConcentrationRoom(Room room, List<String> imageUrls) throws AlipayApiException {
        House house = room.getHouse();
        AlipayEcoRenthouseRoomConcentrationSyncRequest request = new AlipayEcoRenthouseRoomConcentrationSyncRequest();
        FocusSynchronizeHousingModel model = new FocusSynchronizeHousingModel();
        setCommonHouseRoomModel(model, room, house, buildingService.get(house.getBuilding().getId()), imageUrls);
        Room pr = new Room();
        pr.setBuildingType(BuildingTypeEnum.CONCENTRATION.getValue());
        pr.setOrientation(room.getOrientation());
        List<Room> tatalRooms = roomService.findList(pr);
        model.setAll_room_num(CollectionUtils.isNotEmpty(tatalRooms) ? String.valueOf(tatalRooms.size()) : "1");
        List<Room> canbeRentedRooms = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tatalRooms)) {
            for (Room m : tatalRooms) {
                if (RoomStatusEnum.RENT_FOR_RESERVE.getValue().equals(m.getRoomStatus())) {
                    canbeRentedRooms.add(m);
                }
            }
        }
        model.setCan_rent_num(CollectionUtils.isNotEmpty(canbeRentedRooms) ? String.valueOf(canbeRentedRooms.size()) : "1");
        request.setBizContent(JSON.toJSONString(model));
        logger.info("AlipayEcoRenthouseRoomConcentrationSyncRequest is:{}", JSON.toJSONString(request));
        AlipayEcoRenthouseRoomConcentrationSyncResponse response = alipayClient.execute(request);
        logger.info("AlipayEcoRenthouseRoomConcentrationSyncResponse is:{}", JSON.toJSONString(response));
        return processRoomSyncResult(response, room);
    }

    /**
     * 处理房间同步结果
     */
    private boolean processRoomSyncResult(AlipayResponse response, Room room) throws AlipayApiException {
        if (response.isSuccess()) {
            room.setAlipayStatus(1);
            room.setUp(UpEnum.UP.getValue());
            roomService.saveRoom(room);
            return true;
        } else {
            logger.error("sync room error ! roomId is {}, errorMsg is:{},subMsg is:{}", room.getId(), response.getMsg(), response.getSubMsg());
            throw new AlipayApiException(response.getSubMsg());
        }
    }

    /**
     * 房屋同步（分散式/集中式）
     */
    @RequiresPermissions("alipay:houseAndRoom:sync")
    @RequestMapping(value = "syncHouse/{houseId}")
    public String syncHouse(@PathVariable("houseId") String houseId, RedirectAttributes redirectAttributes) {
        House house = houseService.get(houseId);
        house.setPropertyProject(propertyProjectService.get(house.getPropertyProject().getId()));
        boolean result;
        try {
            result = syncPropertyProject(house.getPropertyProject());
        } catch (AlipayApiException e) {
            logger.error("sync property project errors {}, {}", house.getPropertyProject().getId(), e);
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋所属小区同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
        }
        //小区同步过或同步失败
        if (!result) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋所属小区同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
        }
        if (StringUtils.isBlank(house.getAttachmentPath())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋没有上传图片，请上传后再同步！");
            return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
        }
        List<String> imageUrls;//房屋图片
        try {
            imageUrls = syncPictures(house.getAttachmentPath());
        } catch (AlipayApiException e) {
            logger.error("sync pictures error! houseId is:{}", houseId, e);
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋图片同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
        }
        boolean flag = false;
        if (BuildingTypeEnum.DISPERSION.getValue().equals(house.getBuilding().getType())) {
            try {
                flag = syncDispersionHouse(house, imageUrls);
            } catch (AlipayApiException e) {
                addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, e.getMessage());
                return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
            }
        } else if (BuildingTypeEnum.CONCENTRATION.getValue().equals(house.getBuilding().getType())) {
            try {
                flag = syncConcentrationHouse(house, imageUrls);
            } catch (AlipayApiException e) {
                addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, e.getMessage());
                return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
            }
        }
        if (flag) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "房屋同步支付宝成功！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋同步支付宝失败，请联系管理员！");
        }
        return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
    }

    /**
     * 房屋的分散式同步
     */
    private boolean syncDispersionHouse(House house, List<String> imageUrls) throws AlipayApiException {
        AlipayEcoRenthouseRoomDispersionSyncRequest request = new AlipayEcoRenthouseRoomDispersionSyncRequest();
        DispersedSynchronizeHousingModel model = new DispersedSynchronizeHousingModel();
        setCommonHouseRoomModel(model, null, house, buildingService.get(house.getBuilding().getId()), imageUrls);
        request.setBizContent(JSON.toJSONString(model));
        logger.info("AlipayEcoRenthouseRoomDispersionSyncRequest is:{}", JSON.toJSONString(request));
        AlipayEcoRenthouseRoomDispersionSyncResponse response = alipayClient.execute(request);
        logger.info("AlipayEcoRenthouseRoomDispersionSyncResponse is:{}", JSON.toJSONString(response));
        return processHouseSyncResult(response, house);

    }

    /**
     * 房屋集中式同步（集中式只有单间）
     */
    private boolean syncConcentrationHouse(House house, List<String> imageUrls) throws AlipayApiException {
        AlipayEcoRenthouseRoomConcentrationSyncRequest request = new AlipayEcoRenthouseRoomConcentrationSyncRequest();
        FocusSynchronizeHousingModel model = new FocusSynchronizeHousingModel();
        setCommonHouseRoomModel(model, null, house, buildingService.get(house.getBuilding().getId()), imageUrls);
        model.setAll_room_num("1");
        model.setCan_rent_num("1");
        request.setBizContent(JSON.toJSONString(model));
        logger.info("AlipayEcoRenthouseRoomConcentrationSyncRequest is:{}", JSON.toJSONString(request));
        AlipayEcoRenthouseRoomConcentrationSyncResponse response = alipayClient.execute(request);
        logger.info("AlipayEcoRenthouseRoomConcentrationSyncResponse is:{}", JSON.toJSONString(response));
        return processHouseSyncResult(response, house);
    }

    /**
     * 处理房屋同步结果
     */
    private boolean processHouseSyncResult(AlipayResponse response, House house) throws AlipayApiException {
        if (response.isSuccess()) {
            house.setUp(UpEnum.UP.getValue());
            houseService.updateHouseAlipayStatus(house);
            return true;
        } else {
            logger.error("sync house error! houseId is : {},  responseMsg is:{},responseSubMsg is {}", house.getId(), response.getMsg(), response.getSubMsg());
            throw new AlipayApiException(response.getSubMsg());
        }
    }

    /**
     * 同步多张图片
     */
    private List<String> syncPictures(String attachmentPath) throws AlipayApiException {
        List<String> imageUrls = new ArrayList<>();
        String[] images = attachmentPath.split("\\|");
        for (int i = 1; i < images.length; i++) {
            imageUrls.add(syncPicture(images[i]));
        }
        return imageUrls;
    }

    /**
     * 同步单张图片
     */
    private String syncPicture(String path) throws AlipayApiException {
        byte[] data;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(FILE_ACCESS_DOMAN + path).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream inStream = conn.getInputStream();
            data = StreamUtils.getBytes(inStream);//得到图片的二进制数据
            inStream.close();
        } catch (IOException e) {
            logger.error("syncPicture HttpURLConnection errors!", e);
            throw new AlipayApiException(e.getMessage());
        }
        AlipayEcoRenthouseCommonImageUploadRequest request = new AlipayEcoRenthouseCommonImageUploadRequest();
        SyncHousingImgModel syncHousingImgModel = new SyncHousingImgModel();
        syncHousingImgModel.setFile_base(Base64.getEncoder().encodeToString(data));
        syncHousingImgModel.setFile_type("1");
        syncHousingImgModel.setIs_public(true);
        request.setBizContent(JSON.toJSONString(syncHousingImgModel));
        logger.info("AlipayEcoRenthouseCommonImageUploadRequest is:{}", JSON.toJSONString(request));
        AlipayEcoRenthouseCommonImageUploadResponse response = alipayClient.execute(request);
        logger.info("AlipayEcoRenthouseCommonImageUploadResponse is:{}", JSON.toJSONString(response));
        if (response.isSuccess()) {
            return response.getUrl();
        } else {
            logger.error("sync picture response fail! image path is: {}, {}", FILE_ACCESS_DOMAN + path, response.getMsg());
            throw new AlipayApiException("sync image fails!");
        }
    }

    /**
     * 同步小区（物业项目）
     */
    private boolean syncPropertyProject(PropertyProject project) throws AlipayApiException {
        AlipayEcoRenthouseCommunityInfoSyncRequest request = new AlipayEcoRenthouseCommunityInfoSyncRequest();
        SyncPropertyProjectModel sppm = new SyncPropertyProjectModel();
        sppm.setCity_code(project.getCityCode());
        sppm.setCity_name(project.getCityName());
        sppm.setDistrict_code(project.getDistrictCode());
        sppm.setDistrict_name(project.getDistrictName());
        sppm.setCommunity_name(project.getProjectName());
        sppm.setAddress(project.getProjectAddr());
        request.setBizContent(JSON.toJSONString(sppm));
        logger.info("AlipayEcoRenthouseCommunityInfoSyncRequest is:{}", JSON.toJSONString(request));
        AlipayEcoRenthouseCommunityInfoSyncResponse response = alipayClient.execute(request);
        logger.info("AlipayEcoRenthouseCommunityInfoSyncResponse is:{}", JSON.toJSONString(response));
        if (response.isSuccess()) {
            project.setAlipayStatus(Math.toIntExact(response.getStatus()));
            project.setCommReqId(response.getCommReqId());
            propertyProjectService.save(project);
            return true;
        } else {
            logger.error("sync property project error {}, {}", project.getId(), response.getMsg());
            throw new AlipayApiException(response.getSubMsg());
        }

    }

    /**
     * 房屋上架
     */
    @RequiresPermissions("alipay:houseAndRoom:sync")
    @RequestMapping(value = "upHouse/{houseId}")
    public String upHouse(@PathVariable("houseId") String houseId, RedirectAttributes redirectAttributes) {
        if (houseService.upDownHouse(houseId, UpEnum.UP.getValue())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "房屋上架成功！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋上架失败，请联系管理员！");
        }
        return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
    }

    /**
     * 房屋下架
     */
    @RequiresPermissions("alipay:houseAndRoom:sync")
    @RequestMapping(value = "downHouse/{houseId}")
    public String downHouse(@PathVariable("houseId") String houseId, RedirectAttributes redirectAttributes) {
        if (houseService.upDownHouse(houseId, UpEnum.DOWN.getValue())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "房屋下架成功！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋下架失败，请联系管理员！");
        }
        return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
    }

    /**
     * 房间上架
     */
    @RequiresPermissions("alipay:houseAndRoom:sync")
    @RequestMapping(value = "upRoom/{roomId}")
    public String upRoom(@PathVariable("roomId") String roomId, RedirectAttributes redirectAttributes) {
        if (roomService.upDownRoom(roomId, UpEnum.UP.getValue(), houseService.get(roomService.get(roomId).getHouse().getId()).getBuilding().getType())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "房间上架成功！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间上架失败，请联系管理员！");
        }
        return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
    }

    /**
     * 房间下架
     */
    @RequiresPermissions("alipay:houseAndRoom:sync")
    @RequestMapping(value = "downRoom/{roomId}")
    public String downRoom(@PathVariable("roomId") String roomId, RedirectAttributes redirectAttributes) {
        if (roomService.upDownRoom(roomId, UpEnum.DOWN.getValue(), houseService.get(roomService.get(roomId).getHouse().getId()).getBuilding().getType())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "房间下架成功！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间下架失败，请联系管理员！");
        }
        return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
    }

    /**
     * 预约看房
     */
    @RequestMapping(value = "reservation", method = RequestMethod.POST)
    @ResponseBody
    public String reservation(HttpServletRequest request) {
        logger.info("reservation request map is:{}", JSON.toJSONString(request.getParameterMap()));
        if (!checkSign(request)) {
            logger.error("check sign error", JsonUtil.object2Json(request.getParameterMap()));
            return "{\"code\":0}";
        }
        String bookPhone = request.getParameter("bookPhone");
        try {
            bookPhone = AlipayEncrypt.decryptContent(bookPhone, "AES", SPI_PRIVATE_KEY, "UTF-8");
        } catch (AlipayApiException e) {
            logger.error("AlipayEncrypt.decryptContent error {}", bookPhone, e);
            return "{\"code\":0}";
        }
        Customer customer = saveOrUpdateCustomer(bookPhone, request);
        saveOrUpdateBindInfo(customer.getId(), request);
        saveReservation(customer, request);
        return "{\"code\":1}";
    }

    private void saveReservation(Customer customer, HttpServletRequest request) {
        ContractBook record = new ContractBook();
        record.setCustomer(customer);
        record.setBookPhone(customer.getCellPhone());
        record.setBookDate(DateUtils.parseDate(request.getParameter("lookTime")));
        record.setBookStatus(BookStatusEnum.BOOK_APP.value());
        record.setSource("1");
        String roomCode = request.getParameter("roomCode");
        record.setHousingCode(roomCode);
        record.setHousingType(Integer.valueOf(request.getParameter("flatsTag")));
        record.setRemarks(request.getParameter("remark"));
        User salesUser;
        String projectName;
        String buildingName;
        String houseNo;
        String roomNo = "";
        if (roomCode.startsWith("R")) {
            Room room = roomService.getByNewId(roomCode.substring(1));
            projectName = propertyProjectService.get(room.getPropertyProject().getId()).getProjectName();
            buildingName = buildingService.get(room.getBuilding().getId()).getBuildingName();
            houseNo = houseService.get(room.getHouse().getId()).getHouseNo();
            roomNo = room.getRoomNo();
            record.setRoomId(room.getId());
            record.setHouseId(room.getHouse().getId());
            salesUser = room.getSalesUser();
        } else {
            House h = houseService.getByNewId(roomCode.substring(1));
            projectName = propertyProjectService.get(h.getPropertyProject().getId()).getProjectName();
            buildingName = buildingService.get(h.getBuilding().getId()).getBuildingName();
            houseNo = h.getHouseNo();
            record.setHouseId(h.getId());
            salesUser = h.getSalesUser();
        }
        if (salesUser != null) {
            record.setSalesId(salesUser.getId());
            record.setSalesName(salesUser.getName());
            //向该销售发送预约提示短信
            User user = UserUtils.get(salesUser.getId());
            String saleName = user.getName();
            String addressInfo = projectName + buildingName + "楼" + houseNo + "号" + (StringUtils.isNotEmpty(roomNo) ? roomNo + "室" : "");
            String content = saleName + "你好，姓名为" + customer.getTrueName() + "，手机号为" + customer.getCellPhone() + "的客户预约在" + request.getParameter("lookTime") + "看" + addressInfo + "，请提前联系用户做好带看准备。";
            logger.info("短信号码是：{}，短信内容是：{}。", user.getMobile(), content);
            smsService.sendSms(user.getMobile(), content);
        } else {
            logger.error("can not find salesUser data!");
        }
        contractBookService.save(record);
    }

    private void saveOrUpdateBindInfo(String customerId, HttpServletRequest request) {
        CustBindInfo info = custBindInfoService.getByCustomerIdAndType(customerId, AccountTypeEnum.ALIPAY.getValue());
        String aliUserId = request.getParameter("aliUserId");
        String zhimaOpenId = request.getParameter("zhimaOpenId");
        if (info == null) {
            info = new CustBindInfo();
            info.setAccount(aliUserId);
            info.setCustomerId(customerId);
            info.setAccountType(AccountTypeEnum.ALIPAY.getValue());
            info.setValid("0");
            custBindInfoService.save(info);
        } else if (!info.getAccount().equals(aliUserId)) {
            info.setAccount(aliUserId);
            custBindInfoService.save(info);
        }
        if (StringUtils.isNotBlank(zhimaOpenId)) {
            CustBindInfo zhimaInfo = custBindInfoService.getByCustomerIdAndType(customerId, AccountTypeEnum.ZHIMA.getValue());
            if (zhimaInfo == null) {
                info = new CustBindInfo();
                info.setAccount(zhimaOpenId);
                info.setCustomerId(customerId);
                info.setAccountType(AccountTypeEnum.ZHIMA.getValue());
                info.setValid("0");
                custBindInfoService.save(info);
            } else if (!zhimaInfo.getAccount().equals(aliUserId)) {
                info.setAccount(zhimaOpenId);
                custBindInfoService.save(info);
            }
        }
    }

    private Customer saveOrUpdateCustomer(String bookPhone, HttpServletRequest request) {
        String bookName = request.getParameter("bookName");
        String bookSex = request.getParameter("bookSex");
        Customer customer = customerService.findCustomerByTelNo(bookPhone);
        if (customer == null) {
            customer = new Customer();
        }
        customer.setGender(bookSex);
        customer.setTrueName(bookName);
        customer.setCellPhone(bookPhone);
        customerService.save(customer);
        return customer;
    }

    /**
     * 拨号记录
     */
    @RequestMapping(value = "phoneRecord", method = RequestMethod.POST)
    @ResponseBody
    public String phoneRecord(HttpServletRequest request) {
        logger.info("phoneRecord request map is:{}", JSON.toJSONString(request.getParameterMap()));
        if (!checkSign(request)) {
            logger.error("check sign error", JsonUtil.object2Json(request.getParameterMap()));
            return "{\"code\":0}";
        }
        PhoneRecord phoneRecord = buildPhoneRecord(request);
        phoneRecordService.save(phoneRecord);
        return "{\"code\":1}";
    }

    private PhoneRecord buildPhoneRecord(HttpServletRequest request) {
        PhoneRecord phoneRecord = new PhoneRecord();
        phoneRecord.setAliUserId(request.getParameter("aliUserId"));
        phoneRecord.setZhimaOpenId(request.getParameter("zhimaOpenId"));
        phoneRecord.setRoomCode(request.getParameter("roomCode"));
        phoneRecord.setFlatsTag(Integer.valueOf(request.getParameter("flatsTag")));
        phoneRecord.setRecordTime(DateUtils.parseDate(request.getParameter("recordTime")));
        if (phoneRecord.getRoomCode().startsWith("R")) {
            Room room = (Room) buildRoomByRoomCode(phoneRecord.getRoomCode());
            phoneRecord.setProjectId(room.getPropertyProject().getId());
            phoneRecord.setBuildingId(room.getBuilding().getId());
            phoneRecord.setHouseId(room.getHouse().getId());
            phoneRecord.setRoomId(room.getId());
        } else {
            House house = (House) buildRoomByRoomCode(phoneRecord.getRoomCode());
            phoneRecord.setProjectId(house.getPropertyProject().getId());
            phoneRecord.setBuildingId(house.getBuilding().getId());
            phoneRecord.setHouseId(house.getId());
        }
        return phoneRecord;
    }

    private Object buildRoomByRoomCode(String roomCode) {
        if (roomCode.startsWith("R")) {
            return roomService.getByNewId(roomCode.substring(1));
        } else {
            return houseService.getByNewId(roomCode.substring(1));
        }
    }

    private boolean checkSign(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV2(params, ALIPAY_PUBLIC_KEY, "UTF-8");
        } catch (AlipayApiException e) {
            logger.error("signVerified error ", e);
        }
        return signVerified;
    }

    /**
     * 参数设置
     */
    private void setCommonHouseRoomModel(BaseSyncHousingModel model, Room room, House house, Building building, List<String> imageUrls) {
        String commonGoodsConfig;//房源物品配置
        User salesUser;//跟进销售
        if (room != null) {
            model.setRoom_code("R" + room.getNewId());
            model.setRoom_name(room.getRoomNo());
            if (StringUtils.isNotEmpty(room.getOrientation())) {
                model.setRoom_face(String.valueOf(Integer.valueOf(room.getOrientation().split(",")[0]) + 1));
            }
            model.setRoom_area(room.getRoomSpace());
            String roomStatus = room.getRoomStatus();
            if (RoomStatusEnum.BE_RESERVED.getValue().equals(roomStatus) || RoomStatusEnum.RENTED.getValue().equals(roomStatus)) {
                model.setRent_status("2");//已租
            } else {
                model.setRent_status("1");//未租
            }
            model.setIntro(room.getShortDesc() + " " + room.getShortLocation());
            commonGoodsConfig = room.getRoomConfig();
            Integer rentMonthGap = room.getRentMonthGap();
            model.setPay_type(rentMonthGap == null || rentMonthGap == 0 ? "0" : String.valueOf(rentMonthGap));
            Double rental = room.getRental();
            model.setRoom_amount(rental == null ? "0" : String.valueOf(rental));
            Integer deposMonthCounts = room.getDeposMonthCount();
            model.setForegift_amount(rental != null && deposMonthCounts != null && deposMonthCounts != 0 ? String.valueOf(rental * deposMonthCounts) : "0");
            model.setOwners_tel(room.getReservationPhone());
            model.setRent_type(String.valueOf(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()) + 1));
            if (StringUtils.isNotEmpty(room.getFeeConfigInfo())) {
                model.setOther_amount(convertFeeToList(room.getFeeConfigInfo()).toArray(new BaseSyncHousingModel.AlipayEcoRenthouseOtherAmount[]{}));
            }
            salesUser = room.getSalesUser();
        } else {
            model.setRoom_code("H" + house.getNewId());
            String houseStatus = house.getHouseStatus();
            if (HouseStatusEnum.BE_RESERVED.getValue().equals(houseStatus) || HouseStatusEnum.PART_RENT.getValue().equals(houseStatus) || HouseStatusEnum.WHOLE_RENT.getValue().equals(houseStatus)) {
                model.setRent_status("2");//已租
            } else {
                model.setRent_status("1");//未租
            }
            model.setRoom_area(house.getDecorationSpance());
            commonGoodsConfig = house.getShareAreaConfig();
            model.setIntro(house.getShortDesc() + " " + house.getShortLocation());
            Integer rentMonthGap = house.getRentMonthGap();
            model.setPay_type(rentMonthGap == null || rentMonthGap == 0 ? "0" : String.valueOf(rentMonthGap));
            Double rental = house.getRental();
            model.setRoom_amount(rental == null ? "0" : String.valueOf(rental));
            Integer deposMonthCounts = house.getDeposMonthCount();
            model.setForegift_amount(rental != null && deposMonthCounts != null && deposMonthCounts != 0 ? String.valueOf(rental * deposMonthCounts) : "0");
            model.setOwners_tel(house.getReservationPhone());
            model.setRent_type(String.valueOf(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()) + 1));
            if (StringUtils.isNotEmpty(house.getFeeConfigInfo())) {
                model.setOther_amount(convertFeeToList(house.getFeeConfigInfo()).toArray(new BaseSyncHousingModel.AlipayEcoRenthouseOtherAmount[]{}));
            }
            salesUser = house.getSalesUser();
        }
        model.setComm_req_id(house.getPropertyProject().getCommReqId());

        Integer houseFloor = house.getHouseFloor();
        model.setFloor_count(houseFloor == null || houseFloor == 0 ? "0" : String.valueOf(houseFloor));

        Integer totalFloorCount = building.getTotalFloorCount();
        model.setTotal_floor_count(totalFloorCount == null || totalFloorCount == 0 ? String.valueOf(houseFloor) : String.valueOf(totalFloorCount));

        model.setFlat_building(building.getBuildingName());

        model.setRoom_num(house.getHouseNo());

        //对于集中式单间同步，由于目前集中式单间的房屋没有维护正确，暂时用1房1厅1卫代替，等以后有集中式房屋，再改回来。
        //对于集中式公寓面积暂时传为单间面积，等以后有集中式房屋，再改回来。
        if (BuildingTypeEnum.CONCENTRATION.getValue().equals(building.getType())) {
            model.setBedroom_count("1");
            model.setParlor_count("1");
            model.setToilet_count("1");
            model.setFlat_area(room.getRoomSpace());
        } else {
            Integer decoraStrucRoomNum = house.getDecoraStrucRoomNum();
            model.setBedroom_count(decoraStrucRoomNum == null || decoraStrucRoomNum == 0 ? "0" : String.valueOf(decoraStrucRoomNum));
            Integer decoraStrucCusspacNum = house.getDecoraStrucCusspacNum();
            model.setParlor_count(decoraStrucCusspacNum == null || decoraStrucCusspacNum == 0 ? "0" : String.valueOf(decoraStrucCusspacNum));
            Integer decoraStrucWashroNum = house.getDecoraStrucWashroNum();
            model.setToilet_count(decoraStrucWashroNum == null || decoraStrucWashroNum == 0 ? "0" : String.valueOf(decoraStrucWashroNum));
            model.setFlat_area(house.getDecorationSpance());
        }

        if (StringUtils.isNotEmpty(house.getShareAreaConfig())) {
            List<String> targetArrayList = new ArrayList<>();
            for (String s : house.getShareAreaConfig().split(",")) {
                targetArrayList.add(String.valueOf(Integer.valueOf(s) + 1));
            }
            model.setFlat_configs(targetArrayList.toArray(new String[]{}));
        }

        if (StringUtils.isNotEmpty(commonGoodsConfig)) {
            List<String> targetArrayList = new ArrayList<>();
            for (String s : commonGoodsConfig.split(",")) {
                targetArrayList.add(String.valueOf(Integer.valueOf(s) + 1));
            }
            model.setRoom_configs(targetArrayList.toArray(new String[]{}));
        }

        model.setImages(imageUrls.toArray(new String[]{}));

        model.setCheckin_time(DateUtils.formatDate(DateUtils.dateAddDay(new Date(), 1)));

        model.setRoom_status(String.valueOf(UpEnum.UP.getValue()));

        model.setNick_name(building.getNickName());

        model.setMax_amount(building.getMaxAmount());

        model.setOwners_name(salesUser != null ? salesUser.getName() : "");

        model.setRoom_store_no(ALIPAY_ROOM_STORE_NO);
    }
}
