/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.entity;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

/**
 * 房屋设备关联信息Entity
 * 
 * @author huangsc
 * @version 2015-06-28
 */
public class RoomDevices extends DataEntity<RoomDevices> {

	private static final long serialVersionUID = 1L;

	private String propertyProjectId; // 物业项目
	private String projectName; // 物业项目名称

	private String buildingId; // 楼宇
	private String buildingName;// 楼宇名称

	private String houseId; // 房屋
	private String houseNo;// 房屋门牌号

	private String roomId; // 房间 0代表公共区域
	private String roomNo; // 房间 0代表公共区域
	private Room room;

	private String deviceId; // 设备ID
	private List<Devices> roomDevicesDtlList;// 房间的所有设备列表信息

	public RoomDevices() {
		super();
	}
	public RoomDevices(String id) {
		super(id);
	}
	@Length(min = 0, max = 64, message = "物业项目长度必须介于 0 和 64 之间")
	public String getPropertyProjectId() {
		return propertyProjectId;
	}
	public void setPropertyProjectId(String propertyProjectId) {
		this.propertyProjectId = propertyProjectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@Length(min = 0, max = 64, message = "楼宇长度必须介于 0 和 64 之间")
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	@Length(min = 0, max = 64, message = "房屋长度必须介于 0 和 64 之间")
	public String getHouseId() {
		return houseId;
	}
	public void setHouseId(String houseId) {
		this.houseId = houseId;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	@Length(min = 0, max = 64, message = "房间 0代表公共区域长度必须介于 0 和 64 之间")
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	@Length(min = 0, max = 64, message = "设备ID长度必须介于 0 和 64 之间")
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public List<Devices> getRoomDevicesDtlList() {
		return roomDevicesDtlList;
	}
	public void setRoomDevicesDtlList(List<Devices> roomDevicesDtlList) {
		this.roomDevicesDtlList = roomDevicesDtlList;
	}
}