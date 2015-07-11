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
 * 电费结算Entity
 * @author huangsc
 * @version 2015-07-04
 */
public class ElectricFee extends DataEntity<ElectricFee> {
	
	private static final long serialVersionUID = 1L;
	private String rentContractId;		// 出租合同
	private String settleType;		// 结算类型
	private Date startDate;		// 电费缴纳开始时间
	private Date endDate;		// 电费缴纳开始时间
	private String meterValue;		// 入住电表系数
	private Double personFee;		// 自用金额
	private Double mulitiFee;		// 分摊金额
	private String settleStatus;		// 结算状态
	
	private String contractName;
	
	private String paymentTransId;//款项
	
	private String chargeStatus;
	private String chargeId;
	
	public ElectricFee() {
		super();
	}

	public ElectricFee(String id){
		super(id);
	}

	@Length(min=1, max=64, message="出租合同长度必须介于 1 和 64 之间")
	public String getRentContractId() {
		return rentContractId;
	}

	public void setRentContractId(String rentContractId) {
		this.rentContractId = rentContractId;
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
	
	public String getMeterValue() {
		return meterValue;
	}

	public void setMeterValue(String meterValue) {
		this.meterValue = meterValue;
	}
	
	public Double getPersonFee() {
		return personFee;
	}

	public void setPersonFee(Double personFee) {
		this.personFee = personFee;
	}
	
	public Double getMulitiFee() {
		return mulitiFee;
	}

	public void setMulitiFee(Double mulitiFee) {
		this.mulitiFee = mulitiFee;
	}
	
	@Length(min=0, max=64, message="结算状态长度必须介于 0 和 64 之间")
	public String getSettleStatus() {
		return settleStatus;
	}

	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getPaymentTransId() {
		return paymentTransId;
	}

	public void setPaymentTransId(String paymentTransId) {
		this.paymentTransId = paymentTransId;
	}

	public String getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
}