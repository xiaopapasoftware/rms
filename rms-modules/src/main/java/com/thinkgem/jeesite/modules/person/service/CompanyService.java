/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.dao.CompanyDao;
import com.thinkgem.jeesite.modules.person.entity.Company;
import com.thinkgem.jeesite.modules.person.entity.CompanyLinkman;

/**
 * 企业信息Service
 * 
 * @author huangsc
 * @version 2015-06-13
 */
@Service
@Transactional(readOnly = true)
public class CompanyService extends CrudService<CompanyDao, Company> {

	@Autowired
	private CompanyLinkmanService companyLinkmanService;

	public Company get(String id) {
		return super.get(id);
	}

	public List<Company> findList(Company company) {
		return super.findList(company);
	}

	public Page<Company> findPage(Page<Company> page, Company company) {
		return super.findPage(page, company);
	}

	@Transactional(readOnly = false)
	public void save(Company company) {
		super.save(company);
	}

	@Transactional(readOnly = false)
	public void delete(Company company) {
		super.delete(company);

		CompanyLinkman cl = new CompanyLinkman();
		cl.setCompany(company);
		List<CompanyLinkman> clms = companyLinkmanService.findList(cl);
		if (CollectionUtils.isNotEmpty(clms)) {
			for (CompanyLinkman clman : clms) {
				companyLinkmanService.delete(clman);
			}
		}
	}

	@Transactional(readOnly = true)
	public List<Company> findCompanyByIdTypeAndVal(Company company) {
		return dao.findCompanyByIdTypeAndVal(company);
	}

}