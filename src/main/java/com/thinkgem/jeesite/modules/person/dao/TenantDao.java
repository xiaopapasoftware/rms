/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.Tenant;

/**
 * 租客信息DAO接口
 * 
 * @author huangsc
 * @version 2015-06-13
 */
@MyBatisDao
public interface TenantDao extends CrudDao<Tenant> {

	/**
	 * 根据租客证件类型和证件号码查询租客信息
	 * */
	List<Tenant> findTenantByIdTypeAndNo(Tenant tenant);
}