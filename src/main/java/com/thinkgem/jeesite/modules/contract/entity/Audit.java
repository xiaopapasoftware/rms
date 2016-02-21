/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 审核Entity
 * @author huangsc
 * @version 2015-06-14
 */
public class Audit extends DataEntity<Audit> {
	
	private static final long serialVersionUID = 1L;
	private String objectType;		// 审核类型
	private String objectId;		// 审核对象ID
	private String nextRole;		// 下一级审核角色
	
	public Audit() {
		super();
	}

	public Audit(String id){
		super(id);
	}

	@Length(min=0, max=64, message="审核类型长度必须介于 0 和 64 之间")
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	@Length(min=0, max=64, message="审核对象ID长度必须介于 0 和 64 之间")
	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	@Length(min=0, max=100, message="下一级审核角色长度必须介于 0 和 100 之间")
	public String getNextRole() {
		return nextRole;
	}

	public void setNextRole(String nextRole) {
		this.nextRole = nextRole;
	}
	
}