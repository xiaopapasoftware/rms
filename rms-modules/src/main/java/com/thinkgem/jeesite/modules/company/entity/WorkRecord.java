/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.company.entity;

import com.thinkgem.jeesite.modules.entity.User;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 工作记录Entity
 * @author huangsc
 * @version 2015-09-12
 */
public class WorkRecord extends DataEntity<WorkRecord> {
	
	private static final long serialVersionUID = 1L;
	private String recordType;		// 记录类型
	private String recordTitle;		// 记录标题
	private String recordContent;		// 记录内容
	private String recordStatus;		// 状态
	private User user;		// 批阅人
	private Date reviewDate;		// 批阅时间
	private String reviewRemarks;		// 批阅备注
	
	public WorkRecord() {
		super();
	}

	public WorkRecord(String id){
		super(id);
	}

	@Length(min=0, max=64, message="记录类型长度必须介于 0 和 64 之间")
	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
	@Length(min=0, max=255, message="记录标题长度必须介于 0 和 255 之间")
	public String getRecordTitle() {
		return recordTitle;
	}

	public void setRecordTitle(String recordTitle) {
		this.recordTitle = recordTitle;
	}
	
	public String getRecordContent() {
		return recordContent;
	}

	public void setRecordContent(String recordContent) {
		this.recordContent = recordContent;
	}
	
	@Length(min=0, max=64, message="状态长度必须介于 0 和 64 之间")
	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}
	
	@Length(min=0, max=255, message="批阅备注长度必须介于 0 和 255 之间")
	public String getReviewRemarks() {
		return reviewRemarks;
	}

	public void setReviewRemarks(String reviewRemarks) {
		this.reviewRemarks = reviewRemarks;
	}
	
}