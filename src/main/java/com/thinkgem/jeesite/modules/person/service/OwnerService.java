/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import com.thinkgem.jeesite.modules.person.dao.OwnerDao;

/**
 * 业主信息Service
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@Service
@Transactional(readOnly = true)
public class OwnerService extends CrudService<OwnerDao, Owner> {

	public Owner get(String id) {
		return super.get(id);
	}

	public List<Owner> findList(Owner owner) {
		return super.findList(owner);
	}

	public Page<Owner> findPage(Page<Owner> page, Owner owner) {
		return super.findPage(page, owner);
	}

	@Transactional(readOnly = false)
	public void save(Owner owner) {
		super.save(owner);
	}

	@Transactional(readOnly = false)
	public void delete(Owner owner) {
		super.delete(owner);
	}

	@Transactional(readOnly = true)
	public List<Owner> findOwnersByCerNoOrMobNoOrTelNo(Owner owner) {
		return dao.findOwnersByCerNoOrMobNoOrTelNo(owner);
	}
}