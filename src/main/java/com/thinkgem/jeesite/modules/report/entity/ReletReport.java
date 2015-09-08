package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

public class ReletReport extends DataEntity<ReletReport> {
	private String contractCode; // 合同编号
	private PropertyProject propertyProject; // 物业项目
	private Building building; // 楼宇
	private House house; // 房屋
	private Room room; // 房间
	private String projectName; // 物业项目
	private String buildingName;
	private String houseNo;
	private String roomNo;
	private String tenantName;
	private String cellPhone;
	private Date startDate;
	private Date expiredDate;
	private Date nextDate;
	private String remarks;
	
	private Double rental;
	private Double reletRental;
	
	private Date reletStartDate;
	private Date reletExpiredDate;
	
	private String saler;
	private String reletSaler;
	
	private Date signDate;
	
	@ExcelField(title="出租合同编号", align=2, sort=1)
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
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
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	@ExcelField(title="物业项目", align=2, sort=2)
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	@ExcelField(title="楼宇", align=2, sort=3)
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	@ExcelField(title="房屋号", align=2, sort=4)
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	@ExcelField(title="房间号", align=2, sort=5)
	public String getRoomNo() {
		return roomNo;
	}
	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
	@ExcelField(title="租客姓名", align=2, sort=6)
	public String getTenantName() {
		return tenantName;
	}
	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	@ExcelField(title="租客手机号码", align=2, sort=7)
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	@ExcelField(title="原合同开始时间", align=2, sort=8)
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	@ExcelField(title="原合同到期日期", align=2, sort=9)
	public Date getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	public Date getNextDate() {
		return nextDate;
	}
	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@ExcelField(title="原租金", align=2, sort=10)
	public String getRental() {
		return rental.toString();
	}
	public void setRental(Double rental) {
		this.rental = rental;
	}
	@ExcelField(title="现租金", align=2, sort=14)
	public Double getReletRental() {
		return reletRental;
	}
	public void setReletRental(Double reletRental) {
		this.reletRental = reletRental;
	}
	@ExcelField(title="现合同开始时间", align=2, sort=15)
	public Date getReletStartDate() {
		return reletStartDate;
	}
	public void setReletStartDate(Date reletStartDate) {
		this.reletStartDate = reletStartDate;
	}
	@ExcelField(title="现合同到期日期", align=2, sort=16)
	public Date getReletExpiredDate() {
		return reletExpiredDate;
	}
	public void setReletExpiredDate(Date reletExpiredDate) {
		this.reletExpiredDate = reletExpiredDate;
	}
	@ExcelField(title="原成交业务员", align=2, sort=11)
	public String getSaler() {
		return saler;
	}
	public void setSaler(String saler) {
		this.saler = saler;
	}
	@ExcelField(title="现成交业务员", align=2, sort=12)
	public String getReletSaler() {
		return reletSaler;
	}
	public void setReletSaler(String reletSaler) {
		this.reletSaler = reletSaler;
	}
	@ExcelField(title="续租日期", align=2, sort=13)
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
}
