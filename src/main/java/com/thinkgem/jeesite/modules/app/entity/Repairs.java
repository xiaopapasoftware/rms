/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 报修Entity
 * @author huangsc
 * @version 2015-12-14
 */
public class Repairs extends DataEntity<Repairs> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户ID, APP用户为注册手机号
	private String userMobile;		// 报修填写的联系手机号
    private String contractId;		// 合同号
	private String roomId;		// 房间号
	private String description;		// 描述
	private String steward;		// 管家
	private String stewardMobile;		// 管家电话
	private String status;		// 报修状态
	
	public Repairs() {
		super();
	}

	public Repairs(String id){
		super(id);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=64, message="报修填写的联系手机号长度必须介于 0 和 64 之间")
	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
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
	
	@Length(min=0, max=18, message="描述长度必须介于 0 和 18 之间")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=1, message="管家长度必须介于 0 和 1 之间")
	public String getSteward() {
		return steward;
	}

	public void setSteward(String steward) {
		this.steward = steward;
	}
	
	@Length(min=0, max=10, message="管家电话长度必须介于 0 和 10 之间")
	public String getStewardMobile() {
		return stewardMobile;
	}

	public void setStewardMobile(String stewardMobile) {
		this.stewardMobile = stewardMobile;
	}
	
	@Length(min=0, max=11, message="报修状态长度必须介于 0 和 11 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}