package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

public class IncomeReport extends DataEntity<IncomeReport> {
    private static final long serialVersionUID = -1012625587398170868L;
    private PropertyProject propertyProject; // 物业项目
    private Building building; // 楼宇
    private House house; // 房屋
    private String projectName; // 物业项目
    private String buildingName;
    private String houseNo;
    private String sales;
    private String startDate;
    private String expiredDate;
    private String transAmount;
    private String shuidianyaFee;
    private String fangzuyaFee;
    private String fangzuFee;
    private String weiyueFee;
    private String contractCode;
    private String rental;
    private String tradeAmount;
    private String lastAmount;
    private Double depositElectricAmount;
    private Double depositAmount;
    private Double refundAmount;

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

    @ExcelField(title = "物业项目", align = 2, sort = 1)
    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    @ExcelField(title = "楼宇", align = 2, sort = 2)
    public String getBuildingName() {
	return buildingName;
    }

    public void setBuildingName(String buildingName) {
	this.buildingName = buildingName;
    }

    @ExcelField(title = "房号", align = 2, sort = 3)
    public String getHouseNo() {
	return houseNo;
    }

    public void setHouseNo(String houseNo) {
	this.houseNo = houseNo;
    }

    @ExcelField(title = "业务员", align = 2, sort = 4)
    public String getSales() {
	return sales;
    }

    public void setSales(String sales) {
	this.sales = sales;
    }

    @ExcelField(title = "开始时间", align = 2, sort = 5)
    public String getStartDate() {
	return startDate;
    }

    public void setStartDate(String startDate) {
	this.startDate = startDate;
    }

    @ExcelField(title = "结束时间", align = 2, sort = 6)
    public String getExpiredDate() {
	return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
	this.expiredDate = expiredDate;
    }

    @ExcelField(title = "合计", align = 2, sort = 11)
    public String getTransAmount() {
	return transAmount;
    }

    public void setTransAmount(String transAmount) {
	this.transAmount = transAmount;
    }

    @ExcelField(title = "水电押金", align = 2, sort = 7)
    public String getShuidianyaFee() {
	return shuidianyaFee;
    }

    public void setShuidianyaFee(String shuidianyaFee) {
	this.shuidianyaFee = shuidianyaFee;
    }

    @ExcelField(title = "房租押金", align = 2, sort = 8)
    public String getFangzuyaFee() {
	return fangzuyaFee;
    }

    public void setFangzuyaFee(String fangzuyaFee) {
	this.fangzuyaFee = fangzuyaFee;
    }

    @ExcelField(title = "房租收入", align = 2, sort = 9)
    public String getFangzuFee() {
	return fangzuFee;
    }

    public void setFangzuFee(String fangzuFee) {
	this.fangzuFee = fangzuFee;
    }

    @ExcelField(title = "违约收入", align = 2, sort = 10)
    public String getWeiyueFee() {
	return weiyueFee;
    }

    public void setWeiyueFee(String weiyueFee) {
	this.weiyueFee = weiyueFee;
    }

    public String getContractCode() {
	return contractCode;
    }

    public void setContractCode(String contractCode) {
	this.contractCode = contractCode;
    }

    public String getRental() {
	return rental;
    }

    public void setRental(String rental) {
	this.rental = rental;
    }

    public String getTradeAmount() {
	return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
	this.tradeAmount = tradeAmount;
    }

    public String getLastAmount() {
	return lastAmount;
    }

    public void setLastAmount(String lastAmount) {
	this.lastAmount = lastAmount;
    }

    public Double getDepositElectricAmount() {
	return depositElectricAmount;
    }

    public void setDepositElectricAmount(Double depositElectricAmount) {
	this.depositElectricAmount = depositElectricAmount;
    }

    public Double getDepositAmount() {
	return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
	this.depositAmount = depositAmount;
    }

    public Double getRefundAmount() {
	return this.getDepositElectricAmount() + this.getDepositAmount();
    }

    public void setRefundAmount(Double refundAmount) {
	this.refundAmount = refundAmount;
    }
}
