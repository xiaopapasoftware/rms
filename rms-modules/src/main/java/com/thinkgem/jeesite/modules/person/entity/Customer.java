/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.entity;

import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.modules.entity.User;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 客户信息Entity
 * @author huangsc
 * @version 2015-06-06
 */
public class Customer extends DataEntity<Customer> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 销售
	private String contactName;		// 姓名
	private String gender;		// 性别
	private String cellPhone;		// 手机号
	private String isTenant;		// 是否转租客
	
	public Customer() {
		super();
	}

	public Customer(String id){
		super(id);
	}

	@NotNull(message="销售不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@Length(min=1, max=2, message="性别长度必须介于 1 和 2 之间")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Length(min=1, max=100, message="手机号长度必须介于 1 和 100 之间")
	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	@Length(min=0, max=2, message="是否转租客长度必须介于 0 和 2 之间")
	public String getIsTenant() {
		return isTenant;
	}

	public void setIsTenant(String isTenant) {
		this.isTenant = isTenant;
	}
	
}