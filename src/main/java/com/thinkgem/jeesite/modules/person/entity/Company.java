/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 企业信息Entity
 * @author huangsc
 * @version 2015-06-13
 */
public class Company extends DataEntity<Company> {
	
	private static final long serialVersionUID = 1L;
	private String companyName;		// 企业名称
	private String tellPhone;		// 企业电话
	private String idType;		// 证件类型
	private String idNo;		// 证件号码
	private String companyAdress;		// 企业注册地址
	private String businessAdress;		// 企业营业地址
	private String bankName;		// 开户行名称
	private String bankAccount;		// 开户行账号
	
	public Company() {
		super();
	}

	public Company(String id){
		super(id);
	}

	@Length(min=1, max=100, message="企业名称长度必须介于 1 和 100 之间")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Length(min=0, max=100, message="企业电话长度必须介于 0 和 100 之间")
	public String getTellPhone() {
		return tellPhone;
	}

	public void setTellPhone(String tellPhone) {
		this.tellPhone = tellPhone;
	}
	
	@Length(min=0, max=100, message="证件类型长度必须介于 0 和 100 之间")
	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}
	
	@Length(min=0, max=100, message="证件号码长度必须介于0 和 100 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@Length(min=0, max=100, message="企业注册地址长度必须介于 0 和 100 之间")
	public String getCompanyAdress() {
		return companyAdress;
	}

	public void setCompanyAdress(String companyAdress) {
		this.companyAdress = companyAdress;
	}
	
	@Length(min=0, max=100, message="企业营业地址长度必须介于0 和 100 之间")
	public String getBusinessAdress() {
		return businessAdress;
	}

	public void setBusinessAdress(String businessAdress) {
		this.businessAdress = businessAdress;
	}
	
	@Length(min=0, max=100, message="开户行名称长度必须介于 0 和 100 之间")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	@Length(min=0, max=100, message="开户行账号长度必须介于0 和 100 之间")
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	
}