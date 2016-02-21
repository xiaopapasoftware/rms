/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 电费结算Entity
 * 
 * @author huangsc
 * @version 2015-07-04
 */
public class ElectricFee extends DataEntity<ElectricFee> {

    private static final long serialVersionUID = 1L;
    private String rentContractId; // 出租合同
    private String paymentTransId;// 款项ID
    private Date chargeDate; // 充值时间
    private Date startDate;// 选择的开始时间
    private Date endDate;// 选择的结束时间
    private Double chargeAmount; // 充值金额
    private String chargeStatus;// 充值状态
    private String settleStatus;// 结算状态
    private String contractName;
    private String chargeId;

    public ElectricFee() {
	super();
    }

    public ElectricFee(String id) {
	super(id);
    }

    @Length(min = 1, max = 64, message = "出租合同长度必须介于 1 和 64 之间")
    public String getRentContractId() {
	return rentContractId;
    }

    public void setRentContractId(String rentContractId) {
	this.rentContractId = rentContractId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getChargeDate() {
	return chargeDate;
    }

    public void setChargeDate(Date chargeDate) {
	this.chargeDate = chargeDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public Double getChargeAmount() {
	return chargeAmount;
    }

    public void setChargeAmount(Double chargeAmount) {
	this.chargeAmount = chargeAmount;
    }

    public String getContractName() {
	return contractName;
    }

    public void setContractName(String contractName) {
	this.contractName = contractName;
    }

    public String getChargeId() {
	return chargeId;
    }

    public void setChargeId(String chargeId) {
	this.chargeId = chargeId;
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

    public String getSettleStatus() {
	return settleStatus;
    }

    public void setSettleStatus(String settleStatus) {
	this.settleStatus = settleStatus;
    }

}