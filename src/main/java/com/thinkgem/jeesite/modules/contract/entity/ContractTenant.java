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
	private String contractId;		// contract_id
	private String tenantId;		// tenant_id
	
	public ContractTenant() {
		super();
	}

	public ContractTenant(String id){
		super(id);
	}

	@Length(min=1, max=64, message="contract_id长度必须介于 1 和 64 之间")
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	@Length(min=1, max=64, message="tenant_id长度必须介于 1 和 64 之间")
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
}