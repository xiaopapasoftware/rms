/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.entity.NeighborhoodContact;
import com.thinkgem.jeesite.modules.person.dao.NeighborhoodContactDao;

/**
 * 居委会联系人Service
 * 
 * @author huangsc
 * @version 2015-06-03
 */
@Service
@Transactional(readOnly = true)
public class NeighborhoodContactService extends CrudService<NeighborhoodContactDao, NeighborhoodContact> {

	public NeighborhoodContact get(String id) {
		return super.get(id);
	}

	public List<NeighborhoodContact> findList(NeighborhoodContact neighborhoodContact) {
		return super.findList(neighborhoodContact);
	}

	public Page<NeighborhoodContact> findPage(Page<NeighborhoodContact> page, NeighborhoodContact neighborhoodContact) {
		return super.findPage(page, neighborhoodContact);
	}

	@Transactional(readOnly = false)
	public void save(NeighborhoodContact neighborhoodContact) {
		super.save(neighborhoodContact);
	}

	@Transactional(readOnly = false)
	public void delete(NeighborhoodContact neighborhoodContact) {
		super.delete(neighborhoodContact);
	}

	@Transactional(readOnly = true)
	public List<NeighborhoodContact> findNeighborhoodContactByNeiAndTel(NeighborhoodContact neighborhoodContact) {
		return dao.findNeighborhoodContactByNeiAndTel(neighborhoodContact);
	}
}