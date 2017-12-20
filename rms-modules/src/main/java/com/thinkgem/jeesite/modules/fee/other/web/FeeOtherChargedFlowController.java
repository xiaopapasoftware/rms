/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.other.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.other.entity.vo.FeeOtherChargedFlowVo;
import com.thinkgem.jeesite.modules.fee.other.service.FeeOtherChargedFlowService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>宽带、电视费、其他账单收取流水 controller</p>
 * <p>Table: fee_other_charged_flow - 宽带、电视费、其他账单收取流水</p>
 * @since 2017-11-28 03:02:42
 * @author generator code
 */
@RestController
@RequestMapping("/fee/other/charged/flow")
public class FeeOtherChargedFlowController extends FeeBaseController {

      @Autowired
      private FeeOtherChargedFlowService feeOtherChargedFlowService;

    @RequestMapping(value = "list")
    @RequiresPermissions("fee:other:charged:view")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        feeCriteriaEntity.setPage(page);
        List<FeeOtherChargedFlowVo> feeOtherChargedFeeVos = feeOtherChargedFlowService.getFeeOtherChargedFlow(feeCriteriaEntity);
        page.setList(feeOtherChargedFeeVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "generateFlow")
    @RequiresPermissions("fee:other:charged:generate:flow")
    public Object generateFlow(String scope,String businessId) {
        feeOtherChargedFlowService.generatorFlow(scope,businessId);
        return ResponseData.success();
    }

    @RequestMapping(value = "generateOrder")
    @RequiresPermissions("fee:other:charged:generate:flow")
    public Object generaterOrder() {
        feeOtherChargedFlowService.generatorOrder();
        return ResponseData.success();
    }
}