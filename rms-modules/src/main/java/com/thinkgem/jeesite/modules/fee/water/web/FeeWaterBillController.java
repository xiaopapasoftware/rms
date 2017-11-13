/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterBillVo;
import com.thinkgem.jeesite.modules.fee.water.service.FeeWaterBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
          feeWaterBillService.saveFeeWaterBill(feeWaterBill);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeCriteriaEntity feeCriteriaEntity) {
         Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
         feeCriteriaEntity.setPage(page);
         List<FeeWaterBillVo> feeWaterBillVos = feeWaterBillService.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
         page.setList(feeWaterBillVos);
         return ResponseData.success().page(page);
     }

    @RequestMapping(value = "print")
    public Object print(FeeCriteriaEntity feeCriteriaEntity) {
        List<FeeWaterBillVo> feeWaterBillVos = feeWaterBillService.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
        return ResponseData.success().data(feeWaterBillVos);
    }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        feeWaterBillService.deleteFeeWaterBill(id);
        return ResponseData.success();
    }

    @RequestMapping(value = "audit")
    public Object audit(String status, String... id) {
        feeWaterBillService.feeWaterBillAudit(status, id);
        return ResponseData.success();
    }

    @RequestMapping(value = "getTotalAmount")
    public Object getTotalAmount(FeeCriteriaEntity feeCriteriaEntity) {
        return ResponseData.success().data(feeWaterBillService.getTotalAmount(feeCriteriaEntity));
    }

}