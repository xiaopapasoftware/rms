/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.inventory.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;

/**
 * 房屋信息DAO接口
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@MyBatisDao
public interface HouseDao extends CrudDao<House> {

  /**
   * 根据物业项目ID+楼宇ID+房屋号查询房屋信息
   */
  List<House> findHourseByProPrjAndBuildingAndHouseNo(House house);

  /**
   * 获取当前有效房屋的总数量
   */
  int getCurrentValidHouseNum(House house);

  List<House> findFeatureList(House house);

  House getFeatureInfo(House house);

  House getHouseByHouseId(House house);

  /**
   * 新签-整租,更新房屋状态
   */
  int updateHouseStatus4NewSign(House house);

  /**
   * 续签-整租,更新房屋状态
   */
  int updateHouseStatus4RenewSign(House house);

  /**
   * 定金转合同-整租，更新房屋状态
   */
  int updateHouseStatusFromDepositToContract(House house);

  /**
   * 预定-整租，更新房屋状态
   */
  int updateHouseStatus4Deposit(House house);

  int queryHousesCountByProjectPropertyId(@Param("propertyProjectId") String propertyProjectId, @Param("startDate") Date startDate);
}
