package com.thinkgem.jeesite.modules.profit.entity;

import java.io.Serializable;

public class GrossProfitReport implements Comparable<GrossProfitReport>, Serializable{

    private static final long serialVersionUID = 3168884500513682293L;

    private String name;

    private double totalProfit;

    private String profitPercent;

    private double cost;

    private double income;

    //类型标识  公司、服务中心等
    private String type;

    public GrossProfitReport() {
    }

    public GrossProfitReport(String name, double totalProfit, String profitPercent, double cost, double income, String type) {
        this.name = name;
        this.totalProfit = totalProfit;
        this.profitPercent = profitPercent;
        this.cost = cost;
        this.income = income;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public String getProfitPercent() {
        return profitPercent;
    }

    public void setProfitPercent(String profitPercent) {
        this.profitPercent = profitPercent;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(GrossProfitReport o) {
        return (int) (this.getTotalProfit() - o.getTotalProfit());
    }
}
