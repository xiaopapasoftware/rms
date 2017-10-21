/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import com.thinkgem.jeesite.modules.fee.water.service.FeeWaterBillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>水费账单表 controller</p>
 * <p>Table: fee_water_bill - 水费账单表</p>
 * @since 2017-10-20 06:25:59
 * @author generator code
 */
@RestController
@RequestMapping("/fee/water/bill")
public class FeeWaterBillController extends FeeBaseController {

      @Autowired
      private FeeWaterBillService feeWaterBillService;

      @RequestMapping(value = "save")
      public Object save(FeeWaterBill feeWaterBill) {
          feeWaterBillService.save(feeWaterBill);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeWaterBill feeWaterBill,@RequestParam(value = "pageSize",defaultValue = "1") Integer pageSize,@RequestParam(value = "pageSize",defaultValue = "15") Integer pageNo) {
         Page<FeeWaterBill> page = feeWaterBillService.findPage(new Page(pageSize, pageNo), feeWaterBill);
         return ResponseData.success().data(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeWaterBill feeWaterBill = new FeeWaterBill();
        feeWaterBill.setId(id);
        feeWaterBill.setDelFlag(Constants.DEL_FLAG_NO);
        feeWaterBillService.save(feeWaterBill);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeWaterBill feeWaterBill = feeWaterBillService.get(id);
        return ResponseData.success().data(feeWaterBill);
   }

}