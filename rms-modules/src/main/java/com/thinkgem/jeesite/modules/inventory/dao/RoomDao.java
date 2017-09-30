/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.inventory.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

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
   * 更新房间状态
   */
  int updateRoomStatus(Room room);

  List<House> findFeatureList();

  int updateRoomStatus4NewSign(Room paRoom);

  int updateRoomStatus4RenewSign(Room paRoom);

  int updateRoomStatusFromDepositToContract(Room paRoom);

  int updateRoomStatus4Deposit(Room paRoom);

  /**
   * 查询某个小区下面已经出租掉的所有的房间数量
   */
  int queryRoomsCountByProjectPropertyId(@Param("startDate") Date startDate, @Param("propertyProjectId") String propertyProjectId);

  List<Map> getRoomByHouseId(String houseId);
}
