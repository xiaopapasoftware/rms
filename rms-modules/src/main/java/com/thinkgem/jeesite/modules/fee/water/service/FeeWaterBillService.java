/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.water.dao.FeeWaterBillDao;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>水费账单表实现类 service</p>
 * <p>Table: fee_water_bill - 水费账单表</p>
 * @since 2017-10-20 06:25:59
 * @author generator code
 */
@Service
@Transactional(readOnly = true)
public class FeeWaterBillService extends CrudService<FeeWaterBillDao, FeeWaterBill>{

}