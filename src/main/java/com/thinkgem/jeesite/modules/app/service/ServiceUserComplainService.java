/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.app.entity.ServiceUserComplain;
import com.thinkgem.jeesite.modules.app.dao.ServiceUserComplainDao;

/**
 * 管家投拆Service
 * @author daniel
 * @version 2016-07-07
 */
@Service
@Transactional(readOnly = true)
public class ServiceUserComplainService extends CrudService<ServiceUserComplainDao, ServiceUserComplain> {

	public ServiceUserComplain get(String id) {
		return super.get(id);
	}
	
	public List<ServiceUserComplain> findList(ServiceUserComplain serviceUserComplain) {
		return super.findList(serviceUserComplain);
	}
	
	public Page<ServiceUserComplain> findPage(Page<ServiceUserComplain> page, ServiceUserComplain serviceUserComplain) {
		return super.findPage(page, serviceUserComplain);
	}
	
	@Transactional(readOnly = false)
	public void save(ServiceUserComplain serviceUserComplain) {
		super.save(serviceUserComplain);
	}
	
	@Transactional(readOnly = false)
	public void delete(ServiceUserComplain serviceUserComplain) {
		super.delete(serviceUserComplain);
	}
	
}