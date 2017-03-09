/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.Owner;

/**
 * 业主信息DAO接口
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@MyBatisDao
public interface OwnerDao extends CrudDao<Owner> {

	/**
	 * 根据身份证号码或手机号码或电话号码查询业主信息
	 * */
	List<Owner> findOwnersByCerNoOrMobNoOrTelNo(Owner owner);
}