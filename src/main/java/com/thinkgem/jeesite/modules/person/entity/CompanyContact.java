/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.inventory.entity.ManagementCompany;

/**
 * 物业公司联系人Entity
 * @author huangsc
 * @version 2015-06-06
 */
public class CompanyContact extends DataEntity<CompanyContact> {
	
	private static final long serialVersionUID = 1L;
	private ManagementCompany managementCompany;		// 物业公司
	private String contactName;		// 姓名
	private String cellPhone;		// 手机号
	private String deskPhone;		// 座机号
	
	public CompanyContact() {
		super();
	}

	public CompanyContact(String id){
		super(id);
	}

	@NotNull(message="物业公司不能为空")
	public ManagementCompany getManagementCompany() {
		return managementCompany;
	}

	public void setManagementCompany(ManagementCompany managementCompany) {
		this.managementCompany = managementCompany;
	}
	
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@Length(min=1, max=100, message="手机号长度必须介于 1 和 100 之间")
	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	@Length(min=0, max=100, message="座机号长度必须介于 0 和 100 之间")
	public String getDeskPhone() {
		return deskPhone;
	}

	public void setDeskPhone(String deskPhone) {
		this.deskPhone = deskPhone;
	}
	
}