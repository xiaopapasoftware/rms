/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 房屋设备关联信息Entity
 * @author huangsc
 * @version 2015-06-28
 */
public class RoomDevices extends DataEntity<RoomDevices> {
	
	private static final long serialVersionUID = 1L;
	private String propertyProjectId;		// 物业项目
	private String buildingId;		// 楼宇
	private String houseId;		// 房屋
	private String roomId;		// 房间 0代表公共区域
	private String deviceId;		// 设备ID
	
	public RoomDevices() {
		super();
	}

	public RoomDevices(String id){
		super(id);
	}

	@Length(min=0, max=64, message="物业项目长度必须介于 0 和 64 之间")
	public String getPropertyProjectId() {
		return propertyProjectId;
	}

	public void setPropertyProjectId(String propertyProjectId) {
		this.propertyProjectId = propertyProjectId;
	}
	
	@Length(min=0, max=64, message="楼宇长度必须介于 0 和 64 之间")
	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	
	@Length(min=0, max=64, message="房屋长度必须介于 0 和 64 之间")
	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}
	
	@Length(min=0, max=64, message="房间 0代表公共区域长度必须介于 0 和 64 之间")
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	@Length(min=0, max=64, message="设备ID长度必须介于 0 和 64 之间")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}