/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.enums.OrderStatusEnum;
import com.thinkgem.jeesite.modules.fee.enums.OrderTypeEnum;
import com.thinkgem.jeesite.modules.fee.order.dao.FeeOrderDao;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrder;
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
        Arrays.stream(id).forEach(s -> {
            FeeOrder feeOrder = dao.get(s);
            FeeOrder updOrder = new FeeOrder();
            updOrder.setId(feeOrder.getId());
            updOrder.setOrderStatus(OrderStatusEnum.PASS.getValue());

        });
    }

    @Transactional(readOnly = false)
    public void repay(String... id) {
        Arrays.stream(id).forEach(s -> {
            FeeOrder feeOrder = dao.get(s);

        });
    }

    public List<FeeOrderVo> getFeeOrderList(FeeCriteriaEntity feeCriteriaEntity) {

        return null;
    }
}