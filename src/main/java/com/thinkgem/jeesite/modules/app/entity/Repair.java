/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 报修Entity
 * @author daniel
 * @version 2016-07-04
 */
public class Repair extends DataEntity<Repair> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 报修人ID
	private String userName;		// 报修人
	private String userMobile;		// 报修电话
	private String repairMobile;		// 报修联系电话
	private String expectRepairTime;		// 期望维修时间
	private String contractId;		// 合同号
	private String roomId;		// 房间号
	private String description;		// 报修描述
	private String keeper;		// 管家
	private String keeperMobile;		// 管家电话
	private String status;		// 状态
	
	public Repair() {
		super();
	}

	public Repair(String id){
		super(id);
	}

	@Length(min=0, max=20, message="报修人ID长度必须介于 0 和 20 之间")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=20, message="报修人长度必须介于 0 和 20 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=0, max=20, message="报修电话长度必须介于 0 和 20 之间")
	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	
	@Length(min=0, max=20, message="报修联系电话长度必须介于 0 和 20 之间")
	public String getRepairMobile() {
		return repairMobile;
	}

	public void setRepairMobile(String repairMobile) {
		this.repairMobile = repairMobile;
	}
	
	@Length(min=0, max=50, message="期望维修时间长度必须介于 0 和 50 之间")
	public String getExpectRepairTime() {
		return expectRepairTime;
	}

	public void setExpectRepairTime(String expectRepairTime) {
		this.expectRepairTime = expectRepairTime;
	}
	
	@Length(min=0, max=64, message="合同号长度必须介于 0 和 64 之间")
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	
	@Length(min=0, max=64, message="房间号长度必须介于 0 和 64 之间")
	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	@Length(min=0, max=500, message="报修描述长度必须介于 0 和 500 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=20, message="管家长度必须介于 0 和 20 之间")
	public String getKeeper() {
		return keeper;
	}

	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}
	
	@Length(min=0, max=20, message="管家电话长度必须介于 0 和 20 之间")
	public String getKeeperMobile() {
		return keeperMobile;
	}

	public void setKeeperMobile(String keeperMobile) {
		this.keeperMobile = keeperMobile;
	}
	
	@Length(min=0, max=10, message="状态长度必须介于 0 和 10 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}