/**
 * 小爬爬工作室 Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.dao;

import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeElectricityBillVo;
import org.apache.ibatis.annotations.Param;

/**
 * 电费账单表实现类service
 * Table: fee_electricity_bill - 电费账单表
 *
 * @since 2017-09-18 08:24:24
 * @author generator code
 */
@MyBatisDao
public interface FeeElectricityBillDao extends CrudDao<FeeElectricityBill> {

  List<FeeElectricityBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity);

  FeeElectricityBill getLastRecord(@Param("id") String id, @Param("houseId") String houseId);

  FeeElectricityBill getCurrentBillByDateAndHouseNum(@Param("eleBillDate") Date eleBillDate, @Param("houseEleNum") String houseEleNum);

  Double getTotalAmount(FeeCriteriaEntity feeCriteriaEntity);

  List<FeeElectricityBill> getEleBillByIds(@Param("ids") String ids);

  int batchUpdate(List<FeeElectricityBill> feeElectricityBills);
}
