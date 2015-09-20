package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

public class RentDataReport extends DataEntity<RentDataReport> {
	private String reportMonth;
	private PropertyProject propertyProject; // 物业项目
	private String projectName; // 物业项目
	private Integer normal;//正常退租
	private Integer breakContract;//定金违约
	private Integer early;//提前退租
	private Integer special;//特殊退租
	private Integer rent;//出租
	private Integer renting;//月末在租
	
	@ExcelField(title="正常退租", align=2, sort=4)
	public Integer getNormal() {
		return normal;
	}
	public void setNormal(Integer normal) {
		this.normal = normal;
	}
	@ExcelField(title="定金约金", align=2, sort=5)
	public Integer getBreakContract() {
		return breakContract;
	}
	public void setBreakContract(Integer breakContract) {
		this.breakContract = breakContract;
	}
	@ExcelField(title="合同违约退租", align=2, sort=6)
	public Integer getEarly() {
		return early;
	}
	public void setEarly(Integer early) {
		this.early = early;
	}
	@ExcelField(title="特殊情况合同违约退租", align=2, sort=7)
	public Integer getSpecial() {
		return special;
	}
	public void setSpecial(Integer special) {
		this.special = special;
	}
	@ExcelField(title="当月共出租间数", align=2, sort=3)
	public Integer getRent() {
		return rent;
	}
	public void setRent(Integer rent) {
		this.rent = rent;
	}
	@ExcelField(title="月末在出租状态总数", align=2, sort=8)
	public Integer getRenting() {
		return renting;
	}
	public void setRenting(Integer renting) {
		this.renting = renting;
	}
	@ExcelField(title="月份", align=2, sort=1)
	public String getReportMonth() {
		return reportMonth;
	}
	public void setReportMonth(String reportMonth) {
		this.reportMonth = reportMonth;
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
