/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.web;


import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.ResponseData;
import com.thinkgem.jeesite.modules.fee.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.service.FeeConfigService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>费用配置项 controller</p>
 * <p>Table: fee_config - 费用配置项</p>
 * @since 2017-09-17 04:31:55
 * @author generator code
 */
@RestController
@RequestMapping("/fee/config")
public class FeeConfigController extends BaseController {

      @Autowired
      private FeeConfigService feeConfigService;

      @RequestMapping(value = "save")
      public Object save(FeeConfig feeConfig) {
          feeConfigService.save(feeConfig);
          return ResponseData.success();
      }

     @RequestMapping(value = "list")
     public Object list(FeeConfig feeConfig,@RequestParam(value = "pageSize",defaultValue = "1") Integer pageSize,@RequestParam(value = "pageSize",defaultValue = "15") Integer pageNo) {
         Page<FeeConfig> page = feeConfigService.findPage(new Page(pageSize, pageNo), feeConfig);
         return ResponseData.success().data(page);
     }

    @RequestMapping(value = "delete")
    public Object delete(String id) {
        FeeConfig feeConfig = new FeeConfig();
        feeConfig.setId(id);
        feeConfig.setDelFlag(Constants.DEL_FLAG_NO);
        feeConfigService.save(feeConfig);
        return ResponseData.success();
    }

   @RequestMapping(value = "get")
   public Object get(String id) {
        FeeConfig feeConfig = feeConfigService.get(id);
        return ResponseData.success().data(feeConfig);
   }

}