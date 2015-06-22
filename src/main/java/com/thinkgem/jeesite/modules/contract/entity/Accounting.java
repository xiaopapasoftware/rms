/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 退租核算Entity
 * @author huangsc
 * @version 2015-06-11
 */
public class Accounting extends DataEntity<Accounting> {
	
	private static final long serialVersionUID = 1L;
	private RentContract rentContract;		// 出租合同
	private String rentContractName;
	private String accountingType;		// 核算类型
	private String feeDirection;		// 核算费用方向
	private String feeType;		// 核算费用类别
	private Double feeAmount;		// 核算金额
	private User user;		// 核算人
	private Date feeDate;		// 核算时间
	
	private String contractBusiStatus;
	
	public Accounting() {
		super();
	}

	public Accounting(String id){
		super(id);
	}

	@NotNull(message="出租合同不能为空")
	public RentContract getRentContract() {
		return rentContract;
	}

	public void setRentContract(RentContract rentContract) {
		this.rentContract = rentContract;
	}
	
	@Length(min=1, max=64, message="核算类型长度必须介于 1 和 64 之间")
	public String getAccountingType() {
		return accountingType;
	}

	public void setAccountingType(String accountingType) {
		this.accountingType = accountingType;
	}
	
	@Length(min=1, max=64, message="核算费用方向长度必须介于 1 和 64 之间")
	public String getFeeDirection() {
		return feeDirection;
	}

	public void setFeeDirection(String feeDirection) {
		this.feeDirection = feeDirection;
	}
	
	@Length(min=1, max=64, message="核算费用类别长度必须介于 1 和 64 之间")
	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	@NotNull(message="核算金额不能为空")
	public Double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}
	
	@NotNull(message="核算人不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="核算时间不能为空")
	public Date getFeeDate() {
		return feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}

	public String getRentContractName() {
		return rentContractName;
	}

	public void setRentContractName(String rentContractName) {
		this.rentContractName = rentContractName;
	}

	public String getContractBusiStatus() {
		return contractBusiStatus;
	}

	public void setContractBusiStatus(String contractBusiStatus) {
		this.contractBusiStatus = contractBusiStatus;
	}
}