/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrderVo;
import com.thinkgem.jeesite.modules.fee.order.service.FeeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>订单 controller</p>
 * <p>Table: fee_order - 订单</p>
 *
 * @author generator code
 * @since 2017-11-05 10:05:53
 */
@RestController
@RequestMapping("/fee/order")
public class FeeOrderController extends BaseController {

    @Autowired
    private FeeOrderService feeOrderService;

    @RequestMapping(value = "list")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        feeCriteriaEntity.setPage(page);
        List<FeeOrderVo> feeOrderVoList = feeOrderService.getFeeOrderList(feeCriteriaEntity);
        page.setList(feeOrderVoList);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "payed")
    public Object payed(String... id) {
        feeOrderService.payed(id);
        return ResponseData.success();
    }

    @RequestMapping(value = "repay")
    public Object repay(String... id) {
        feeOrderService.repay(id);
        return ResponseData.success();
    }

}