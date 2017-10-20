/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import com.thinkgem.jeesite.modules.fee.gas.service.FeeGasBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>燃气账单表 controller</p>
 * <p>Table: fee_gas_bill - 燃气账单表</p>
 * @since 2017-10-20 06:26:27
 * @author generator code
 */
@RestController
@RequestMapping("/fee/gas/bill")
public class FeeGasBillController extends FeeBaseController {

      @Autowired
      private FeeGasBillService feeGasBillService;

      @RequestMapping(value = "save")
      public Object save(FeeGasBill feeGasBill) {
          feeGasBillService.save(feeGasBill);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeGasBill feeGasBill,@RequestParam(value = "pageSize",defaultValue = "1") Integer pageSize,@RequestParam(value = "pageSize",defaultValue = "15") Integer pageNo) {
         Page<FeeGasBill> page = feeGasBillService.findPage(new Page(pageSize, pageNo), feeGasBill);
         return ResponseData.success().data(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeGasBill feeGasBill = new FeeGasBill();
        feeGasBill.setId(id);
        feeGasBill.setDelFlag(Constants.DEL_FLAG_NO);
        feeGasBillService.save(feeGasBill);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeGasBill feeGasBill = feeGasBillService.get(id);
        return ResponseData.success().data(feeGasBill);
   }

}