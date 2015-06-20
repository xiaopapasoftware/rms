/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 发票信息Entity
 * @author huangsc
 * @version 2015-06-11
 */
public class Invoice extends DataEntity<Invoice> {
	
	private static final long serialVersionUID = 1L;
	private TradingAccounts tradingAccounts;		// 账务交易
	private String invoiceType;		// 开票类型
	private String invoiceNo;		// 发票号码
	private String invoiceTitle;		// 发票抬头
	private Date invoiceDate;		// 开票日期
	private Double invoiceAmount;		// 发票金额
	
	public Invoice() {
		super();
	}

	public Invoice(String id){
		super(id);
	}

	@NotNull(message="账务交易不能为空")
	public TradingAccounts getTradingAccounts() {
		return tradingAccounts;
	}

	public void setTradingAccounts(TradingAccounts tradingAccounts) {
		this.tradingAccounts = tradingAccounts;
	}
	
	@Length(min=1, max=64, message="开票类型长度必须介于 1 和 64 之间")
	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	
	@Length(min=1, max=64, message="发票号码长度必须介于 1 和 64 之间")
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	@Length(min=0, max=64, message="发票抬头长度必须介于 0 和 64 之间")
	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="开票日期不能为空")
	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	@NotNull(message="发票金额不能为空")
	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	
}