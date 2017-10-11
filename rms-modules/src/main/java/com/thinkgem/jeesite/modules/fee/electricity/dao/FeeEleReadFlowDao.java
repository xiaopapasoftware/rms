/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import com.thinkgem.jeesite.modules.fee.common.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleReadFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>抄电表流水实现类service</p>
 * <p>Table: fee_ele_read_flow - 抄电表流水</p>
 * @since 2017-09-18 08:24:39
 * @author generator code
*/
@MyBatisDao
public interface FeeEleReadFlowDao extends CrudDao<FeeEleReadFlow>{

    List<FeeEleReadFlowVo> getFeeEleReadFlowWithAllInfo(FeeCriteriaEntity feeCriteriaEntity);

    FeeEleReadFlowVo getFeeEleReadFlowByFeeBillId(@Param("feeEleBillId") String feeEleBillId);
}