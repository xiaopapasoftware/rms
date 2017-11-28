/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.other.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.other.entity.FeeOtherBill;
import com.thinkgem.jeesite.modules.fee.other.entity.vo.FeeOtherBillVo;
import com.thinkgem.jeesite.modules.fee.other.service.FeeOtherBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>宽带、电视费、其他账单表 controller</p>
 * <p>Table: fee_other_bill - 宽带、电视费、其他账单表</p>
 * @since 2017-11-28 03:02:33
 * @author generator code
 */
@RestController
@RequestMapping("/fee/other/bill")
public class FeeOtherBillController extends BaseController {

      @Autowired
      private FeeOtherBillService feeOtherBillService;

      @RequestMapping(value = "save")
      public Object save(FeeOtherBill feeOtherBill) {
          feeOtherBillService.saveFeeOtherBill(feeOtherBill);
          return ResponseData.success();
      }

    @RequestMapping(value = "list")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        feeCriteriaEntity.setPage(page);
        List<FeeOtherBillVo> feeOtherBillVos = feeOtherBillService.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
        page.setList(feeOtherBillVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "print")
    public Object print(FeeCriteriaEntity feeCriteriaEntity) {
        List<FeeOtherBillVo> feeOtherBillVos = feeOtherBillService.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
        return ResponseData.success().data(feeOtherBillVos);
    }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        feeOtherBillService.deleteFeeGasBill(id);
        return ResponseData.success();
    }

    @RequestMapping(value = "audit")
    public Object audit(String status, String... id) {
        feeOtherBillService.feeOtherBillAudit(status, id);
        return ResponseData.success();
    }

    @RequestMapping(value = "getTotalAmount")
    public Object getTotalAmount(FeeCriteriaEntity feeCriteriaEntity) {
        return ResponseData.success().data(feeOtherBillService.getTotalAmount(feeCriteriaEntity));
    }

}