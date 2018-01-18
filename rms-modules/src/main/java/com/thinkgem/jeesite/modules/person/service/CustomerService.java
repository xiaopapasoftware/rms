/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.dao.CustomerDao;
import com.thinkgem.jeesite.modules.person.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
	public List<Customer> findCustomerByTelNo(String cellPhone) {
		return dao.findCustomerByTelNo(cellPhone);
	}

	/**
	 * 更新客户是否转化为租客的状态
	 * */
	@Transactional(readOnly = false)
	public void updateCustomerTransStat(Customer customer) {
		dao.updateCustomerTransStat(customer);
	}
}