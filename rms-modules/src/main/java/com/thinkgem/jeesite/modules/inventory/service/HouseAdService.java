/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.entity.HouseAd;
import com.thinkgem.jeesite.modules.inventory.dao.HouseAdDao;

/**
 * 广告管理Service
 * @author huangsc
 */
@Service
@Transactional(readOnly = true)
public class HouseAdService extends CrudService<HouseAdDao, HouseAd> {

	public HouseAd get(String id) {
		return super.get(id);
	}
	
	public List<HouseAd> findList(HouseAd houseAd) {
		return super.findList(houseAd);
	}
	
	public Page<HouseAd> findPage(Page<HouseAd> page, HouseAd houseAd) {
		return super.findPage(page, houseAd);
	}
	
	@Transactional(readOnly = false)
	public void save(HouseAd houseAd) {
		super.save(houseAd);
	}
	
	@Transactional(readOnly = false)
	public void delete(HouseAd houseAd) {
		super.delete(houseAd);
	}
	
}