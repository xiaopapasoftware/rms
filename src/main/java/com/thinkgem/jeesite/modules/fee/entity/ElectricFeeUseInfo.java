package com.thinkgem.jeesite.modules.fee.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 电费使用情况查询
 * 
 * @author wangshujin
 */
public class ElectricFeeUseInfo {

    private String contractCode; // 合同编号
    private String startDate;// 开始日期
    private String endDate;// 结束日期
    private String personalUseEle;// 个人使用电量（度）
    private String personalUseAmount;// 个人使用电费（元）
    private String publicUseEle;// 公摊使用电量（度）
    private String publicUseAmount;// 公摊使用电费（元）
    private String remainedEle;// 剩余可用电量（度）
    private String remainedEleAmount;// 剩余电费（元）
    private String personalPrice;// 个人电量单价（元/度）
    private String publicPrice;// 公摊电量单价（元/度）
    private String returnValue;// 电表系统的返回值；

    public String getPersonalUseEle() {
	return personalUseEle;
    }

    public void setPersonalUseEle(String personalUseEle) {
	this.personalUseEle = personalUseEle;
    }

    public String getPublicUseEle() {
	return publicUseEle;
    }

    public void setPublicUseEle(String publicUseEle) {
	this.publicUseEle = publicUseEle;
    }

    public String getRemainedEle() {
	return remainedEle;
    }

    public void setRemainedEle(String remainedEle) {
	this.remainedEle = remainedEle;
    }

    public String getPersonalUseAmount() {
	return personalUseAmount;
    }

    public void setPersonalUseAmount(String personalUseAmount) {
	this.personalUseAmount = personalUseAmount;
    }

    public String getPublicUseAmount() {
	return publicUseAmount;
    }

    public void setPublicUseAmount(String publicUseAmount) {
	this.publicUseAmount = publicUseAmount;
    }

    public String getRemainedEleAmount() {
	return remainedEleAmount;
    }

    public void setRemainedEleAmount(String remainedEleAmount) {
	this.remainedEleAmount = remainedEleAmount;
    }

    public String getPersonalPrice() {
	return personalPrice;
    }

    public void setPersonalPrice(String personalPrice) {
	this.personalPrice = personalPrice;
    }

    public String getPublicPrice() {
	return publicPrice;
    }

    public void setPublicPrice(String publicPrice) {
	this.publicPrice = publicPrice;
    }

    public String getContractCode() {
	return contractCode;
    }

    public void setContractCode(String contractCode) {
	this.contractCode = contractCode;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public String getStartDate() {
	return startDate;
    }

    public void setStartDate(String startDate) {
	this.startDate = startDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public String getEndDate() {
	return endDate;
    }

    public void setEndDate(String endDate) {
	this.endDate = endDate;
    }

    public String getReturnValue() {
	return returnValue;
    }

    public void setReturnValue(String returnValue) {
	this.returnValue = returnValue;
    }
}
