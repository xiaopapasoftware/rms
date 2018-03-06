package com.thinkgem.jeesite.modules.inventory.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayEcoRenthouseRoomStateSyncRequest;
import com.alipay.api.response.AlipayEcoRenthouseRoomStateSyncResponse;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.service.AttachmentService;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.enums.RoomStatusEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * 房间信息Service
 */
@Service
@Transactional(readOnly = true)
public class RoomService extends CrudService<RoomDao, Room> {

    @Autowired
    private AttachmentService attachmentService;

    private AlipayClient alipayClient;
    public static String TP_PRIVATEKEY;//私钥
    public static String TP_OPENAPI_URL;//网关
    public static String TP_APPID;

    @PostConstruct
    public void initParams() {
        Global global = Global.getInstance();
        TP_PRIVATEKEY = global.getConfig("alipay.private.signkey");
        TP_OPENAPI_URL = global.getConfig("alipay.open.api");
        TP_APPID = global.getConfig("alipay.app.id");
        alipayClient = new DefaultAlipayClient(TP_OPENAPI_URL, TP_APPID, TP_PRIVATEKEY, "json", "UTF-8", "", "RSA2");
    }

    public Page<House> findFeaturePage(Page<House> page) {
        page.setList(dao.findFeatureList());
        return page;
    }

    @Override
    public List<Room> findList(Room entity) {
        areaScopeFilter(entity, "dsf", "pp.area_id=sua.area_id");
        return super.findList(entity);
    }

    @Override
    public Page<Room> findPage(Page<Room> page, Room entity) {
        areaScopeFilter(entity, "dsf", "pp.area_id=sua.area_id");
        return super.findPage(page, entity);
    }

    /**
     * 根据房屋ID查询房间信息
     */
    @Transactional(readOnly = true)
    public List<Room> findRoomListByHouseId(String houseId) {
        return dao.findRoomListByHouseId(houseId);
    }

    @Transactional(readOnly = false)
    public void saveRoom(Room room) {
        if (room.getIsNewRecord()) {// 新增
            String id = super.saveAndReturnId(room);
            if (StringUtils.isNotEmpty(room.getAttachmentPath())) {
                processRoomAttachment(id, room.getAttachmentPath());
            }
        } else {// 更新
            Attachment attachment = new Attachment();
            attachment.setRoomId(room.getId());
            List<Attachment> attachmentList = attachmentService.findList(attachment);
            String id = super.saveAndReturnId(room);
            if (CollectionUtils.isNotEmpty(attachmentList)) {// 更新时候，不管AttachmentPath有值无值，都要更新，防止空值不更新的情况。
                Attachment atta = new Attachment();
                atta.setRoomId(id);
                atta.setAttachmentPath(room.getAttachmentPath());
                attachmentService.updateAttachmentPathByType(atta);
            } else {// 修改时，新增附件
                processRoomAttachment(id, room.getAttachmentPath());
            }
        }
    }

    private void processRoomAttachment(String roomId, String roomAttachmentPath) {
        Attachment attachment = new Attachment();
        attachment.setRoomId(roomId);
        attachment.setAttachmentType(FileType.ROOM_MAP.getValue());
        attachment.setAttachmentPath(roomAttachmentPath);
        attachmentService.save(attachment);
    }

    @Transactional(readOnly = false)
    public void delete(Room room) {
        room.preUpdate();
        super.delete(room);
        Attachment atta = new Attachment();
        atta.setRoomId(room.getId());
        attachmentService.delete(atta);
    }

    @Transactional(readOnly = true)
    public List<Room> findRoomByPrjAndBldAndHouNoAndRomNo(Room room) {
        return dao.findRoomByPrjAndBldAndHouNoAndRomNo(room);
    }

