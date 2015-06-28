/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.device.entity.RoomDevices;
import com.thinkgem.jeesite.modules.device.dao.RoomDevicesDao;

/**
 * 房屋设备关联信息Service
 * @author huangsc
 * @version 2015-06-28
 */
@Service
@Transactional(readOnly = true)
public class RoomDevicesService extends CrudService<RoomDevicesDao, RoomDevices> {

	public RoomDevices get(String id) {
		return super.get(id);
	}
	
	public List<RoomDevices> findList(RoomDevices roomDevices) {
		return super.findList(roomDevices);
	}
	
	public Page<RoomDevices> findPage(Page<RoomDevices> page, RoomDevices roomDevices) {
		return super.findPage(page, roomDevices);
	}
	
	@Transactional(readOnly = false)
	public void save(RoomDevices roomDevices) {
		super.save(roomDevices);
	}
	
	@Transactional(readOnly = false)
	public void delete(RoomDevices roomDevices) {
		super.delete(roomDevices);
	}
	
}