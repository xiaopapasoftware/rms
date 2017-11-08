/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasBillVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>燃气账单表实现类service</p>
 * <p>Table: fee_gas_bill - 燃气账单表</p>
 * @since 2017-10-20 06:26:27
 * @author generator code
*/
@MyBatisDao
public interface FeeGasBillDao extends CrudDao<FeeGasBill>{

    List<FeeGasBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity);

    FeeGasBill getLastRecord(@Param("id") String id, @Param("houseId") String houseId);

    FeeGasBill getCurrentBillByDateAndHouseId(@Param("eleBillDate") Date eleBillDate, @Param("houseId") String houseId);

    Double getTotalAmount(FeeCriteriaEntity feeCriteriaEntity);
}