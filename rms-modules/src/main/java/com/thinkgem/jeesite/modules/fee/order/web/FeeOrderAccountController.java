/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrderAccount;

import com.thinkgem.jeesite.modules.fee.order.service.FeeOrderAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>订单台账 controller</p>
 * <p>Table: fee_order_account - 订单台账</p>
 * @since 2017-11-05 10:05:47
 * @author generator code
 */
@RestController
@RequestMapping("/fee/order/account")
public class FeeOrderAccountController extends BaseController {

      @Autowired
      private FeeOrderAccountService feeOrderAccountService;

      @RequestMapping(value = "save")
      public Object save(FeeOrderAccount feeOrderAccount) {
          feeOrderAccountService.save(feeOrderAccount);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeOrderAccount feeOrderAccount,@RequestParam(value = "pageSize",defaultValue = "1") Integer pageSize,@RequestParam(value = "pageSize",defaultValue = "15") Integer pageNo) {
         Page<FeeOrderAccount> page = feeOrderAccountService.findPage(new Page(pageSize, pageNo), feeOrderAccount);
         return ResponseData.success().data(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeOrderAccount feeOrderAccount = new FeeOrderAccount();
        feeOrderAccount.setId(id);
        feeOrderAccount.setDelFlag(Constants.DEL_FLAG_NO);
        feeOrderAccountService.save(feeOrderAccount);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeOrderAccount feeOrderAccount = feeOrderAccountService.get(id);
        return ResponseData.success().data(feeOrderAccount);
   }

}