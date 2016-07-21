/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.entity.Lessor;
import com.thinkgem.jeesite.modules.person.dao.LessorDao;

/**
 * 出租人管理Service
 * @author wage
 * @version 2015-09-18
 */
@Service
@Transactional(readOnly = true)
public class LessorService extends CrudService<LessorDao, Lessor> {

	public Lessor get(String id) {
		return super.get(id);
	}
	
	public List<Lessor> findList(Lessor lessor) {
		return super.findList(lessor);
	}
	
	public Page<Lessor> findPage(Page<Lessor> page, Lessor lessor) {
		return super.findPage(page, lessor);
	}
	
	@Transactional(readOnly = false)
	public void save(Lessor lessor) {
		super.save(lessor);
	}
	
	@Transactional(readOnly = false)
	public void delete(Lessor lessor) {
		super.delete(lessor);
	}
	
}