/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * 款项交易Entity
 * 
 * @author wangshujin
 * @version 2016-02-25
 */
public class PaymentTrans4Export extends DataEntity<PaymentTrans4Export> {
    private static final long serialVersionUID = -6853893240961762292L;
    private String tradeType; // 交易类型
    private String paymentType; // 款项类型
    private String transName; // 交易对象名称
    private String transObjectNo;// 交易对象编号
    private String tradeDirection; // 交易款项方向
    private Date startDate; // 交易款项开始时间，用于查询条件
    private Date expiredDate; // 交易款项到期时间，用于查询条件
    private String tradeAmount4Export; // 应该交易金额
    private String transAmount4Export; // 实际交易金额
    private String lastAmount4Export; // 剩余交易金额
    private String transStatus; // 交易款项状态

    public PaymentTrans4Export() {
	super();
    }

    public PaymentTrans4Export(String id) {
	super(id);
    }

    @ExcelField(title = "交易类型", align = 2, sort = 1)
    public String getTradeType() {
	return tradeType;
    }

    public void setTradeType(String tradeType) {
	this.tradeType = tradeType;
    }

    @ExcelField(title = "款项类型", align = 2, sort = 2)
    public String getPaymentType() {
	return paymentType;
    }

    public void setPaymentType(String paymentType) {
	this.paymentType = paymentType;
    }

    @ExcelField(title = "交易对象名称", align = 2, sort = 3)
    public String getTransName() {
	return transName;
    }

    public void setTransName(String transName) {
	this.transName = transName;
    }

    @ExcelField(title = "交易对象编号", align = 2, sort = 4)
    public String getTransObjectNo() {
	return transObjectNo;
    }

    public void setTransObjectNo(String transObjectNo) {
	this.transObjectNo = transObjectNo;
    }

    @ExcelField(title = "交易款项方向", align = 2, sort = 5)
    public String getTradeDirection() {
	return tradeDirection;
    }

    public void setTradeDirection(String tradeDirection) {
	this.tradeDirection = tradeDirection;
    }

    @ExcelField(title = "款项开始时间", align = 2, sort = 6)
    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    @ExcelField(title = "款项到期时间", align = 2, sort = 7)
    public Date getExpiredDate() {
	return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
	this.expiredDate = expiredDate;
    }

    @ExcelField(title = "应该交易金额", align = 2, sort = 8)
    public String getTradeAmount4Export() {
	return tradeAmount4Export;
    }

    public void setTradeAmount4Export(String tradeAmount4Export) {
	this.tradeAmount4Export = tradeAmount4Export;
    }

    @ExcelField(title = "实际交易金额", align = 2, sort = 9)
    public String getTransAmount4Export() {
	return transAmount4Export;
    }

    public void setTransAmount4Export(String transAmount4Export) {
	this.transAmount4Export = transAmount4Export;
    }

    @ExcelField(title = "剩余交易金额", align = 2, sort = 10)
    public String getLastAmount4Export() {
	return lastAmount4Export;
    }

    public void setLastAmount4Export(String lastAmount4Export) {
	this.lastAmount4Export = lastAmount4Export;
    }

    @ExcelField(title = "款项交易状态", align = 2, sort = 11)
    public String getTransStatus() {
	return transStatus;
    }

    public void setTransStatus(String transStatus) {
	this.transStatus = transStatus;
    }

}