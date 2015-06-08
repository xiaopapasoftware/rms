/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;

/**
 * 定金协议Service
 * @author huangsc
 * @version 2015-06-09
 */
@Service
@Transactional(readOnly = true)
public class DepositAgreementService extends CrudService<DepositAgreementDao, DepositAgreement> {

	public DepositAgreement get(String id) {
		return super.get(id);
	}
	
	public List<DepositAgreement> findList(DepositAgreement depositAgreement) {
		return super.findList(depositAgreement);
	}
	
	public Page<DepositAgreement> findPage(Page<DepositAgreement> page, DepositAgreement depositAgreement) {
		return super.findPage(page, depositAgreement);
	}
	
	@Transactional(readOnly = false)
	public void save(DepositAgreement depositAgreement) {
		super.save(depositAgreement);
	}
	
	@Transactional(readOnly = false)
	public void delete(DepositAgreement depositAgreement) {
		super.delete(depositAgreement);
	}
	
}