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
import com.thinkgem.jeesite.modules.contract.dao.AccountingDao;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 退租核算Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class AccountingService extends CrudService<AccountingDao, Accounting> {
	@Autowired
	private PaymentTransDao paymentTransDao;
	
	public Accounting get(String id) {
		return super.get(id);
	}
	
	public List<Accounting> findList(Accounting accounting) {
		return super.findList(accounting);
	}
	
	public Page<Accounting> findPage(Page<Accounting> page, Accounting accounting) {
		return super.findPage(page, accounting);
	}
	
	@Transactional(readOnly = false)
	public void save(Accounting accounting) {
		super.save(accounting);
		
		/*更新款项*/
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setTransId(accounting.getRentContract().getId());
		paymentTrans.setTradeType("7");//正常退租
		paymentTransDao.delete(paymentTrans);
		
		Accounting tmpAccounting = new Accounting();
		tmpAccounting.setRentContract(accounting.getRentContract());
		tmpAccounting.setAccountingType(accounting.getAccountingType());
		List<Accounting> list = super.findList(tmpAccounting);
		for(Accounting saveAccounting : list) {
			paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType("7");//正常退租
			paymentTrans.setPaymentType(saveAccounting.getFeeType());
			paymentTrans.setTransId(saveAccounting.getRentContract().getId());
			paymentTrans.setTradeDirection("1");//收款
			paymentTrans.setStartDate(saveAccounting.getFeeDate());
			paymentTrans.setExpiredDate(saveAccounting.getFeeDate());
			paymentTrans.setTradeAmount(saveAccounting.getFeeAmount());
			paymentTrans.setLastAmount(saveAccounting.getFeeAmount());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");//未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			paymentTransDao.insert(paymentTrans);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Accounting accounting) {
		super.delete(accounting);
	}
	
}