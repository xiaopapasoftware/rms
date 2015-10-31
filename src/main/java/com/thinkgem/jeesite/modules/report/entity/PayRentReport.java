package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

public class PayRentReport extends DataEntity<PayRentReport> {
    private static final long serialVersionUID = 1322887612553349228L;
    private PropertyProject propertyProject; // 物业项目
    private Building building; // 楼宇
    private House house; // 房屋
    private String projectName; // 物业项目
    private String buildingName;
    private String houseNo;
    private String accountName;
    private String bankName;
    private String bankAccount;
    private String payDate;
    private String payDateValue;
    private String payAmount;
    private String oriStrucRoomNum;
    private String oriStrucCusspacNum;
    private String oriStrucWashroNum;

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

    @ExcelField(title = "房号", align = 2, sort = 4)
    public String getHouseNo() {
	return houseNo;
    }

    public void setHouseNo(String houseNo) {
	this.houseNo = houseNo;
    }

    @ExcelField(title = "汇款人姓名", align = 2, sort = 1)
    public String getAccountName() {
	return accountName;
    }

    public void setAccountName(String accountName) {
	this.accountName = accountName;
    }

    @ExcelField(title = "开户行名称", align = 2, sort = 7)
    public String getBankName() {
	return bankName;
    }

    public void setBankName(String bankName) {
	this.bankName = bankName;
    }

    @ExcelField(title = "汇款人账号", align = 2, sort = 8)
    public String getBankAccount() {
	return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
	this.bankAccount = bankAccount;
    }

    @ExcelField(title = "付款日期", align = 2, sort = 6)
    public String getPayDate() {
	return payDate;
    }

    public void setPayDate(String payDate) {
	this.payDate = payDate;
    }

    public String getPayDateValue() {
	return payDateValue;
    }

    public void setPayDateValue(String payDateValue) {
	this.payDateValue = payDateValue;
    }

    @ExcelField(title = "打款金额", align = 2, sort = 9)
    public String getPayAmount() {
	return payAmount;
    }

    public void setPayAmount(String payAmount) {
	this.payAmount = payAmount;
    }

    public String getOriStrucRoomNum() {
	return oriStrucRoomNum;
    }

    public void setOriStrucRoomNum(String oriStrucRoomNum) {
	this.oriStrucRoomNum = oriStrucRoomNum;
    }

    public String getOriStrucCusspacNum() {
	return oriStrucCusspacNum;
    }

    public void setOriStrucCusspacNum(String oriStrucCusspacNum) {
	this.oriStrucCusspacNum = oriStrucCusspacNum;
    }

    public String getOriStrucWashroNum() {
	return oriStrucWashroNum;
    }

    public void setOriStrucWashroNum(String oriStrucWashroNum) {
	this.oriStrucWashroNum = oriStrucWashroNum;
    }

    @ExcelField(title = "原始房屋结构", align = 2, sort = 5)
    public String getStruct() {
	return oriStrucRoomNum + "房" + oriStrucCusspacNum + "厅" + oriStrucWashroNum + "卫";
    }
}
