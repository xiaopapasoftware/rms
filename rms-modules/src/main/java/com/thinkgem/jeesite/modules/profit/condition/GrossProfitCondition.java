package com.thinkgem.jeesite.modules.profit.condition;

import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;

import java.util.Date;

public class GrossProfitCondition {

    private GrossProfitTypeEnum typeEnum;

    private String id;

    private Date startDate;

    private Date endDate;

    public GrossProfitTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(GrossProfitTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
