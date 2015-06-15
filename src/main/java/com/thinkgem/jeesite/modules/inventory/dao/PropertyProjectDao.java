/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

/**
 * 物业项目DAO接口
 * 
 * @author huangsc
 * @version 2015-06-03
 */
@MyBatisDao
public interface PropertyProjectDao extends CrudDao<PropertyProject> {

	List<PropertyProject> findPropertyProjectByNameAndAddress(PropertyProject propertyProject);
}