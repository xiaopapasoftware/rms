/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.entity.ManagementCompany;
import com.thinkgem.jeesite.modules.inventory.dao.ManagementCompanyDao;

/**
 * 物业公司Service
 * @author huangsc
 * @version 2015-06-03
 */
@Service
@Transactional(readOnly = true)
public class ManagementCompanyService extends CrudService<ManagementCompanyDao, ManagementCompany> {

	public ManagementCompany get(String id) {
		return super.get(id);
	}
	
	public List<ManagementCompany> findList(ManagementCompany managementCompany) {
		return super.findList(managementCompany);
	}
	
	public Page<ManagementCompany> findPage(Page<ManagementCompany> page, ManagementCompany managementCompany) {
		return super.findPage(page, managementCompany);
	}
	
	@Transactional(readOnly = false)
	public void save(ManagementCompany managementCompany) {
		super.save(managementCompany);
	}
	
	@Transactional(readOnly = false)
	public void delete(ManagementCompany managementCompany) {
		super.delete(managementCompany);
	}
	
}