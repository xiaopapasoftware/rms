/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.config.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.fee.common.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.config.entity.vo.FeeConfigVo;
import com.thinkgem.jeesite.modules.fee.config.service.FeeConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>费用配置项 controller</p>
 * <p>Table: fee_config - 费用配置项</p>
 *
 * @author generator code
 * @since 2017-09-18 08:14:27
 */
@RestController
@RequestMapping("/fee/config")
public class FeeConfigController extends BaseController {

    @Autowired
    private SelectItemService selectItemService;

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeConfigService feeConfigService;

    @RequestMapping(value = "save")
    public Object save(FeeConfig feeConfig) {
        feeConfigService.saveFeeConfig(feeConfig);
        return ResponseData.success();
    }

    @RequestMapping(value = "list")
    public Object list(FeeConfig feeConfig, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        List<FeeConfigVo> feeConfigList = feeConfigService.getFeeConfigList(feeConfig);
        page.setList(feeConfigList);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeConfig feeConfig = new FeeConfig();
        feeConfig.setId(id);
        feeConfig.setDelFlag(Constants.DEL_FLAG_YES);
        feeConfigService.save(feeConfig);
        return ResponseData.success();
    }

    @RequestMapping(value = "changeConfigStatus")
    public Object changeConfigStatus(String id, int configStatus) {
        FeeConfig feeConfig = new FeeConfig();
        feeConfig.setId(id);
        feeConfig.setConfigStatus(configStatus);
        feeConfigService.save(feeConfig);
        return ResponseData.success();
    }

    @RequestMapping(value = "get")
    public Object get(String id) {
        FeeConfig feeConfig = feeConfigService.get(id);
        return ResponseData.success().data(feeConfig);
    }

    @RequestMapping(value = "getArea")
    public Object getArea(String type) {
        List<SelectItem> selectItems = feeCommonService.getAreaWithAuthByType(type);
        return ResponseData.success().data(selectItems);
    }

    @RequestMapping(value = "getSubOrgList")
    public Object getSubOrgList(SelectItemCondition condition) {
        List<SelectItem> selectItems = selectItemService.getSelectListByBusinessCode(condition);
        return ResponseData.success().data(selectItems);
    }

}