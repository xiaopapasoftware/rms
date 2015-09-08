/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.entity.Customer;
import com.thinkgem.jeesite.modules.person.dao.CustomerDao;

/**
 * 客户信息Service
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@Service
@Transactional(readOnly = true)
public class CustomerService extends CrudService<CustomerDao, Customer> {

	public Customer get(String id) {
		return super.get(id);
	}

	public List<Customer> findList(Customer customer) {
		return super.findList(customer);
	}

	public Page<Customer> findPage(Page<Customer> page, Customer customer) {
		return super.findPage(page, customer);
	}

	@Transactional(readOnly = false)
	public void save(Customer customer) {
		super.save(customer);
	}

	@Transactional(readOnly = false)
	public void delete(Customer customer) {
		super.delete(customer);
	}

	@Transactional(readOnly = true)
	public List<Customer> findCustomerByTelNo(Customer customer) {
		return dao.findCustomerByTelNo(customer);
	}

	/**
	 * 更新客户是否转化为租客的状态
	 * */
	@Transactional(readOnly = false)
	public void updateCustomerTransStat(Customer customer) {
		dao.updateCustomerTransStat(customer);
	}
}