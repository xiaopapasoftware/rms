package com.thinkgem.jeesite.modules.app.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.enums.HouseTypeEnum;
import com.thinkgem.jeesite.modules.app.enums.UpEnum;
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
import com.thinkgem.jeesite.modules.person.entity.Owner;
import com.thinkgem.jeesite.modules.person.service.OwnerService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String TP_PRIVATEKEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKK0PXoLKnBkgtOl0kvyc9X2tUUdh/lRZr9RE1frjr2ZtAulZ+Moz9VJZFew1UZIzeK0478obY/DjHmD3GMfqJoTguVqJ2MEg+mJ8hJKWelvKLgfFBNliAw+/9O6Jah9Q3mRzCD8pABDEHY7BM54W7aLcuGpIIOa/qShO8dbXn+FAgMBAAECgYA8+nQ380taiDEIBZPFZv7G6AmT97doV3u8pDQttVjv8lUqMDm5RyhtdW4n91xXVR3ko4rfr9UwFkflmufUNp9HU9bHIVQS+HWLsPv9GypdTSNNp+nDn4JExUtAakJxZmGhCu/WjHIUzCoBCn6viernVC2L37NL1N4zrR73lSCk2QJBAPb/UOmtSx+PnA/mimqnFMMP3SX6cQmnynz9+63JlLjXD8rowRD2Z03U41Qfy+RED3yANZXCrE1V6vghYVmASYsCQQCoomZpeNxAKuUJZp+VaWi4WQeMW1KCK3aljaKLMZ57yb5Bsu+P3odyBk1AvYIPvdajAJiiikRdIDmi58dqfN0vAkEAjFX8LwjbCg+aaB5gvsA3t6ynxhBJcWb4UZQtD0zdRzhKLMuaBn05rKssjnuSaRuSgPaHe5OkOjx6yIiOuz98iQJAXIDpSMYhm5lsFiITPDScWzOLLnUR55HL/biaB1zqoODj2so7G2JoTiYiznamF9h9GuFC2TablbINq80U2NcxxQJBAMhw06Ha/U7qTjtAmr2qAuWSWvHU4ANu2h0RxYlKTpmWgO0f47jCOQhdC3T/RK7f38c7q8uPyi35eZ7S1e/PznY=";
    private String TP_OPENAPI_URL = "http://oepnapi.eco.dl.alipaydev.com/gateway.do";
    private String TP_APPID = "2015122300879608";//测试环境

    /**
     * 基础信息维护
     */
    @RequestMapping(value = "baseInfoSync/{name}")
    @ResponseBody
    public String baseInfoSync(@PathVariable("name") String name) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseKaBaseinfoSyncRequest request = new AlipayEcoRenthouseKaBaseinfoSyncRequest();
        //注意该接口使用的前提是，需要开通新的ISVappid权限，目前开发环境的测试账号已被使用
        request.setBizContent("{" +
                "    \"ka_name\": \" " + name + " \"" +
                "}");
        AlipayEcoRenthouseKaBaseinfoSyncResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return response.getKaCode();
        } else {
            return response.getMsg();
        }
    }

    /**
     * 基础信息获取
     */
    @RequestMapping(value = "baseInfoQuery/{kaCode}")
    @ResponseBody
    public String baseInfoQuery(@PathVariable("kaCode") String kaCode) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseKaBaseinfoQueryRequest request = new AlipayEcoRenthouseKaBaseinfoQueryRequest();
        request.setBizContent("{" + "\"ka_code\": \"" + kaCode + "\"" + "}");
        AlipayEcoRenthouseKaBaseinfoQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return response.getValid();
        } else {
            return response.getMsg();
        }
    }

    /**
     * 公寓运营商服务地址注册
     */
    @RequestMapping(value = "serviceCreate/{type}")
    @ResponseBody
    public String serviceCreate(@PathVariable("type") String type) throws AlipayApiException {
        PropertiesLoader loader = new PropertiesLoader("jeesite.properties");
        String kaCode = loader.getProperty("alipay.kacode");
        String url = "";
        if ("1".equals(type)) {
            url = loader.getProperty("alipay.url.reservation");
        } else if ("2".equals(type)) {
            url = loader.getProperty("alipay.url.affirm");
        } else if ("3".equals(type)) {
            url = loader.getProperty("alipay.url.call");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseKaServiceCreateRequest request = new AlipayEcoRenthouseKaServiceCreateRequest();
        request.setBizContent("{" +
                "    \"address\": \"" + url + "\"," +
                "    \"ka_code\": \"" + kaCode + "\"," +
                "    \"type\": " + type +
                "}");
        AlipayEcoRenthouseKaServiceCreateResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return "success";
        } else {
            return response.getMsg();
        }
    }

    /**
     * 房间的分散式同步
     */
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
        List<String> imageUrls = new ArrayList<>();
        try {
            imageUrls = syncPictures(room.getAttachmentPath());
        } catch (Exception e) {
            logger.error("sync pictures error {}, {}", roomId, e);
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房间图片同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
        }
        boolean flag = false;
        if (HouseTypeEnum.DISPERSION.getValue().equals(house.getType())) {
            flag = syncDispersionRoom(room, imageUrls);
        } else if (HouseTypeEnum.CONCENTRATION.getValue().equals(house.getType())) {
            flag = syncConcentrationRoom(room, imageUrls);
        }
        if (flag) {
            addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "房屋同步支付宝成功！");
        } else {
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋同步支付宝失败，请联系管理员！");
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
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseRoomDispersionSyncRequest request = new AlipayEcoRenthouseRoomDispersionSyncRequest();
        request.setBizContent("{" +
                "    \"comm_req_id\": \"" + house.getPropertyProject().getCommReqId() + "\"," +
                "    \"room_code\": \"00" + room.getNewId() + "\"," +
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
                "    \"owners_tel\": \"" + owner.getCellPhone() + "\"," +
                "    \"checkin_time\": \"" + DateUtils.formatDate(new Date()) + "\"," +
                "    \"room_status\": " + 1 + "," +
                "    \"rent_type\": " + (Integer.valueOf(house.getIntentMode()) + 1) +
                "}");
        try {
            AlipayEcoRenthouseRoomDispersionSyncResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                room.setAlipayStatus(1);
                room.setUp(UpEnum.UP.getValue());
                roomService.saveRoom(room);
                return true;
            } else {
                logger.error("sync room error {}, {}", room.getId(), response.getMsg());
                return false;
            }
        } catch (Exception e) {
            logger.error("sync room error {}", room.getId(), e);
            return false;
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
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseRoomDispersionSyncRequest request = new AlipayEcoRenthouseRoomDispersionSyncRequest();
        request.setBizContent("{" +
                "    \"comm_req_id\": \"" + house.getPropertyProject().getCommReqId() + "\"," +
                "    \"room_code\": \"00" + room.getNewId() + "\"," +
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
                "    \"owners_tel\": \"" + owner.getCellPhone() + "\"," +
                "    \"checkin_time\": \"" + DateUtils.formatDate(new Date()) + "\"," +
                "    \"room_status\": " + 1 + "," +
                "    \"rent_type\": " + (Integer.valueOf(house.getIntentMode()) + 1) +
                "}");
        try {
            AlipayEcoRenthouseRoomDispersionSyncResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                room.setAlipayStatus(1);
                room.setUp(UpEnum.UP.getValue());
                roomService.saveRoom(room);
                return true;
            } else {
                logger.error("sync room error {}, {}", room.getId(), response.getMsg());
                return false;
            }
        } catch (Exception e) {
            logger.error("sync room error {}", room.getId(), e);
            return false;
        }
    }

    /**
     * 房屋同步
     */
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
        List<String> imageUrls = new ArrayList<>();
        try {
            imageUrls = syncPictures(house.getAttachmentPath());
        } catch (Exception e) {
            logger.error("sync pictures error {}, {}", houseId, e);
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房屋图片同步支付宝失败，请联系管理员！");
            return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
        }
        boolean flag = false;
        if (HouseTypeEnum.DISPERSION.getValue().equals(house.getType())) {
            flag = syncDispersionHouse(house, imageUrls);
        } else if (HouseTypeEnum.CONCENTRATION.getValue().equals(house.getType())) {
            flag = syncConcentrationHouse(house, imageUrls);
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
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseRoomDispersionSyncRequest request = new AlipayEcoRenthouseRoomDispersionSyncRequest();
        request.setBizContent("{" +
                "    \"comm_req_id\": \"" + house.getPropertyProject().getCommReqId() + "\"," +
                "    \"room_code\": \"0" + house.getNewId() + "\"," +
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
                "    \"owners_tel\": \"" + owner.getCellPhone() + "\"," +
                "    \"checkin_time\": \"" + DateUtils.formatDate(new Date()) + "\"," +
                "    \"room_status\": " + 1 + "," +
                "    \"rent_type\": " + (Integer.valueOf(house.getIntentMode()) + 1) +
                "}");
        try {
            AlipayEcoRenthouseRoomDispersionSyncResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                house.setUp(UpEnum.UP.getValue());
                houseService.updateHouseAlipayStatus(house);
                return true;
            } else {
                logger.error("sync house error {}, {}", house.getId(), response.getMsg());
                return false;
            }
        } catch (Exception e) {
            logger.error("sync house error {}", house.getId(), e);
            return false;
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
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseRoomDispersionSyncRequest request = new AlipayEcoRenthouseRoomDispersionSyncRequest();
        request.setBizContent("{" +
                "    \"comm_req_id\": \"" + house.getPropertyProject().getCommReqId() + "\"," +
                "    \"room_code\": \"0" + house.getNewId() + "\"," +
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
                "    \"owners_tel\": \"" + owner.getCellPhone() + "\"," +
                "    \"checkin_time\": \"" + DateUtils.formatDate(new Date()) + "\"," +
                "    \"room_status\": " + 1 + "," +
                "    \"rent_type\": " + (Integer.valueOf(house.getIntentMode()) + 1) +
                "}");
        try {
            AlipayEcoRenthouseRoomDispersionSyncResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                house.setUp(UpEnum.UP.getValue());
                houseService.updateHouseAlipayStatus(house);
                return true;
            } else {
                logger.error("sync house error {}, {}", house.getId(), response.getMsg());
                return false;
            }
        } catch (Exception e) {
            logger.error("sync house error {}", house.getId(), e);
            return false;
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
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(path);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(data);
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseCommonImageUploadRequest request = new AlipayEcoRenthouseCommonImageUploadRequest();
        request.setBizContent("{" +
                "    \"file_base\": \"" + encode + "\"," +
                "    \"file_type\": \"1\"," +
                "    \"is_public\": true" +
                "}");
        AlipayEcoRenthouseCommonImageUploadResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return response.getUrl();
        } else {
            logger.error("sync picture error {}, {}", path, response.getMsg());
            throw new IllegalArgumentException("调用失败");
        }
    }

    private boolean syncPropertyProject(PropertyProject project) {
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
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
            AlipayEcoRenthouseCommunityInfoSyncResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                project.setAlipayStatus(Math.toIntExact(response.getStatus()));
                project.setCommReqId(response.getCommReqId());
                projectService.save(project);
                return true;
            } else {
                logger.error("sync property project error {}, {}", project.getId(), response.getMsg());
                return false;
            }
        } catch (AlipayApiException e) {
            logger.error("sync property project error {}", project.getId(), e);
            return false;
        }
    }

    /**
     * 房屋上架
     */
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
     * 房屋上架
     */
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
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseRoomStateSyncRequest request = new AlipayEcoRenthouseRoomStateSyncRequest();
        request.setBizContent("{" +
                "    \"room_code\": \"0" + house.getNewId() + "\"," +
                "    \"room_status\": " + type + "," +
                "    \"rent_status\": " + rentStatus + "," +
                "    \"flats_tag\": " + house.getType() +
                "}");
        try {
            AlipayEcoRenthouseRoomStateSyncResponse response = alipayClient.execute(request);
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
     * 房间上架
     */
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
        AlipayClient alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8");
        AlipayEcoRenthouseRoomStateSyncRequest request = new AlipayEcoRenthouseRoomStateSyncRequest();
        request.setBizContent("{" +
                "    \"room_code\": \"00" + room.getNewId() + "\"," +
                "    \"room_status\": " + type + "," +
                "    \"rent_status\": " + rentStatus + "," +
                "    \"flats_tag\": " + house.getType() +
                "}");
        try {
            AlipayEcoRenthouseRoomStateSyncResponse response = alipayClient.execute(request);
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

}
