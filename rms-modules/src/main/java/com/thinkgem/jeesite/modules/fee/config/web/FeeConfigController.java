/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.config.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.common.web.FeeBaseController;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.config.entity.vo.FeeConfigVo;
import com.thinkgem.jeesite.modules.fee.config.service.FeeConfigService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
public class FeeConfigController extends FeeBaseController {

    @Autowired
    private FeeConfigService feeConfigService;

    @RequestMapping(value = "save")
    @RequiresPermissions("fee:config:add")
    public Object save(FeeConfig feeConfig) {
        feeConfigService.saveFeeConfig(feeConfig);
        return ResponseData.success();
    }

    @RequestMapping(value = "list")
    @RequiresPermissions("fee:config:view")
    public Object list(FeeConfig feeConfig, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "15") Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        List<FeeConfigVo> feeConfigList = feeConfigService.getFeeConfigList(feeConfig);
        page.setList(feeConfigList);
        return ResponseData.success().page(page);
    }

    @RequestMapping(value = "delete")
    @RequiresPermissions("fee:config:delete")
    public Object delete(String id) {
        FeeConfig feeConfig = new FeeConfig();
        feeConfig.setId(id);
        feeConfig.setDelFlag(Constants.DEL_FLAG_YES);
        feeConfigService.save(feeConfig);
        return ResponseData.success();
    }

    @RequestMapping(value = "changeConfigStatus")
    @RequiresPermissions("fee:config:stop")
    public Object changeConfigStatus(String id, int configStatus) {
        FeeConfig feeConfig = new FeeConfig();
        feeConfig.setId(id);
        feeConfig.setConfigStatus(configStatus);
        feeConfigService.save(feeConfig);
        return ResponseData.success();
    }
}