    /**
     * 新签-单间，是否成功锁定房源 把房间从“待出租可预订”变为“已出租”
     */
    @Transactional(readOnly = false)
    public boolean isLockSingleRoom4NewSign(String roomId) {
        Room paRoom = new Room();
        paRoom.preUpdate();
        paRoom.setId(roomId);
        int updatedCount = dao.updateRoomStatus4NewSign(paRoom);
        if (updatedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 续签-单间，是否成功锁定房源
     */
    @Transactional(readOnly = false)
    public boolean isLockSingleRoom4RenewSign(String roomId) {
        Room paRoom = new Room();
        paRoom.preUpdate();
        paRoom.setId(roomId);
        int updatedCount = dao.updateRoomStatus4RenewSign(paRoom);
        if (updatedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 定金转合同-单间，是否成功锁定房源
     */
    @Transactional(readOnly = false)
    public boolean isLockSingleRoomFromDepositToContract(String roomId) {
        Room paRoom = new Room();
        paRoom.preUpdate();
        paRoom.setId(roomId);
        int updatedCount = dao.updateRoomStatusFromDepositToContract(paRoom);
        if (updatedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 整租，在房屋状态成功锁定后，锁定房屋下所有的房间状态
     */
    @Transactional(readOnly = false)
    public void lockRooms(String houseId) {
        House h = new House();
        h.setId(houseId);
        Room parameterRoom = new Room();
        parameterRoom.setHouse(h);
        List<Room> rooms = super.findList(parameterRoom);
        if (CollectionUtils.isNotEmpty(rooms)) {
            for (Room r : rooms) {
                r.setRoomStatus(RoomStatusEnum.RENTED.getValue());
                super.save(r);
            }
        }
    }

    /**
     * 整租，在房屋状态成功锁定后，锁定房屋下所有的房间状态
     */
    @Transactional(readOnly = false)
    public void depositAllRooms(String houseId) {
        House h = new House();
        h.setId(houseId);
        Room parameterRoom = new Room();
        parameterRoom.setHouse(h);
        List<Room> rooms = super.findList(parameterRoom);
        if (CollectionUtils.isNotEmpty(rooms)) {
            for (Room r : rooms) {
                r.setRoomStatus(RoomStatusEnum.BE_RESERVED.getValue());
                super.save(r);
            }
        }
    }

    /**
     * 预定-单间，是否成功锁定房源
     * 把房间从“待出租可预订”变为“已预定”
     */
    @Transactional(readOnly = false)
    public boolean isLockSingleRoom4Deposit(String roomId) {
        Room paRoom = new Room();
        paRoom.preUpdate();
        paRoom.setId(roomId);
        int updatedCount = dao.updateRoomStatus4Deposit(paRoom);
        if (updatedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询某个小区下面所有的房间数量
     */
    public int queryRoomsCountByProjectPropertyId(Date endDate, String propertyProjectId) {
        return dao.queryRoomsCountByProjectPropertyId(endDate, propertyProjectId);
    }

    /**
     * 查询没在t_fee_report中出现过但符合条件的room
     */
    public List<Room> getValidFeeRoomList() {
        return dao.getValidFeeRoomList();
    }

    public Room getByNewId(String newId) {
        return dao.getByNewId(newId);
    }

    /**
     * 支付宝上下架
     *
     * @param buildingType 公寓类型，分散式/集中式
     */
    @Transactional(readOnly = false)
    public boolean upDownRoom(String roomId, Integer type, String buildingType) {
        Room room = dao.get(roomId);
        String rentStatus;
        String roomStatus = room.getRoomStatus();
        if (RoomStatusEnum.BE_RESERVED.getValue().equals(roomStatus) || RoomStatusEnum.RENTED.getValue().equals(roomStatus)) {
            rentStatus = "2";//已租
        } else {
            rentStatus = "1";//未租
        }
        AlipayEcoRenthouseRoomStateSyncRequest request = new AlipayEcoRenthouseRoomStateSyncRequest();
        request.setBizContent("{" +
                "    \"room_code\": \"R" + room.getNewId() + "\"," +
                "    \"room_status\": " + type + "," +
                "    \"rent_status\": " + rentStatus + "," +
                "    \"flats_tag\": " + buildingType + "}");
        try {
            logger.info("AlipayEcoRenthouseRoomStateSyncRequest is:{}", JSON.toJSONString(request));
            AlipayEcoRenthouseRoomStateSyncResponse response = alipayClient.execute(request);
            logger.info("AlipayEcoRenthouseRoomStateSyncResponse is:{}", JSON.toJSONString(response));
            if (response.isSuccess()) {
                room.setUp(type);
                super.save(room);
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
