/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasReadFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>抄燃气表流水实现类service</p>
 * <p>Table: fee_gas_read_flow - 抄燃气表流水</p>
 * @since 2017-10-20 06:26:38
 * @author generator code
*/
@MyBatisDao
public interface FeeGasReadFlowDao extends CrudDao<FeeGasReadFlow>{
    List<FeeGasReadFlowVo> getFeeGasReadFlowWithAllInfo(FeeCriteriaEntity feeCriteriaEntity);

    FeeGasReadFlow getFeeGasReadFlowByFeeBillId(@Param("feeEleBillId") String feeEleBillId);

    FeeGasReadFlow getLastRecord(@Param("id") String id, @Param("houseId") String houseId);

    FeeGasReadFlow getCurrentReadByDateAndHouseId(@Param("eleReadDate") Date eleReadDate, @Param("houseId") String houseId);
}