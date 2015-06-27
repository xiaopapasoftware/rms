/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.entity.CompanyContact;
import com.thinkgem.jeesite.modules.person.dao.CompanyContactDao;

/**
 * 物业公司联系人Service
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@Service
@Transactional(readOnly = true)
public class CompanyContactService extends CrudService<CompanyContactDao, CompanyContact> {

	public CompanyContact get(String id) {
		return super.get(id);
	}

	public List<CompanyContact> findList(CompanyContact companyContact) {
		return super.findList(companyContact);
	}

	public Page<CompanyContact> findPage(Page<CompanyContact> page, CompanyContact companyContact) {
		return super.findPage(page, companyContact);
	}

	@Transactional(readOnly = false)
	public void save(CompanyContact companyContact) {
		super.save(companyContact);
	}

	@Transactional(readOnly = false)
	public void delete(CompanyContact companyContact) {
		super.delete(companyContact);
	}

	@Transactional(readOnly = true)
	public List<CompanyContact> findCompanyContactByCompAndTel(CompanyContact companyContact) {
		return dao.findCompanyContactByCompAndTel(companyContact);
	}

}