package com.thinkgem.jeesite.modules.contract.entity;

import java.util.Date;

public class LeaseContractDtlCondition {

    private String leaseContractId;

    private Date startDate;

    private Date endDate;

    public String getLeaseContractId() {
        return leaseContractId;
    }

    public void setLeaseContractId(String leaseContractId) {
        this.leaseContractId = leaseContractId;
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
