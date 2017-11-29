/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleChargedFlowVo;
import com.thinkgem.jeesite.modules.fee.electricity.service.FeeEleChargedFlowService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>电费收取流水 controller</p>
 * <p>Table: fee_ele_charged_flow - 电费收取流水</p>
 *
 * @author generator code
 * @since 2017-09-18 08:24:32
 */
@RestController
@RequestMapping("/fee/ele/charged/flow")
public class FeeEleChargedFlowController extends FeeBaseController {

    @Autowired
    private FeeEleChargedFlowService feeEleChargedFlowService;

    @RequestMapping(value = "list")
    @RequiresPermissions("fee:ele:charged:view")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        List<FeeEleChargedFlowVo> feeEleChargedFeeVos = feeEleChargedFlowService.getFeeEleChargedFee(feeCriteriaEntity);
        page.setList(feeEleChargedFeeVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "generateOrder")
    @RequiresPermissions("fee:ele:charge:generate:order")
    public Object generateOrder(){
        feeEleChargedFlowService.generatorOrder();
        return ResponseData.success();
    }
}