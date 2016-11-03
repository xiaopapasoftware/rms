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

  List<RentContract> findContractList(RentContract rentContract);

  Integer getAllValidRentContractCounts();

  RentContract getByHouseId(RentContract rentContract);

  RentContract findContractByCode(String contractCode);

  /**
   * 查询已出租的有效的单间合同数
   */
  int queryValidSingleRoomCount(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("propertyProjectId") String propertyProjectId);
}
