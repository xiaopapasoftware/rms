/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 账务交易Entity
 * 
 * @author huangsc
 * @version 2015-06-11
 */
public class TradingAccounts extends DataEntity<TradingAccounts> {

	private static final long serialVersionUID = 1L;
	private String tradeId; // 账务交易对象
	private String tradeType; // 账务交易类型
	private String tradeTypeDesc; // 账务交易类型描述
	private String tradeDirection; // 账务交易方向
	private String tradeDirectionDesc; // 账务交易方向描述
	private Double tradeAmount; // 交易金额
	private String payeeName; // 收款人名称
	private String payeeType; // 收款人类型
	private String tradeStatus; // 账务状态

	private String transIds;//款项ID列表
	private String tradeName;//交易对象名称

	private String transStatus;
	private String transBusiStatus;

	private List<Receipt> receiptList = new ArrayList<Receipt>();

	public TradingAccounts() {
		super();
	}

	public TradingAccounts(String id) {
		super(id);
	}
	public String getTradeTypeDesc() {
		return tradeTypeDesc;
	}

	public void setTradeTypeDesc(String tradeTypeDesc) {
		this.tradeTypeDesc = tradeTypeDesc;
	}

	public String getTradeDirectionDesc() {
		return tradeDirectionDesc;
	}

	public void setTradeDirectionDesc(String tradeDirectionDesc) {
		this.tradeDirectionDesc = tradeDirectionDesc;
	}
	@Length(min = 1, max = 64, message = "账务交易对象长度必须介于 1 和 64 之间")
	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	@Length(min = 1, max = 64, message = "账务交易类型长度必须介于 1 和 64 之间")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	@Length(min = 1, max = 64, message = "账务交易方向长度必须介于 1 和 64 之间")
	public String getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(String tradeDirection) {
		this.tradeDirection = tradeDirection;
	}

	@NotNull(message = "交易金额不能为空")
	public Double getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeType() {
		return payeeType;
	}

	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}

	@Length(min = 1, max = 64, message = "账务状态长度必须介于 1 和 64 之间")
	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getTransIds() {
		return transIds;
	}

	public void setTransIds(String transIds) {
		this.transIds = transIds;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getTransBusiStatus() {
		return transBusiStatus;
	}

	public void setTransBusiStatus(String transBusiStatus) {
		this.transBusiStatus = transBusiStatus;
	}

	public List<Receipt> getReceiptList() {
		return receiptList;
	}

	public void setReceiptList(List<Receipt> receiptList) {
		this.receiptList = receiptList;
	}
}