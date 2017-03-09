/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 广告管理Entity
 * @author huangsc
 */
public class HouseAd extends DataEntity<HouseAd> {
	
	private static final long serialVersionUID = 1L;
	private String adType;		// 广告类型 0:图片广告 1:房源链接式广告
	private String adName;		// 广告名称
	private String adValue;		// 房源ID
	private String adUrl;		// 广告图片地址
	private PropertyProject propertyProject; // 物业项目
	private Building building; // 楼宇
	private House house; // 房屋
	private Room room; // 房间
	
	public HouseAd() {
		super();
	}

	public HouseAd(String id){
		super(id);
	}

	public String getAdType() {
		return adType;
	}

	public void setAdType(String adType) {
		this.adType = adType;
	}
	
	@Length(min=0, max=64, message="广告名称长度必须介于 0 和 64 之间")
	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}
	
	@Length(min=0, max=64, message="房源ID长度必须介于 0 和 64 之间")
	public String getAdValue() {
		return adValue;
	}

	public void setAdValue(String adValue) {
		this.adValue = adValue;
	}
	
	public String getAdUrl() {
		return adUrl;
	}

	public void setAdUrl(String adUrl) {
		this.adUrl = adUrl;
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
}