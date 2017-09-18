/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.service.FeeEleReadFlowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>抄电表流水 controller</p>
 * <p>Table: fee_ele_read_flow - 抄电表流水</p>
 * @since 2017-09-18 08:24:39
 * @author generator code
 */
@RestController
@RequestMapping("/fee/ele/read/flow")
public class FeeEleReadFlowController extends BaseController {

      @Autowired
      private FeeEleReadFlowService feeEleReadFlowService;

      @RequestMapping(value = "save")
      public Object save(FeeEleReadFlow feeEleReadFlow) {
          feeEleReadFlowService.save(feeEleReadFlow);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeEleReadFlow feeEleReadFlow,@RequestParam(value = "pageSize",defaultValue = "1") Integer pageSize,@RequestParam(value = "pageSize",defaultValue = "15") Integer pageNo) {
         Page<FeeEleReadFlow> page = feeEleReadFlowService.findPage(new Page(pageSize, pageNo), feeEleReadFlow);
         return ResponseData.success().data(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeEleReadFlow feeEleReadFlow = new FeeEleReadFlow();
        feeEleReadFlow.setId(id);
        feeEleReadFlow.setDelFlag(Constants.DEL_FLAG_NO);
        feeEleReadFlowService.save(feeEleReadFlow);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeEleReadFlow feeEleReadFlow = feeEleReadFlowService.get(id);
        return ResponseData.success().data(feeEleReadFlow);
   }

}