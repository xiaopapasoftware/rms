package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

public class RentReport extends DataEntity<RentReport> {
    private static final long serialVersionUID = -6078530446363900143L;
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
    private Double rental;
    private Date rentDate;
    private String rentType;
    private String saler;
    private Double rentAmount;

    @ExcelField(title = "出租合同编号", align = 2, sort = 1)
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

    @ExcelField(title = "物业项目", align = 2, sort = 2)
    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    @ExcelField(title = "楼宇", align = 2, sort = 3)
    public String getBuildingName() {
	return buildingName;
    }

    public void setBuildingName(String buildingName) {
	this.buildingName = buildingName;
    }

    @ExcelField(title = "房屋号", align = 2, sort = 4)
    public String getHouseNo() {
	return houseNo;
    }

    public void setHouseNo(String houseNo) {
	this.houseNo = houseNo;
    }

    @ExcelField(title = "房间号", align = 2, sort = 5)
    public String getRoomNo() {
	return roomNo;
    }

    public void setRoomNo(String roomNo) {
	this.roomNo = roomNo;
    }

    @ExcelField(title = "租客姓名", align = 2, sort = 6)
    public String getTenantName() {
	return tenantName;
    }

    public void setTenantName(String tenantName) {
	this.tenantName = tenantName;
    }

    @ExcelField(title = "租客手机号码", align = 2, sort = 7)
    public String getCellPhone() {
	return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
	this.cellPhone = cellPhone;
    }

    @ExcelField(title = "原合同开始日期", align = 2, sort = 8)
    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    @ExcelField(title = "原合同到期日期", align = 2, sort = 9)
    public Date getExpiredDate() {
	return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
	this.expiredDate = expiredDate;
    }

    @ExcelField(title = "租金", align = 2, sort = 11)
    public String getRental() {
	return rental.toString();
    }

    public void setRental(Double rental) {
	this.rental = rental;
    }

    @ExcelField(title = "退租时间", align = 2, sort = 10)
    public Date getRentDate() {
	return rentDate;
    }

    public void setRentDate(Date rentDate) {
	this.rentDate = rentDate;
    }

    @ExcelField(title = "退租类型", align = 2, sort = 12, dictType = "rent_contract_busi_status")
    public String getRentType() {
	return rentType;
    }

    public void setRentType(String rentType) {
	this.rentType = rentType;
    }

    @ExcelField(title = "销售姓名", align = 2, sort = 14)
    public String getSaler() {
	return saler;
    }

    public void setSaler(String saler) {
	this.saler = saler;
    }

    @ExcelField(title = "退租扣款金额", align = 2, sort = 13)
    public String getRentAmount() {
	return rentAmount.toString();
    }

    public void setRentAmount(Double rentAmount) {
	this.rentAmount = rentAmount;
    }
}
