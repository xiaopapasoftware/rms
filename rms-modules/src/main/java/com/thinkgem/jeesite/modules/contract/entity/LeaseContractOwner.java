package com.thinkgem.jeesite.modules.contract.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class LeaseContractOwner extends DataEntity<LeaseContractOwner> {

    private static final long serialVersionUID = 8304235078547498446L;

    private String leaseContractId;

    private String ownerId;

    public String getLeaseContractId() {
        return leaseContractId;
    }

    public void setLeaseContractId(String leaseContractId) {
        this.leaseContractId = leaseContractId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
