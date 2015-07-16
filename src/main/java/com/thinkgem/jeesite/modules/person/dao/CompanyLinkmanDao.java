/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.CompanyLinkman;

/**
 * 企业联系人DAO接口
 * 
 * @author huangsc
 * @version 2015-06-13
 */
@MyBatisDao
public interface CompanyLinkmanDao extends CrudDao<CompanyLinkman> {

	/**
	 * 根据企业+手机号查询企业联系人
	 * */
	List<CompanyLinkman> findCompLinkMansByCompAndTelNo(CompanyLinkman companyLinkman);
}