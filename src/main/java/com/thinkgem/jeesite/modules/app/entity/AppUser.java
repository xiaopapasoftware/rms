/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * APP用户Entity
 * @author mabindong
 * @version 2015-11-08
 */
public class AppUser extends DataEntity<AppUser> {
	
	private static final long serialVersionUID = 1L;
	private String phone;		// 手机号码
	private String password;		// 密码
	
	public AppUser() {
		super();
	}

	public AppUser(String id){
		super(id);
	}

	@Length(min=0, max=20, message="手机号码长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=20, message="密码长度必须介于 0 和 20 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}