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
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.inventory.dao.HouseDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.HouseOwner;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.enums.HouseStatusEnum;
import com.thinkgem.jeesite.modules.inventory.enums.RoomStatusEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class HouseService extends CrudService<HouseDao, House> {

    @Autowired
    private RoomService roomService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private HouseOwnerService houseOwnerService;

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

    @Override
    public List<House> findList(House entity) {
        areaScopeFilter(entity, "dsf", "pp.area_id=sua.area_id");
        return super.findList(entity);
    }

    @Override
    public Page<House> findPage(Page<House> page, House entity) {
        areaScopeFilter(entity, "dsf", "pp.area_id=sua.area_id");
        return super.findPage(page, entity);
    }

    /**
     * 精选房源
     */
    public Page<House> findFeaturePage(Page<House> page, House house) {
        house.setPage(page);
        page.setList(dao.findFeatureList(house));
        return page;
    }

    public House getFeatureInfo(House house) {
        return dao.getFeatureInfo(house);
    }

    @Transactional(readOnly = false)
    public void saveHouse(House house) {
        String id = saveAndReturnId(house);
        Attachment attachment = new Attachment();
        attachment.setHouseId(house.getId());
        List<Attachment> attachmentList = attachmentService.findList(attachment);
        if (CollectionUtils.isNotEmpty(attachmentList)) {// 更新时候，不管AttachmentPath有值无值，都要更新，防止空值不更新的情况。
            Attachment atta = new Attachment();
            atta.setHouseId(id);
            atta.setAttachmentPath(house.getAttachmentPath());
            atta.preUpdate();
            attachmentService.updateAttachmentPathByType(atta);
        } else {// 新增附件
            Attachment toAddattachment = new Attachment();
            toAddattachment.setHouseId(id);
            toAddattachment.setAttachmentType(FileType.HOUSE_MAP.getValue());
            toAddattachment.setAttachmentPath(house.getAttachmentPath());
            attachmentService.save(toAddattachment);
        }
        houseOwnerService.processHouseAndOwner(house.getId(), house.getOwnerList());// 房屋业主关系信息
    }

    @Transactional(readOnly = false)
    public void delete(House house) {
        house.preUpdate();
        super.delete(house);

        // 删除房屋业主关系信息
        HouseOwner houseOwner = new HouseOwner();
        houseOwner.setHouseId(house.getId());
        houseOwner.preUpdate();
        houseOwnerService.delete(houseOwner);

        // 删除房屋附件
        Attachment atta = new Attachment();
        atta.setHouseId(house.getId());
        atta.preUpdate();
        attachmentService.delete(atta);

        // 删除房屋所属房间
        Room r = new Room();
        r.setHouse(house);
        List<Room> rooms = roomService.findList(r);
        if (CollectionUtils.isNotEmpty(rooms)) {
            for (Room ro : rooms) {
                roomService.delete(ro);
            }
        }
    }

    /**
     * 根据物业项目ID+楼宇ID+房屋号查询房屋信息
     */
    @Transactional(readOnly = true)
    public List<House> findHouseListByProPrjAndBuildingAndHouseNo(House house) {
        return dao.findHouseListByProPrjAndBuildingAndHouseNo(house);
    }

    /**
     * 根据物业项目ID查询房屋信息
     */
    @Transactional(readOnly = true)
    public List<House> findHouseListByBuildingId(String buildingId) {
        return dao.findHouseListByBuildingId(buildingId);
    }

    /**
     * 取消预定（整租房屋） 把房屋； 把房屋从“已预定”改为“待出租可预订”
     */
    @Transactional(readOnly = false)
    public void releaseWholeHouse(House house) {
        if (HouseStatusEnum.BE_RESERVED.getValue().equals(house.getHouseStatus())) {
            house.setHouseStatus(HouseStatusEnum.RENT_FOR_RESERVE.getValue());
            house.preUpdate();
            dao.update(house);
            Room m = new Room();
            m.setHouse(house);
            List<Room> rooms = roomService.findList(m);
            if (CollectionUtils.isNotEmpty(rooms)) {
                for (Room m1 : rooms) {
                    if (RoomStatusEnum.BE_RESERVED.getValue().equals(m1.getRoomStatus())) {
                        m1.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
                        roomService.save(m1);
                    }
                }
            }
        }
    }

    /**
     * 取消预定（单间） 把单间从“已预定”改为“待出租可预订”
     */
    @Transactional(readOnly = false)
    public void releaseSingleRoom(Room room) {
        if (RoomStatusEnum.BE_RESERVED.getValue().equals(room.getRoomStatus())) {// 更新房间状态
            room.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
            roomService.save(room);
        }
        calculateHouseStatus(room.getId());
    }

    /**
     * 装修完成（整套房屋）
     */
    @Transactional(readOnly = false)
    public void finishHouseDirect4Status(House house) {
        if (HouseStatusEnum.TO_RENOVATION.getValue().equals(house.getHouseStatus())) {
            house.setHouseStatus(HouseStatusEnum.RENT_FOR_RESERVE.getValue());
            house.preUpdate();
            dao.update(house);
            Room m = new Room();
            m.setHouse(house);
            List<Room> rooms = roomService.findList(m);
            if (CollectionUtils.isNotEmpty(rooms)) {
                for (Room m1 : rooms) {
                    if (RoomStatusEnum.TO_RENOVATION.getValue().equals(m1.getRoomStatus())) {
                        m1.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
                        roomService.save(m1);
                    }
                }
            }
        }
    }

    /**
     * 装修完成（单间）
     */
    @Transactional(readOnly = false)
    public void finishSingleRoomDirect4Status(Room r) {
        if (RoomStatusEnum.TO_RENOVATION.getValue().equals(r.getRoomStatus())) {
            r.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
            roomService.save(r);
        }
        House h = dao.get(r.getHouse().getId());// 同时更新房屋状态
        if (HouseStatusEnum.TO_RENOVATION.getValue().equals(h.getHouseStatus())) {
            h.setHouseStatus(HouseStatusEnum.RENT_FOR_RESERVE.getValue());
            super.save(h);
        }
    }

    /**
     * 预定-整租，是否成功锁定房源，整租房源状态从“待出租可预订”改为“已预定”
     *
     * @return true表示已经成功锁定房源，false表示未能锁定房源
     */
    @Transactional(readOnly = false)
    public boolean isLockWholeHouse4Deposit(String houseId) {
        House paHouse = new House();
        paHouse.preUpdate();
        paHouse.setId(houseId);
        int updatedCount = dao.updateHouseStatus4Deposit(paHouse);
        if (updatedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 新签-整租，是否成功锁定房源 把房屋从“待出租可预订”变为“完全出租”
     *
     * @return true表示已经成功锁定房源，false表示未能锁定房源
     */
    @Transactional(readOnly = false)
    public boolean isLockWholeHouse4NewSign(String houseId) {
        House paHouse = new House();
        paHouse.preUpdate();
        paHouse.setId(houseId);
        int updatedCount = dao.updateHouseStatus4NewSign(paHouse);
        if (updatedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 续签-整租，是否成功锁定房源
     *
     * @return true表示已经成功锁定房源，false表示未能锁定房源
     */
    @Transactional(readOnly = false)
    public boolean isLockWholeHouse4RenewSign(String houseId) {
        House paHouse = new House();
        paHouse.preUpdate();
        paHouse.setId(houseId);
        int updatedCount = dao.updateHouseStatus4RenewSign(paHouse);
        if (updatedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 定金转合同-整租，是否成功锁定房源
     */
    @Transactional(readOnly = false)
    public boolean isLockWholeHouseFromDepositToContract(String houseId) {
        House paHouse = new House();
        paHouse.preUpdate();
        paHouse.setId(houseId);
        int updatedCount = dao.updateHouseStatusFromDepositToContract(paHouse);
        if (updatedCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 退租（整租房屋） 把房屋从“完全出租”改为“待出租可预订”
     */
    @Transactional(readOnly = false)
    public void returnWholeHouse(House house) {
        recoveRoomStatus(house);
    }

    /**
     * 退租（单间） 把单间从“已出租”改为“待出租可预订”
     */
    @Transactional(readOnly = false)
    public void returnSingleRoom(Room room) {
        if (RoomStatusEnum.RENTED.getValue().equals(room.getRoomStatus())) {
            room.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
            roomService.save(room);
        }
        calculateHouseStatus(room.getId());
    }

    /**
     * 取消签约（整租房屋）
     */
    @Transactional(readOnly = false)
    public void cancelSign4WholeHouse(House house) {
        recoveRoomStatus(house);
    }

    private void recoveRoomStatus(House house) {
        if (HouseStatusEnum.WHOLE_RENT.getValue().equals(house.getHouseStatus())) {
            house.setHouseStatus(HouseStatusEnum.RENT_FOR_RESERVE.getValue());
            super.save(house);
            Room m = new Room();
            m.setHouse(house);
            List<Room> rooms = roomService.findList(m);
            if (CollectionUtils.isNotEmpty(rooms)) {
                for (Room m1 : rooms) {
                    if (RoomStatusEnum.RENTED.getValue().equals(m1.getRoomStatus())) {
                        m1.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
                        roomService.save(m1);
                    }
                }
            }
        }
    }

    /**
     * 取消签约（单间）
     */
    @Transactional(readOnly = false)
    public void cancelSign4SingleRoom(Room room) {
        if (RoomStatusEnum.RENTED.getValue().equals(room.getRoomStatus())) {
            room.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
            roomService.save(room);
        }
        calculateHouseStatus(room.getId());
    }

    @Transactional(readOnly = true)
    public int getCurrentValidHouseNum() {
        return dao.getCurrentValidHouseNum(new House());
    }

    /**
     * 根据单间的状态变更，改变房屋的状态（适用于合租的情况）
     */
    @Transactional(readOnly = false)
    public void calculateHouseStatus(String roomId) {
        Room room = roomService.get(roomId);
        House h = dao.get(room.getHouse().getId());
        Room queryRoom = new Room();
        queryRoom.setHouse(h);
        List<Room> roomsOfHouse = roomService.findList(queryRoom);
        if (CollectionUtils.isNotEmpty(roomsOfHouse)) {
            int reservedRoomCount = 0;// 预定掉的间数
            int rentedRoomCount = 0;// 出租掉的间数
            int roomCount = roomsOfHouse.size();
            for (Room rentedRoom : roomsOfHouse) {
                if (RoomStatusEnum.BE_RESERVED.getValue().equals(rentedRoom.getRoomStatus())) {
                    reservedRoomCount = reservedRoomCount + 1;
                }
                if (RoomStatusEnum.RENTED.getValue().equals(rentedRoom.getRoomStatus())) {
                    rentedRoomCount = rentedRoomCount + 1;
                }
            }
            if (rentedRoomCount == 0) {
                if (reservedRoomCount == 0) {
                    h.setHouseStatus(HouseStatusEnum.RENT_FOR_RESERVE.getValue());
                } else if (reservedRoomCount > 0 && reservedRoomCount < roomCount) {
                    h.setHouseStatus(HouseStatusEnum.RENT_FOR_RESERVE.getValue());
                } else if (reservedRoomCount == roomCount) {
                    h.setHouseStatus(HouseStatusEnum.BE_RESERVED.getValue());
                }
            }
            if (rentedRoomCount > 0 && rentedRoomCount < roomCount) {
                h.setHouseStatus(HouseStatusEnum.PART_RENT.getValue());
            }
            if (rentedRoomCount == roomCount) {
                h.setHouseStatus(HouseStatusEnum.WHOLE_RENT.getValue());
            }
            super.save(h);
        }
    }

    @Transactional(readOnly = true)
    public House getHouseById(String id) {
        return dao.getHouseById(id);
    }

    /**
     * 查询房源根据户号和户号类型
     *
     * @param accountNum 水电煤户号  地址
     * @param numType    0：电费 1：水费 2 燃气费 3 地址
     * @return
     */
    public List<Map> getHouseByAccountNumAndNumType(String accountNum, String numType) {
        return dao.getHouseByAccountNumAndNumType(accountNum, numType);
    }

    @Transactional(readOnly = false)
    public void updateHouseAlipayStatus(House house) {
        house.preUpdate();
        dao.updateHouseAlipayStatus(house);
    }

    public House getByNewId(String newId) {
        return dao.getByNewId(newId);
    }

    /**
     * 支付宝-整租房屋上下架
     */
    @Transactional(readOnly = false)
    public boolean upDownHouse(String houseId, Integer type) {
        House house = dao.get(houseId);
        String houseStatus = house.getHouseStatus();
        String rentStatus;
        if (HouseStatusEnum.BE_RESERVED.getValue().equals(houseStatus) || HouseStatusEnum.PART_RENT.getValue().equals(houseStatus) || HouseStatusEnum.WHOLE_RENT.getValue().equals(houseStatus)) {
            rentStatus = "2";//已租
        } else {
            rentStatus = "1";//未租
        }
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
                updateHouseAlipayStatus(house);
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
     * 把房源状态从“已预定”取消为“待出租可预订”
     */
    public void cancelDepositHouseAndRoomDepositState(String rentMode, String houseId, String roomId) {
        if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentMode)) {
            House house = super.get(houseId);
            releaseWholeHouse(house);
        } else {
            Room room = roomService.get(roomId);
            releaseSingleRoom(room);
        }
    }
}
