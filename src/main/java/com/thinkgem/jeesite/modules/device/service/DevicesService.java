/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.device.dao.DevicesDao;
import com.thinkgem.jeesite.modules.device.entity.Devices;

/**
 * 设备信息Service
 * @author huangsc
 * @version 2015-06-13
 */
@Service
@Transactional(readOnly = true)
public class DevicesService extends CrudService<DevicesDao, Devices> {

	public Devices get(String id) {
		return super.get(id);
	}
	
	public List<Devices> findList(Devices devices) {
		return super.findList(devices);
	}
	
	public Page<Devices> findPage(Page<Devices> page, Devices devices) {
		return super.findPage(page, devices);
	}
	
	@Transactional(readOnly = false)
	public void save(Devices devices) {
		super.save(devices);
	}
	
	@Transactional(readOnly = false)
	public void delete(Devices devices) {
		super.delete(devices);
	}
	
}