/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.entity.NormalFee;
import com.thinkgem.jeesite.modules.fee.dao.NormalFeeDao;

/**
 * 一般费用结算Service
 * @author huangsc
 * @version 2015-07-04
 */
@Service
@Transactional(readOnly = true)
public class NormalFeeService extends CrudService<NormalFeeDao, NormalFee> {

	public NormalFee get(String id) {
		return super.get(id);
	}
	
	public List<NormalFee> findList(NormalFee normalFee) {
		return super.findList(normalFee);
	}
	
	public Page<NormalFee> findPage(Page<NormalFee> page, NormalFee normalFee) {
		return super.findPage(page, normalFee);
	}
	
	@Transactional(readOnly = false)
	public void save(NormalFee normalFee) {
		super.save(normalFee);
	}
	
	@Transactional(readOnly = false)
	public void delete(NormalFee normalFee) {
		super.delete(normalFee);
	}
	
}