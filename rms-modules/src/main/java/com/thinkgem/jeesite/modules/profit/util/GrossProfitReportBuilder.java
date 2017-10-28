package com.thinkgem.jeesite.modules.profit.util;

import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class GrossProfitReportBuilder {

    private GrossProfitTypeEnum typeEnum;

    private double income;

    private double cost;

    private String name;

    public GrossProfitReportBuilder typeEnum(GrossProfitTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
        return this;
    }

    public GrossProfitReportBuilder income(double income) {
        this.income = income;
        return this;
    }

    public GrossProfitReportBuilder cost(double cost) {
        this.cost = cost;
        return this;
    }

    public GrossProfitReportBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GrossProfitReport build() {
        return new GrossProfitReport(name, BigDecimal.valueOf(income).subtract(BigDecimal.valueOf(cost)).doubleValue(), getProfitPercent(income, cost), cost, income, typeEnum.getCode());
    }

    private String getProfitPercent(double income, double cost) {
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        if (BigDecimal.valueOf(income).subtract(BigDecimal.valueOf(cost)).doubleValue() == 0d) {
            return "0";
        } else if (income == 0d) {
            return "-100";
        } else {
            return decimalFormat.format(BigDecimal.valueOf(income).subtract(BigDecimal.valueOf(cost)).doubleValue() * 100 / income);
        }
    }
}
