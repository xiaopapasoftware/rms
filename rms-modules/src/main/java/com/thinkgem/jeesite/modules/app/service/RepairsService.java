/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.entity.Repairs;
import com.thinkgem.jeesite.modules.app.dao.RepairsDao;

/**
 * 报修Service
 * @author huangsc
 * @version 2015-12-14
 */
@Service
@Transactional(readOnly = true)
public class RepairsService extends CrudService<RepairsDao, Repairs> {

	public Repairs get(String id) {
		return super.get(id);
	}
	
	public List<Repairs> findList(Repairs repairs) {
		return super.findList(repairs);
	}
	
	public Page<Repairs> findPage(Page<Repairs> page, Repairs repairs) {
		return super.findPage(page, repairs);
	}
	
	@Transactional(readOnly = false)
	public void save(Repairs repairs) {
		super.save(repairs);
	}
	
	@Transactional(readOnly = false)
	public void delete(Repairs repairs) {
		super.delete(repairs);
	}
	
}