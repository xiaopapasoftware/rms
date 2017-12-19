/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterChargedFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterChargedFlowVo;
import com.thinkgem.jeesite.modules.fee.water.service.FeeWaterChargedFlowService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>水收取流水 controller</p>
 * <p>Table: fee_water_charged_flow - 水收取流水</p>
 *
 * @author generator code
 * @since 2017-10-20 06:26:08
 */
@RestController
@RequestMapping("/fee/water/charged/flow")
public class FeeWaterChargedFlowController extends FeeBaseController {

    @Autowired
    private FeeWaterChargedFlowService feeWaterChargedFlowService;

    @RequestMapping(value = "save")
    @RequiresPermissions("fee:water:charged:add")
    public Object save(FeeWaterChargedFlow feeWaterChargedFlow) {
        feeWaterChargedFlowService.save(feeWaterChargedFlow);
        return ResponseData.success();
    }

    @RequestMapping(value = "list")
    @RequiresPermissions("fee:water:charged:view")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        feeCriteriaEntity.setPage(page);
        List<FeeWaterChargedFlowVo> feeWaterChargedFeeVos = feeWaterChargedFlowService.getFeeWaterChargedFee(feeCriteriaEntity);
        page.setList(feeWaterChargedFeeVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "generateFlow")
    @RequiresPermissions("fee:water:charged:generate:flow")
    public Object generateFlow(String scope,String businessId) {
        feeWaterChargedFlowService.generatorFlow(scope,businessId);
        return ResponseData.success();
    }

    @RequestMapping(value = "generateOrder")
    @RequiresPermissions("fee:water:charged:generate:order")
    public Object generateOrder() {
        feeWaterChargedFlowService.generatorOrder();
        return ResponseData.success();
    }
}