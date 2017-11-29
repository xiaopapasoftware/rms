/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasChargedFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasChargedFlowVo;
import com.thinkgem.jeesite.modules.fee.gas.service.FeeGasChargedFlowService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>煤气收取流水 controller</p>
 * <p>Table: fee_gas_charged_flow - 煤气收取流水</p>
 *
 * @author generator code
 * @since 2017-10-20 06:26:33
 */
@RestController
@RequestMapping("/fee/gas/charged/flow")
public class FeeGasChargedFlowController extends FeeBaseController {

    @Autowired
    private FeeGasChargedFlowService feeGasChargedFlowService;

    @RequestMapping(value = "list")
    @RequiresPermissions("fee:gas:charged:view")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        List<FeeGasChargedFlowVo> feeGasChargedFeeVos = feeGasChargedFlowService.getFeeGasChargedFee(feeCriteriaEntity);
        page.setList(feeGasChargedFeeVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "generateFlow")
    @RequiresPermissions("fee:gas:charge:generate:flow")
    public Object generateFlow() {
        feeGasChargedFlowService.generatorFlow();
        return ResponseData.success();
    }

    @RequestMapping(value = "generateOrder")
    @RequiresPermissions("fee:gas:charge:generate:order")
    public Object generaterOrder() {
        feeGasChargedFlowService.generatorOrder();
        return ResponseData.success();
    }
}