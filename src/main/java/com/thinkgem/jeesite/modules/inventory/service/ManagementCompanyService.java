/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.dao.ManagementCompanyDao;
import com.thinkgem.jeesite.modules.inventory.entity.ManagementCompany;
import com.thinkgem.jeesite.modules.person.entity.CompanyContact;
import com.thinkgem.jeesite.modules.person.service.CompanyContactService;

/**
 * 物业公司Service
 * 
 * @author huangsc
 * @version 2015-06-03
 */
@Service
@Transactional(readOnly = true)
public class ManagementCompanyService extends CrudService<ManagementCompanyDao, ManagementCompany> {

	@Autowired
	private CompanyContactService companyContactService;

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

		CompanyContact nc = new CompanyContact();
		nc.setManagementCompany(managementCompany);
		List<CompanyContact> ccs = companyContactService.findList(nc);
		if (CollectionUtils.isNotEmpty(ccs)) {
			for (CompanyContact cc : ccs) {
				companyContactService.delete(cc);
			}
		}
	}

	@Transactional(readOnly = true)
	public List<ManagementCompany> findCompanyByNameAndAddress(ManagementCompany managementCompany) {
		return dao.findCompanyByNameAndAddress(managementCompany);
	}

}