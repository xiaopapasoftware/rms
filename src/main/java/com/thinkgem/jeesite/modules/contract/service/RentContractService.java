/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;

/**
 * 出租合同Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class RentContractService extends CrudService<RentContractDao, RentContract> {

	public RentContract get(String id) {
		return super.get(id);
	}
	
	public List<RentContract> findList(RentContract rentContract) {
		return super.findList(rentContract);
	}
	
	public Page<RentContract> findPage(Page<RentContract> page, RentContract rentContract) {
		return super.findPage(page, rentContract);
	}
	
	@Transactional(readOnly = false)
	public void save(RentContract rentContract) {
		rentContract.setContractBusiStatus("0");//有效
		super.save(rentContract);
	}
	
	@Transactional(readOnly = false)
	public void delete(RentContract rentContract) {
		super.delete(rentContract);
	}
	
}