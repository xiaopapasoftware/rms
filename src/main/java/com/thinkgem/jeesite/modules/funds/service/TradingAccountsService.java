/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.ReceiptDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 账务交易Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class TradingAccountsService extends CrudService<TradingAccountsDao, TradingAccounts> {
	@Autowired
	private PaymentTransService paymentTransService;
	@Autowired
	private PaymentTradeDao paymentTradeDao;
	@Autowired
	private DepositAgreementDao depositAgreementDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private AuditHisDao auditHisDao;
	@Autowired
	private TradingAccountsDao tradingAccountsDao;
	@Autowired
	private RentContractDao rentContractDao;
	@Autowired
	private ReceiptDao receiptDao;
	
	private static final String TRADING_ACCOUNTS_ROLE = "trading_accounts_role";//账务审批
	
	public TradingAccounts get(String id) {
		return super.get(id);
	}
	
	public List<TradingAccounts> findList(TradingAccounts tradingAccounts) {
		return super.findList(tradingAccounts);
	}
	
	public Page<TradingAccounts> findPage(Page<TradingAccounts> page, TradingAccounts tradingAccounts) {
		return super.findPage(page, tradingAccounts);
	}
	
	@Transactional(readOnly = false)
	public void audit(AuditHis auditHis) {
		AuditHis saveAuditHis = new AuditHis();
		saveAuditHis.setId(IdGen.uuid());
		saveAuditHis.setObjectType("2");//账务
		saveAuditHis.setObjectId(auditHis.getObjectId());
		saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
		saveAuditHis.setAuditStatus(auditHis.getAuditStatus());//1:通过 2:拒绝
		saveAuditHis.setCreateDate(new Date());
		saveAuditHis.setCreateBy(UserUtils.getUser());
		saveAuditHis.setUpdateDate(new Date());
		saveAuditHis.setUpdateBy(UserUtils.getUser());
		saveAuditHis.setAuditTime(new Date());
		saveAuditHis.setAuditUser(UserUtils.getUser().getId());
		saveAuditHis.setDelFlag("0");
		auditHisDao.insert(saveAuditHis);
		
		TradingAccounts tradingAccounts = tradingAccountsDao.get(auditHis.getObjectId());
		tradingAccounts.setUpdateDate(new Date());
		tradingAccounts.setUpdateBy(UserUtils.getUser());
		tradingAccounts.setTradeStatus(auditHis.getAuditStatus());
		tradingAccountsDao.update(tradingAccounts);
		
		if("1".equals(tradingAccounts.getTradeType())) {//定金协议
			DepositAgreement depositAgreement = depositAgreementDao.get(tradingAccounts.getTradeId());
			depositAgreement.setUpdateBy(UserUtils.getUser());
			depositAgreement.setUpdateDate(new Date());
			if(!"5".equals(depositAgreement.getAgreementStatus())) {
				depositAgreement.setAgreementStatus("1".equals(auditHis.getAuditStatus())?"5":"4");//5:到账收据审核通过 4:到账收据审核拒绝
				depositAgreementDao.update(depositAgreement);
			}
		} else if("3".equals(tradingAccounts.getTradeType())) {//新签合同
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			if(!"6".equals(rentContract.getContractStatus())) {
				rentContract.setContractStatus("1".equals(auditHis.getAuditStatus())?"6":"5");//6:到账收据审核通过 5:到账收据审核拒绝
				rentContractDao.update(rentContract);
			}
		} else if("7".equals(tradingAccounts.getTradeType())) {//正常退租
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus())?"8":"6");//8:正常退租 6:退租款项审核拒绝
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContractDao.update(rentContract);
		} else if("6".equals(tradingAccounts.getTradeType())) {//提前退租
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus())?"7":"6");//7:提前退租 6:退租款项审核拒绝
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContractDao.update(rentContract);
		} else if("8".equals(tradingAccounts.getTradeType())) {//逾期退租
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus())?"9":"6");//9:逾期退租 6:退租款项审核拒绝
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContractDao.update(rentContract);
		} else if("9".equals(tradingAccounts.getTradeType())) {//特殊退租
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus())?"16":"6");//16:特殊退租 6:退租款项审核拒绝
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContractDao.update(rentContract);
		}
	}
	
	@Transactional(readOnly = false)
	public void save(TradingAccounts tradingAccounts) {
		String id = super.saveAndReturnId(tradingAccounts);
		
		/*更新款项状态*/
		String[] transIds = tradingAccounts.getTransIds().split(",");
		for(int i=0;i<transIds.length;i++) {
			PaymentTrans paymentTrans = paymentTransService.get(transIds[i]);
			paymentTrans.setTransStatus("2");//完全到账登记
			paymentTrans.setTransAmount(paymentTrans.getTradeAmount());//实际交易金额
			paymentTrans.setLastAmount(0D);//剩余交易金额
			paymentTransService.save(paymentTrans);
			
			/*款项账务关联*/
			PaymentTrade paymentTrade = new PaymentTrade();
			paymentTrade.setTradeId(id);
			paymentTrade.setTransId(paymentTrans.getId());
			paymentTrade.setId(IdGen.uuid());
			paymentTrade.setCreateDate(new Date());
			paymentTrade.setCreateBy(UserUtils.getUser());
			paymentTrade.setUpdateDate(new Date());
			paymentTrade.setUpdateBy(UserUtils.getUser());
			paymentTrade.setDelFlag("0");
			paymentTradeDao.insert(paymentTrade);
		}
		
		String tradeId = tradingAccounts.getTradeId();
		String tradeType = tradingAccounts.getTradeType();
		
		if("1".equals(tradeType)) {//预约定金
			DepositAgreement depositAgreement = depositAgreementDao.get(tradeId);
			depositAgreement.setUpdateBy(UserUtils.getUser());
			depositAgreement.setUpdateDate(new Date());
			if(!"5".equals(depositAgreement.getAgreementStatus())) {
				depositAgreement.setAgreementStatus("1");//到账收据登记完成内容待审核
				depositAgreementDao.update(depositAgreement);
			}
		} else if("3".equals(tradeType)) {//新签合同
			RentContract rentContract = rentContractDao.get(tradeId);
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			if(!"6".equals(rentContract.getContractStatus())) {
				rentContract.setContractStatus("2");//到账收据完成合同内容待审核
				rentContractDao.update(rentContract);
			}
		} else if("7".equals(tradeType)||"8".equals(tradeType)||"6".equals(tradeType)) {
			RentContract rentContract = rentContractDao.get(tradeId);
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			if(!"6".equals(rentContract.getContractStatus())) {
				rentContract.setContractBusiStatus("5");//退租款项待审核
				rentContractDao.update(rentContract);
			}
		} else if("9".equals(tradeType)) {//特殊退租
			RentContract rentContract = rentContractDao.get(tradeId);
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContract.setContractBusiStatus("11");//特殊退租结算待审核
			rentContractDao.update(rentContract);
		}
		
		//审核
		Audit audit = new Audit();
		audit.setId(IdGen.uuid());
		audit.setObjectId(id);
		audit.setObjectType("2");//账务
		audit.setNextRole(TRADING_ACCOUNTS_ROLE);
		audit.setCreateDate(new Date());
		audit.setCreateBy(UserUtils.getUser());
		audit.setUpdateDate(new Date());
		audit.setUpdateBy(UserUtils.getUser());
		audit.setDelFlag("0");
		auditDao.insert(audit);
		
		/*收据*/
		for(Receipt receipt : tradingAccounts.getReceiptList()) {
			receipt.setId(IdGen.uuid());
			receipt.setTradingAccounts(tradingAccounts);
			receipt.setCreateDate(new Date());
			receipt.setCreateBy(UserUtils.getUser());
			receipt.setUpdateDate(new Date());
			receipt.setUpdateBy(UserUtils.getUser());
			receipt.setDelFlag("0");
			receiptDao.insert(receipt);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TradingAccounts tradingAccounts) {
		super.delete(tradingAccounts);
	}
	
}