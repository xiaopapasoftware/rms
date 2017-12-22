/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.other.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.other.entity.FeeOtherChargedFlow;
import com.thinkgem.jeesite.modules.fee.other.entity.vo.FeeOtherChargedFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>宽带、电视费、其他账单收取流水实现类service</p>
 * <p>Table: fee_other_charged_flow - 宽带、电视费、其他账单收取流水</p>
 * @since 2017-11-28 03:02:42
 * @author generator code
*/
@MyBatisDao
public interface FeeOtherChargedFlowDao extends CrudDao<FeeOtherChargedFlow>{
    List<FeeOtherChargedFlow> getFeeOtherChargedFlowByBusinessIdAndFromSource(@Param("businessId") String businessId, @Param("fromSource") int fromSource);

    List<FeeOtherChargedFlowVo> getFeeOtherChargedFlow(FeeCriteriaEntity feeCriteriaEntity);

    FeeOtherChargedFlow getLastRecord(@Param("houseId") String houseId, @Param("roomId") String roomId);

    List<FeeOtherChargedFlow> getGenerateFeeOtherChargedFlow(@Param("scope") String scope,@Param("businessId") String businessId);

    void batchInsert(List<FeeOtherChargedFlow> feeGasChargedFlows);

    void batchUpdate(List<FeeOtherChargedFlow> feeGasChargedFlows);
}