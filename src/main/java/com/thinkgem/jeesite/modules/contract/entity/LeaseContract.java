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
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.person.entity.Remittancer;

/**
 * 承租合同Entity
 * @author huangsc
 * @version 2015-06-06
 */
public class LeaseContract extends DataEntity<LeaseContract> {
	
	private static final long serialVersionUID = 1L;
	private PropertyProject propertyProject;		// 物业项目
	private Building building;		// 楼宇
	private House house;		// 房屋
	private Remittancer remittancer;		// 汇款人
	private String contractCode; 		// 合同编号
	private String contractName;		// 承租合同名称
	private Date effectiveDate;		// 合同生效时间
	private Date firstRemittanceDate;		// 首次打款日期
	private String remittanceDate;		// 打款日期
	private Date expiredDate;		// 合同过期时间
	private Date contractDate;		// 合同签订时间
	private Double deposit;		// 承租押金
	private String contractStatus;		// 合同审核状态
	
	private String landlordId;//房东身份证
	private String profile;//委托证明
	private String certificate;//房产证
	private String relocation;//动迁协议
	
	private String projectName;
	private String buildingBame;
	private String houseNo;
	private String remittancerName;
	
	private String type;
	
	private List<LeaseContractDtl> leaseContractDtlList = new ArrayList<LeaseContractDtl>();
	
	private Integer monthSpace;
	
	public LeaseContract() {
		super();
	}

	public LeaseContract(String id){
		super(id);
	}

	@NotNull(message="物业项目不能为空")
	public PropertyProject getPropertyProject() {
		return propertyProject;
	}

	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}
	
	@NotNull(message="楼宇不能为空")
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}
	
	@NotNull(message="房屋不能为空")
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}
	
	@NotNull(message="汇款人不能为空")
	public Remittancer getRemittancer() {
		return remittancer;
	}

	public void setRemittancer(Remittancer remittancer) {
		this.remittancer = remittancer;
	}
	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	@Length(min=1, max=100, message="承租合同名称长度必须介于 1 和 100 之间")
	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="合同生效时间不能为空")
	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="首次打款日期不能为空")
	public Date getFirstRemittanceDate() {
		return firstRemittanceDate;
	}

	public void setFirstRemittanceDate(Date firstRemittanceDate) {
		this.firstRemittanceDate = firstRemittanceDate;
	}
	
	@Length(min=1, max=64, message="打款日期长度必须介于 1 和 64 之间")
	public String getRemittanceDate() {
		return remittanceDate;
	}

	public void setRemittanceDate(String remittanceDate) {
		this.remittanceDate = remittanceDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="合同过期时间不能为空")
	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull(message="合同签订时间不能为空")
	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
	
	@NotNull(message="承租押金不能为空")
	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}
	
	@Length(min=1, max=64, message="合同审核状态长度必须介于 1 和 64 之间")
	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getLandlordId() {
		return landlordId;
	}

	public void setLandlordId(String landlordId) {
		this.landlordId = landlordId;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBuildingBame() {
		return buildingBame;
	}

	public void setBuildingBame(String buildingBame) {
		this.buildingBame = buildingBame;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getRemittancerName() {
		return remittancerName;
	}

	public void setRemittancerName(String remittancerName) {
		this.remittancerName = remittancerName;
	}

	public List<LeaseContractDtl> getLeaseContractDtlList() {
		return leaseContractDtlList;
	}

	public void setLeaseContractDtlList(List<LeaseContractDtl> leaseContractDtlList) {
		this.leaseContractDtlList = leaseContractDtlList;
	}

	public String getRelocation() {
		return relocation;
	}

	public void setRelocation(String relocation) {
		this.relocation = relocation;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getMonthSpace() {
		return monthSpace;
	}

	public void setMonthSpace(Integer monthSpace) {
		this.monthSpace = monthSpace;
	}
}