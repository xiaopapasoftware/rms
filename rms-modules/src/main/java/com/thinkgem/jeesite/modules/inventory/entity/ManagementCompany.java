/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 物业公司Entity
 * @author huangsc
 * @version 2015-06-03
 */
public class ManagementCompany extends DataEntity<ManagementCompany> {
	
	private static final long serialVersionUID = 1L;
	private String companyName;		// 物业公司名称
	private String companyAddr;		// 物业公司地址
	
	public ManagementCompany() {
		super();
	}

	public ManagementCompany(String id){
		super(id);
	}

	@Length(min=1, max=100, message="物业公司名称长度必须介于 1 和 100 之间")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Length(min=1, max=100, message="物业公司地址长度必须介于 1 和 100 之间")
	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	
}