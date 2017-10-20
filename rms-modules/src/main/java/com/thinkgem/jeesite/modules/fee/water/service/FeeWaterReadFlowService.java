/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.water.dao.FeeWaterReadFlowDao;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterReadFlow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>抄水表流水实现类 service</p>
 * <p>Table: fee_water_read_flow - 抄水表流水</p>
 * @since 2017-10-20 06:26:14
 * @author generator code
 */
@Service
@Transactional(readOnly = true)
public class FeeWaterReadFlowService extends CrudService<FeeWaterReadFlowDao, FeeWaterReadFlow>{

}