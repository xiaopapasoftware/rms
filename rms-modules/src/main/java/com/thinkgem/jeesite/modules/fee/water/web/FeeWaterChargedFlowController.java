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
    public Object save(FeeWaterChargedFlow feeWaterChargedFlow) {
        feeWaterChargedFlowService.save(feeWaterChargedFlow);
        return ResponseData.success();
    }

    @RequestMapping(value = "list")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        List<FeeWaterChargedFlowVo> feeWaterChargedFeeVos = feeWaterChargedFlowService.getFeeWaterChargedFee(feeCriteriaEntity);
        page.setList(feeWaterChargedFeeVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeWaterChargedFlow feeWaterChargedFlow = new FeeWaterChargedFlow();
        feeWaterChargedFlow.setId(id);
        feeWaterChargedFlow.setDelFlag(Constants.DEL_FLAG_NO);
        feeWaterChargedFlowService.save(feeWaterChargedFlow);
        return ResponseData.success();
    }

    @RequestMapping(value = "get")
    public Object get(String id) {
        FeeWaterChargedFlow feeWaterChargedFlow = feeWaterChargedFlowService.get(id);
        return ResponseData.success().data(feeWaterChargedFlow);
    }

    @RequestMapping(value = "generatorFlow")
    public Object generatorFlow() {
        feeWaterChargedFlowService.generatorFlow();
        return ResponseData.success();
    }

    @RequestMapping(value = "generatorOrder")
    public Object generatorOrder() {
        feeWaterChargedFlowService.generatorOrder();
        return ResponseData.success();
    }
}