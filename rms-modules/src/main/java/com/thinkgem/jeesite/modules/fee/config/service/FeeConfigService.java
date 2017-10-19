/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.config.service;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.config.dao.FeeConfigDao;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.config.entity.vo.FeeConfigVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
* <p>费用配置项实现类 service</p>
* <p>Table: fee_config - 费用配置项</p>
* @since 2017-09-18 08:14:27
* @author generator code
*/
@Service
@Transactional(readOnly = true)
public class FeeConfigService extends CrudService<FeeConfigDao, FeeConfig> {

    @Transactional(readOnly = false)
    public void saveFeeConfig(FeeConfig feeConfig){
        FeeConfig queryConfig = new FeeConfig();
        queryConfig.setBusinessId(feeConfig.getBusinessId());
        queryConfig.setConfigType(feeConfig.getConfigType());
        queryConfig.setFeeType(feeConfig.getFeeType());
        List<FeeConfig> feeConfigs = dao.findList(queryConfig);
        if(!CollectionUtils.isEmpty(feeConfigs)){
            FeeConfig existConfig = feeConfigs.get(0);
            feeConfig.setId(existConfig.getId());
        }
        this.save(feeConfig);
    }

    public List<FeeConfigVo> getFeeConfigList(FeeConfig feeConfig){
        return dao.getFeeConfigList(feeConfig);
    }

    public Map<String,FeeConfig> getFeeConfig(){
        FeeConfig queryConfig = new FeeConfig();
        queryConfig.setConfigStatus(0);
        List<FeeConfig> feeConfigs = dao.findList(queryConfig);
        return Maps.uniqueIndex(feeConfigs,
                feeConfig -> feeConfig.getBusinessId() + "_" + feeConfig.getFeeType());
    }

}