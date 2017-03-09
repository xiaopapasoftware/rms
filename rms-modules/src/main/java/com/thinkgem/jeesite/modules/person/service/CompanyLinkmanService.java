/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.dao.CompanyLinkmanDao;
import com.thinkgem.jeesite.modules.person.entity.CompanyLinkman;

/**
 * 企业联系人Service
 * 
 * @author huangsc
 * @version 2015-06-13
 */
@Service
@Transactional(readOnly = true)
public class CompanyLinkmanService extends CrudService<CompanyLinkmanDao, CompanyLinkman> {

	public CompanyLinkman get(String id) {
		return super.get(id);
	}

	public List<CompanyLinkman> findList(CompanyLinkman companyLinkman) {
		return super.findList(companyLinkman);
	}

	public Page<CompanyLinkman> findPage(Page<CompanyLinkman> page, CompanyLinkman companyLinkman) {
		return super.findPage(page, companyLinkman);
	}

	@Transactional(readOnly = false)
	public void save(CompanyLinkman companyLinkman) {
		super.save(companyLinkman);
	}

	@Transactional(readOnly = false)
	public void delete(CompanyLinkman companyLinkman) {
		super.delete(companyLinkman);
	}

	@Transactional(readOnly = true)
	public List<CompanyLinkman> findCompLinkMansByCompAndTelNo(CompanyLinkman companyLinkman) {
		return dao.findCompLinkMansByCompAndTelNo(companyLinkman);
	}
}