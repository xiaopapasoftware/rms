/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 电费结算Service
 * @author huangsc
 * @version 2015-07-04
 */
@Service
@Transactional(readOnly = true)
public class ElectricFeeService extends CrudService<ElectricFeeDao, ElectricFee> {
	@Autowired
	private PaymentTransDao paymentTransDao;
	
	public ElectricFee get(String id) {
		return super.get(id);
	}
	
	public List<ElectricFee> findList(ElectricFee electricFee) {
		return super.findList(electricFee);
	}
	
	public Page<ElectricFee> findPage(Page<ElectricFee> page, ElectricFee electricFee) {
		return super.findPage(page, electricFee);
	}
	
	@Transactional(readOnly = false)
	public void save(ElectricFee electricFee) {
		electricFee.setSettleStatus("1");//结算待审核
		super.save(electricFee);
		
		/*生成款项*/
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setId(IdGen.uuid());
		paymentTrans.setTradeType("11");//电费充值
		paymentTrans.setPaymentType("11");//电费自用金额
		paymentTrans.setTransId(electricFee.getRentContractId());//合同
		paymentTrans.setTradeDirection("1");//收款
		paymentTrans.setStartDate(new Date());
		paymentTrans.setExpiredDate(new Date());
		paymentTrans.setTradeAmount(electricFee.getPersonFee());
		paymentTrans.setLastAmount(0D);
		paymentTrans.setTransAmount(electricFee.getPersonFee());
		paymentTrans.setTransStatus("2");//完全到账登记
		paymentTrans.setCreateDate(new Date());
		paymentTrans.setCreateBy(UserUtils.getUser());
		paymentTrans.setUpdateDate(new Date());
		paymentTrans.setUpdateBy(UserUtils.getUser());
		paymentTrans.setDelFlag("0");
		paymentTransDao.insert(paymentTrans);
	}
	
	@Transactional(readOnly = false)
	public void delete(ElectricFee electricFee) {
		super.delete(electricFee);
	}
	
}