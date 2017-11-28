/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.other.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.other.entity.FeeOtherChargedFlow;
import com.thinkgem.jeesite.modules.fee.other.entity.vo.FeeOtherChargedFlowVo;
import com.thinkgem.jeesite.modules.fee.other.service.FeeOtherChargedFlowService;
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
public class FeeOtherChargedFlowController extends BaseController {

      @Autowired
      private FeeOtherChargedFlowService feeOtherChargedFlowService;

      @RequestMapping(value = "save")
      public Object save(FeeOtherChargedFlow feeOtherChargedFlow) {
          feeOtherChargedFlowService.save(feeOtherChargedFlow);
          return ResponseData.success();
      }

    @RequestMapping(value = "list")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        List<FeeOtherChargedFlowVo> feeOtherChargedFeeVos = feeOtherChargedFlowService.getFeeOtherChargedFlow(feeCriteriaEntity);
        page.setList(feeOtherChargedFeeVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeOtherChargedFlow feeOtherChargedFlow = new FeeOtherChargedFlow();
        feeOtherChargedFlow.setId(id);
        feeOtherChargedFlow.setDelFlag(Constants.DEL_FLAG_NO);
        feeOtherChargedFlowService.save(feeOtherChargedFlow);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeOtherChargedFlow feeOtherChargedFlow = feeOtherChargedFlowService.get(id);
        return ResponseData.success().data(feeOtherChargedFlow);
   }

    @RequestMapping(value = "generateFlow")
    public Object generateFlow() {
        feeOtherChargedFlowService.generatorFlow();
        return ResponseData.success();
    }

    @RequestMapping(value = "generateOrder")
    public Object generaterOrder() {
        feeOtherChargedFlowService.generatorOrder();
        return ResponseData.success();
    }
}