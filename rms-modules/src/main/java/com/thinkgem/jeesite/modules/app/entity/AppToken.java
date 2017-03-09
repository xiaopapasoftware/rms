/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * TOKENEntity
 * @author mabindong
 * @version 2015-11-08
 */
public class AppToken extends DataEntity<AppToken> {
	
	private static final long serialVersionUID = 1L;
	private String phone;		// 手机号码
	private String token;		// TOKEN
	private Date exprie;		// 过期时间
	
	public AppToken() {
		super();
	}

	public AppToken(String id){
		super(id);
	}

	@Length(min=0, max=20, message="手机号码长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=200, message="TOKEN长度必须介于 0 和 200 之间")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="过期时间不能为空")
	public Date getExprie() {
		return exprie;
	}

	public void setExprie(Date exprie) {
		this.exprie = exprie;
	}
	
}