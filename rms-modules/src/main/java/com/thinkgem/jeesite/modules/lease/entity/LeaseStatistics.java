package com.thinkgem.jeesite.modules.lease.entity;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;

public class LeaseStatistics {

    private String name;

    private int totalRooms;

    private int leasedRooms;

    private double rentSum;

    private String rentAvg;

    private String leasedPercent;

    public LeaseStatistics() {
    }

    public LeaseStatistics(String name, int totalRooms, int leasedRooms, double rentSum, String rentAvg, String leasedPercent) {
        this.name = name;
        this.totalRooms = totalRooms;
        this.leasedRooms = leasedRooms;
        this.rentSum = rentSum;
        this.rentAvg = rentAvg;
        this.leasedPercent = leasedPercent;
    }

    @ExcelField(title = "维度名称", align = 2, sort = 1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ExcelField(title = "房间总数(间)", align = 2, sort = 2)
    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    @ExcelField(title = "已出租数(间)", align = 2, sort = 3)
    public int getLeasedRooms() {
        return leasedRooms;
    }

    public void setLeasedRooms(int leasedRooms) {
        this.leasedRooms = leasedRooms;
    }

    @ExcelField(title = "房租总计(元)", align = 2, sort = 5)
    public double getRentSum() {
        return rentSum;
    }

    public void setRentSum(double rentSum) {
        this.rentSum = rentSum;
    }

    @ExcelField(title = "月房租均价(元)", align = 2, sort = 6)
    public String getRentAvg() {
        return rentAvg;
    }

    public void setRentAvg(String rentAvg) {
        this.rentAvg = rentAvg;
    }

    @ExcelField(title = "出租率", align = 2, sort = 4)
    public String getLeasedPercent() {
        return leasedPercent;
    }

    public void setLeasedPercent(String leasedPercent) {
        this.leasedPercent = leasedPercent;
    }
}
