/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.entity.Customer;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.dao.TenantDao;

/**
 * 租客信息Service
 * 
 * @author huangsc
 * @version 2015-06-13
 */
@Service
@Transactional(readOnly = true)
public class TenantService extends CrudService<TenantDao, Tenant> {

	@Autowired
	private CustomerService customerService;

	public Tenant get(String id) {
		return super.get(id);
	}

	public List<Tenant> findList(Tenant tenant) {
		return super.findList(tenant);
	}

	public Page<Tenant> findPage(Page<Tenant> page, Tenant tenant) {
		return super.findPage(page, tenant);
	}

	@Transactional(readOnly = false)
	public void save(Tenant tenant) {
		super.save(tenant);
	}

	/**
	 * 保存从客户转化而来的租客
	 * */
	@Transactional(readOnly = false)
	public void saveAndUpdateCusStat(Tenant tenant) {
		super.save(tenant);
		String customerId = tenant.getCustomerId();
		if (StringUtils.isNotEmpty(customerId)) {
			Customer c = new Customer();
			c.setId(customerId);
			c.setIsTenant("1");
			customerService.updateCustomerTransStat(c);
		}
	}

	@Transactional(readOnly = false)
	public void delete(Tenant tenant) {
		super.delete(tenant);
	}

	@Transactional(readOnly = true)
	public List<Tenant> findTenantByIdTypeAndNo(Tenant tenant) {
		return dao.findTenantByIdTypeAndNo(tenant);
	}

}