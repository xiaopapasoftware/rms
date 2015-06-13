/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.dao.AgreementChangeDao;

/**
 * 协议变更Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class AgreementChangeService extends CrudService<AgreementChangeDao, AgreementChange> {

	public AgreementChange get(String id) {
		return super.get(id);
	}
	
	public List<AgreementChange> findList(AgreementChange agreementChange) {
		return super.findList(agreementChange);
	}
	
	public Page<AgreementChange> findPage(Page<AgreementChange> page, AgreementChange agreementChange) {
		return super.findPage(page, agreementChange);
	}
	
	@Transactional(readOnly = false)
	public void save(AgreementChange agreementChange) {
		super.save(agreementChange);
	}
	
	@Transactional(readOnly = false)
	public void delete(AgreementChange agreementChange) {
		super.delete(agreementChange);
	}
	
}