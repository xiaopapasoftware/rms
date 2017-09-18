/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.config.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.config.dao.FeeConfigDao;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* <p>费用配置项实现类 service</p>
* <p>Table: fee_config - 费用配置项</p>
* @since 2017-09-18 08:14:27
* @author generator code
*/
@Service
@Transactional(readOnly = true)
public class FeeConfigService extends CrudService<FeeConfigDao, FeeConfig> {

}