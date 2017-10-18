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
import com.thinkgem.jeesite.modules.fee.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.common.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.common.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleReadFlowVo;
import com.thinkgem.jeesite.modules.fee.electricity.service.FeeEleReadFlowService;
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
    private SelectItemService selectItemService;

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeEleReadFlowService feeEleReadFlowService;

    @RequestMapping(value = "save")
    public Object save(FeeEleReadFlow feeEleReadFlow, HttpServletRequest request) {
        String[] roomIds = request.getParameterValues("roomIds");
        String[] eleDegrees = request.getParameterValues("eleDegrees");
        feeEleReadFlowService.feeEleReadFlowSave(feeEleReadFlow, roomIds, eleDegrees);
        return ResponseData.success();
    }

    @RequestMapping(value = "list")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageNum(), feeCriteriaEntity.getPageSize());
        feeCriteriaEntity.setPage(page);
        List<FeeEleReadFlowVo> feeEleReadFlowVos = feeEleReadFlowService.getFeeEleReadFlowWithAllInfo(feeCriteriaEntity);
        page.setList(feeEleReadFlowVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        feeEleReadFlowService.deleteFeeEleReadFlow(id);
        return ResponseData.success();
    }

    @RequestMapping(value = "getSubOrgList")
    public Object getSubOrgList(SelectItemCondition condition) {
        List<SelectItem> selectItems = selectItemService.getSelectListByBusinessCode(condition);
        return ResponseData.success().data(selectItems);
    }

    @RequestMapping(value = "getArea")
    public Object getArea() {
        List<SelectItem> selectItems = feeCommonService.getAreaWithAuth();
        return ResponseData.success().data(selectItems);
    }

    @RequestMapping(value = "houseInfo")
    public Object houseInfo(String accountNum) {
        return ResponseData.success().data(feeCommonService.getHouseByQueryWhereAndType(accountNum, "3"));
    }

    @RequestMapping(value = "roomInfo")
    public Object roomInfo(String houseId) {
        return ResponseData.success().data(feeCommonService.getRoomByHouseId(houseId));
    }
}