/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;

/**
 * 款项交易Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class PaymentTransService extends CrudService<PaymentTransDao, PaymentTrans> {

	public PaymentTrans get(String id) {
		return super.get(id);
	}
	
	public List<PaymentTrans> findList(PaymentTrans paymentTrans) {
		return super.findList(paymentTrans);
	}
	
	public Page<PaymentTrans> findPage(Page<PaymentTrans> page, PaymentTrans paymentTrans) {
		return super.findPage(page, paymentTrans);
	}
	
	@Transactional(readOnly = false)
	public void save(PaymentTrans paymentTrans) {
		super.save(paymentTrans);
	}
	
	@Transactional(readOnly = false)
	public void delete(PaymentTrans paymentTrans) {
		super.delete(paymentTrans);
	}
	
}