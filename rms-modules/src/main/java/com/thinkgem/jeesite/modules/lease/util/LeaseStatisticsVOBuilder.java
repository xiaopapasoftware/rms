package com.thinkgem.jeesite.modules.lease.util;

import com.thinkgem.jeesite.modules.lease.entity.LeaseStatisticsVO;

import java.math.BigDecimal;
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
        return new LeaseStatisticsVO(name, totalRooms, leasedRooms, formatSum(rentSum), format(rentSum, leasedRooms), format(leasedRooms * 100.0 , totalRooms) + "%");
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
