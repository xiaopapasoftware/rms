package com.thinkgem.jeesite.modules.report.entity;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

public class GrossProfitCompareCondition {

    private List<ReportCompareCondition> conditionList;

    private String startDate;

    private String endDate;

    private int startDay = 26;

    private int endDay = 25;

    public List<ReportCompareCondition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<ReportCompareCondition> conditionList) {
        this.conditionList = conditionList;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(conditionList) || isDateEmpty();
    }

    public boolean isDateEmpty() {
        return StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate);
    }

    public Date getStart() {
        if (!isDateEmpty()) {
            String[] start = this.startDate.split("-");
            return new Date(Integer.valueOf(start[0]) - 1900, Integer.valueOf(start[1]) - 1, startDay);
        } else {
            return null;
        }
    }

    public Date getEnd() {
        if (!isDateEmpty()) {
            String[] end = this.endDate.split("-");
            return new Date(Integer.valueOf(end[0]) - 1900, Integer.valueOf(end[1]) - 1, endDay);
        } else {
            return null;
        }
    }

}
