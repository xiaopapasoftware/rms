package com.thinkgem.jeesite.modules.app.web;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipayEncrypt;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.service.ServiceException;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StreamUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.alipay.AlipayConfig;
import com.thinkgem.jeesite.modules.app.entity.CustBindInfo;
import com.thinkgem.jeesite.modules.app.enums.AccountTypeEnum;
import com.thinkgem.jeesite.modules.app.enums.BookStatusEnum;
import com.thinkgem.jeesite.modules.app.enums.BuildingTypeEnum;
import com.thinkgem.jeesite.modules.app.enums.UpEnum;
import com.thinkgem.jeesite.modules.app.service.CustBindInfoService;
import com.thinkgem.jeesite.modules.app.util.JsonUtil;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.entity.PhoneRecord;
import com.thinkgem.jeesite.modules.contract.service.ContractBookService;
import com.thinkgem.jeesite.modules.contract.service.PhoneRecordService;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.enums.HouseStatusEnum;
import com.thinkgem.jeesite.modules.inventory.enums.RoomStatusEnum;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.person.entity.Customer;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import com.thinkgem.jeesite.modules.person.service.CustomerService;
import com.thinkgem.jeesite.modules.person.service.OwnerService;
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
import java.util.Base64.Encoder;

/**
 * 支付宝Controller
 *
 * @author xiao
 * @version 2018-01-02
 */
