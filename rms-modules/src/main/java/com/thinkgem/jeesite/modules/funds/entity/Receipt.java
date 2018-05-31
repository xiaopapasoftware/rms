package com.thinkgem.jeesite.modules.funds.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 账务收据
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
  private String tradeNo;
  private String tradeType;
  private String tradeTypeDesc;
  private String paymentType;
  private String paymentTypeDesc;
  private String transBeginDateDesc;// 对应的款项集合的最早的开始时间-描述
  private String transEndDateDesc;// 对应的款项集合的最晚的结束时间-描述
  private List<String> tradingAccountsIdList; // 用于sql条件

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

  public String getTradeNo() {
    return tradeNo;
  }

  public void setTradeNo(String tradeNo) {
    this.tradeNo = tradeNo;
  }

  public String getTradeType() {
    return tradeType;
  }

  public void setTradeType(String tradeType) {
    this.tradeType = tradeType;
  }

  public String getTradeTypeDesc() {
    return tradeTypeDesc;
  }

  public void setTradeTypeDesc(String tradeTypeDesc) {
    this.tradeTypeDesc = tradeTypeDesc;
  }

  public String getPaymentType() {
    return paymentType;
  }

  public void setPaymentType(String paymentType) {
    this.paymentType = paymentType;
  }

  public String getPaymentTypeDesc() {
    return paymentTypeDesc;
  }

  public void setPaymentTypeDesc(String paymentTypeDesc) {
    this.paymentTypeDesc = paymentTypeDesc;
  }

  public String getTransBeginDateDesc() {
    return transBeginDateDesc;
  }

  public void setTransBeginDateDesc(String transBeginDateDesc) {
    this.transBeginDateDesc = transBeginDateDesc;
  }

  public String getTransEndDateDesc() {
    return transEndDateDesc;
  }

  public void setTransEndDateDesc(String transEndDateDesc) {
    this.transEndDateDesc = transEndDateDesc;
  }

  public String getTradeId() {
    return tradeId;
  }

  public void setTradeId(String tradeId) {
    this.tradeId = tradeId;
  }

  public List<String> getTradingAccountsIdList() {
    return tradingAccountsIdList;
  }

  public void setTradingAccountsIdList(List<String> tradingAccountsIdList) {
    this.tradingAccountsIdList = tradingAccountsIdList;
  }
}
