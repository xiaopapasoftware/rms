/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 企业联系人Entity
 * @author huangsc
 * @version 2015-06-13
 */
public class CompanyLinkman extends DataEntity<CompanyLinkman> {
	
	private static final long serialVersionUID = 1L;
	private Company company;		// 企业
	private String personName;		// 姓名
	private String cellPhone;		// 手机号码
	private String tellPhone;		// 座机号码
	private String email;		// 邮箱
	
	public CompanyLinkman() {
		super();
	}

	public CompanyLinkman(String id){
		super(id);
	}

	@NotNull(message="企业不能为空")
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@Length(min=1, max=100, message="手机号码长度必须介于 1 和 100 之间")
	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	@Length(min=0, max=100, message="座机号码长度必须介于 0 和 100 之间")
	public String getTellPhone() {
		return tellPhone;
	}

	public void setTellPhone(String tellPhone) {
		this.tellPhone = tellPhone;
	}
	
	@Length(min=0, max=100, message="邮箱长度必须介于 0 和 100 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}