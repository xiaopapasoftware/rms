/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.dao.PropertyProjectDao;

/**
 * 物业项目Service
 * @author huangsc
 * @version 2015-06-03
 */
@Service
@Transactional(readOnly = true)
public class PropertyProjectService extends CrudService<PropertyProjectDao, PropertyProject> {

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
		super.save(propertyProject);
	}
	
	@Transactional(readOnly = false)
	public void delete(PropertyProject propertyProject) {
		super.delete(propertyProject);
	}
	
}