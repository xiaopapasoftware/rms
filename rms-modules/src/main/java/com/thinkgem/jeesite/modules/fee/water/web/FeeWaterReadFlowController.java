/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterReadFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterReadFlowVo;
import com.thinkgem.jeesite.modules.fee.water.service.FeeWaterReadFlowService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
      @RequiresPermissions("fee:water:read:add")
      public Object save(FeeWaterReadFlow feeWaterReadFlow) {
          feeWaterReadFlowService.saveFeeWaterReadFlow(feeWaterReadFlow);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     @RequiresPermissions("fee:water:read:view")
     public Object list(FeeCriteriaEntity feeCriteriaEntity) {
         Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
         feeCriteriaEntity.setPage(page);
         List<FeeWaterReadFlowVo> feeWaterReadFlowVos = feeWaterReadFlowService.getFeeWaterReadFlowWithAllInfo(feeCriteriaEntity);
         page.setList(feeWaterReadFlowVos);
         return ResponseData.success().page(page);
     }

    @RequestMapping(value = "delete")
    @RequiresPermissions("fee:water:read:delete")
    public Object delete(String id) {
        feeWaterReadFlowService.deleteFeeWaterReadFlow(id);
        return ResponseData.success();
    }

}