/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrderAccount;

import java.util.List;

/**
 * <p>订单台账实现类service</p>
 * <p>Table: fee_order_account - 订单台账</p>
 * @since 2017-11-05 10:05:47
 * @author generator code
*/
@MyBatisDao
public interface FeeOrderAccountDao extends CrudDao<FeeOrderAccount>{

    void batchInsert(List<FeeOrderAccount> feeOrderAccounts);
}