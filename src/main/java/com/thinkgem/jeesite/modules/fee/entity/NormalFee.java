/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 一般费用结算Entity
 * @author huangsc
 * @version 2015-07-04
 */
public class NormalFee extends DataEntity<NormalFee> {
	
	private static final long serialVersionUID = 1L;
	private String rentContractId;		// 出租合同
	private String feeType;		// 费用类型
	private String settleType;		// 结算类型
	private Date startDate;		// 电费缴纳开始时间
	private Date endDate;		// 电费缴纳开始时间
	private Double meterValue;		// 表系数
	private Double personFee;		// 金额
	private String settleStatus;		// 结算状态
	
	private String type;
	private String contractName;
	
	public NormalFee() {
		super();
	}

	public NormalFee(String id){
		super(id);
	}

	@Length(min=1, max=64, message="出租合同长度必须介于 1 和 64 之间")
	public String getRentContractId() {
		return rentContractId;
	}

	public void setRentContractId(String rentContractId) {
		this.rentContractId = rentContractId;
	}
	
	@Length(min=1, max=64, message="费用类型长度必须介于 1 和 64 之间")
	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
	@Length(min=1, max=64, message="结算类型长度必须介于 1 和 64 之间")
	public String getSettleType() {
		return settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="电费缴纳开始时间不能为空")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="电费缴纳开始时间不能为空")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Double getMeterValue() {
		return meterValue;
	}

	public void setMeterValue(Double meterValue) {
		this.meterValue = meterValue;
	}
	
	public Double getPersonFee() {
		return personFee;
	}

	public void setPersonFee(Double personFee) {
		this.personFee = personFee;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getSettleStatus() {
		return settleStatus;
	}

	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}	
}