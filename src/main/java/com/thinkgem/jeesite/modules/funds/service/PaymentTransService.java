/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.common.enums.ActFlagEnum;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 款项交易Service
 * 
 * @author huangsc @version 2015-06-11
 * @author wangshujin @version 2016-08-23
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

    public Page<PaymentTrans> findRemind(Page<PaymentTrans> page, PaymentTrans paymentTrans) {
	paymentTrans.setPage(page);
	page.setList(dao.findRemindList(paymentTrans));
	return page;
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

    @Transactional(readOnly = false)
    public void generateAndSavePaymentTrans(String tradeType, String paymentType, String transId, String tradeDirection, Double tradeAmount, Double lastAmount, Double transAmount, String transStatus, Date startDate, Date expiredDate) {
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.setId(IdGen.uuid());
	paymentTrans.setTradeType(tradeType);
	paymentTrans.setPaymentType(paymentType);
	paymentTrans.setTransId(transId);
	paymentTrans.setTradeDirection(tradeDirection);
	paymentTrans.setStartDate(startDate);
	paymentTrans.setExpiredDate(expiredDate);
	paymentTrans.setTradeAmount(tradeAmount);
	paymentTrans.setLastAmount(lastAmount);
	paymentTrans.setTransAmount(transAmount);
	paymentTrans.setTransStatus(transStatus);
	User currentUser = UserUtils.getUser();
	Date currentDate = new Date();
	paymentTrans.setCreateDate(currentDate);
	paymentTrans.setCreateBy(currentUser);
	paymentTrans.setUpdateDate(currentDate);
	paymentTrans.setUpdateBy(currentUser);
	paymentTrans.setDelFlag(ActFlagEnum.NORMAL.getValue());
	dao.insert(paymentTrans);
    }
}