/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 定金协议Service
 * @author huangsc
 * @version 2015-06-09
 */
@Service
@Transactional(readOnly = true)
public class DepositAgreementService extends CrudService<DepositAgreementDao, DepositAgreement> {
	@Autowired
	private PaymentTransDao paymentTransDao;
	
	public DepositAgreement get(String id) {
		return super.get(id);
	}
	
	public List<DepositAgreement> findList(DepositAgreement depositAgreement) {
		return super.findList(depositAgreement);
	}
	
	public Page<DepositAgreement> findPage(Page<DepositAgreement> page, DepositAgreement depositAgreement) {
		return super.findPage(page, depositAgreement);
	}
	
	@Transactional(readOnly = false)
	public void save(DepositAgreement depositAgreement) {
		depositAgreement.setAgreementStatus("0");//录入完成到账收据待登记
		depositAgreement.setAgreementBusiStatus("0");//待转合同
		
		String id = super.saveAndReturnId(depositAgreement);
		//生成款项
		PaymentTrans delPaymentTrans = new PaymentTrans();
		delPaymentTrans.setTransId(id);
		paymentTransDao.delete(delPaymentTrans);
		
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setId(IdGen.uuid());
		paymentTrans.setTradeType("1");//定金协议
		paymentTrans.setPaymentType("0");//应收定金
		paymentTrans.setTransId(id);
		paymentTrans.setTradeDirection("1");//收款
		paymentTrans.setStartDate(depositAgreement.getStartDate());
		paymentTrans.setExpiredDate(depositAgreement.getExpiredDate());
		paymentTrans.setTradeAmount(depositAgreement.getDepositAmount());
		paymentTrans.setTransStatus("0");//未支付
		paymentTrans.setCreateDate(new Date());
		paymentTrans.setCreateBy(UserUtils.getUser());
		paymentTrans.setUpdateDate(new Date());
		paymentTrans.setUpdateBy(UserUtils.getUser());
		paymentTrans.setDelFlag("0");
		paymentTransDao.insert(paymentTrans);
	}
	
	@Transactional(readOnly = false)
	public void delete(DepositAgreement depositAgreement) {
		super.delete(depositAgreement);
	}
	
}