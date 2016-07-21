package com.thinkgem.jeesite.modules.report.entity;

import java.util.Date;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

public class PaymentReport extends DataEntity<PaymentReport> {
    private static final long serialVersionUID = -8632474197456779879L;
    private String contractCode;
    private PropertyProject propertyProject; // 物业项目
    private Building building; // 楼宇
    private House house; // 房屋
    private String projectName; // 物业项目
    private String buildingName;
    private String houseNo;
    private String custName;
    private String cellPhone;
    private Double depositAmount;
    private Double depositElectricAmount;
    private Double rental;
    private Double salesAmount;
    private Double received;
    private Double payment;
    private Date expiredDate;
    private String saler;

    @ExcelField(title = "合同号", align = 2, sort = 1)
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

    @ExcelField(title = "客户名称", align = 2, sort = 5)
    public String getCustName() {
	return custName;
    }

    public void setCustName(String custName) {
	this.custName = custName;
    }

    public String getCellPhone() {
	return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
	this.cellPhone = cellPhone;
    }

    @ExcelField(title = "房租押金", align = 2, sort = 6)
    public Double getDepositAmount() {
	return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
	this.depositAmount = depositAmount;
    }

    @ExcelField(title = "水电押金", align = 2, sort = 7)
    public Double getDepositElectricAmount() {
	return depositElectricAmount;
    }

    public void setDepositElectricAmount(Double depositElectricAmount) {
	this.depositElectricAmount = depositElectricAmount;
    }

    @ExcelField(title = "月租金", align = 2, sort = 8)
    public Double getRental() {
	return rental;
    }

    public void setRental(Double rental) {
	this.rental = rental;
    }

    @ExcelField(title = "销售金额", align = 2, sort = 9)
    public Double getSalesAmount() {
	return salesAmount;
    }

    public void setSalesAmount(Double salesAmount) {
	this.salesAmount = salesAmount;
    }

    @ExcelField(title = "已收款", align = 2, sort = 10)
    public Double getReceived() {
	return received;
    }

    public void setReceived(Double received) {
	this.received = received;
    }

    @ExcelField(title = "应收账款", align = 2, sort = 11)
    public String getPayment() {
	return String.valueOf(this.salesAmount - this.received);
    }

    public void setPayment(Double payment) {
	this.payment = payment;
    }

    @ExcelField(title = "合同到期日", align = 2, sort = 12)
    public Date getExpiredDate() {
	return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
	this.expiredDate = expiredDate;
    }

    @ExcelField(title = "业务员", align = 2, sort = 13)
    public String getSaler() {
	return saler;
    }

    public void setSaler(String saler) {
	this.saler = saler;
    }
}
