/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.device.entity.RoomDevices;

/**
 * 房屋设备关联信息DAO接口
 * @author huangsc
 * @version 2015-06-28
 */
@MyBatisDao
public interface RoomDevicesDao extends CrudDao<RoomDevices> {
	
}