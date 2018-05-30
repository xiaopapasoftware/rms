package com.thinkgem.jeesite.modules.inventory.entity;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

/**
 * @author wangganggang
 * @date 2018年05月30日 上午9:40
 */
public class HouseVO {

    private String projectName;

    private String buildingName;

    private String ownerNamesOfHouse;

    private String houseCode;

    private String houseNo;

    private String reservationPhone;

    private String isFeature;

    private String buildingType;

    private String houseStatus;

    private String houseFloor;

    private String houseSpace;

    private String decorationSpance;

    private String oriStruName;

    private String decoraStrucName;

    private String certificateNo;

    private String eleAccountNum;

    private String waterAccountNum;

    private String gasAccountNum;

    private String rentMonthGap;

    private String deposMonthCount;

    private String alipayName;

    private String alipayStatusName;

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

    @ExcelField(title = "业主", align = 2, sort = 3)
    public String getOwnerNamesOfHouse() {
        return ownerNamesOfHouse;
    }

    public void setOwnerNamesOfHouse(String ownerNamesOfHouse) {
        this.ownerNamesOfHouse = ownerNamesOfHouse;
    }

    @ExcelField(title = "房屋编码", align = 2, sort = 4)
    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    @ExcelField(title = "房屋号", align = 2, sort = 5)
    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    @ExcelField(title = "预约热线电话", align = 2, sort = 6)
    public String getReservationPhone() {
        return reservationPhone;
    }

    public void setReservationPhone(String reservationPhone) {
        this.reservationPhone = reservationPhone;
    }

    @ExcelField(title = "精选房源", align = 2, sort = 7)
    public String getIsFeature() {
        return isFeature;
    }

    public void setIsFeature(String isFeature) {
        this.isFeature = isFeature;
    }

    @ExcelField(title = "公寓类型", align = 2, sort = 8)
    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    @ExcelField(title = "房屋状态", align = 2, sort = 9)
    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    @ExcelField(title = "楼层", align = 2, sort = 10)
    public String getHouseFloor() {
        return houseFloor;
    }

    public void setHouseFloor(String houseFloor) {
        this.houseFloor = houseFloor;
    }

    @ExcelField(title = "原始面积", align = 2, sort = 11)
    public String getHouseSpace() {
        return houseSpace;
    }

    public void setHouseSpace(String houseSpace) {
        this.houseSpace = houseSpace;
    }

    @ExcelField(title = "装修面积", align = 2, sort = 12)
    public String getDecorationSpance() {
        return decorationSpance;
    }

    public void setDecorationSpance(String decorationSpance) {
        this.decorationSpance = decorationSpance;
    }

    @ExcelField(title = "原始结构", align = 2, sort = 13)
    public String getOriStruName() {
        return oriStruName;
    }

    public void setOriStruName(String oriStruName) {
        this.oriStruName = oriStruName;
    }

    @ExcelField(title = "装修结构", align = 2, sort = 14)
    public String getDecoraStrucName() {
        return decoraStrucName;
    }

    public void setDecoraStrucName(String decoraStrucName) {
        this.decoraStrucName = decoraStrucName;
    }

    @ExcelField(title = "产权证号", align = 2, sort = 15)
    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    @ExcelField(title = "电户号", align = 2, sort = 16)
    public String getEleAccountNum() {
        return eleAccountNum;
    }

    public void setEleAccountNum(String eleAccountNum) {
        this.eleAccountNum = eleAccountNum;
    }

    @ExcelField(title = "水户号", align = 2, sort = 17)
    public String getWaterAccountNum() {
        return waterAccountNum;
    }

    public void setWaterAccountNum(String waterAccountNum) {
        this.waterAccountNum = waterAccountNum;
    }

    @ExcelField(title = "煤气户号", align = 2, sort = 18)
    public String getGasAccountNum() {
        return gasAccountNum;
    }

    public void setGasAccountNum(String gasAccountNum) {
        this.gasAccountNum = gasAccountNum;
    }

    @ExcelField(title = "支付间隔月数", align = 2, sort = 19)
    public String getRentMonthGap() {
        return rentMonthGap;
    }

    public void setRentMonthGap(String rentMonthGap) {
        this.rentMonthGap = rentMonthGap;
    }

    @ExcelField(title = "押金月数", align = 2, sort = 20)
    public String getDeposMonthCount() {
        return deposMonthCount;
    }

    public void setDeposMonthCount(String deposMonthCount) {
        this.deposMonthCount = deposMonthCount;
    }

    @ExcelField(title = "支付宝同步状态", align = 2, sort = 22)
    public String getAlipayName() {
        return alipayName;
    }

    public void setAlipayName(String alipayName) {
        this.alipayName = alipayName;
    }

    @ExcelField(title = "支付宝上下架状态", align = 2, sort = 23)
    public String getAlipayStatusName() {
        return alipayStatusName;
    }

    public void setAlipayStatusName(String alipayStatusName) {
        this.alipayStatusName = alipayStatusName;
    }
}
