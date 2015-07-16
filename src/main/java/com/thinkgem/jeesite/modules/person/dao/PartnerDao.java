/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.Partner;

/**
 * 合作人信息DAO接口
 * 
 * @author huangsc
 * @version 2015-06-09
 */
@MyBatisDao
public interface PartnerDao extends CrudDao<Partner> {

	/**
	 * 根据合作人手机号和类型查询合作人信息
	 * */
	List<Partner> findPartnersByCellNoAndType(Partner partner);
}