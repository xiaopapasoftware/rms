/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.device.entity.DevicesHis;

/**
 * 设备变更信息DAO接口
 * @author huangsc
 * @version 2015-06-28
 */
@MyBatisDao
public interface DevicesHisDao extends CrudDao<DevicesHis> {
	
}