/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleChargedFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleChargedFeeVo;
import com.thinkgem.jeesite.modules.fee.electricity.service.FeeEleChargedFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>电费收取流水 controller</p>
 * <p>Table: fee_ele_charged_flow - 电费收取流水</p>
 * @since 2017-09-18 08:24:32
 * @author generator code
 */
@RestController
@RequestMapping("/fee/ele/charged/flow")
public class FeeEleChargedFlowController extends BaseController {

      @Autowired
      private FeeEleChargedFlowService feeEleChargedFlowService;

      @RequestMapping(value = "save")
      public Object save(FeeEleChargedFlow feeEleChargedFlow) {
          feeEleChargedFlowService.save(feeEleChargedFlow);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeCriteriaEntity feeCriteriaEntity) {
         Page page = new Page(feeCriteriaEntity.getPageNum(),feeCriteriaEntity.getPageSize());
         List<FeeEleChargedFeeVo> feeEleChargedFeeVos = feeEleChargedFlowService.getFeeEleChargedFee(feeCriteriaEntity);
         page.setList(feeEleChargedFeeVos);
         return ResponseData.success().page(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeEleChargedFlow feeEleChargedFlow = new FeeEleChargedFlow();
        feeEleChargedFlow.setId(id);
        feeEleChargedFlow.setDelFlag(Constants.DEL_FLAG_NO);
        feeEleChargedFlowService.save(feeEleChargedFlow);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeEleChargedFlow feeEleChargedFlow = feeEleChargedFlowService.get(id);
        return ResponseData.success().data(feeEleChargedFlow);
   }

}