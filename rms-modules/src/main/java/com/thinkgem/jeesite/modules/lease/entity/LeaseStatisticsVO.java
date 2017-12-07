package com.thinkgem.jeesite.modules.lease.entity;

import java.io.Serializable;
import java.util.List;

public class LeaseStatisticsVO implements Serializable{

    private static final long serialVersionUID = -7758244293101903126L;

    private LeaseStatistics parent;

    private List<LeaseStatistics> childList;

    public LeaseStatistics getParent() {
        return parent;
    }

    public void setParent(LeaseStatistics parent) {
        this.parent = parent;
    }

    public List<LeaseStatistics> getChildList() {
        return childList;
    }

    public void setChildList(List<LeaseStatistics> childList) {
        this.childList = childList;
    }
}
