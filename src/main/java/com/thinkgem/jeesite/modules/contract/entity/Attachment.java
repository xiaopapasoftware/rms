/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 附件Entity
 * @author huangsc
 * @version 2015-06-14
 */
public class Attachment extends DataEntity<Attachment> {
	
	private static final long serialVersionUID = 1L;
	private String leaseContractId;		// 承租合同
	private String rentContractId;		// 出租合同
	private String attachmentName;		// 附件名称
	private String attachmentType;		// 附件类型
	private String attachmentPath;		// 附件地址
	
	public Attachment() {
		super();
	}

	public Attachment(String id){
		super(id);
	}

	@Length(min=0, max=64, message="承租合同长度必须介于 0 和 64 之间")
	public String getLeaseContractId() {
		return leaseContractId;
	}

	public void setLeaseContractId(String leaseContractId) {
		this.leaseContractId = leaseContractId;
	}
	
	@Length(min=0, max=64, message="出租合同长度必须介于 0 和 64 之间")
	public String getRentContractId() {
		return rentContractId;
	}

	public void setRentContractId(String rentContractId) {
		this.rentContractId = rentContractId;
	}
	
	@Length(min=0, max=64, message="附件名称长度必须介于 0 和 64 之间")
	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}
	
	@Length(min=0, max=64, message="附件类型长度必须介于 0 和 64 之间")
	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	
	@Length(min=0, max=64, message="附件地址长度必须介于 0 和 64 之间")
	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	
}