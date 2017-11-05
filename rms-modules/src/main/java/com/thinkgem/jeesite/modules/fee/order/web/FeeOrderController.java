/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrder;
import com.thinkgem.jeesite.modules.fee.order.service.FeeOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>订单 controller</p>
 * <p>Table: fee_order - 订单</p>
 * @since 2017-11-05 10:05:53
 * @author generator code
 */
@RestController
@RequestMapping("/fee/order")
public class FeeOrderController extends BaseController {

      @Autowired
      private FeeOrderService feeOrderService;

      @RequestMapping(value = "save")
      public Object save(FeeOrder feeOrder) {
          feeOrderService.save(feeOrder);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeOrder feeOrder,@RequestParam(value = "pageSize",defaultValue = "1") Integer pageSize,@RequestParam(value = "pageSize",defaultValue = "15") Integer pageNo) {
         Page<FeeOrder> page = feeOrderService.findPage(new Page(pageSize, pageNo), feeOrder);
         return ResponseData.success().data(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeOrder feeOrder = new FeeOrder();
        feeOrder.setId(id);
        feeOrder.setDelFlag(Constants.DEL_FLAG_NO);
        feeOrderService.save(feeOrder);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeOrder feeOrder = feeOrderService.get(id);
        return ResponseData.success().data(feeOrder);
   }

}