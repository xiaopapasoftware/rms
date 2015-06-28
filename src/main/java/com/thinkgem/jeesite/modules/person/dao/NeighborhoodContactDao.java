/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.NeighborhoodContact;

/**
 * 居委会联系人DAO接口
 * 
 * @author huangsc
 * @version 2015-06-03
 */
@MyBatisDao
public interface NeighborhoodContactDao extends CrudDao<NeighborhoodContact> {

	/**
	 * 根据所属居委和手机号查询居委联系人
	 * */
	List<NeighborhoodContact> findNeighborhoodContactByNeiAndTel(NeighborhoodContact neighborhoodContact);
}