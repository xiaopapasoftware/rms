package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

public class ReletRateReport extends DataEntity<ReletRateReport> {
	private PropertyProject propertyProject; // 物业项目
	private String projectName;
	private String expiredMonth;
	private Integer roomTotal;
	private Integer reletTotal;
	private Double reletRate;
	private Integer contractTotal;
	private Integer reletContractTotal;
	private Double reletContractRate;
	
	@ExcelField(title="月份", align=2, sort=1)
	public String getExpiredMonth() {
		return expiredMonth;
	}
	public void setExpiredMonth(String expiredMonth) {
		this.expiredMonth = expiredMonth;
	}
	@ExcelField(title="到期间数", align=2, sort=3)
	public Integer getRoomTotal() {
		return roomTotal;
	}
	public void setRoomTotal(Integer roomTotal) {
		this.roomTotal = roomTotal;
	}
	@ExcelField(title="续租间数", align=2, sort=4)
	public Integer getReletTotal() {
		return reletTotal;
	}
	public void setReletTotal(Integer reletTotal) {
		this.reletTotal = reletTotal;
	}
	@ExcelField(title="续租间数比", align=2, sort=5)
	public Double getReletRate() {
		return reletRate;
	}
	public void setReletRate(Double reletRate) {
		this.reletRate = reletRate;
	}
	@ExcelField(title="到期合同数", align=2, sort=6)
	public Integer getContractTotal() {
		return contractTotal;
	}
	public void setContractTotal(Integer contractTotal) {
		this.contractTotal = contractTotal;
	}
	@ExcelField(title="续租合同数", align=2, sort=7)
	public Integer getReletContractTotal() {
		return reletContractTotal;
	}
	public void setReletContractTotal(Integer reletContractTotal) {
		this.reletContractTotal = reletContractTotal;
	}
	@ExcelField(title="续租合同比", align=2, sort=8)
	public Double getReletContractRate() {
		return reletContractRate;
	}
	public void setReletContractRate(Double reletContractRate) {
		this.reletContractRate = reletContractRate;
	}
	public PropertyProject getPropertyProject() {
		return propertyProject;
	}
	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}
	@ExcelField(title="物业项目", align=2, sort=2)
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}