/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterChargedFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterChargedFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>水收取流水实现类service</p>
 * <p>Table: fee_water_charged_flow - 水收取流水</p>
 *
 * @author generator code
 * @since 2017-10-20 06:26:08
 */
@MyBatisDao
public interface FeeWaterChargedFlowDao extends CrudDao<FeeWaterChargedFlow> {

    List<FeeWaterChargedFlow> getFeeWaterChargedFlowByBusinessIdAndFromSource(@Param("businessId") String businessId, @Param("fromSource") int fromSource);

    FeeWaterChargedFlow getLastRecord(@Param("houseId") String houseId, @Param("roomId") String roomId);

    List<FeeWaterChargedFlowVo> getFeeWaterChargedFlow(FeeCriteriaEntity feeCriteriaEntity);

    List<FeeWaterChargedFlow> getGenerateFeeWaterChargedFlow(@Param("scope") String scope,@Param("businessId") String businessId);

    void batchInsert(List<FeeWaterChargedFlow> feeWaterChargedFlows);

    void batchUpdate(List<FeeWaterChargedFlow> feeWaterChargedFlows);
}