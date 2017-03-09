/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.company.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 办公文件管理Entity
 * @author huangsc
 * @version 2015-09-12
 */
public class Document extends DataEntity<Document> {
	
	private static final long serialVersionUID = 1L;
	private String attachmentName;		// 附件名称
	private String attachmentType;		// 附件类型
	private String attachmentPath;		// 附件地址
	
	private List<String> pathList = new ArrayList<String>();
	
	public Document() {
		super();
	}

	public Document(String id){
		super(id);
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
	
	@Length(min=0, max=4000, message="附件地址长度必须介于 0 和 4000 之间")
	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public List<String> getPathList() {
		return pathList;
	}

	public void setPathList(List<String> pathList) {
		this.pathList = pathList;
	}
}