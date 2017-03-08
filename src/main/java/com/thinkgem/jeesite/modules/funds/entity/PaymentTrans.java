/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.funds.entity;

import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 款项交易Entity
 * 
 * @author huangsc
 * @version 2015-06-11
 */
public class PaymentTrans extends DataEntity<PaymentTrans> {

  private static final long serialVersionUID = 1L;
  private String tradeType; // 交易类型
  private String paymentType; // 款项类型
  private String transId; // 交易对象
  private String transName; // 交易对象名称
  private String transObjectNo;// 交易对象编号
  private String tradeDirection; // 交易款项方向
  private Date startDate_begin;// 款项开始日期-始（查询条件）
  private Date startDate_end;// 款项开始日期-终（查询条件）
  private Date startDate; // 交易款项开始时间
  private Date expiredDate; // 交易款项到期时间
  private Date expiredDate_begin;// 款项到期日期-始（查询条件）
  private Date expiredDate_end;// 款项到期日期-终（查询条件）
  private Double tradeAmount; // 应该交易金额
  private Double transAmount; // 实际交易金额
  private Double lastAmount; // 剩余交易金额
  private String tradeAmount4Export;// 应该交易金额（导出用）
  private String transAmount4Export; // 实际交易金额（导出用）
  private String lastAmount4Export; // 剩余交易金额（导出用）
  private Double transferDepositAmount; // 定金转合同的实转金额
  private String transStatus; // 交易款项状态
  private String remittanceDate;// 查询条件：打款日期
  private String splitPaidMonths;// 查询结果：款项付费周期（表明该合同的房租是几个月一付的）

  public PaymentTrans() {
    super();
  }

  public PaymentTrans(String id) {
    super(id);
  }

  @Length(min = 1, max = 64, message = "交易类型长度必须介于 1 和 64 之间")
  public String getTradeType() {
    return tradeType;
  }

  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }

  @Length(min = 1, max = 64, message = "款项类型长度必须介于 1 和 64 之间")
  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  @Length(min = 1, max = 64, message = "交易对象长度必须介于 1 和 64 之间")
  public String getTransId() {
    return transId;
  }

  public void setTransId(String transId) {
    this.transId = transId;
  }

  @Length(min = 1, max = 64, message = "交易款项方向长度必须介于 1 和 64 之间")
  public String getTradeDirection() {
    return tradeDirection;
  }

  public void setTradeDirection(String tradeDirection) {
    this.tradeDirection = tradeDirection;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public Date getStartDate_begin() {
    return startDate_begin;
  }

  public void setStartDate_begin(Date startDate_begin) {
    this.startDate_begin = startDate_begin;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public Date getStartDate_end() {
    return startDate_end;
  }

  public void setStartDate_end(Date startDate_end) {
    this.startDate_end = startDate_end;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  public Date getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
  }

  public Date getExpiredDate_begin() {
    return expiredDate_begin;
  }

  public void setExpiredDate_begin(Date expiredDate_begin) {
    this.expiredDate_begin = expiredDate_begin;
  }

  public Date getExpiredDate_end() {
    return expiredDate_end;
  }

  public void setExpiredDate_end(Date expiredDate_end) {
    this.expiredDate_end = expiredDate_end;
  }

  @NotNull(message = "应该交易金额不能为空")
  public Double getTradeAmount() {
    return tradeAmount;
  }

  public void setTradeAmount(Double tradeAmount) {
    this.tradeAmount = tradeAmount;
  }

  @NotNull(message = "实际交易金额不能为空")
  public Double getTransAmount() {
    return transAmount;
  }

  public void setTransAmount(Double transAmount) {
    this.transAmount = transAmount;
  }

  public Double getLastAmount() {
    return lastAmount;
  }

  public void setLastAmount(Double lastAmount) {
    this.lastAmount = lastAmount;
  }

  public String getTradeAmount4Export() {
    return tradeAmount4Export;
  }

  public void setTradeAmount4Export(String tradeAmount4Export) {
    this.tradeAmount4Export = tradeAmount4Export;
  }

  public String getTransAmount4Export() {
    return transAmount4Export;
  }

  public void setTransAmount4Export(String transAmount4Export) {
    this.transAmount4Export = transAmount4Export;
  }

  public String getLastAmount4Export() {
    return lastAmount4Export;
  }

  public void setLastAmount4Export(String lastAmount4Export) {
    this.lastAmount4Export = lastAmount4Export;
  }

  public Double getTransferDepositAmount() {
    return transferDepositAmount;
  }

  public void setTransferDepositAmount(Double transferDepositAmount) {
    this.transferDepositAmount = transferDepositAmount;
  }

  @Length(min = 1, max = 64, message = "交易款项状态长度必须介于 1 和 64 之间")
  public String getTransStatus() {
    return transStatus;
  }

  public void setTransStatus(String transStatus) {
    this.transStatus = transStatus;
  }

  public String getTransName() {
    return transName;
  }

  public void setTransName(String transName) {
    this.transName = transName;
  }

  public String getTransObjectNo() {
    return transObjectNo;
  }

  public void setTransObjectNo(String transObjectNo) {
    this.transObjectNo = transObjectNo;
  }

  public String getRemittanceDate() {
    return remittanceDate;
  }

  public void setRemittanceDate(String remittanceDate) {
    this.remittanceDate = remittanceDate;
  }

  public String getSplitPaidMonths() {
    return splitPaidMonths;
  }

  public void setSplitPaidMonths(String splitPaidMonths) {
    this.splitPaidMonths = splitPaidMonths;
  }

}
