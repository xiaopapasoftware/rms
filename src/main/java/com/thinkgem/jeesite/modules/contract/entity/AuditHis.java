/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 审核历史Entity
 * @author huangsc
 * @version 2015-06-14
 */
public class AuditHis extends DataEntity<AuditHis> {
	
	private static final long serialVersionUID = 1L;
	private String objectType;		// 审核类型
	private String objectId;		// 审核对象ID
	private String auditUser;		// 审核人
	private Date auditTime;			// 审核时间
	private String auditStatus;		// 审核状态
	private String auditMsg;		// 审核意见
	private String auditUserName;
	private String updateUser;
	
	private String type;
	
	public AuditHis() {
		super();
	}

	public AuditHis(String id){
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
	
	@Length(min=0, max=100, message="审核人长度必须介于 0 和 100 之间")
	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	@Length(min=0, max=100, message="审核状态长度必须介于 0 和 100 之间")
	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@Length(min=0, max=100, message="审核意见长度必须介于 0 和 100 之间")
	public String getAuditMsg() {
		return auditMsg;
	}

	public void setAuditMsg(String auditMsg) {
		this.auditMsg = auditMsg;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}	
}