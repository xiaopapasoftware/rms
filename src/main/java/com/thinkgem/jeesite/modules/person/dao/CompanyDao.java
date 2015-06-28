/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.Company;

/**
 * 企业信息DAO接口
 * 
 * @author huangsc
 * @version 2015-06-13
 */
@MyBatisDao
public interface CompanyDao extends CrudDao<Company> {

	/**
	 * 根据企业证书类型和证书号查询企业信息
	 * */
	List<Company> findCompanyByIdTypeAndVal(Company company);
}