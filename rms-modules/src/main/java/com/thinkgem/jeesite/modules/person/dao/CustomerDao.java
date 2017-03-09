/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.person.entity.Customer;

/**
 * 客户信息DAO接口
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@MyBatisDao
public interface CustomerDao extends CrudDao<Customer> {

	/**
	 * 根据手机号查询客户信息
	 * */
	List<Customer> findCustomerByTelNo(Customer customer);

	/**
	 * 更新客户是否转化为租客的状态
	 * */
	void updateCustomerTransStat(Customer customer);
}