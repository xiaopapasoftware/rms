/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.gas.dao.FeeGasReadFlowDao;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>抄燃气表流水实现类 service</p>
 * <p>Table: fee_gas_read_flow - 抄燃气表流水</p>
 * @since 2017-10-20 06:26:38
 * @author generator code
 */
@Service
@Transactional(readOnly = true)
public class FeeGasReadFlowService extends CrudService<FeeGasReadFlowDao, FeeGasReadFlow> {

}