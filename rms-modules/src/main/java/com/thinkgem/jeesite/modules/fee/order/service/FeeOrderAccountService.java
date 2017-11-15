/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.order.dao.FeeOrderAccountDao;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrderAccount;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <p>订单台账实现类 service</p>
 * <p>Table: fee_order_account - 订单台账</p>
 *
 * @author generator code
 * @since 2017-11-05 10:05:47
 */
@Service
@Transactional(readOnly = true)
public class FeeOrderAccountService extends CrudService<FeeOrderAccountDao, FeeOrderAccount> {

    void batchInsert(List<FeeOrderAccount> feeOrderAccounts) {
        batchInsert(feeOrderAccounts);
    }
}