/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.web;


import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleReadFlowVo;
import com.thinkgem.jeesite.modules.fee.electricity.service.FeeEleReadFlowService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>抄电表流水 controller</p>
 * <p>Table: fee_ele_read_flow - 抄电表流水</p>
 *
 * @author generator code
 * @since 2017-09-18 08:24:39
 */
@RestController
@RequestMapping("/fee/ele/read/flow")
public class FeeEleReadFlowController extends FeeBaseController {

    @Autowired
    private FeeEleReadFlowService feeEleReadFlowService;

    @RequestMapping(value = "save")
    @RequiresPermissions("fee:ele:read:add")
    public Object save(FeeEleReadFlow feeEleReadFlow, HttpServletRequest request) {
        String[] roomIds = request.getParameterValues("roomIds");
        String[] eleDegrees = request.getParameterValues("eleDegrees");
        feeEleReadFlowService.feeEleReadFlowSave(feeEleReadFlow, roomIds, eleDegrees);
        return ResponseData.success();
    }

    @RequestMapping(value = "list")
    @RequiresPermissions("fee:ele:read:view")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        feeCriteriaEntity.setPage(page);
        List<FeeEleReadFlowVo> feeEleReadFlowVos = feeEleReadFlowService.getFeeEleReadFlowWithAllInfo(feeCriteriaEntity);
        page.setList(feeEleReadFlowVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "delete")
    @RequiresPermissions("fee:ele:read:delete")
    public Object delete(String id) {
        feeEleReadFlowService.deleteFeeEleReadFlow(id);
        return ResponseData.success();
    }

}