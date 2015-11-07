package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

public class LandlordReport extends DataEntity<LandlordReport> {
    private static final long serialVersionUID = -7249645028832594122L;
    private String name;
    private String identity;
    private Double rental;
    private Date startDate;
    private Date expiredDate;
    private String cellPhone;
    private String certificateNo;
    private PropertyProject propertyProject; // 物业项目
    private Building building; // 楼宇
    private House house; // 房屋
    private String projectName; // 物业项目
    private String buildingName;
    private String houseNo;
    private String company;
    private String leaseId;

    @ExcelField(title = "姓名", align = 2, sort = 1)
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @ExcelField(title = "身份证号", align = 2, sort = 2)
    public String getIdentity() {
	return identity;
    }

    public void setIdentity(String identity) {
	this.identity = identity;
    }

    @ExcelField(title = "房租金额", align = 2, sort = 3)
    public String getRental() {
	return rental.toString();
    }

    public void setRental(Double rental) {
	this.rental = rental;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    @ExcelField(title = "房租租期", align = 2, sort = 4)
    public Date getExpiredDate() {
	return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
	this.expiredDate = expiredDate;
    }

    @ExcelField(title = "联系电话", align = 2, sort = 5)
    public String getCellPhone() {
	return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
	this.cellPhone = cellPhone;
    }

    @ExcelField(title = "产权证号", align = 2, sort = 9)
    public String getCertificateNo() {
	return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
	this.certificateNo = certificateNo;
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

    @ExcelField(title = "物业项目", align = 2, sort = 6)
    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    @ExcelField(title = "楼宇", align = 2, sort = 7)
    public String getBuildingName() {
	return buildingName;
    }

    public void setBuildingName(String buildingName) {
	this.buildingName = buildingName;
    }

    @ExcelField(title = "房号", align = 2, sort = 8)
    public String getHouseNo() {
	return houseNo;
    }

    public void setHouseNo(String houseNo) {
	this.houseNo = houseNo;
    }

    @ExcelField(title = "所租公司", align = 2, sort = 10)
    public String getCompany() {
	return "上海唐巢投资有限公司";
    }

    public void setCompany(String company) {
	this.company = company;
    }

    @ExcelField(title = "法人身份证号", align = 2, sort = 11)
    public String getLeaseId() {
	return "31022419630503663X";
    }

    public void setLeaseId(String leaseId) {
	this.leaseId = leaseId;
    }
}
