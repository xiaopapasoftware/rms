/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.dao.AccountingDao;

/**
 * 退租核算Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class AccountingService extends CrudService<AccountingDao, Accounting> {

	public Accounting get(String id) {
		return super.get(id);
	}
	
	public List<Accounting> findList(Accounting accounting) {
		return super.findList(accounting);
	}
	
	public Page<Accounting> findPage(Page<Accounting> page, Accounting accounting) {
		return super.findPage(page, accounting);
	}
	
	@Transactional(readOnly = false)
	public void save(Accounting accounting) {
		super.save(accounting);
	}
	
	@Transactional(readOnly = false)
	public void delete(Accounting accounting) {
		super.delete(accounting);
	}
	
}