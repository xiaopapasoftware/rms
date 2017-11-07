/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterBillVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>水费账单表实现类service</p>
 * <p>Table: fee_water_bill - 水费账单表</p>
 * @since 2017-10-20 06:26:00
 * @author generator code
*/
@MyBatisDao
public interface FeeWaterBillDao extends CrudDao<FeeWaterBill>{

    List<FeeWaterBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity);

    FeeWaterBill getLastRecord(@Param("id") String id, @Param("houseId") String houseId);

    FeeWaterBill getCurrentBillByDateAndHouseNum(@Param("waterBillDate") Date waterBillDate, @Param("houseWaterNum") String houseWaterNum);

    Double getTotalAmount(FeeCriteriaEntity feeCriteriaEntity);
}