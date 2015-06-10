/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 合作人信息Entity
 * @author huangsc
 * @version 2015-06-09
 */
public class Partner extends DataEntity<Partner> {
	
	private static final long serialVersionUID = 1L;
	private String partnerType;		// 合作人类型
	private String partnerName;		// 姓名
	private String cellPhone;		// 手机号
	private String deskPhone;		// 座机号
	private String userName;		// 开户人姓名
	private String bankName;		// 开户行名称
	private String bankAccount;		// 开户行账号
	private Double commissionPercent;		// 佣金百分比
	
	public Partner() {
		super();
	}

	public Partner(String id){
		super(id);
	}

	@Length(min=1, max=100, message="合作人类型长度必须介于 1 和 100 之间")
	public String getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}
	
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
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
	
	@Length(min=0, max=100, message="开户人姓名长度必须介于 0 和 100 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=0, max=100, message="开户行名称长度必须介于 0 和 100 之间")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	@Length(min=0, max=100, message="开户行账号长度必须介于 0 和 100 之间")
	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	
	public Double getCommissionPercent() {
		return commissionPercent;
	}

	public void setCommissionPercent(Double commissionPercent) {
		this.commissionPercent = commissionPercent;
	}
	
}