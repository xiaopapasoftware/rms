package com.thinkgem.jeesite.modules.lease.util;

import com.thinkgem.jeesite.modules.lease.entity.LeaseStatisticsVO;

import java.text.DecimalFormat;

public class LeaseStatisticsVOBuilder {

    private int totalRooms;

    private int leasedRooms;

    private double rentSum;

    private String name;

    public LeaseStatisticsVOBuilder totalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
        return this;
    }

    public LeaseStatisticsVOBuilder leasedRooms(int leasedRooms) {
        this.leasedRooms = leasedRooms;
        return this;
    }

    public LeaseStatisticsVOBuilder rentSum(double rentSum) {
        this.rentSum = rentSum;
        return this;
    }

    public LeaseStatisticsVOBuilder name(String name) {
        this.name = name;
        return this;
    }

    public LeaseStatisticsVO build() {
        return new LeaseStatisticsVO(name, totalRooms, leasedRooms, rentSum, format(rentSum/leasedRooms), format(leasedRooms * 100.0 / totalRooms) + "%");
    }

    private String getLeasedPercent(int totalRooms, int leasedRooms) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return decimalFormat.format(leasedRooms * 100.0 / totalRooms) + "%";
    }

    private String format(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return decimalFormat.format(value);
    }
}
