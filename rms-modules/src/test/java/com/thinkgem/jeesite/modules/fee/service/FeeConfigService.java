/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.dao.FeeConfigDao;
import com.thinkgem.jeesite.modules.fee.entity.FeeConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* <p>费用配置项实现类 service</p>
* <p>Table: fee_config - 费用配置项</p>
* @since 2017-09-17 04:31:55
* @author generator code
*/
@Service
@Transactional(readOnly = true)
public class FeeConfigService extends CrudService<FeeConfigDao, FeeConfig> {

}