/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import com.thinkgem.jeesite.modules.fee.gas.service.FeeGasReadFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>抄燃气表流水 controller</p>
 * <p>Table: fee_gas_read_flow - 抄燃气表流水</p>
 * @since 2017-10-20 06:26:38
 * @author generator code
 */
@RestController
@RequestMapping("/fee/gas/read/flow")
public class FeeGasReadFlowController extends FeeBaseController {

      @Autowired
      private FeeGasReadFlowService feeGasReadFlowService;

      @RequestMapping(value = "save")
      public Object save(FeeGasReadFlow feeGasReadFlow) {
          feeGasReadFlowService.save(feeGasReadFlow);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeGasReadFlow feeGasReadFlow,@RequestParam(value = "pageSize",defaultValue = "1") Integer pageSize,@RequestParam(value = "pageSize",defaultValue = "15") Integer pageNo) {
         Page<FeeGasReadFlow> page = feeGasReadFlowService.findPage(new Page(pageSize, pageNo), feeGasReadFlow);
         return ResponseData.success().data(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeGasReadFlow feeGasReadFlow = new FeeGasReadFlow();
        feeGasReadFlow.setId(id);
        feeGasReadFlow.setDelFlag(Constants.DEL_FLAG_NO);
        feeGasReadFlowService.save(feeGasReadFlow);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeGasReadFlow feeGasReadFlow = feeGasReadFlowService.get(id);
        return ResponseData.success().data(feeGasReadFlow);
   }

}