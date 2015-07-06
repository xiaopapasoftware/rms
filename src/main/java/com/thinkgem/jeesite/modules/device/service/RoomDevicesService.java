/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.device.dao.RoomDevicesDao;
import com.thinkgem.jeesite.modules.device.entity.Devices;
import com.thinkgem.jeesite.modules.device.entity.DevicesHis;
import com.thinkgem.jeesite.modules.device.entity.RoomDevices;

/**
 * 房屋设备关联信息Service
 * 
 * @author huangsc
 * @version 2015-06-28
 */
@Service
@Transactional(readOnly = true)
public class RoomDevicesService extends CrudService<RoomDevicesDao, RoomDevices> {

	@Autowired
	private DevicesService devicesService;

	@Autowired
	private DevicesHisService devicesHisService;

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

	/**
	 * 删除房间设备关联关系
	 * */
	@Transactional(readOnly = false)
	public void deleteRoomDevices(RoomDevices rd, List<Devices> deledDevices) {
		if (CollectionUtils.isNotEmpty(deledDevices)) {
			for (Devices d : deledDevices) {
				d.setDeviceStatus("0");// 状态出库已经分配
				d.setDistrSerlNum("");// “分配序号”删除
				devicesService.updateDevicesStatus(d);

				d.setDevicesChooseFlag("0");// 未设置，用于变色显示
				devicesService.updateDevicesChooseFlag(d);

				DevicesHis addDevicesHis = new DevicesHis();
				addDevicesHis.setPropertyProjectId(rd.getPropertyProjectId());
				addDevicesHis.setBuildingId(rd.getBuildingId());
				addDevicesHis.setHouseId(rd.getHouseId());
				addDevicesHis.setRoomId(rd.getRoomId());
				addDevicesHis.setOperType("1");
				addDevicesHis.setDeviceId(d.getId());
				devicesHisService.save(addDevicesHis); // 插入到设备变更记录表，

				RoomDevices deletedRD = new RoomDevices();
				deletedRD.setPropertyProjectId(rd.getPropertyProjectId());
				deletedRD.setBuildingId(rd.getBuildingId());
				deletedRD.setHouseId(rd.getHouseId());
				deletedRD.setRoomId(rd.getRoomId());
				deletedRD.setDeviceId(d.getId());
				delete(deletedRD);
			}
		}
	}

	/**
	 * 新增房间设备关联关系
	 * */
	@Transactional(readOnly = false)
	public void addRoomDevices(RoomDevices rd, List<Devices> addedDevices) {
		if (CollectionUtils.isNotEmpty(addedDevices)) {
			for (Devices d : addedDevices) {
				d.setDeviceStatus("1");// 状态出库已经分配
				devicesService.updateDevicesStatus(d);

				d.setDevicesChooseFlag("1");// 已设置，用于变色显示
				devicesService.updateDevicesChooseFlag(d);

				RoomDevices addedRD = new RoomDevices();
				addedRD.setPropertyProjectId(rd.getPropertyProjectId());
				addedRD.setBuildingId(rd.getBuildingId());
				addedRD.setHouseId(rd.getHouseId());
				addedRD.setRoomId(rd.getRoomId());
				addedRD.setDeviceId(d.getId());
				save(addedRD);// 保存房间、设备关联关系
				
				DevicesHis addDevicesHis = new DevicesHis();
				addDevicesHis.setPropertyProjectId(rd.getPropertyProjectId());
				addDevicesHis.setBuildingId(rd.getBuildingId());
				addDevicesHis.setHouseId(rd.getHouseId());
				addDevicesHis.setRoomId(rd.getRoomId());
				addDevicesHis.setOperType("0");
				addDevicesHis.setDeviceId(d.getId());
				devicesHisService.save(addDevicesHis); // 插入到设备变更记录表，
			}
		}
	}
}