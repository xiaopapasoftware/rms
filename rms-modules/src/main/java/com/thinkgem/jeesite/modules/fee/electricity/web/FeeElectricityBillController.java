/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeElectricityBillVo;
import com.thinkgem.jeesite.modules.fee.electricity.service.FeeElectricityBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>电费账单表 controller</p>
 * <p>Table: fee_electricity_bill - 电费账单表</p>
 *
 * @author generator code
 * @since 2017-09-18 08:24:24
 */
@RestController
@RequestMapping("/fee/electricity/bill")
public class FeeElectricityBillController extends FeeBaseController {

    @Autowired
    private FeeElectricityBillService feeElectricityBillService;

    @RequestMapping(value = "save")
    public Object save(FeeElectricityBill feeElectricityBill) {
        feeElectricityBillService.saveFeeElectricityBill(feeElectricityBill);
        return ResponseData.success().data(feeElectricityBill.getId());
    }

    @RequestMapping(value = "list")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        feeCriteriaEntity.setPage(page);
        List<FeeElectricityBillVo> feeElectricityBillVos = feeElectricityBillService.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
        page.setList(feeElectricityBillVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "print")
    public Object print(FeeCriteriaEntity feeCriteriaEntity) {
        List<FeeElectricityBillVo> feeElectricityBillVos = feeElectricityBillService.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
        return ResponseData.success().data(feeElectricityBillVos);
    }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        feeElectricityBillService.deleteFeeElectricityBill(id);
        return ResponseData.success();
    }

    @RequestMapping(value = "audit")
    public Object audit(String status, String... id) {
        feeElectricityBillService.feeElectricityBillAudit(status, id);
        return ResponseData.success();
    }

    @RequestMapping(value = "getTotalAmount")
    public Object getTotalAmount(FeeCriteriaEntity feeCriteriaEntity) {
        return ResponseData.success().data(feeElectricityBillService.getTotalAmount(feeCriteriaEntity));
    }

}