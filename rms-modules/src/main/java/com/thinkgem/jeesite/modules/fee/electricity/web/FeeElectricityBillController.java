/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.fee.common.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeElectricityBillVo;
import com.thinkgem.jeesite.modules.fee.electricity.service.FeeElectricityBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>电费账单表 controller</p>
 * <p>Table: fee_electricity_bill - 电费账单表</p>
 * @since 2017-09-18 08:24:24
 * @author generator code
 */
@RestController
@RequestMapping("/fee/electricity/bill")
public class FeeElectricityBillController extends BaseController {

    @Autowired
    private SelectItemService selectItemService;

    @Autowired
    private FeeElectricityBillService feeElectricityBillService;

    @RequestMapping(value = "save")
    public Object save(FeeElectricityBill feeElectricityBill) {
        feeElectricityBillService.saveFeeElectricityBill(feeElectricityBill);
        return ResponseData.success().data(feeElectricityBill.getId());
    }

    @RequestMapping(value = "list")
    public Object list(FeeCriteriaEntity feeCriteriaEntity) {
        Page page = new Page(feeCriteriaEntity.getPageSize(), feeCriteriaEntity.getPageNum());
        feeCriteriaEntity.setPage(page);
        List<FeeElectricityBillVo> feeElectricityBillVos = feeElectricityBillService.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
        page.setList(feeElectricityBillVos);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeElectricityBill feeElectricityBill = new FeeElectricityBill();
        feeElectricityBill.setId(id);
        feeElectricityBill.setDelFlag(Constants.DEL_FLAG_YES);
        feeElectricityBillService.save(feeElectricityBill);
        return ResponseData.success();
    }

    @RequestMapping(value = "get")
    public Object get(String id,String houseId) {
        FeeElectricityBillVo feeElectricityBillVo = feeElectricityBillService.getWithProperty(id,houseId);
        return ResponseData.success().data(feeElectricityBillVo);
    }

    @RequestMapping(value = "getSubOrgList")
    public List<SelectItem> getSubOrgList(SelectItemCondition condition) {
        return selectItemService.getSelectListByBusinessCode(condition);
    }


}