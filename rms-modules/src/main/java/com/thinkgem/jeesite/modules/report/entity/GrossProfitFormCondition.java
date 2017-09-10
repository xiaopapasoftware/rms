package com.thinkgem.jeesite.modules.report.entity;

import org.springframework.util.StringUtils;

import java.util.Date;

public class GrossProfitFormCondition {

    private String company;

    private String center;

    private String area;

    private String project;

    private String house;

    private String startDate;

    private String endDate;

    private int startDay = 25;

    private int endDay = 24;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
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
        return isAddressEmpty() || isDateEmpty();
    }

    public boolean isAddressEmpty() {
        return StringUtils.isEmpty(company) && StringUtils.isEmpty(center) && StringUtils.isEmpty(area)
                && StringUtils.isEmpty(project) && StringUtils.isEmpty(house);
    }

    public boolean isDateEmpty() {
        return StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate);
    }

    public Date getStart() {
        if (!isDateEmpty()) {
            String[] start = this.startDate.split("-");
            return new Date(Integer.valueOf(start[0]) - 1900, Integer.valueOf(start[1]), startDay);
        } else {
            return null;
        }
    }

    public Date getEnd() {
        if (!isDateEmpty()) {
            String[] end = this.endDate.split("-");
            return new Date(Integer.valueOf(end[0]) - 1900, Integer.valueOf(end[1]), endDay);
        } else {
            return null;
        }
    }

}
