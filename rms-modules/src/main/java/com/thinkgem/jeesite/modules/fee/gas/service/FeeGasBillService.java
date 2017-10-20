/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.gas.dao.FeeGasBillDao;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>燃气账单表实现类 service</p>
 * <p>Table: fee_gas_bill - 燃气账单表</p>
 * @since 2017-10-20 06:26:27
 * @author generator code
 */
@Service
@Transactional(readOnly = true)
public class FeeGasBillService extends CrudService<FeeGasBillDao, FeeGasBill>{

}