/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;

/**
 * 电费结算Service
 * @author huangsc
 * @version 2015-07-04
 */
@Service
@Transactional(readOnly = true)
public class ElectricFeeService extends CrudService<ElectricFeeDao, ElectricFee> {

	public ElectricFee get(String id) {
		return super.get(id);
	}
	
	public List<ElectricFee> findList(ElectricFee electricFee) {
		return super.findList(electricFee);
	}
	
	public Page<ElectricFee> findPage(Page<ElectricFee> page, ElectricFee electricFee) {
		return super.findPage(page, electricFee);
	}
	
	@Transactional(readOnly = false)
	public void save(ElectricFee electricFee) {
		super.save(electricFee);
	}
	
	@Transactional(readOnly = false)
	public void delete(ElectricFee electricFee) {
		super.delete(electricFee);
	}
	
}