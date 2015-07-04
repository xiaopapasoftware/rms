/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.device.entity.Devices;

/**
 * 设备信息DAO接口
 * 
 * @author huangsc
 * @version 2015-06-13
 */
@MyBatisDao
public interface DevicesDao extends CrudDao<Devices> {

	/**
	 * 查询是否有 设备编号 + 设备名称 ＋ 设备型号 ＋ 设备类型 ＋ 设备品牌 设备状态！=已报废4 一致的设备信息
	 * */
	List<Devices> findExistedDevices(Devices devices);
	
	/**
	 * 更新设备状态
	 * */
	void updateDevicesStatus(Devices devices);
}