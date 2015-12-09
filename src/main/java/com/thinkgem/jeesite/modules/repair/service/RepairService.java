/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.repair.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.repair.entity.Repair;
import com.thinkgem.jeesite.modules.repair.dao.RepairDao;

/**
 * 报修Service
 * @author daniel
 * @version 2015-12-06
 */
@Service
@Transactional(readOnly = true)
public class RepairService extends CrudService<RepairDao, Repair> {

	public Repair get(String id) {
		return super.get(id);
	}
	
	public List<Repair> findList(Repair repair) {
		return super.findList(repair);
	}
	
	public Page<Repair> findPage(Page<Repair> page, Repair repair) {
		return super.findPage(page, repair);
	}
	
	@Transactional(readOnly = false)
	public void save(Repair repair) {
		super.save(repair);
	}
	
	@Transactional(readOnly = false)
	public void delete(Repair repair) {
		super.delete(repair);
	}
	
}