/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;

/**
 * <p>燃气账单表实现类service</p>
 * <p>Table: fee_gas_bill - 燃气账单表</p>
 * @since 2017-10-20 06:26:27
 * @author generator code
*/
@MyBatisDao
public interface FeeGasBillDao extends CrudDao<FeeGasBill>{
}