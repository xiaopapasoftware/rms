/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 款项账务关联信息Entity
 * @author huangsc
 * @version 2015-06-20
 */
public class PaymentTrade extends DataEntity<PaymentTrade> {
	
	private static final long serialVersionUID = 1L;
	private String transId;		// 款项ID
	private String tradeId;		// 账务ID
	
	public PaymentTrade() {
		super();
	}

	public PaymentTrade(String id){
		super(id);
	}

	@Length(min=0, max=64, message="款项ID长度必须介于 0 和 64 之间")
	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}
	
	@Length(min=0, max=64, message="账务ID长度必须介于 0 和 64 之间")
	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	
}