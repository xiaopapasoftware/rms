/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 楼宇Entity
 * 
 * @author huangsc
 * @version 2015-06-03
 */
public class Building extends DataEntity<Building> {

	private static final long serialVersionUID = 1L;
	private PropertyProject propertyProject; // 物业项目
	private String buildingName; // 楼宇名称
	private String attachmentPath; // 楼宇图片路径

	public Building() {
		super();
	}

	public Building(String id) {
		super(id);
	}

	@NotNull(message = "物业项目不能为空")
	public PropertyProject getPropertyProject() {
		return propertyProject;
	}

	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}

	@Length(min = 1, max = 100, message = "楼宇名称长度必须介于 1 和 100 之间")
	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
}