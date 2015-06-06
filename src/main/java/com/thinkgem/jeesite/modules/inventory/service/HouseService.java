/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.dao.HouseDao;

/**
 * 房屋信息Service
 * @author huangsc
 * @version 2015-06-06
 */
@Service
@Transactional(readOnly = true)
public class HouseService extends CrudService<HouseDao, House> {

	public House get(String id) {
		return super.get(id);
	}
	
	public List<House> findList(House house) {
		return super.findList(house);
	}
	
	public Page<House> findPage(Page<House> page, House house) {
		return super.findPage(page, house);
	}
	
	@Transactional(readOnly = false)
	public void save(House house) {
		super.save(house);
	}
	
	@Transactional(readOnly = false)
	public void delete(House house) {
		super.delete(house);
	}
	
}