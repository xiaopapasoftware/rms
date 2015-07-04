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
import com.thinkgem.jeesite.modules.fee.entity.NormalFee;
import com.thinkgem.jeesite.modules.fee.dao.NormalFeeDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 一般费用结算Service
 * @author huangsc
 * @version 2015-07-04
 */
@Service
@Transactional(readOnly = true)
public class NormalFeeService extends CrudService<NormalFeeDao, NormalFee> {
	@Autowired
	private PaymentTransDao paymentTransDao;
	
	public NormalFee get(String id) {
		return super.get(id);
	}
	
	public List<NormalFee> findList(NormalFee normalFee) {
		return super.findList(normalFee);
	}
	
	public Page<NormalFee> findPage(Page<NormalFee> page, NormalFee normalFee) {
		return super.findPage(page, normalFee);
	}
	
	@Transactional(readOnly = false)
	public void save(NormalFee normalFee) {
		normalFee.setSettleStatus("1");//结算待审核
		super.save(normalFee);
		
		/*生成款项*/
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setId(IdGen.uuid());
		String tradeTyp="",parmentType="";
		if("0".equals(normalFee.getFeeType())) {
			tradeTyp = "12";//水费缴纳
			parmentType = "14";//水费金额
		} else if("1".equals(normalFee.getFeeType())) {
			tradeTyp = "13";//燃气费缴纳
			parmentType = "16";//燃气金额
		} else if("2".equals(normalFee.getFeeType())) {
			tradeTyp = "15";//宽带费缴纳
			parmentType = "20";//宽带费
		} else if("3".equals(normalFee.getFeeType())) {
			tradeTyp = "14";//有线费缴纳
			parmentType = "18";//有线电视费
		}
		paymentTrans.setTradeType(tradeTyp);
		paymentTrans.setPaymentType(parmentType);
		paymentTrans.setTransId(normalFee.getRentContractId());//合同
		paymentTrans.setTradeDirection("1");//收款
		paymentTrans.setStartDate(new Date());
		paymentTrans.setExpiredDate(new Date());
		paymentTrans.setTradeAmount(normalFee.getPersonFee());
		paymentTrans.setLastAmount(0D);
		paymentTrans.setTransAmount(normalFee.getPersonFee());
		paymentTrans.setTransStatus("2");//完全到账登记
		paymentTrans.setCreateDate(new Date());
		paymentTrans.setCreateBy(UserUtils.getUser());
		paymentTrans.setUpdateDate(new Date());
		paymentTrans.setUpdateBy(UserUtils.getUser());
		paymentTrans.setDelFlag("0");
		paymentTransDao.insert(paymentTrans);
	}
	
	@Transactional(readOnly = false)
	public void delete(NormalFee normalFee) {
		super.delete(normalFee);
	}
	
}