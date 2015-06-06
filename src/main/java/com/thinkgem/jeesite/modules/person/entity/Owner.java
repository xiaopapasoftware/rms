/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 业主信息Entity
 * @author huangsc
 * @version 2015-06-06
 */
public class Owner extends DataEntity<Owner> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String socialNumber;		// 身份证号
	private String cellPhone;		// 手机号
	private String deskPhone;		// 座机号
	private String address;		// 详细居住地址
	
	public Owner() {
		super();
	}

	public Owner(String id){
		super(id);
	}

	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=100, message="身份证号长度必须介于 1 和 100 之间")
	public String getSocialNumber() {
		return socialNumber;
	}

	public void setSocialNumber(String socialNumber) {
		this.socialNumber = socialNumber;
	}
	
	@Length(min=1, max=100, message="手机号长度必须介于 1 和 100 之间")
	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	@Length(min=1, max=100, message="座机号长度必须介于 1 和 100 之间")
	public String getDeskPhone() {
		return deskPhone;
	}

	public void setDeskPhone(String deskPhone) {
		this.deskPhone = deskPhone;
	}
	
	@Length(min=0, max=300, message="详细居住地址长度必须介于 0 和 300 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}