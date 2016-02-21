/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.device.entity.DevicesHis;
import com.thinkgem.jeesite.modules.device.dao.DevicesHisDao;

/**
 * 设备变更信息Service
 * @author huangsc
 * @version 2015-06-28
 */
@Service
@Transactional(readOnly = true)
public class DevicesHisService extends CrudService<DevicesHisDao, DevicesHis> {

	public DevicesHis get(String id) {
		return super.get(id);
	}
	
	public List<DevicesHis> findList(DevicesHis devicesHis) {
		return super.findList(devicesHis);
	}
	
	public Page<DevicesHis> findPage(Page<DevicesHis> page, DevicesHis devicesHis) {
		return super.findPage(page, devicesHis);
	}
	
	@Transactional(readOnly = false)
	public void save(DevicesHis devicesHis) {
		super.save(devicesHis);
	}
	
	@Transactional(readOnly = false)
	public void delete(DevicesHis devicesHis) {
		super.delete(devicesHis);
	}
	
}