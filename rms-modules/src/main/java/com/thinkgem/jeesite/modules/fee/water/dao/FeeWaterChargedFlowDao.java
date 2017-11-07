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
 * @since 2017-10-20 06:26:08
 * @author generator code
*/
@MyBatisDao
public interface FeeWaterChargedFlowDao extends CrudDao<FeeWaterChargedFlow>{

    FeeWaterChargedFlow getFeeWaterChargedFlowByBusinessIdAndFromSourceAndRoom(@Param("businessId") String businessId, @Param("fromSource")int fromSource,@Param("roomId")String roomId);

    List<FeeWaterChargedFlowVo> getFeeWaterChargedFlow(FeeCriteriaEntity feeCriteriaEntity);

    void batchInsert(List<FeeWaterChargedFlow> feeWaterChargedFlows);
}