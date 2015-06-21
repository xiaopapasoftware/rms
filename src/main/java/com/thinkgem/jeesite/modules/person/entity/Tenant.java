/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;

/**
 * 租客信息Entity
 * @author huangsc
 * @version 2015-06-13
 */
public class Tenant extends DataEntity<Tenant> {
	
	private static final long serialVersionUID = 1L;
	private DepositAgreement depositAgreement;		// 定金协议
	private LeaseContract leaseContract;		// 承租合同
	private RentContract rentContract;		// 出租合同
	private AgreementChange leaseContractChangeId;		// 承租合同变更协议
	private AgreementChange rentContractChangeId;		// 出租合同变更协议
	private String tenantType;		// 租客类型
	private String companyId;		// 企业
	private String tenantName;		// 姓名
	private String gender;		// 性别
	private String idType;		// 证件类型
	private String idNo;		// 证件号码
	private Date birthday;		// 出生日期
	private String degrees;		// 学历
	private String cellPhone;		// 手机号码
	private String email;		// 电子邮箱
	private String houseRegister;		// 户籍所在地
	private String position;		// 职位
	
	public Tenant() {
		super();
	}

	public Tenant(String id){
		super(id);
	}

	public DepositAgreement getDepositAgreement() {
		return depositAgreement;
	}

	public void setDepositAgreement(DepositAgreement depositAgreement) {
		this.depositAgreement = depositAgreement;
	}
	
	public LeaseContract getLeaseContract() {
		return leaseContract;
	}

	public void setLeaseContract(LeaseContract leaseContract) {
		this.leaseContract = leaseContract;
	}
	
	public RentContract getRentContract() {
		return rentContract;
	}

	public void setRentContract(RentContract rentContract) {
		this.rentContract = rentContract;
	}
	
	public AgreementChange getLeaseContractChangeId() {
		return leaseContractChangeId;
	}

	public void setLeaseContractChangeId(AgreementChange leaseContractChangeId) {
		this.leaseContractChangeId = leaseContractChangeId;
	}
	
	public AgreementChange getRentContractChangeId() {
		return rentContractChangeId;
	}

	public void setRentContractChangeId(AgreementChange rentContractChangeId) {
		this.rentContractChangeId = rentContractChangeId;
	}
	
	@Length(min=1, max=64, message="租客类型长度必须介于 1 和 64 之间")
	public String getTenantType() {
		return tenantType;
	}

	public void setTenantType(String tenantType) {
		this.tenantType = tenantType;
	}
	
	@Length(min=0, max=64, message="企业长度必须介于 0 和 64 之间")
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	
	@Length(min=1, max=100, message="性别长度必须介于 1 和 100 之间")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Length(min=1, max=100, message="证件类型长度必须介于 1 和 100 之间")
	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}
	
	@Length(min=1, max=100, message="证件号码长度必须介于 1 和 100 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@Length(min=0, max=64, message="学历长度必须介于 0 和 64 之间")
	public String getDegrees() {
		return degrees;
	}

	public void setDegrees(String degrees) {
		this.degrees = degrees;
	}
	
	@Length(min=1, max=64, message="手机号码长度必须介于 1 和 64 之间")
	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	@Length(min=0, max=64, message="电子邮箱长度必须介于 0 和 64 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=64, message="户籍所在地长度必须介于 0 和 64 之间")
	public String getHouseRegister() {
		return houseRegister;
	}

	public void setHouseRegister(String houseRegister) {
		this.houseRegister = houseRegister;
	}
	
	@Length(min=0, max=64, message="职位长度必须介于 0 和 64 之间")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
}