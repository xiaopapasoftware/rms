/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 账务交易Entity
 * @author huangsc
 * @version 2015-06-11
 */
public class TradingAccounts extends DataEntity<TradingAccounts> {
	
	private static final long serialVersionUID = 1L;
	private String tradeId;		// 账务交易对象
	private String tradeType;		// 账务交易类型
	private String tradeDirection;		// 账务交易方向
	private String tradeMode;		// 交易方式
	private Double tradeAmount;		// 交易金额
	private Date tradeTime;		// 交易时间
	private String payeeName;		// 收款人名称
	private String payeeType;		// 收款人类型
	private String tradeStatus;		// 账务状态
	
	private String transIds;
	
	private String tradeName;
	
	private String transStatus;
	private String transBusiStatus;
	
	public TradingAccounts() {
		super();
	}

	public TradingAccounts(String id){
		super(id);
	}

	@Length(min=1, max=64, message="账务交易对象长度必须介于 1 和 64 之间")
	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	
	@Length(min=1, max=64, message="账务交易类型长度必须介于 1 和 64 之间")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	
	@Length(min=1, max=64, message="账务交易方向长度必须介于 1 和 64 之间")
	public String getTradeDirection() {
		return tradeDirection;
	}

	public void setTradeDirection(String tradeDirection) {
		this.tradeDirection = tradeDirection;
	}
	
	@Length(min=1, max=64, message="交易方式长度必须介于 1 和 64 之间")
	public String getTradeMode() {
		return tradeMode;
	}

	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}
	
	@NotNull(message="交易金额不能为空")
	public Double getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
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
	
	@Length(min=1, max=64, message="账务状态长度必须介于 1 和 64 之间")
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
}