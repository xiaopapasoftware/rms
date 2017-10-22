/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleChargedFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleChargedFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>电费收取流水实现类service</p>
 * <p>Table: fee_ele_charged_flow - 电费收取流水</p>
 * @since 2017-09-18 08:24:32
 * @author generator code
*/
@MyBatisDao
public interface FeeEleChargedFlowDao extends CrudDao<FeeEleChargedFlow>{

    FeeEleChargedFlow getFeeEleChargedFlowByBusinessIdAndFromSource(@Param("businessId") String businessId,@Param("fromSource")int fromSource);

    List<FeeEleChargedFlowVo> getFeeEleChargedFlow(FeeCriteriaEntity feeCriteriaEntity);

}