/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.dao.BuildingDao;

/**
 * 楼宇Service
 * @author huangsc
 * @version 2015-06-03
 */
@Service
@Transactional(readOnly = true)
public class BuildingService extends CrudService<BuildingDao, Building> {

	public Building get(String id) {
		return super.get(id);
	}
	
	public List<Building> findList(Building building) {
		return super.findList(building);
	}
	
	public Page<Building> findPage(Page<Building> page, Building building) {
		return super.findPage(page, building);
	}
	
	@Transactional(readOnly = false)
	public void save(Building building) {
		super.save(building);
	}
	
	@Transactional(readOnly = false)
	public void delete(Building building) {
		super.delete(building);
	}
	
}