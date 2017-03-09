/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.CompanyContact;

/**
 * 物业公司联系人DAO接口
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@MyBatisDao
public interface CompanyContactDao extends CrudDao<CompanyContact> {

	/**
	 * 根据所属物业公司和手机号查询物业公司联系人
	 * */
	List<CompanyContact> findCompanyContactByCompAndTel(CompanyContact companyContact);

}