/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

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

import java.util.Date;
import java.util.List;

/**
 * 房间信息Service
 * 
 * @author huangsc
 * @version 2015-06-09
 */
@Service
@Transactional(readOnly = true)
public class RoomService extends CrudService<RoomDao, Room> {

  @Autowired
  private AttachmentService attachmentService;

  public Page<House> findFeaturePage(Page<House> page) {
    page.setList(dao.findFeatureList());
    return page;
  }

  @Override
  public List<Room> findList(Room entity) {
    areaScopeFilter(entity,"dsf","pp.area_id=sua.area_id");
    return super.findList(entity);
  }

  @Override
  public Page<Room> findPage(Page<Room> page, Room entity) {
    areaScopeFilter(entity,"dsf","pp.area_id=sua.area_id");
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
        Attachment attachment = new Attachment();
        attachment.setRoomId(id);
        attachment.setAttachmentType(FileType.ROOM_MAP.getValue());
        attachment.setAttachmentPath(room.getAttachmentPath());
        attachmentService.save(attachment);
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
        Attachment toAddattachment = new Attachment();
        toAddattachment.setRoomId(id);
        toAddattachment.setAttachmentType(FileType.ROOM_MAP.getValue());
        toAddattachment.setAttachmentPath(room.getAttachmentPath());
        attachmentService.save(toAddattachment);
      }
    }
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
   * 根据主键查询智能电表号
   */
  public String queryMeterNoByRoomId(String roomId) {
    return dao.queryMeterNoByRoomId(roomId);
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
}
