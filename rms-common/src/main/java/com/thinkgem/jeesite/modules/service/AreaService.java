/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.service;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.modules.dao.AreaDao;
import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 区域Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class AreaService extends TreeService<AreaDao, Area> {

	@Autowired
	private AreaDao areaDao;

	public List<Area> findAll(){
		return UserUtils.getAreaList();
	}

	@Transactional(readOnly = false)
	@Override
	public void save(Area area) {
		super.save(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}
	
	@Transactional(readOnly = false)
	@Override
	public void delete(Area area) {
		super.delete(area);
		UserUtils.removeCache(UserUtils.CACHE_AREA_LIST);
	}

	public List<Area> getCompanyList(){
		return areaDao.getAreaByType("3");
	}

	public List<Area> getAreaByParentId(String parentId, String type){
		return areaDao.getAreaByParentId(parentId, type);
	}
	
}
