/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

/**
 * 设备变更信息Entity
 * 
 * @author huangsc
 * @version 2015-06-28
 */
public class DevicesHis extends DataEntity<DevicesHis> {

	private static final long serialVersionUID = 1L;

	private String propertyProjectId;
	private PropertyProject propertyProject;// 物业项目

	private String buildingId;
	private Building building;// 楼宇

	private String houseId;
	private House house;// 房屋

	private String roomId;
	private Room room;// 房间

	private String deviceId;
	private Devices devices;// 设备

	private String operType; // 行为（添加0/删除1）

	public DevicesHis() {
		super();
	}

	public DevicesHis(String id) {
		super(id);
	}

	@Length(min = 0, max = 64, message = "行为（添加0/删除1）长度必须介于 0 和 64 之间")
	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public PropertyProject getPropertyProject() {
		return propertyProject;
	}

	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Devices getDevices() {
		return devices;
	}

	public void setDevices(Devices devices) {
		this.devices = devices;
	}
	public String getPropertyProjectId() {
		return propertyProjectId;
	}

	public void setPropertyProjectId(String propertyProjectId) {
		this.propertyProjectId = propertyProjectId;
	}

	public String getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}

	public String getHouseId() {
		return houseId;
	}

	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}