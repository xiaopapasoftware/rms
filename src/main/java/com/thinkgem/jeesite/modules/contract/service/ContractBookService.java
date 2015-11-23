/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.dao.ContractBookDao;

/**
 * 预约看房信息Service
 * @author huangsc
 */
@Service
@Transactional(readOnly = true)
public class ContractBookService extends CrudService<ContractBookDao, ContractBook> {

	public ContractBook get(String id) {
		return super.get(id);
	}
	
	public List<ContractBook> findList(ContractBook contractBook) {
		return super.findList(contractBook);
	}
	
	public Page<ContractBook> findPage(Page<ContractBook> page, ContractBook contractBook) {
		return super.findPage(page, contractBook);
	}
	
	@Transactional(readOnly = false)
	public void save(ContractBook contractBook) {
		super.save(contractBook);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractBook contractBook) {
		super.delete(contractBook);
	}
	
}