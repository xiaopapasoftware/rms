/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.config.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.config.entity.vo.FeeConfigVo;

import java.util.List;

/**
 * <p>费用配置项实现类service</p>
 * <p>Table: fee_config - 费用配置项</p>
 * @since 2017-09-18 08:14:27
 * @author generator code
*/
@MyBatisDao
public interface FeeConfigDao extends CrudDao<FeeConfig>{

    List<FeeConfigVo> getFeeConfigList(FeeConfig feeConfig);
}