/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.person.entity.Partner;
import com.thinkgem.jeesite.modules.person.dao.PartnerDao;

/**
 * 合作人信息Service
 * 
 * @author huangsc
 * @version 2015-06-09
 */
@Service
@Transactional(readOnly = true)
public class PartnerService extends CrudService<PartnerDao, Partner> {

	public Partner get(String id) {
		return super.get(id);
	}

	public List<Partner> findList(Partner partner) {
		return super.findList(partner);
	}

	public Page<Partner> findPage(Page<Partner> page, Partner partner) {
		return super.findPage(page, partner);
	}

	@Transactional(readOnly = false)
	public void save(Partner partner) {
		super.save(partner);
	}

	@Transactional(readOnly = false)
	public void delete(Partner partner) {
		super.delete(partner);
	}

	@Transactional(readOnly = true)
	public List<Partner> findPartnersByCellNoAndType(Partner partner) {
		return dao.findPartnersByCellNoAndType(partner);
	}

}