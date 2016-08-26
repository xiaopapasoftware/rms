/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.enums.TradingAccountsStatusEnum;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.dao.ReceiptDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;

/**
 * 款项交易Service
 * 
 * @author huangsc @version 2015-06-11
 * @author wangshujin @version 2016-08-23
 */
@Service
@Transactional(readOnly = true)
public class PaymentTransService extends CrudService<PaymentTransDao, PaymentTrans> {

    @Autowired
    private TradingAccountsDao tradingAccountsDao;

    @Autowired
    private PaymentTradeDao paymentTradeDao;

    @Autowired
    private ReceiptDao receiptDao;

    @Autowired
    private AttachmentDao attachmentDao;

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
	paymentTrans.preInsert();
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
	dao.insert(paymentTrans);
    }

    /**
     * 删除对象下所有的款项，账务，款项账务关联关系，以及相关收据
     */
    @Transactional(readOnly = false)
    public void deletePaymentTransAndTradingAcctouns(String objectID) {
	// 删除款项记录
	PaymentTrans delPaymentTrans = new PaymentTrans();
	delPaymentTrans.setTransId(objectID);
	delPaymentTrans.preUpdate();
	dao.delete(delPaymentTrans);
	// 删除款项账务的关联关系、收据
	TradingAccounts tradingAccounts = new TradingAccounts();
	tradingAccounts.setTradeId(objectID);
	tradingAccounts.setTradeStatus(TradingAccountsStatusEnum.TO_AUDIT.getValue());
	List<TradingAccounts> list = tradingAccountsDao.findList(tradingAccounts);
	if (CollectionUtils.isNotEmpty(list)) {
	    for (TradingAccounts tmpTradingAccounts : list) {
		PaymentTrade pt = new PaymentTrade();
		pt.setTradeId(tmpTradingAccounts.getId());
		pt.preUpdate();
		paymentTradeDao.delete(pt);
		Receipt receipt = new Receipt();
		receipt.setTradingAccounts(tmpTradingAccounts);
		receipt.preUpdate();
		receiptDao.delete(receipt);
		Attachment attachment = new Attachment();
		attachment.setTradingAccountsId(tmpTradingAccounts.getId());
		attachment.preUpdate();
		attachmentDao.delete(attachment);
	    }
	}
	// 删除账务交易记录
	tradingAccounts.preUpdate();
	tradingAccountsDao.delete(tradingAccounts);
    }
}