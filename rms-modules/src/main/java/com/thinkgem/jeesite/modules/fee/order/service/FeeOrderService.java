/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.order.dao.FeeOrderDao;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>订单实现类 service</p>
 * <p>Table: fee_order - 订单</p>
 * @since 2017-11-05 10:05:53
 * @author generator code
 */
@Service
@Transactional(readOnly = true)
public class FeeOrderService extends CrudService<FeeOrderDao, FeeOrder> {

}