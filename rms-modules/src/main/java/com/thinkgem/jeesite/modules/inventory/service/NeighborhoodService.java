/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.dao.NeighborhoodDao;
import com.thinkgem.jeesite.modules.inventory.entity.Neighborhood;
import com.thinkgem.jeesite.modules.person.entity.NeighborhoodContact;
import com.thinkgem.jeesite.modules.person.service.NeighborhoodContactService;

/**
 * 居委会Service
 * 
 * @author huangsc
 * @version 2015-06-03
 */
@Service
@Transactional(readOnly = true)
public class NeighborhoodService extends CrudService<NeighborhoodDao, Neighborhood> {

	@Autowired
	private NeighborhoodContactService neighborhoodContactService;

	public Neighborhood get(String id) {
		return super.get(id);
	}

	public List<Neighborhood> findList(Neighborhood neighborhood) {
		areaScopeFilter(neighborhood,"dsf","a.area_id=sua.area_id");
		return super.findList(neighborhood);
	}

	public Page<Neighborhood> findPage(Page<Neighborhood> page, Neighborhood neighborhood) {
		areaScopeFilter(neighborhood,"dsf","a.area_id=sua.area_id");
		return super.findPage(page, neighborhood);
	}

	@Transactional(readOnly = false)
	public void save(Neighborhood neighborhood) {
		super.save(neighborhood);
	}

	@Transactional(readOnly = false)
	public void delete(Neighborhood neighborhood) {
		super.delete(neighborhood);

		NeighborhoodContact neighborhoodContact = new NeighborhoodContact();
		neighborhoodContact.setNeighborhood(neighborhood);
		List<NeighborhoodContact> neiConts = neighborhoodContactService.findList(neighborhoodContact);
		if (CollectionUtils.isNotEmpty(neiConts)) {
			for (NeighborhoodContact nc : neiConts) {
				neighborhoodContactService.delete(nc);
			}
		}
	}

	/**
	 * 根据居委会名称+地址查询状态为正常/审核 的居委会数量
	 * */
	@Transactional(readOnly = true)
	public List<Neighborhood> findNeighborhoodByNameAndAddress(Neighborhood neighborhood) {
		areaScopeFilter(neighborhood,"dsf","a.area_id=sua.area_id");
		return dao.findNeighborhoodByNameAndAddress(neighborhood);
	}
}