@Controller
@RequestMapping(value = "${adminPath}/app/alipay")
public class AlipayController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AlipayController.class);

    @Autowired
    private HouseService houseService;

    @Autowired
    private PropertyProjectService projectService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private PhoneRecordService phoneRecordService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustBindInfoService custBindInfoService;

    @Autowired
    private ContractBookService contractBookService;

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
    }

    /**
     * 基础信息维护
     */
    @RequiresPermissions("alipay:baseinfo:sync")
    @RequestMapping(value = "baseInfoSync")
    @ResponseBody
    public String baseInfoSync() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
        AlipayEcoRenthouseKaBaseinfoSyncRequest request = new AlipayEcoRenthouseKaBaseinfoSyncRequest();
        request.setBizContent("{\"ka_name\": \"" + COMPANY_NAME + "\"," +
                "\"ka_code\": \"" + KA_CODE + "\"}");
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
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
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
    public String serviceCreate(@PathVariable("type") String type) throws AlipayApiException {
        String url = "";
        if ("1".equals(type)) {
            url = RESERVATION_URL;
        } else if ("2".equals(type)) {
            url = AFFIRM_URL;
        } else if ("3".equals(type)) {
            url = RECORD_URL;
        }
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
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
        if (house.getOwner() == null) {
            List<Owner> ownerList = ownerService.findByHouse(house);
            if (CollectionUtils.isEmpty(ownerList)) {
                addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间所属房屋没有业主信息，请联系管理员！");
                return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
            }
            house.setOwner(ownerList.get(0));
        }
        room.setHouse(house);
        house.setPropertyProject(projectService.get(house.getPropertyProject().getId()));
        //小区同步过或同步失败
        if (StringUtils.isBlank(house.getPropertyProject().getCommReqId()) && !syncPropertyProject(house.getPropertyProject())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间所属小区同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
        }
        if (StringUtils.isBlank(room.getOrientation())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间没有选择朝向，请上传后再同步！");
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
            } catch (ServiceException e) {
                addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, e.getMessage());
                return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
            }
        } else if (BuildingTypeEnum.CONCENTRATION.getValue().equals(house.getBuilding().getType())) {
            try {
                flag = syncConcentrationRoom(room, imageUrls);
            } catch (ServiceException e) {
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
    private boolean syncDispersionRoom(Room room, List<String> imageUrls) {
        House house = room.getHouse();
        Building building = buildingService.get(house.getBuilding().getId());
        Owner owner = ownerService.get(room.getHouse().getOwner().getId());
        int rentStatus = 2;
        if (RoomStatusEnum.RENT_FOR_RESERVE.getValue().equals(room.getRoomStatus())) {
            rentStatus = 1;
        }
        StringBuilder sb = new StringBuilder("");
        for (String imageUrl : imageUrls) {
            sb.append(imageUrl).append("\",\"");
        }
        StringBuilder shareAreaConfigs = new StringBuilder("");
        for (String s : house.getShareAreaConfig().split(",")) {
            shareAreaConfigs.append(Integer.valueOf(s) + 1).append("\",\"");
        }
        String flatConfigs = "";
        if (shareAreaConfigs.length() > 0) {
            flatConfigs = shareAreaConfigs.delete(shareAreaConfigs.lastIndexOf(","), shareAreaConfigs.length()).toString();
        }
        StringBuilder roomConfig = new StringBuilder("");
        for (String s : room.getRoomConfig().split(",")) {
            roomConfig.append(Integer.valueOf(s) + 1).append("\",\"");
        }
        String roomConfigs = "";
        if (roomConfig.length() > 0) {
            roomConfigs = roomConfig.delete(roomConfig.lastIndexOf(","), roomConfig.length()).toString();
        }
        Integer face = Integer.valueOf(room.getOrientation().split(",")[0]) + 1;
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
        AlipayEcoRenthouseRoomDispersionSyncRequest request = new AlipayEcoRenthouseRoomDispersionSyncRequest();
        request.setBizContent("{" +
                "    \"comm_req_id\": \"" + house.getPropertyProject().getCommReqId() + "\"," +
                "    \"room_code\": \"R" + room.getNewId() + "\"," +
                "    \"floor_count\": " + house.getHouseFloor() + "," +
                "    \"total_floor_count\": \"" + building.getTotalFloorCount() + "\"," +
                "    \"flat_building\": \"" + building.getBuildingName() + "\"," +
                "    \"room_num\": \"" + house.getHouseNo() + "\"," +
                "    \"room_name\": \"" + room.getRoomNo() + "\"," +
                "    \"room_face\": " + face + "," +
                "    \"bedroom_count\": " + house.getDecoraStrucRoomNum() + "," +
                "    \"parlor_count\": " + house.getDecoraStrucCusspacNum() + "," +
                "    \"toilet_count\": " + house.getDecoraStrucWashroNum() + "," +
                "    \"flat_area\": \"" + house.getDecorationSpance() + "\"," +
                "    \"room_area\": \"" + room.getRoomSpace() + "\"," +
                "    \"rent_status\": " + rentStatus + "," +
                "    \"intro\": \"" + room.getShortDesc() + "\"," +
                "    \"flat_configs\":[\"" + flatConfigs + "]" + "," +
                "    \"room_configs\":[\"" + roomConfigs + "]" + "," +
                "    \"pay_type\": " + room.getRentMonthGap() + "," +
                "    \"room_amount\": " + room.getRental() + "," +
                "    \"foregift_amount\": " + (room.getRental() * room.getDeposMonthCount()) + "," +
                "    \"images\":[\"" + sb.delete(sb.lastIndexOf(","), sb.length()).toString() + "]," +
                "    \"owners_name\": \"" + owner.getName() + "\"," +
                "    \"owners_tel\": \"" + room.getReservationPhone() + "\"," +
                "    \"checkin_time\": \"" + DateUtils.formatDate(new Date()) + "\"," +
                "    \"room_status\": " + 1 + "," +
                "    \"rent_type\": " + (Integer.valueOf(house.getIntentMode()) + 1) +
                "}");
        try {
            logger.info("AlipayEcoRenthouseRoomDispersionSyncRequest is:{}", JSON.toJSONString(request));
            AlipayEcoRenthouseRoomDispersionSyncResponse response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseRoomDispersionSyncResponse is:{}", JSON.toJSONString(response));
            if (response.isSuccess()) {
                room.setAlipayStatus(1);
                room.setUp(UpEnum.UP.getValue());
                roomService.saveRoom(room);
                return true;
            } else {
                logger.error("sync room error {}, {}", room.getId(), response.getMsg());
                throw new ServiceException(response.getMsg());
            }
        } catch (Exception e) {
            logger.error("sync room error {}", room.getId(), e);
            throw new ServiceException(e);
        }
    }

    /**
     * 房间的集中式同步
     */
    private boolean syncConcentrationRoom(Room room, List<String> imageUrls) {
        House house = room.getHouse();
        Building building = buildingService.get(house.getBuilding().getId());
        Owner owner = ownerService.get(room.getHouse().getOwner().getId());
        int rentStatus = 2;
        if (RoomStatusEnum.RENT_FOR_RESERVE.getValue().equals(room.getRoomStatus())) {
            rentStatus = 1;
        }
        StringBuilder sb = new StringBuilder("");
        for (String imageUrl : imageUrls) {
            sb.append(imageUrl).append("\",\"");
        }
        StringBuilder roomConfig = new StringBuilder("");
        for (String s : room.getRoomConfig().split(",")) {
            roomConfig.append(Integer.valueOf(s) + 1).append("\",\"");
        }
        String roomConfigs = "";
        if (roomConfig.length() > 0) {
            roomConfigs = roomConfig.delete(roomConfig.lastIndexOf(","), roomConfig.length()).toString();
        }
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
        AlipayEcoRenthouseRoomConcentrationSyncRequest request = new AlipayEcoRenthouseRoomConcentrationSyncRequest();
        request.setBizContent("{" +
                "    \"comm_req_id\": \"" + house.getPropertyProject().getCommReqId() + "\"," +
                "    \"room_code\": \"R" + room.getNewId() + "\"," +
                "    \"floor_count\": " + house.getHouseFloor() + "," +
                "    \"total_floor_count\": \"" + building.getTotalFloorCount() + "\"," +
                "    \"bedroom_count\": " + house.getDecoraStrucRoomNum() + "," +
                "    \"parlor_count\": " + house.getDecoraStrucCusspacNum() + "," +
                "    \"toilet_count\": " + house.getDecoraStrucWashroNum() + "," +
                "    \"flat_area\": \"" + house.getDecorationSpance() + "\"," +
                "    \"room_area\": \"" + room.getRoomSpace() + "\"," +
                "    \"rent_status\": " + rentStatus + "," +
                "    \"intro\": \"" + room.getShortDesc() + "\"," +
                "    \"nick_name\": \"" + building.getNickName() + "\"," +
                "    \"max_amount\": \"" + building.getMaxAmount() + "\"," +
                "    \"room_configs\":[\"" + roomConfigs + "]," +
                "    \"pay_type\": " + room.getRentMonthGap() + "," +
                "    \"room_amount\": " + room.getRental() + "," +
                "    \"foregift_amount\": " + (room.getRental() * room.getDeposMonthCount()) + "," +
                "    \"images\":[\"" + sb.delete(sb.lastIndexOf(","), sb.length()).toString() + "]," +
                "    \"owners_name\": \"" + owner.getName() + "\"," +
                "    \"owners_tel\": \"" + room.getReservationPhone() + "\"," +
                "    \"checkin_time\": \"" + DateUtils.formatDate(new Date()) + "\"," +
                "    \"room_status\": " + 1 + "," +
                "    \"rent_type\": " + (Integer.valueOf(house.getIntentMode()) + 1) +
                "}");
        try {
            logger.info("AlipayEcoRenthouseRoomConcentrationSyncRequest is:{}", JSON.toJSONString(request));
            AlipayEcoRenthouseRoomConcentrationSyncResponse response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseRoomConcentrationSyncResponse is:{}", JSON.toJSONString(response));
            if (response.isSuccess()) {
                room.setAlipayStatus(1);
                room.setUp(UpEnum.UP.getValue());
                roomService.saveRoom(room);
                return true;
            } else {
                logger.error("sync room error {}, {}", room.getId(), response.getMsg());
                throw new ServiceException(response.getMsg());
            }
        } catch (Exception e) {
            logger.error("sync room error {}", room.getId(), e);
            throw new ServiceException(e);
        }
    }

    /**
     * 房屋同步（分散式/集中式）
     */
    @RequiresPermissions("alipay:houseAndRoom:sync")
    @RequestMapping(value = "syncHouse/{houseId}")
    public String syncHouse(@PathVariable("houseId") String houseId, RedirectAttributes redirectAttributes) {
        House house = houseService.get(houseId);
        if (house.getOwner() == null) {
            List<Owner> ownerList = ownerService.findByHouse(house);
            if (CollectionUtils.isEmpty(ownerList)) {
                addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间所属房屋没有业主信息，请联系管理员！");
                return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
            }
            house.setOwner(ownerList.get(0));
        }
        house.setPropertyProject(projectService.get(house.getPropertyProject().getId()));
        //小区同步过或同步失败
        if (StringUtils.isBlank(house.getPropertyProject().getCommReqId()) && !syncPropertyProject(house.getPropertyProject())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋所属小区同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
        }
        if (StringUtils.isBlank(house.getAttachmentPath())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋没有上传图片，请上传后再同步！");
            return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
        }
        List<String> imageUrls;
        try {
            imageUrls = syncPictures(house.getAttachmentPath());
        } catch (Exception e) {
            logger.error("sync pictures error {}, {}", houseId, e);
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋图片同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
        }
        boolean flag = false;
        if (BuildingTypeEnum.DISPERSION.getValue().equals(house.getBuilding().getType())) {
            try {
                flag = syncDispersionHouse(house, imageUrls);
            } catch (ServiceException e) {
                addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, e.getMessage());
                return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
            }
        } else if (BuildingTypeEnum.CONCENTRATION.getValue().equals(house.getBuilding().getType())) {
            try {
                flag = syncConcentrationHouse(house, imageUrls);
            } catch (ServiceException e) {
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
    private boolean syncDispersionHouse(House house, List<String> imageUrls) {
        Building building = buildingService.get(house.getBuilding().getId());
        Owner owner = ownerService.get(house.getOwner().getId());
        int rentStatus = 2;
        if (HouseStatusEnum.RENT_FOR_RESERVE.getValue().equals(house.getHouseStatus())) {
            rentStatus = 1;
        }
        StringBuilder sb = new StringBuilder("");
        for (String imageUrl : imageUrls) {
            sb.append(imageUrl).append("\",\"");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
        AlipayEcoRenthouseRoomDispersionSyncRequest request = new AlipayEcoRenthouseRoomDispersionSyncRequest();
        request.setBizContent("{" +
                "    \"comm_req_id\": \"" + house.getPropertyProject().getCommReqId() + "\"," +
                "    \"room_code\": \"H" + house.getNewId() + "\"," +
                "    \"floor_count\": " + house.getHouseFloor() + "," +
                "    \"total_floor_count\": \"" + building.getTotalFloorCount() + "\"," +
                "    \"flat_building\": \"" + building.getBuildingName() + "\"," +
                "    \"room_num\": \"" + house.getHouseNo() + "\"," +
                "    \"bedroom_count\": " + house.getDecoraStrucRoomNum() + "," +
                "    \"parlor_count\": " + house.getDecoraStrucCusspacNum() + "," +
                "    \"toilet_count\": " + house.getDecoraStrucWashroNum() + "," +
                "    \"flat_area\": \"" + house.getDecorationSpance() + "\"," +
                "    \"room_area\": \"" + house.getDecorationSpance() + "\"," +
                "    \"rent_status\": " + rentStatus + "," +
                "    \"intro\": \"" + house.getShortDesc() + "\"," +
                "    \"pay_type\": " + house.getRentMonthGap() + "," +
                "    \"room_amount\": " + house.getRental() + "," +
                "    \"foregift_amount\": " + (house.getRental() * house.getDeposMonthCount()) + "," +
                "    \"images\":[\"" + sb.delete(sb.lastIndexOf(","), sb.length()) + "]," +
                "    \"owners_name\": \"" + owner.getName() + "\"," +
                "    \"owners_tel\": \"" + house.getReservationPhone() + "\"," +
                "    \"checkin_time\": \"" + DateUtils.formatDate(new Date()) + "\"," +
                "    \"room_status\": " + 1 + "," +
                "    \"rent_type\": " + (Integer.valueOf(house.getIntentMode()) + 1) +
                "}");
        try {
            logger.info("AlipayEcoRenthouseRoomDispersionSyncRequest is:{}", JSON.toJSONString(request));
            AlipayEcoRenthouseRoomDispersionSyncResponse response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseRoomDispersionSyncResponse is:{}", JSON.toJSONString(response));
            if (response.isSuccess()) {
                house.setUp(UpEnum.UP.getValue());
                houseService.updateHouseAlipayStatus(house);
                return true;
            } else {
                logger.error("sync house error {}, {}", house.getId(), response.getMsg());
                throw new ServiceException(response.getMsg());
            }
        } catch (Exception e) {
            logger.error("sync house error {}", house.getId(), e);
            throw new ServiceException(e);
        }
    }

    /**
     * 房屋的集中式同步
     */
    private boolean syncConcentrationHouse(House house, List<String> imageUrls) {
        Building building = buildingService.get(house.getBuilding().getId());
        Owner owner = ownerService.get(house.getOwner().getId());
        int rentStatus = 2;
        if (HouseStatusEnum.RENT_FOR_RESERVE.getValue().equals(house.getHouseStatus())) {
            rentStatus = 1;
        }
        StringBuilder sb = new StringBuilder("");
        for (String imageUrl : imageUrls) {
            sb.append(imageUrl).append("\",\"");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
        AlipayEcoRenthouseRoomConcentrationSyncRequest request = new AlipayEcoRenthouseRoomConcentrationSyncRequest();
        request.setBizContent("{" +
                "    \"comm_req_id\": \"" + house.getPropertyProject().getCommReqId() + "\"," +
                "    \"room_code\": \"H" + house.getNewId() + "\"," +
                "    \"floor_count\": " + house.getHouseFloor() + "," +
                "    \"total_floor_count\": \"" + building.getTotalFloorCount() + "\"," +
                "    \"bedroom_count\": " + house.getDecoraStrucRoomNum() + "," +
                "    \"parlor_count\": " + house.getDecoraStrucCusspacNum() + "," +
                "    \"toilet_count\": " + house.getDecoraStrucWashroNum() + "," +
                "    \"room_area\": \"" + house.getDecorationSpance() + "\"," +
                "    \"rent_status\": " + rentStatus + "," +
                "    \"intro\": \"" + house.getShortDesc() + "\"," +
                "    \"nick_name\": \"" + building.getNickName() + "\"," +
                "    \"max_amount\": \"" + building.getMaxAmount() + "\"," +
                "    \"pay_type\": " + house.getRentMonthGap() + "," +
                "    \"room_amount\": " + house.getRental() + "," +
                "    \"foregift_amount\": " + (house.getRental() * house.getDeposMonthCount()) + "," +
                "    \"images\":[\"" + sb.delete(sb.lastIndexOf(","), sb.length()) + "]," +
                "    \"owners_name\": \"" + owner.getName() + "\"," +
                "    \"owners_tel\": \"" + house.getReservationPhone() + "\"," +
                "    \"checkin_time\": \"" + DateUtils.formatDate(new Date()) + "\"," +
                "    \"room_status\": " + 1 + "," +
                "    \"rent_type\": " + (Integer.valueOf(house.getIntentMode()) + 1) +
                "}");
        try {
            logger.info("AlipayEcoRenthouseRoomConcentrationSyncRequest is:{}", JSON.toJSONString(request));
            AlipayEcoRenthouseRoomConcentrationSyncResponse response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseRoomConcentrationSyncResponse is:{}", JSON.toJSONString(response));
            if (response.isSuccess()) {
                house.setUp(UpEnum.UP.getValue());
                houseService.updateHouseAlipayStatus(house);
                return true;
            } else {
                logger.error("sync house error {}, {}", house.getId(), response.getMsg());
                throw new ServiceException(response.getMsg());
            }
        } catch (Exception e) {
            logger.error("sync house error {}", house.getId(), e);
            throw new ServiceException(e);
        }
    }

    private List<String> syncPictures(String attachmentPath) throws Exception {
        List<String> imageUrls = new ArrayList<>();
        String[] images = attachmentPath.split("\\|");
        for (int i = 1; i < images.length; i++) {
            imageUrls.add(syncPicture(images[i]));
        }
        return imageUrls;
    }

    private String syncPicture(String path) throws Exception {
        byte[] data = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(FILE_ACCESS_DOMAN + path).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream inStream = conn.getInputStream();
            data = StreamUtils.getBytes(inStream);//得到图片的二进制数据
            inStream.close();
        } catch (IOException e) {
            logger.error("syncPicture errors!", e);
        }
        Encoder encoder = Base64.getEncoder();
        String encode = encoder.encodeToString(data);
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
        AlipayEcoRenthouseCommonImageUploadRequest request = new AlipayEcoRenthouseCommonImageUploadRequest();
        request.setBizContent("{" +
                "    \"file_base\": \"" + encode + "\"," +
                "    \"file_type\": \"1\"," +
                "    \"is_public\": true" +
                "}");
        AlipayEcoRenthouseCommonImageUploadResponse response = new AlipayEcoRenthouseCommonImageUploadResponse();
        try {
            logger.info("AlipayEcoRenthouseCommonImageUploadRequest is:{}", JSON.toJSONString(request));
            response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseCommonImageUploadResponse is:{}", JSON.toJSONString(response));
        } catch (Exception e) {
            logger.error("sync picture errors ", e);
        }
        if (response.isSuccess()) {
            return response.getUrl();
        } else {
            logger.error("sync picture error {}, {}", path, response.getMsg());
            throw new IllegalArgumentException("调用失败");
        }
    }

    private boolean syncPropertyProject(PropertyProject project) {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
        AlipayEcoRenthouseCommunityInfoSyncRequest request = new AlipayEcoRenthouseCommunityInfoSyncRequest();
        request.setBizContent("{" +
                "    \"city_code\": \"" + project.getCityCode() + "\"," +
                "    \"city_name\": \"" + project.getCityName() + "\"," +
                "    \"district_code\": \"" + project.getDistrictCode() + "\"," +
                "    \"district_name\": \"" + project.getDistrictName() + "\"," +
                "    \"community_name\": \"" + project.getProjectName() + "\"," +
                "    \"address\": \"" + project.getProjectAddr() + "\"" +
                "}");
        try {
            logger.info("AlipayEcoRenthouseCommunityInfoSyncRequest is:{}", JSON.toJSONString(request));
            AlipayEcoRenthouseCommunityInfoSyncResponse response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseCommunityInfoSyncResponse is:{}", JSON.toJSONString(response));
            if (response.isSuccess()) {
                project.setAlipayStatus(Math.toIntExact(response.getStatus()));
                project.setCommReqId(response.getCommReqId());
                projectService.save(project);
                return true;
            } else {
                logger.error("sync property project error {}, {}", project.getId(), response.getMsg());
                throw new ServiceException(response.getMsg());
            }
        } catch (AlipayApiException e) {
            logger.error("sync property project error {}", project.getId(), e);
            throw new ServiceException(e);
        }
    }

    /**
     * 房屋上架
     */
    @RequiresPermissions("alipay:houseAndRoom:sync")
    @RequestMapping(value = "upHouse/{houseId}")
    public String upHouse(@PathVariable("houseId") String houseId, RedirectAttributes redirectAttributes) {
        if (upDownHouse(houseId, UpEnum.UP.getValue())) {
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
        if (upDownHouse(houseId, UpEnum.DOWN.getValue())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "房屋下架成功！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋下架失败，请联系管理员！");
        }
        return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
    }

    private boolean upDownHouse(String houseId, Integer type) {
        House house = houseService.get(houseId);
        int rentStatus = 2;
        if (HouseStatusEnum.RENT_FOR_RESERVE.getValue().equals(house.getHouseStatus())) {
            rentStatus = 1;
        }
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
        AlipayEcoRenthouseRoomStateSyncRequest request = new AlipayEcoRenthouseRoomStateSyncRequest();
        request.setBizContent("{" +
                "    \"room_code\": \"H" + house.getNewId() + "\"," +
                "    \"room_status\": " + type + "," +
                "    \"rent_status\": " + rentStatus + "," +
                "    \"flats_tag\": " + house.getBuilding().getType() +
                "}");
        try {
            logger.info("AlipayEcoRenthouseRoomStateSyncRequest is:{}", JSON.toJSONString(request));
            AlipayEcoRenthouseRoomStateSyncResponse response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseRoomStateSyncResponse is:{}", JSON.toJSONString(response));
            if (response.isSuccess()) {
                house.setUp(type);
                houseService.updateHouseAlipayStatus(house);
                return true;
            } else {
                logger.error("up down house error {}, {}", houseId, response.getMsg());
                return false;
            }
        } catch (AlipayApiException e) {
            logger.error("up down house error {}", houseId, e);
            return false;
        }
    }

    /**
     * 房间上架
     */
    @RequiresPermissions("alipay:houseAndRoom:sync")
    @RequestMapping(value = "upRoom/{roomId}")
    public String upRoom(@PathVariable("roomId") String roomId, RedirectAttributes redirectAttributes) {
        if (upDownRoom(roomId, UpEnum.UP.getValue())) {
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
        if (upDownRoom(roomId, UpEnum.DOWN.getValue())) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "房间下架成功！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间下架失败，请联系管理员！");
        }
        return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
    }

    private boolean upDownRoom(String roomId, Integer type) {
        Room room = roomService.get(roomId);
        House house = houseService.get(room.getHouse().getId());
        int rentStatus = 2;
        if (HouseStatusEnum.RENT_FOR_RESERVE.getValue().equals(house.getHouseStatus())) {
            rentStatus = 1;
        }
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
        AlipayEcoRenthouseRoomStateSyncRequest request = new AlipayEcoRenthouseRoomStateSyncRequest();
        request.setBizContent("{" +
                "    \"room_code\": \"R" + room.getNewId() + "\"," +
                "    \"room_status\": " + type + "," +
                "    \"rent_status\": " + rentStatus + "," +
                "    \"flats_tag\": " + house.getBuilding().getType() +
                "}");
        try {
            logger.info("AlipayEcoRenthouseRoomStateSyncRequest is:{}", JSON.toJSONString(request));
            AlipayEcoRenthouseRoomStateSyncResponse response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseRoomStateSyncResponse is:{}", JSON.toJSONString(response));
            if (response.isSuccess()) {
                room.setUp(type);
                roomService.save(room);
                return true;
            } else {
                logger.error("up down room error {}, {}", roomId, response.getMsg());
                return false;
            }
        } catch (AlipayApiException e) {
            logger.error("up down room error {}", roomId, e);
            return false;
        }
    }

    /**
     * 预约看房
     */
    @RequestMapping(value = "reservation", method = RequestMethod.POST)
    @ResponseBody
    public String reservation(HttpServletRequest request) {
        logger.info(JsonUtil.object2Json(request.getParameterMap()));
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
        record.setBookDate(DateUtils.parseDate(request.getParameter("recordTime")));
        record.setBookStatus(BookStatusEnum.BOOK_APP.value());
        record.setSource("1");
        String roomCode = request.getParameter("roomCode");
        record.setHousingCode(roomCode);
        record.setHousingType(Integer.valueOf(request.getParameter("flatsTag")));
        record.setRemarks(request.getParameter("remark"));
        if (roomCode.startsWith("R")) {
            record.setRoomId(roomCode.substring(1));
            record.setHouseId(roomService.get(roomCode.substring(1)).getHouse().getId());
        } else {
            record.setHouseId(roomCode.substring(1));
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
        Map<String, String> params = new HashMap<String, String>();
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
            signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.ali_public_key, "UTF-8");
        } catch (AlipayApiException e) {
            logger.error("signVerified error ", e);
        }
        return signVerified;
    }

}
