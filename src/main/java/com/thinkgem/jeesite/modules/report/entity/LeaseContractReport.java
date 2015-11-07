package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.person.entity.Remittancer;

public class LeaseContractReport extends DataEntity<LeaseContractReport> {
	private PropertyProject propertyProject;		// 物业项目
	private Building building;		// 楼宇
	private House house;		// 房屋
	private Remittancer remittancer;		// 汇款人
	private String contractName;		// 承租合同名称
	private Date effectiveDate;		// 合同生效时间
	private Date firstRemittanceDate;		// 首次打款日期
	private String remittanceDate;		// 打款日期
	private Date expiredDate;		// 合同过期时间
	private Date contractDate;		// 合同签订时间
	private Double deposit;		// 承租押金
	
	private String projectName;
	private String buildingBame;
	private String houseNo;
	private String remittancerName;
	
	private String ownerName;
	private String ownerCellPhone;
	private String houseStructure;//房屋原始结构
	private String houseSpace;//房屋原始面积
	
	private Integer oriStrucRoomNum;// 原始房屋结构-房数
	private Integer oriStrucCusspacNum;// 原始房屋结构-厅数
	private Integer oriStrucWashroNum;// 原始房屋结构-卫数
	
	public PropertyProject getPropertyProject() {
		return propertyProject;
	}
	public void setPropertyProject(PropertyProject propertyProject) {
		this.propertyProject = propertyProject;
	}
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building building) {
		this.building = building;
	}
	public House getHouse() {
		return house;
	}
	public void setHouse(House house) {
		this.house = house;
	}
	public Remittancer getRemittancer() {
		return remittancer;
	}
	public void setRemittancer(Remittancer remittancer) {
		this.remittancer = remittancer;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	@ExcelField(title="承租合同开始日期", align=2, sort=8)
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getFirstRemittanceDate() {
		return firstRemittanceDate;
	}
	public void setFirstRemittanceDate(Date firstRemittanceDate) {
		this.firstRemittanceDate = firstRemittanceDate;
	}
	public String getRemittanceDate() {
		return remittanceDate;
	}
	public void setRemittanceDate(String remittanceDate) {
		this.remittanceDate = remittanceDate;
	}
	@ExcelField(title="承租合同结束日期", align=2, sort=9)
	public Date getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	public Date getContractDate() {
		return contractDate;
	}
	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
	public Double getDeposit() {
		return deposit;
	}
	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}
	@ExcelField(title="物业项目名称", align=2, sort=3)
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@ExcelField(title="楼宇", align=2, sort=4)
	public String getBuildingBame() {
		return buildingBame;
	}
	public void setBuildingBame(String buildingBame) {
		this.buildingBame = buildingBame;
	}
	@ExcelField(title="房屋", align=2, sort=5)
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
	@ExcelField(title="业主姓名", align=2, sort=1)
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	@ExcelField(title="业主手机号码", align=2, sort=2)
	public String getOwnerCellPhone() {
		return ownerCellPhone;
	}
	public void setOwnerCellPhone(String ownerCellPhone) {
		this.ownerCellPhone = ownerCellPhone;
	}
	@ExcelField(title="房屋原始结构", align=2, sort=6)
	public String getHouseStructure() {
		String str = "";
		if(null != oriStrucRoomNum)
			str += oriStrucRoomNum+"房";
		if(null != oriStrucCusspacNum)
			str += oriStrucCusspacNum+"厅";
		if(null != oriStrucWashroNum)
			str += oriStrucWashroNum+"卫";
		return str;
	}
	public void setHouseStructure(String houseStructure) {
		this.houseStructure = houseStructure;
	}
	@ExcelField(title="房屋原始面积", align=2, sort=7)
	public String getHouseSpace() {
		return houseSpace;
	}
	public void setHouseSpace(String houseSpace) {
		this.houseSpace = houseSpace;
	}
	@ExcelField(title="承租合同备注", align=2, sort=10)
	public String getRemarks() {
		return remarks;
	}
}
