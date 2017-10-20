/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterReadFlow;
import com.thinkgem.jeesite.modules.fee.water.service.FeeWaterReadFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>抄水表流水 controller</p>
 * <p>Table: fee_water_read_flow - 抄水表流水</p>
 * @since 2017-10-20 06:26:14
 * @author generator code
 */
@RestController
@RequestMapping("/fee/water/read/flow")
public class FeeWaterReadFlowController extends FeeBaseController {

      @Autowired
      private FeeWaterReadFlowService feeWaterReadFlowService;

      @RequestMapping(value = "save")
      public Object save(FeeWaterReadFlow feeWaterReadFlow) {
          feeWaterReadFlowService.save(feeWaterReadFlow);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeWaterReadFlow feeWaterReadFlow,@RequestParam(value = "pageSize",defaultValue = "1") Integer pageSize,@RequestParam(value = "pageSize",defaultValue = "15") Integer pageNo) {
         Page<FeeWaterReadFlow> page = feeWaterReadFlowService.findPage(new Page(pageSize, pageNo), feeWaterReadFlow);
         return ResponseData.success().data(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeWaterReadFlow feeWaterReadFlow = new FeeWaterReadFlow();
        feeWaterReadFlow.setId(id);
        feeWaterReadFlow.setDelFlag(Constants.DEL_FLAG_NO);
        feeWaterReadFlowService.save(feeWaterReadFlow);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeWaterReadFlow feeWaterReadFlow = feeWaterReadFlowService.get(id);
        return ResponseData.success().data(feeWaterReadFlow);
   }

}