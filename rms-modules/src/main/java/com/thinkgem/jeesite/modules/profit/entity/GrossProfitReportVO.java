package com.thinkgem.jeesite.modules.profit.entity;

import java.io.Serializable;
import java.util.List;

public class GrossProfitReportVO implements Serializable{

    private static final long serialVersionUID = -5337535522254551133L;

    private GrossProfitReport parent;

    private List<GrossProfitReport> childReportList;

    public GrossProfitReport getParent() {
        return parent;
    }

    public void setParent(GrossProfitReport parent) {
        this.parent = parent;
    }

    public List<GrossProfitReport> getChildReportList() {
        return childReportList;
    }

    public void setChildReportList(List<GrossProfitReport> childReportList) {
        this.childReportList = childReportList;
    }
}
