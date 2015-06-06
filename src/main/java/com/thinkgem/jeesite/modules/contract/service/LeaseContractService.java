/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.dao.LeaseContractDao;

/**
 * 承租合同Service
 * @author huangsc
 * @version 2015-06-06
 */
@Service
@Transactional(readOnly = true)
public class LeaseContractService extends CrudService<LeaseContractDao, LeaseContract> {

	public LeaseContract get(String id) {
		return super.get(id);
	}
	
	public List<LeaseContract> findList(LeaseContract leaseContract) {
		return super.findList(leaseContract);
	}
	
	public Page<LeaseContract> findPage(Page<LeaseContract> page, LeaseContract leaseContract) {
		return super.findPage(page, leaseContract);
	}
	
	@Transactional(readOnly = false)
	public void save(LeaseContract leaseContract) {
		super.save(leaseContract);
	}
	
	@Transactional(readOnly = false)
	public void delete(LeaseContract leaseContract) {
		super.delete(leaseContract);
	}
	
}