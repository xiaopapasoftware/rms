/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 协议变更Entity
 * @author huangsc
 * @version 2015-06-11
 */
public class AgreementChange extends DataEntity<AgreementChange> {
	
	private static final long serialVersionUID = 1L;
	private RentContract rentContract;		// 出租合同
	private String agreementChangeName;		// 合同变更协议名称
	private Date startDate;		// 协议生效时间
	private String rentMode;		// 出租方式
	private String agreementStatus;		// 协议审核状态
	private User user;		// 核算人
	
	private String contractId;
	
	private List<Tenant> tenantList = new ArrayList<Tenant>();//承租人
	private List<Tenant> liveList = new ArrayList<Tenant>();//入住人
	
	public AgreementChange() {
		super();
	}

	public AgreementChange(String id){
		super(id);
	}

	@NotNull(message="出租合同不能为空")
	public RentContract getRentContract() {
		return rentContract;
	}

	public void setRentContract(RentContract rentContract) {
		this.rentContract = rentContract;
	}
	
	@Length(min=1, max=64, message="合同变更协议名称长度必须介于 1 和 64 之间")
	public String getAgreementChangeName() {
		return agreementChangeName;
	}

	public void setAgreementChangeName(String agreementChangeName) {
		this.agreementChangeName = agreementChangeName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="协议生效时间不能为空")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Length(min=1, max=64, message="出租方式长度必须介于 1 和 64 之间")
	public String getRentMode() {
		return rentMode;
	}

	public void setRentMode(String rentMode) {
		this.rentMode = rentMode;
	}
	
	@Length(min=1, max=64, message="协议审核状态长度必须介于 1 和 64 之间")
	public String getAgreementStatus() {
		return agreementStatus;
	}

	public void setAgreementStatus(String agreementStatus) {
		this.agreementStatus = agreementStatus;
	}
	
	@NotNull(message="核算人不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public List<Tenant> getTenantList() {
		return tenantList;
	}

	public void setTenantList(List<Tenant> tenantList) {
		this.tenantList = tenantList;
	}

	public List<Tenant> getLiveList() {
		return liveList;
	}

	public void setLiveList(List<Tenant> liveList) {
		this.liveList = liveList;
	}
}