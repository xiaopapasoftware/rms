/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.enums.OrderStatusEnum;
import com.thinkgem.jeesite.modules.fee.order.dao.FeeOrderDao;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrder;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrderAccount;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrderVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>订单实现类 service</p>
 * <p>Table: fee_order - 订单</p>
 *
 * @author generator code
 * @since 2017-11-05 10:05:53
 */
@Service
@Transactional(readOnly = true)
public class FeeOrderService extends CrudService<FeeOrderDao, FeeOrder> {

    public void batchInsert(List<FeeOrder> feeOrders) {
        dao.batchInsert(feeOrders);
    }

    @Transactional(readOnly = false)
    public void payed(String... id) {
        List<FeeOrder> feeOrders = Lists.newArrayList();
        List<FeeOrderAccount> feeOrderAccounts = Lists.newArrayList();
        Arrays.stream(id).forEach(s -> {
            FeeOrder feeOrder = dao.get(s);
            feeOrder.setOrderStatus(OrderStatusEnum.PASS.getValue());
            feeOrders.add(feeOrder);

            feeOrderAccounts.add(feeOrderToAccount(feeOrder));
        });

    }

    @Transactional(readOnly = false)
    public void repay(String... id) {
        List<FeeOrder> feeOrders = Lists.newArrayList();
        Arrays.stream(id).forEach(s -> {
            FeeOrder feeOrder = dao.get(s);
            feeOrder.setOrderStatus(OrderStatusEnum.REJECT.getValue());
            feeOrder.setDelFlag(Constants.DEL_FLAG_YES);
            feeOrders.add(feeOrder);
        });
    }

    private FeeOrderAccount feeOrderToAccount(FeeOrder feeOrder){
        FeeOrderAccount feeOrderAccount = new FeeOrderAccount();

        return feeOrderAccount;
    }

    public List<FeeOrderVo> getFeeOrderList(FeeCriteriaEntity feeCriteriaEntity) {

        return null;
    }
}