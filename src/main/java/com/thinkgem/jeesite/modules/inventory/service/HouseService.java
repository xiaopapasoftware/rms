/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.service.AttachmentService;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.inventory.dao.HouseDao;
import com.thinkgem.jeesite.modules.inventory.dao.HouseOwnerDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.HouseOwner;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.enums.HouseStatusEnum;
import com.thinkgem.jeesite.modules.inventory.enums.RoomStatusEnum;
import com.thinkgem.jeesite.modules.person.entity.Owner;

/**
 * 房屋信息Service
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@Service
@Transactional(readOnly = true)
public class HouseService extends CrudService<HouseDao, House> {

    @Autowired
    private RoomService roomService;

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private HouseOwnerDao houseOwnerDao;

    public House get(String id) {
	return super.get(id);
    }

    public List<House> findList(House house) {
	return super.findList(house);
    }

    public Page<House> findPage(Page<House> page, House house) {
	return super.findPage(page, house);
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
    public void save(House house) {
	if (house.getIsNewRecord()) {// 新增
	    String id = super.saveAndReturnId(house);
	    if (StringUtils.isNotEmpty(house.getAttachmentPath())) {
		Attachment attachment = new Attachment();
		attachment.preInsert();
		attachment.setHouseId(id);
		attachment.setAttachmentType(FileType.HOUSE_MAP.getValue());
		attachment.setAttachmentPath(house.getAttachmentPath());
		attachmentDao.insert(attachment);
	    }
	} else {// 修改
		// 先查询原先该房屋下是否有附件，有的话则直接更新。
	    Attachment attachment = new Attachment();
	    attachment.setHouseId(house.getId());
	    List<Attachment> attachmentList = attachmentDao.findList(attachment);
	    String id = super.saveAndReturnId(house);
	    if (CollectionUtils.isNotEmpty(attachmentList)) {// 更新时候，不管AttachmentPath有值无值，都要更新，防止空值不更新的情况。
		Attachment atta = new Attachment();
		atta.setHouseId(id);
		atta.setAttachmentPath(house.getAttachmentPath());
		atta.preUpdate();
		attachmentService.updateAttachmentPathByType(atta);
	    } else {// 新增附件
		Attachment toAddattachment = new Attachment();
		toAddattachment.preInsert();
		toAddattachment.setHouseId(id);
		toAddattachment.setAttachmentType(FileType.HOUSE_MAP.getValue());
		toAddattachment.setAttachmentPath(house.getAttachmentPath());
		attachmentDao.insert(toAddattachment);
	    }
	}
	// 房屋业主关系信息
	HouseOwner houseOwner = new HouseOwner();
	houseOwner.setHouseId(house.getId());
	houseOwner.preUpdate();
	houseOwnerDao.delete(houseOwner);

	List<Owner> ownerList = house.getOwnerList();
	for (Owner owner : ownerList) {
	    houseOwner = new HouseOwner();
	    houseOwner.preInsert();
	    houseOwner.setOwnerId(owner.getId());
	    houseOwner.setHouseId(house.getId());
	    houseOwnerDao.insert(houseOwner);
	}
    }

    @Transactional(readOnly = false)
    public void delete(House house) {

	super.delete(house);
	Attachment atta = new Attachment();
	atta.setHouseId(house.getId());
	atta.preUpdate();
	attachmentDao.delete(atta);

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
    public List<House> findHourseByProPrjAndBuildingAndHouseNo(House house) {
	return dao.findHourseByProPrjAndBuildingAndHouseNo(house);
    }

    /**
     * 取消整套房屋的预定，更新房屋状态，房屋及其所属的房间状态都设置为“待出租可预订”
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
			m1.preUpdate();
			roomService.update(m1);
		    }
		}
	    }
	}
    }

    /**
     * 取消单间的预定，更新单间状态，单间及其所属的房屋状态都设置为“待出租可预订”
     */
    @Transactional(readOnly = false)
    public void releaseSingleRoom(Room room) {
	if (RoomStatusEnum.BE_RESERVED.getValue().equals(room.getRoomStatus())) {// 更新房间状态
	    room.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
	    room.preUpdate();
	    roomService.update(room);
	}
	House h = dao.get(room.getHouse().getId());// 同时更新房屋状态
	if (HouseStatusEnum.BE_RESERVED.getValue().equals(h.getHouseStatus())) {// 如果房屋状态是“已预定”
	    h.setHouseStatus(HouseStatusEnum.RENT_FOR_RESERVE.getValue());// 待出租可预订
	    h.preUpdate();
	    dao.update(h);
	}
    }

    /**
     * 房屋装修完成，变更房屋及其房间的状态
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
			m1.preUpdate();
			roomService.update(m1);
		    }
		}
	    }
	}

    }

    /**
     * 单间装修完成，变更单间及其所属房屋的状态
     */
    @Transactional(readOnly = false)
    public void finishSingleRoomDirect4Status(Room r) {
	if (RoomStatusEnum.TO_RENOVATION.getValue().equals(r.getRoomStatus())) {
	    r.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
	    r.preUpdate();
	    roomService.update(r);
	}
	House h = dao.get(r.getHouse().getId());// 同时更新房屋状态
	if (HouseStatusEnum.TO_RENOVATION.getValue().equals(h.getHouseStatus())) {
	    h.setHouseStatus(HouseStatusEnum.RENT_FOR_RESERVE.getValue());// 待出租可预订
	    h.preUpdate();
	    dao.update(h);
	}
    }

    /**
     * 整租房屋预定，变更房屋及其房间的状态
     */
    @Transactional(readOnly = false)
    public void depositWholeHouse(House house) {
	if (HouseStatusEnum.RENT_FOR_RESERVE.getValue().equals(house.getHouseStatus()) || HouseStatusEnum.RETURN_FOR_RENT.getValue().equals(house.getHouseStatus())) {
	    house.setHouseStatus(HouseStatusEnum.BE_RESERVED.getValue());
	    house.preUpdate();
	    dao.update(house);
	}
	// 同时把所属的房间状态都更新为“已预定”
	Room parameterRoom = new Room();
	parameterRoom.setHouse(house);
	List<Room> rooms = roomService.findList(parameterRoom);
	if (CollectionUtils.isNotEmpty(rooms)) {
	    for (Room r : rooms) {
		if (RoomStatusEnum.RENT_FOR_RESERVE.getValue().equals(r.getRoomStatus()) || RoomStatusEnum.RETURN_FOR_RESERVE.getValue().equals(r.getRoomStatus())) {
		    r.setRoomStatus(RoomStatusEnum.BE_RESERVED.getValue());// 已预定
		    r.preUpdate();
		    roomService.update(r);
		}
	    }
	}
    }

    /**
     * 单间预定，变更单间及其所属房屋的状态
     */
    @Transactional(readOnly = false)
    public void depositSingleRoom(Room room) {
	if (RoomStatusEnum.RENT_FOR_RESERVE.getValue().equals(room.getRoomStatus()) || RoomStatusEnum.RETURN_FOR_RESERVE.getValue().equals(room.getRoomStatus())) {
	    room.setRoomStatus(RoomStatusEnum.BE_RESERVED.getValue());// 已预定
	    room.preUpdate();
	    roomService.update(room);
	}
	// 同时更新该房间所属房屋的状态
	House h = dao.get(room.getHouse().getId());
	if (HouseStatusEnum.RENT_FOR_RESERVE.getValue().equals(h.getHouseStatus()) || HouseStatusEnum.PART_RENT.getValue().equals(h.getHouseStatus()) || HouseStatusEnum.RETURN_FOR_RENT.getValue().equals(h.getHouseStatus())) {
	    Room queryRoom = new Room();
	    queryRoom.setHouse(h);
	    List<Room> roomsOfHouse = roomService.findList(queryRoom);
	    if (CollectionUtils.isNotEmpty(roomsOfHouse)) {
		int depositCount = 0;// 预定或出租的数量
		for (Room depositedRoom : roomsOfHouse) {
		    if (RoomStatusEnum.BE_RESERVED.getValue().equals(depositedRoom.getRoomStatus()) || RoomStatusEnum.RENTED.getValue().equals(depositedRoom.getRoomStatus())) {
			depositCount = depositCount + 1;
		    }
		}
		if (depositCount == roomsOfHouse.size()) {// 房屋内房间全部出租或预定，房屋状态更新为“已预定”
		    h.preUpdate();
		    h.setHouseStatus(HouseStatusEnum.BE_RESERVED.getValue());// 已预定
		    dao.update(h);
		}
	    }
	}
    }

    @Transactional(readOnly = true)
    public List<House> findAllHouses() {
	return dao.findAllList(new House());
    }

    @Transactional(readOnly = true)
    public int getCurrentValidHouseNum() {
	return dao.getCurrentValidHouseNum(new House());
    }

    public House getHouseByHouseId(House house) {
	return dao.getHouseByHouseId(house);
    }

    public void update(House house) {
	house.preUpdate();
	dao.update(house);
    }
}