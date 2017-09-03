/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;

/**
 * 出租合同DAO接口
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@MyBatisDao
public interface RentContractDao extends CrudDao<RentContract> {

  Integer getAllValidRentContractCounts();

  RentContract getByHouseId(RentContract rentContract);

  RentContract findContractByCode(String contractCode);

  /**
   * 获取某个日期的所有已经出租掉的房间数，包括合租的和整租的
   */
  int queryValidSingleRoomCount(@Param("startDate") Date startDate, @Param("propertyProjectId") String propertyProjectId);

  /**
   * 查询已出租的有效的单间合同列表
   */
  List<RentContract> queryValidSingleRooms(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("propertyProjectId") String propertyProjectId);

  /**
   * 查询指定时间内某房屋的有效合同
   */
  List<RentContract> queryHousesByHouseId(@Param("houseId") String houseId);

  /**
   * 查询指定日期所有部分出租+完全出租的房屋套数
   */
  int queryValidEntireHouseCount(@Param("propertyProjectId") String propertyProjectId, @Param("startDate") Date startDate);

  /**
   * 查询不同房型的有效整租合同列表
   */
  List<RentContract> queryValidConditionalEntireHouses(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("propertyProjectId") String propertyProjectId,
                                                       @Param("roomNum") Integer roomNum, @Param("cusspacNum") Integer cusspacNum);
}
