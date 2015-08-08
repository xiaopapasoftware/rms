/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.dao.LeaseContractDao;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.dao.NormalFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.fee.entity.NormalFee;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.dao.ReceiptDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
/**
 * 账务交易Service
 * 
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
	@Autowired
	private LeaseContractDao leaseContractDao;
	@Autowired
	private PaymentTransDao paymentTransDao;
	@Autowired
	private ElectricFeeDao electricFeeDao;
	@Autowired
	private NormalFeeDao normalFeeDao;

	private static final String TRADING_ACCOUNTS_ROLE = "trading_accounts_role";// 账务审批

	public TradingAccounts get(String id) {
		return super.get(id);
	}

	public List<TradingAccounts> findList(TradingAccounts tradingAccounts) {
		return super.findList(tradingAccounts);
	}

	public List<Receipt> findReceiptList(TradingAccounts tradingAccounts) {
		Receipt receipt = new Receipt();
		receipt.setTradingAccounts(tradingAccounts);
		return receiptDao.findList(receipt);
	}

	public Page<TradingAccounts> findPage(Page<TradingAccounts> page, TradingAccounts tradingAccounts) {
		return super.findPage(page, tradingAccounts);
	}

	@Transactional(readOnly = false)
	public void audit(AuditHis auditHis) {
		AuditHis saveAuditHis = new AuditHis();
		saveAuditHis.setId(IdGen.uuid());
		saveAuditHis.setObjectType("2");// 账务
		saveAuditHis.setObjectId(auditHis.getObjectId());
		saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
		saveAuditHis.setAuditStatus(auditHis.getAuditStatus());// 1:通过 2:拒绝
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

		// if ("0".equals(tradingAccounts.getTradeType())) {// 承租合同
		// LeaseContract leaseContract =
		// leaseContractDao.get(tradingAccounts.getTradeId());
		// if (!"1".equals(leaseContract.getContractStatus())) {
		// leaseContract.setContractStatus("1".equals(auditHis.getAuditStatus())
		// ? "1" : "2");
		// leaseContract.setUpdateBy(UserUtils.getUser());
		// leaseContract.setUpdateDate(new Date());
		// leaseContractDao.update(leaseContract);
		// }
		// } else
		if ("1".equals(tradingAccounts.getTradeType())) {
			// 定金协议 5:到账收据审核通过 4:到账收据审核拒绝
			DepositAgreement depositAgreement = depositAgreementDao.get(tradingAccounts.getTradeId());
			if (!"5".equals(depositAgreement.getAgreementStatus())) {
				depositAgreement.setUpdateBy(UserUtils.getUser());
				depositAgreement.setUpdateDate(new Date());
				depositAgreement.setAgreementStatus("1".equals(auditHis.getAuditStatus()) ? "5" : "4");
				if ("1".equals(auditHis.getAuditStatus())) {
					depositAgreement.setAgreementBusiStatus("0");// 待转合同
				}
				depositAgreementDao.update(depositAgreement);
			}
		} else if ("3".equals(tradingAccounts.getTradeType()) || "4".equals(tradingAccounts.getTradeType())
				|| "5".equals(tradingAccounts.getTradeType())) {// 新签合同、正常人工续签、逾期自动续签
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			if (!"6".equals(rentContract.getContractStatus())) {
				if ("1".equals(auditHis.getAuditStatus())) {// 账务交易审核成功
					if (checkRentContractTransAmountEnough(rentContract)) {
						rentContract.setContractStatus("6");// 6:到账收据审核通过
						rentContract.setContractBusiStatus("0");// 有效
						rentContract.setUpdateBy(UserUtils.getUser());
						rentContract.setUpdateDate(new Date());
						rentContractDao.update(rentContract);
					}
				} else {
					rentContract.setContractStatus("5");// 5:到账收据审核拒绝
					rentContract.setUpdateBy(UserUtils.getUser());
					rentContract.setUpdateDate(new Date());
					rentContractDao.update(rentContract);
				}
			}
		} else if ("7".equals(tradingAccounts.getTradeType())) {
			// 正常退租 8:正常退租 6:退租款项审核拒绝
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus()) ? "8" : "6");
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContractDao.update(rentContract);
		} else if ("6".equals(tradingAccounts.getTradeType())) {
			// 提前退租 7:提前退租 6:退租款项审核拒绝
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus()) ? "7" : "6");
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContractDao.update(rentContract);
		} else if ("8".equals(tradingAccounts.getTradeType())) {
			// 逾期退租 9:逾期退租 6:退租款项审核拒绝
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus()) ? "9" : "6");
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContractDao.update(rentContract);
		} else if ("9".equals(tradingAccounts.getTradeType())) {
			// 特殊退租 16:特殊退租 6:退租款项审核拒绝
			RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
			rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus()) ? "16" : "6");
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContractDao.update(rentContract);
		} else if ("10".equals(tradingAccounts.getTradeType()) || "11".equals(tradingAccounts.getTradeType())) {// 电费缴纳、电费充值
			PaymentTrade paymentTrade = new PaymentTrade();
			paymentTrade.setDelFlag("0");
			paymentTrade.setTradeId(tradingAccounts.getId());
			List<PaymentTrade> list = paymentTradeDao.findList(paymentTrade);

			/* 更新充值记录 */
			for (PaymentTrade tmpPaymentTrade : list) {
				PaymentTrans paymentTrans = paymentTransDao.get(tmpPaymentTrade.getTransId());
				ElectricFee electricFee = new ElectricFee();
				electricFee.setPaymentTransId(paymentTrans.getId());
				electricFee.setDelFlag("0");
				electricFee = electricFeeDao.get(electricFee);
				if (null != electricFee) {
					electricFee.setSettleStatus("1".equals(auditHis.getAuditStatus()) ? "3" : "2");// 3:审核通过
																									// 2:审核拒绝
					electricFeeDao.update(electricFee);
				}
			}
		} else if ("12".equals(tradingAccounts.getTradeType()) || "13".equals(tradingAccounts.getTradeType())
				|| "14".equals(tradingAccounts.getTradeType()) || "15".equals(tradingAccounts.getTradeType())) {
			// 水费缴纳、燃气费缴纳、有线费缴纳、宽带费缴纳
			PaymentTrade paymentTrade = new PaymentTrade();
			paymentTrade.setDelFlag("0");
			paymentTrade.setTradeId(tradingAccounts.getId());
			List<PaymentTrade> list = paymentTradeDao.findList(paymentTrade);

			/* 更新充值记录 */
			for (PaymentTrade tmpPaymentTrade : list) {
				PaymentTrans paymentTrans = paymentTransDao.get(tmpPaymentTrade.getTransId());
				NormalFee normalFee = new NormalFee();
				normalFee.setPaymentTransId(paymentTrans.getId());
				normalFee.setDelFlag("0");
				normalFee = normalFeeDao.get(normalFee);
				if (null != normalFee) {
					normalFee.setSettleStatus("1".equals(auditHis.getAuditStatus()) ? "3" : "2");// 3:审核通过
																									// 2:审核拒绝
					normalFeeDao.update(normalFee);
				}
			}
		}
	}
	@Transactional(readOnly = false)
	public void save(TradingAccounts tradingAccounts) {
		String id = super.saveAndReturnId(tradingAccounts);

		/* 更新款项状态 */
		if (!StringUtils.isEmpty(tradingAccounts.getTransIds())) {
			String[] transIds = tradingAccounts.getTransIds().split(",");
			/* 款项账务关联 */
			PaymentTrade paymentTrade = new PaymentTrade();
			paymentTrade.setTradeId(id);
			paymentTradeDao.delete(paymentTrade);
			for (int i = 0; i < transIds.length; i++) {
				PaymentTrans paymentTrans = paymentTransService.get(transIds[i]);
				paymentTrans.setTransStatus("2");// 完全到账登记
				paymentTrans.setTransAmount(paymentTrans.getTradeAmount());// 实际交易金额
				paymentTrans.setLastAmount(0D);// 剩余交易金额
				paymentTransService.save(paymentTrans);


				paymentTrade.setTransId(paymentTrans.getId());
				paymentTrade.setId(IdGen.uuid());
				paymentTrade.setCreateDate(new Date());
				paymentTrade.setCreateBy(UserUtils.getUser());
				paymentTrade.setUpdateDate(new Date());
				paymentTrade.setUpdateBy(UserUtils.getUser());
				paymentTrade.setDelFlag("0");
				paymentTradeDao.insert(paymentTrade);
			}
		}

		String tradeId = tradingAccounts.getTradeId();
		String tradeType = tradingAccounts.getTradeType();

		if ("1".equals(tradeType)) {// 预约定金
			DepositAgreement depositAgreement = depositAgreementDao.get(tradeId);
			depositAgreement.setUpdateBy(UserUtils.getUser());
			depositAgreement.setUpdateDate(new Date());
			if ("0".equals(depositAgreement.getAgreementStatus())) {// 定金协议状态为“录入完成到账收据待登记”
				depositAgreement.setAgreementStatus("1");// '1':'到账收据登记完成内容待审核'
				depositAgreementDao.update(depositAgreement);
			} else {
				if (!"3".equals(depositAgreement.getAgreementStatus())// '3':'内容审核通过到账收据待审核'
						&& !"5".equals(depositAgreement.getAgreementStatus())) {// "5":到账收据审核通过
					depositAgreement.setAgreementStatus("3");// 内容审核通过到账收据待审核
					depositAgreementDao.update(depositAgreement);
				}
			}
		} else if ("3".equals(tradeType) || "4".equals(tradeType) || "5".equals(tradeType)) {// 新签合同、正常人工续签、逾期自动续签
			RentContract rentContract = rentContractDao.get(tradeId);
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			if ("1".equals(rentContract.getContractStatus())) {// '1':'录入完成到账收据待登记'
				rentContract.setContractStatus("2");// '2':'到账收据完成合同内容待审核'
				rentContractDao.update(rentContract);
			} else {
				if (!"4".equals(rentContract.getContractStatus()) && !"6".equals(rentContract.getContractStatus())) {// '4':'内容审核通过到账收据待审核',"6":到账收据审核通过
					rentContract.setContractStatus("4");// 内容审核通过到账收据待审核
					rentContractDao.update(rentContract);
				}
			}
		} else if ("7".equals(tradeType) || "8".equals(tradeType) || "6".equals(tradeType)) {//'6''提前退租';'7''正常退租';'8''逾期退租'
			RentContract rentContract = rentContractDao.get(tradeId);
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			if ("6".equals(rentContract.getContractStatus())) {//'6''到账收据审核通过'
				rentContract.setContractBusiStatus("5");// 退租款项待审核
				rentContractDao.update(rentContract);
			}
		} else if ("9".equals(tradeType)) {// 特殊退租
			RentContract rentContract = rentContractDao.get(tradeId);
			rentContract.setUpdateBy(UserUtils.getUser());
			rentContract.setUpdateDate(new Date());
			rentContract.setContractBusiStatus("11");// 特殊退租结算待审核
			rentContractDao.update(rentContract);
		}

		// 审核
		Audit audit = new Audit();
		audit.setId(IdGen.uuid());
		audit.setObjectId(id);
		audit.setObjectType("2");// 账务
		audit.setNextRole(TRADING_ACCOUNTS_ROLE);
		audit.setCreateDate(new Date());
		audit.setCreateBy(UserUtils.getUser());
		audit.setUpdateDate(new Date());
		audit.setUpdateBy(UserUtils.getUser());
		audit.setDelFlag("0");
		auditDao.insert(audit);

		/* 收据 */
		if (null != tradingAccounts.getReceiptList()) {
			Receipt delReceipt = new Receipt();
			TradingAccounts delTradingAccounts = new TradingAccounts();
			delTradingAccounts.setId(id);
			delReceipt.setTradingAccounts(delTradingAccounts);
			receiptDao.delete(delReceipt);

			for (Receipt receipt : tradingAccounts.getReceiptList()) {
				receipt.setId(IdGen.uuid());
				receipt.setTradingAccounts(tradingAccounts);
				receipt.setReceiptDate(new Date());
				receipt.setCreateDate(new Date());
				receipt.setCreateBy(UserUtils.getUser());
				receipt.setUpdateDate(new Date());
				receipt.setUpdateBy(UserUtils.getUser());
				receipt.setDelFlag("0");
				receiptDao.insert(receipt);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(TradingAccounts tradingAccounts) {
		super.delete(tradingAccounts);
	}

	/**
	 * 校验新签合同（直接新签、定金转合同）已经成功到账的金额是否超过合同的水电押金+房租押金+1个月房租之和
	 * 校验续签合同已成功到账的金额是否已超过水电押金差额+房租押金差额+1个月房租之和
	 * */
	private boolean checkRentContractTransAmountEnough(RentContract rentContract) {
		// 计算合同成功到账的总金额
		TradingAccounts ta = new TradingAccounts();
		ta.setTradeId(rentContract.getId());
		ta.setTradeStatus("1");// 账务交易审核通过
		if ("0".equals(rentContract.getSignType())) {// 新签
			ta.setTradeType("3");// 账务交易类型为“新签合同”
		}
		if ("1".equals(rentContract.getSignType())) {// 正常续签
			ta.setTradeType("4");// 账务交易类型为“正常人工续签”
		}
		if ("2".equals(rentContract.getSignType())) {// 逾期续签
			ta.setTradeType("5");// 逾期自动续签
		}
		BigDecimal totalAmount = BigDecimal.ZERO;// 合同已经被审核通过的总已到账款项
		List<TradingAccounts> tradingAccounts = tradingAccountsDao.findList(ta);
		if (CollectionUtils.isNotEmpty(tradingAccounts)) {
			for (TradingAccounts tempTA : tradingAccounts) {
				if ("1".equals(tempTA.getTradeDirection())) {// 入账
					if (tempTA.getTradeAmount() != null && tempTA.getTradeAmount() > 0) {
						totalAmount = totalAmount.add(new BigDecimal(tempTA.getTradeAmount()));
					}
				}
			}
		}
		// 计算合同至少需到账金额
		BigDecimal needBeAmount = BigDecimal.ZERO;
		// 新签,至少需要到账金额满足水电押金+房租押金+1个月房租
		// 正常续签时，到账金额至少满足水电押金差额+房租押金差额+1个月房租
		if ("0".equals(rentContract.getSignType()) || "1".equals(rentContract.getSignType())) {
			if (DateUtils.getMonthSpace(rentContract.getStartDate(), rentContract.getExpiredDate()) < 1) {// 如果合同期不足一个月
				needBeAmount = new BigDecimal(rentContract.getDepositElectricAmount() + rentContract.getDepositAmount());
			} else {// 如果合同期超过一个月
				needBeAmount = new BigDecimal(rentContract.getDepositElectricAmount() + rentContract.getDepositAmount()
						+ rentContract.getRental());
			}
		}
		if ("2".equals(rentContract.getSignType())) {// 逾期续签,暂不做限制
		}

		if (totalAmount.compareTo(needBeAmount) >= 0) {
			return true;
		} else {
			return false;
		}
	}
}