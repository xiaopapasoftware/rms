package com.thinkgem.jeesite.modules.report.entity;

import org.springframework.util.StringUtils;

public class GrossProfitFormCondition {

    private String company;

    private String center;

    private String area;

    private String project;

    private String house;

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

    public boolean isEmpty() {
        return StringUtils.isEmpty(company) && StringUtils.isEmpty(center) && StringUtils.isEmpty(area)
                && StringUtils.isEmpty(project) && StringUtils.isEmpty(house);
    }
}
