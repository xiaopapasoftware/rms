/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasBillVo;
import com.thinkgem.jeesite.modules.fee.gas.service.FeeGasBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>燃气账单表 controller</p>
 * <p>Table: fee_gas_bill - 燃气账单表</p>
 *
 * @author generator code
 * @since 2017-10-20 06:26:27
 */
@RestController
@RequestMapping("/fee/gas/bill")
public class FeeGasBillController extends FeeBaseController {

    @Autowired
    private FeeGasBillService feeGasBillService;

    @RequestMapping(value = "save")
    public Object save(FeeGasBill feeGasBill) {
        feeGasBillService.saveFeeGasBill(feeGasBill);
        return ResponseData.success();
    }

    @RequestMapping(value = "list")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        feeCriteriaEntity.setPage(page);
        List<FeeGasBillVo> feeGasBillVos = feeGasBillService.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
        page.setList(feeGasBillVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "print")
    public Object print(FeeCriteriaEntity feeCriteriaEntity) {
        List<FeeGasBillVo> feeGasBillVos = feeGasBillService.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
        return ResponseData.success().data(feeGasBillVos);
    }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        feeGasBillService.deleteFeeGasBill(id);
        return ResponseData.success();
    }

    @RequestMapping(value = "audit")
    public Object audit(String status, String... id) {
        feeGasBillService.feeGasBillAudit(status, id);
        return ResponseData.success();
    }

    @RequestMapping(value = "getTotalAmount")
    public Object getTotalAmount(FeeCriteriaEntity feeCriteriaEntity) {
        return ResponseData.success().data(feeGasBillService.getTotalAmount(feeCriteriaEntity));
    }

}