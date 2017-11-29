/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasReadFlowVo;
import com.thinkgem.jeesite.modules.fee.gas.service.FeeGasReadFlowService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
      @RequiresPermissions("fee:gas:read:add")
      public Object save(FeeGasReadFlow feeGasReadFlow) {
          feeGasReadFlowService.saveFeeGasReadFlow(feeGasReadFlow);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     @RequiresPermissions("fee:gas:read:view")
     public Object list(FeeCriteriaEntity feeCriteriaEntity) {
         Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
         feeCriteriaEntity.setPage(page);
         List<FeeGasReadFlowVo> feeGasReadFlowVos = feeGasReadFlowService.getFeeGasReadFlowWithAllInfo(feeCriteriaEntity);
         page.setList(feeGasReadFlowVos);
         return ResponseData.success().page(page);
     }

    @RequestMapping(value = "delete")
    @RequiresPermissions("fee:gas:read:delete")
    public Object delete(String id) {
        feeGasReadFlowService.deleteFeeGasReadFlow(id);
        return ResponseData.success();
    }
}