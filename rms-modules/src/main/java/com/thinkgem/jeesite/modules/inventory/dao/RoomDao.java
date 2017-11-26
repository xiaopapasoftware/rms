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

  /**
   * 更新房间状态
   */
  int updateRoomStatus(Room room);

  List<House> findFeatureList();

  int updateRoomStatus4NewSign(Room paRoom);

  int updateRoomStatus4RenewSign(Room paRoom);

  int updateRoomStatusFromDepositToContract(Room paRoom);

  int updateRoomStatus4Deposit(Room paRoom);

  /**
   * 查询某个小区下面所有的房间数量
   */
  int queryRoomsCountByProjectPropertyId(@Param("startDate") Date startDate, @Param("propertyProjectId") String propertyProjectId);

  /**
   * 查询某个小区下面所有的房间数量
   */
  int queryRoomsByProjectPropertyId(@Param("propertyProjectId") String propertyProjectId);

  /**
   * 查询某个房屋下面所有的房间数量
   */
  int queryRoomsByHouseId(@Param("houseId") String houseId);
}
