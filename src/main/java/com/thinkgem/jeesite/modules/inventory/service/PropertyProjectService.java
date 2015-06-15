/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.entity.FileType;
import com.thinkgem.jeesite.modules.inventory.dao.PropertyProjectDao;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 物业项目Service
 * 
 * @author huangsc
 * @version 2015-06-03
 */
@Service
@Transactional(readOnly = true)
public class PropertyProjectService extends CrudService<PropertyProjectDao, PropertyProject> {

	@Autowired
	private AttachmentDao attachmentDao;

	public PropertyProject get(String id) {
		return super.get(id);
	}

	public List<PropertyProject> findList(PropertyProject propertyProject) {
		return super.findList(propertyProject);
	}

	public Page<PropertyProject> findPage(Page<PropertyProject> page, PropertyProject propertyProject) {
		return super.findPage(page, propertyProject);
	}

	@Transactional(readOnly = false)
	public void save(PropertyProject propertyProject) {
		String id = super.saveAndReturnId(propertyProject);
		if (StringUtils.isNotEmpty(propertyProject.getAttachmentPath())) {
			Attachment attachment = new Attachment();
			attachment.setId(IdGen.uuid());
			attachment.setPropertyProjectId(id);
			attachment.setAttachmentType(FileType.PROJECT_CHART.getValue());
			attachment.setAttachmentPath(propertyProject.getAttachmentPath());
			attachment.setCreateDate(new Date());
			attachment.setCreateBy(UserUtils.getUser());
			attachment.setUpdateDate(new Date());
			attachment.setUpdateBy(UserUtils.getUser());
			attachmentDao.insert(attachment);
		}
	}

	@Transactional(readOnly = false)
	public void delete(PropertyProject propertyProject) {
		super.delete(propertyProject);
	}

	/**
	 * 根据物业名称+地址查询状态为正常/审核的物业项目数量
	 * */
	@Transactional(readOnly = true)
	public List<PropertyProject> findPropertyProjectByNameAndAddress(PropertyProject propertyProject) {
		return dao.findPropertyProjectByNameAndAddress(propertyProject);
	}
}