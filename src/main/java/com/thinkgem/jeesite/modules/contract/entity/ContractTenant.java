/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 合同租客关联信息Entity
 * @author huangsc
 * @version 2015-06-21
 */
public class ContractTenant extends DataEntity<ContractTenant> {
	
	private static final long serialVersionUID = 1L;
	private String depositAgreementId;		// 定金协议
	private String contractId;		// 出租合同
	private String tenantId;		// 租客
	private String leaseContractId;		// 承租合同
	
	public ContractTenant() {
		super();
	}

	public ContractTenant(String id){
		super(id);
	}

	@Length(min=0, max=64, message="定金协议长度必须介于 0 和 64 之间")
	public String getDepositAgreementId() {
		return depositAgreementId;
	}

	public void setDepositAgreementId(String depositAgreementId) {
		this.depositAgreementId = depositAgreementId;
	}
	
	@Length(min=1, max=64, message="出租合同长度必须介于 1 和 64 之间")
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	@Length(min=1, max=64, message="租客长度必须介于 1 和 64 之间")
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	@Length(min=0, max=64, message="承租合同长度必须介于 0 和 64 之间")
	public String getLeaseContractId() {
		return leaseContractId;
	}

	public void setLeaseContractId(String leaseContractId) {
		this.leaseContractId = leaseContractId;
	}
	
}