/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inventory.entity.Building;

import java.util.List;

/**
 * 楼宇DAO接口
 * 
 * @author huangsc
 * @version 2015-06-03
 */
@MyBatisDao
public interface BuildingDao extends CrudDao<Building> {

	/**
	 * 根据楼宇名称+物业项目查询楼宇信息
	 * */
	List<Building> findBuildingByBldNameAndProProj(Building building);

	List<Building> getBuildingListByProjectId(String projectId);
}