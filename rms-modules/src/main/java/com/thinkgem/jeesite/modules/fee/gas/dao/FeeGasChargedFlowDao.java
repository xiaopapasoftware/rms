/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasChargedFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasChargedFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>煤气收取流水实现类service</p>
 * <p>Table: fee_gas_charged_flow - 煤气收取流水</p>
 *
 * @author generator code
 * @since 2017-10-20 06:26:33
 */
@MyBatisDao
public interface FeeGasChargedFlowDao extends CrudDao<FeeGasChargedFlow> {

    List<FeeGasChargedFlow> getFeeGasChargedFlowByBusinessIdAndFromSource(@Param("businessId") String businessId, @Param("fromSource") int fromSource);

    List<FeeGasChargedFlowVo> getFeeGasChargedFlow(FeeCriteriaEntity feeCriteriaEntity);

    List<FeeGasChargedFlow> getGenerateFeeGasChargedFlow(@Param("scope") String scope,@Param("businessId") String businessId);

    FeeGasChargedFlow getLastRecord(@Param("houseId") String houseId, @Param("roomId") String roomId);

    void batchInsert(List<FeeGasChargedFlow> feeGasChargedFlows);

    void batchUpdate(List<FeeGasChargedFlow> feeGasChargedFlows);
}