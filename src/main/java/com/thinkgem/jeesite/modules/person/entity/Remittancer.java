/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 汇款人信息Entity
 * 
 * @author huangsc
 * @version 2015-06-06
 */
public class Remittancer extends DataEntity<Remittancer> {

	private static final long serialVersionUID = 1L;
	private String userName; // 开户人姓名
	private String bankName; // 开户行名称
	private String bankAccount; // 开户行账号

	public Remittancer() {
		super();
	}

	public Remittancer(String id) {
		super(id);
	}

	@Length(min = 1, max = 100, message = "开户人姓名长度必须介于 1 和 100 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Length(min = 1, max = 100, message = "开户行名称长度必须介于 1 和 100 之间")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Length(min = 1, max = 100, message = "开户行账号长度必须介于 1 和 100 之间")
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

}