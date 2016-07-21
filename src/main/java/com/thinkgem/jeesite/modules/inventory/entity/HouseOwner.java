package com.thinkgem.jeesite.modules.inventory.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

public class HouseOwner extends DataEntity<HouseOwner> {

    private static final long serialVersionUID = -6551861829972266169L;
    
    private String houseId;
    private String ownerId;

    public String getHouseId() {
	return houseId;
    }

    public void setHouseId(String houseId) {
	this.houseId = houseId;
    }

    public String getOwnerId() {
	return ownerId;
    }

    public void setOwnerId(String ownerId) {
	this.ownerId = ownerId;
    }
}
