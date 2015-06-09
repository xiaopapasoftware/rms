/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 房间信息Entity
 * @author huangsc
 * @version 2015-06-09
 */
public class Room extends DataEntity<Room> {
	
	private static final long serialVersionUID = 1L;
	private PropertyProject propertyProject;		// 物业项目
	private Building building;		// 楼宇
	private House house;		// 房屋号
	private String roomNo;		// 房间号
	private String meterNo;		// 电表号
	private Double roomSpace;		// 房间面积
	private String orientation;		// 朝向
	private String structure;		// 附属结构
	private String roomStatus;		// 房间状态
	
	public Room() {
		super();
	}

	public Room(String id){
		super(id);
	}

	@NotNull(message="物业项目不能为空")
	public PropertyProject getPropertyProject() {
		return propertyProject;
	}

	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}
	
	@NotNull(message="楼宇不能为空")
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}
	
	@NotNull(message="房屋号不能为空")
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}
	
	@Length(min=1, max=100, message="房间号长度必须介于 1 和 100 之间")
	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	
	@Length(min=0, max=100, message="电表号长度必须介于 0 和 100 之间")
	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}
	
	public Double getRoomSpace() {
		return roomSpace;
	}

	public void setRoomSpace(Double roomSpace) {
		this.roomSpace = roomSpace;
	}
	
	@Length(min=0, max=64, message="朝向长度必须介于 0 和 64 之间")
	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	
	@Length(min=0, max=64, message="附属结构长度必须介于 0 和 64 之间")
	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}
	
	@Length(min=1, max=100, message="房间状态长度必须介于 1 和 100 之间")
	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}
	
}