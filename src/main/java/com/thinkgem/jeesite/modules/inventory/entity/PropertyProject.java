/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 物业项目Entity
 * @author huangsc
 * @version 2015-06-03
 */
public class PropertyProject extends DataEntity<PropertyProject> {
	
	private static final long serialVersionUID = 1L;
	private Neighborhood neighborhood;		// 居委会
	private ManagementCompany managementCompany;		// 物业公司
	private String projectName;		// 物业项目名称
	private String projectAddr;		// 物业项目地址
	
	public PropertyProject() {
		super();
	}

	public PropertyProject(String id){
		super(id);
	}

	@NotNull(message="居委会不能为空")
	public Neighborhood getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(Neighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}
	
	@NotNull(message="物业公司不能为空")
	public ManagementCompany getManagementCompany() {
		return managementCompany;
	}

	public void setManagementCompany(ManagementCompany managementCompany) {
		this.managementCompany = managementCompany;
	}
	
	@Length(min=1, max=100, message="物业项目名称长度必须介于 1 和 100 之间")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	@Length(min=1, max=300, message="物业项目地址长度必须介于 1 和 300 之间")
	public String getProjectAddr() {
		return projectAddr;
	}

	public void setProjectAddr(String projectAddr) {
		this.projectAddr = projectAddr;
	}
	
}