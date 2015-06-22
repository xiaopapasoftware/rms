/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.Dict;

/**
 * 房间信息Entity
 * 
 * @author huangsc
 * @version 2015-06-09
 */
public class Room extends DataEntity<Room> {

	private static final long serialVersionUID = 1L;
	private PropertyProject propertyProject; // 物业项目
	private Building building; // 楼宇
	private House house; // 房屋
	private String roomNo; // 房间号
	private String meterNo; // 电表号
	private String roomSpace; // 房间面积

	private String orientation;
	private List<Dict> orientationList = Lists.newArrayList(new Dict());// 朝向列表

	private String structure;
	private List<Dict> structureList = Lists.newArrayList(new Dict());// 附属结构列表

	private String roomStatus; // 房间状态
	private String attachmentPath;// 房间图片

	public Room() {
		super();
	}

	public Room(String id) {
		super(id);
	}

	@NotNull(message = "物业项目不能为空")
	public PropertyProject getPropertyProject() {
		return propertyProject;
	}

	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}

	@NotNull(message = "楼宇不能为空")
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	@NotNull(message = "房屋号不能为空")
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	@Length(min = 1, max = 100, message = "房间号长度必须介于 1 和 100 之间")
	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	@Length(min = 0, max = 100, message = "电表号长度必须介于 0 和 100 之间")
	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public String getRoomSpace() {
		return roomSpace;
	}

	public void setRoomSpace(String roomSpace) {
		this.roomSpace = roomSpace;
	}

	@Length(min = 1, max = 100, message = "房间状态长度必须介于 1 和 100 之间")
	public String getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}
	public List<Dict> getOrientationList() {
		return orientationList;
	}

	public void setOrientationList(List<Dict> orientationList) {
		this.orientationList = orientationList;
	}

	public List<Dict> getStructureList() {
		return structureList;
	}

	public void setStructureList(List<Dict> structureList) {
		this.structureList = structureList;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

}