package com.thinkgem.jeesite.modules.lease.util;

import com.thinkgem.jeesite.modules.lease.entity.LeaseStatistics;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class LeaseStatisticsBuilder {

    private int totalRooms;

    private int leasedRooms;

    private double rentSum;

    private String name;

    public LeaseStatisticsBuilder totalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
        return this;
    }

    public LeaseStatisticsBuilder leasedRooms(int leasedRooms) {
        this.leasedRooms = leasedRooms;
        return this;
    }

    public LeaseStatisticsBuilder rentSum(double rentSum) {
        this.rentSum = rentSum;
        return this;
    }

    public LeaseStatisticsBuilder name(String name) {
        this.name = name;
        return this;
    }

    public LeaseStatistics build() {
        return new LeaseStatistics(name, totalRooms, leasedRooms, formatSum(rentSum), format(rentSum, leasedRooms), format(leasedRooms * 100.0 , totalRooms) + "%");
    }

    private double formatSum(Double sum) {
        BigDecimal b = new BigDecimal(sum);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private String format(Double value, int count) {
        if (count == 0) {
            return "0";
        }
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        return decimalFormat.format(value  / count);
    }
}
