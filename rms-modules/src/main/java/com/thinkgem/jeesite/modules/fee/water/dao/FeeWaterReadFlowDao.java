/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterReadFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterReadFlowVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>抄水表流水实现类service</p>
 * <p>Table: fee_water_read_flow - 抄水表流水</p>
 * @since 2017-10-20 06:26:14
 * @author generator code
*/
@MyBatisDao
public interface FeeWaterReadFlowDao extends CrudDao<FeeWaterReadFlow>{

    List<FeeWaterReadFlowVo> getFeeWaterReadFlowWithAllInfo(FeeCriteriaEntity feeCriteriaEntity);

    FeeWaterReadFlow getFeeWaterReadFlowByFeeBillId(@Param("feeEleBillId") String feeEleBillId);

    FeeWaterReadFlow getLastRecord(@Param("id") String id,@Param("houseId") String houseId);

    FeeWaterReadFlow getCurrentReadByDateAndHouseId(@Param("waterReadDate") Date waterReadDate, @Param("houseId") String houseId);

}