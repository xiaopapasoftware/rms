/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.device.entity.Devices;

/**
 * 设备信息DAO接口
 * @author huangsc
 * @version 2015-06-13
 */
@MyBatisDao
public interface DevicesDao extends CrudDao<Devices> {
	
}