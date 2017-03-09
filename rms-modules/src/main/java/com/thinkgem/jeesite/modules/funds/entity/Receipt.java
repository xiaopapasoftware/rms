/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.funds.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 账务收据Entity
 * 
 * @author huangsc
 * @version 2015-06-11
 */
public class Receipt extends DataEntity<Receipt> {

  private static final long serialVersionUID = 1L;
  private TradingAccounts tradingAccounts; // 账务交易
  private String receiptNo; // 收据号码
  private Date receiptDate; // 收据日期
  private Double receiptAmount; // 收据金额
  private String tradeMode; // 交易方式
  private String tradeId;
  private String tradeName;
  private String tradeType;
  private String paymentType;

  public Receipt() {
    super();
  }

  public Receipt(String id) {
    super(id);
  }

  @NotNull(message = "账务交易不能为空")
  public TradingAccounts getTradingAccounts() {
    return tradingAccounts;
  }

  public void setTradingAccounts(TradingAccounts tradingAccounts) {
    this.tradingAccounts = tradingAccounts;
  }

  @Length(min = 1, max = 100, message = "收据号码长度必须介于 1 和 100 之间")
  public String getReceiptNo() {
    return receiptNo;
  }

  public void setReceiptNo(String receiptNo) {
    this.receiptNo = receiptNo;
  }

  @JsonFormat(pattern = "yyyy-MM-dd")
  @NotNull(message = "收据日期不能为空")
  public Date getReceiptDate() {
    return receiptDate;
  }

  public void setReceiptDate(Date receiptDate) {
    this.receiptDate = receiptDate;
  }

  @NotNull(message = "收据金额不能为空")
  public Double getReceiptAmount() {
    return receiptAmount;
  }

  public void setReceiptAmount(Double receiptAmount) {
    this.receiptAmount = receiptAmount;
  }

  public String getTradeMode() {
    return tradeMode;
  }

  public void setTradeMode(String tradeMode) {
    this.tradeMode = tradeMode;
  }

  public String getTradeName() {
    return tradeName;
  }

  public void setTradeName(String tradeName) {
    this.tradeName = tradeName;
  }

  public String getTradeType() {
    return tradeType;
  }

  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }

  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  public String getTradeId() {
    return tradeId;
  }

  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }
}
