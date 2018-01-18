/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.inventory.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 房间信息DAO接口
 * 
 * @author huangsc
 * @author wangshujin
 */
@MyBatisDao
public interface RoomDao extends CrudDao<Room> {

  /**
   * 根据物业项目ID+楼宇ID+房屋ID+房间号 查询房间信息
   */
  List<Room> findRoomByPrjAndBldAndHouNoAndRomNo(Room room);

  /**
   * 根据房屋ID查询房间信息
   */
  List<Room> findRoomListByHouseId(@Param("houseId") String houseId);

  List<House> findFeatureList();

  int updateRoomStatus4NewSign(Room paRoom);

  int updateRoomStatus4RenewSign(Room paRoom);

  int updateRoomStatusFromDepositToContract(Room paRoom);

  int updateRoomStatus4Deposit(Room paRoom);

  /**
   * 查询某个小区下面所有的房间数量
   */
  int queryRoomsCountByProjectPropertyId(@Param("endDate") Date endDate, @Param("propertyProjectId") String propertyProjectId);

  /**
   * 根据主键查询智能电表号
   */
  String queryMeterNoByRoomId(String roomId);

  List<Room> getValidFeeRoomList();

  /**
   * 根据new_id查找room
   */
  Room getByNewId(@Param("newId") String newId);
}
