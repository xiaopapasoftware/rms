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
import com.thinkgem.jeesite.modules.fee.order.entity.vo.FeeOrderVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> ids = Arrays.stream(id).collect(Collectors.toList());
        List<FeeOrder> feeOrders = dao.getFeeOrderByIds(ids);

        List<FeeOrder> updOrders = Lists.newArrayList();
        List<FeeOrderAccount> feeOrderAccounts = Lists.newArrayList();
        feeOrders.forEach(f -> {
            f.setOrderStatus(OrderStatusEnum.PASS.getValue());
            f.preUpdate();
            updOrders.add(f);

            feeOrderAccounts.add(feeOrderToAccount(f));
        });

        dao.batchUpdate(updOrders);
    }

    @Transactional(readOnly = false)
    public void repay(String... id) {
        List<String> ids = Arrays.stream(id).collect(Collectors.toList());
        List<FeeOrder> feeOrders = dao.getFeeOrderByIds(ids);
        List<FeeOrder> updOrders = Lists.newArrayList();
        feeOrders.forEach(f -> {
            f.setOrderStatus(OrderStatusEnum.REJECT.getValue());
            f.setDelFlag(Constants.DEL_FLAG_YES);
            f.preUpdate();
            updOrders.add(f);
        });

        dao.batchUpdate(updOrders);
    }

    private FeeOrderAccount feeOrderToAccount(FeeOrder feeOrder) {
        FeeOrderAccount feeOrderAccount = new FeeOrderAccount();
        feeOrderAccount.setAmount(feeOrder.getAmount());
        feeOrderAccount.setHouseId(feeOrder.getHouseId());
        feeOrderAccount.setOrderNo(feeOrder.getOrderNo());
        feeOrderAccount.setOrderType(feeOrder.getOrderType());
        feeOrderAccount.setOrderStatus(feeOrder.getOrderStatus());
        feeOrderAccount.setPayDate(new Date());
        feeOrderAccount.setPropertyId(feeOrder.getPropertyId());
        feeOrderAccount.setRoomId(feeOrder.getRoomId());
        feeOrderAccount.preInsert();
        return feeOrderAccount;
    }

    public List<FeeOrderVo> getFeeOrderList(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeOrderList(feeCriteriaEntity);
    }
}