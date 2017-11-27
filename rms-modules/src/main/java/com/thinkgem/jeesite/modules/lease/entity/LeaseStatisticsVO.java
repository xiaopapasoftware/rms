package com.thinkgem.jeesite.modules.lease.entity;

public class LeaseStatisticsVO {

    private String name;

    private int totalRooms;

    private int leasedRooms;

    private double rentSum;

    private String rentAvg;

    private String leasedPercent;

    public LeaseStatisticsVO() {
    }

    public LeaseStatisticsVO(String name, int totalRooms, int leasedRooms, double rentSum, String rentAvg, String leasedPercent) {
        this.name = name;
        this.totalRooms = totalRooms;
        this.leasedRooms = leasedRooms;
        this.rentSum = rentSum;
        this.rentAvg = rentAvg;
        this.leasedPercent = leasedPercent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public int getLeasedRooms() {
        return leasedRooms;
    }

    public void setLeasedRooms(int leasedRooms) {
        this.leasedRooms = leasedRooms;
    }

    public double getRentSum() {
        return rentSum;
    }

    public void setRentSum(double rentSum) {
        this.rentSum = rentSum;
    }

    public String getRentAvg() {
        return rentAvg;
    }

    public void setRentAvg(String rentAvg) {
        this.rentAvg = rentAvg;
    }

    public String getLeasedPercent() {
        return leasedPercent;
    }

    public void setLeasedPercent(String leasedPercent) {
        this.leasedPercent = leasedPercent;
    }
}
