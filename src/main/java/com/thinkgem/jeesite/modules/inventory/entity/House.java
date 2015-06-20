/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.person.entity.Owner;

/**
 * 房屋信息Entity
 * 
 * @author huangsc
 * @version 2015-06-06
 */
public class House extends DataEntity<House> {

	private static final long serialVersionUID = 1L;
	private PropertyProject propertyProject; // 物业项目
	private Building building; // 楼宇
	private Owner owner; // 业主
	private String houseNo; // 房屋号
	private Integer houseFloor; // 楼层
	private String houseSpace; // 原始建筑面积
	private String decorationSpance; // 装修建筑面积
	private String houseStructure; // 原始房屋结构
	private String decorationStructure; // 装修房屋结构
	private String houseStatus; // 房屋状态
	private String attachmentPath; // 房屋图片路径

	public House() {
		super();
	}

	public House(String id) {
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

	@NotNull(message = "业主不能为空")
	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	@Length(min = 1, max = 100, message = "房屋号长度必须介于 1 和 100 之间")
	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	@NotNull(message = "楼层不能为空")
	public Integer getHouseFloor() {
		return houseFloor;
	}

	public void setHouseFloor(Integer houseFloor) {
		this.houseFloor = houseFloor;
	}

	public String getHouseSpace() {
		return houseSpace;
	}

	public void setHouseSpace(String houseSpace) {
		this.houseSpace = houseSpace;
	}

	public String getDecorationSpance() {
		return decorationSpance;
	}

	public void setDecorationSpance(String decorationSpance) {
		this.decorationSpance = decorationSpance;
	}

	@Length(min = 0, max = 100, message = "原始房屋结构长度必须介于 0 和 100 之间")
	public String getHouseStructure() {
		return houseStructure;
	}

	public void setHouseStructure(String houseStructure) {
		this.houseStructure = houseStructure;
	}

	@Length(min = 0, max = 100, message = "装修房屋结构长度必须介于 0 和 100 之间")
	public String getDecorationStructure() {
		return decorationStructure;
	}

	public void setDecorationStructure(String decorationStructure) {
		this.decorationStructure = decorationStructure;
	}

	@Length(min = 1, max = 100, message = "房屋状态长度必须介于 1 和 100 之间")
	public String getHouseStatus() {
		return houseStatus;
	}

	public void setHouseStatus(String houseStatus) {
		this.houseStatus = houseStatus;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
}