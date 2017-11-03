/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.config.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.config.dao.FeeConfigDao;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.config.entity.vo.FeeConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>费用配置项实现类 service</p>
 * <p>Table: fee_config - 费用配置项</p>
 *
 * @author generator code
 * @since 2017-09-18 08:14:27
 */
@Service
@Transactional(readOnly = true)
public class FeeConfigService extends CrudService<FeeConfigDao, FeeConfig> {

    @Autowired
    private FeeCommonService feeCommonService;

    @Transactional(readOnly = false)
    public void saveFeeConfig(FeeConfig feeConfig) {
        FeeConfig queryConfig = new FeeConfig();
        queryConfig.setBusinessId(feeConfig.getBusinessId());
        queryConfig.setConfigType(feeConfig.getConfigType());
        queryConfig.setFeeType(feeConfig.getFeeType());
        List<FeeConfig> feeConfigs = dao.findList(queryConfig);
        if (!CollectionUtils.isEmpty(feeConfigs)) {
            FeeConfig existConfig = feeConfigs.get(0);
            feeConfig.setId(existConfig.getId());
        }
        this.save(feeConfig);
        //清楚缓存
        feeCommonService.clearFeeConfigCache();
    }

    public List<FeeConfigVo> getFeeConfigList(FeeConfig feeConfig) {
        return dao.getFeeConfigList(feeConfig);
    }

}