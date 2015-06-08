/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.entity.Remittancer;
import com.thinkgem.jeesite.modules.person.dao.RemittancerDao;

/**
 * 汇款人信息Service
 * @author huangsc
 * @version 2015-06-06
 */
@Service
@Transactional(readOnly = true)
public class RemittancerService extends CrudService<RemittancerDao, Remittancer> {

	public Remittancer get(String id) {
		return super.get(id);
	}
	
	public List<Remittancer> findList(Remittancer remittancer) {
		return super.findList(remittancer);
	}
	
	public Page<Remittancer> findPage(Page<Remittancer> page, Remittancer remittancer) {
		return super.findPage(page, remittancer);
	}
	
	@Transactional(readOnly = false)
	public void save(Remittancer remittancer) {
		super.save(remittancer);
	}
	
	@Transactional(readOnly = false)
	public void delete(Remittancer remittancer) {
		super.delete(remittancer);
	}
	
}