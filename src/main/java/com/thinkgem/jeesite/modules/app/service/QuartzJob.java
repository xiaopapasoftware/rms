package com.thinkgem.jeesite.modules.app.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.funds.dao.PaymentOrderDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentOrder;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;

@Service
@Lazy(false)
public class QuartzJob {
	Logger log = LoggerFactory.getLogger(QuartzJob.class);
	
	@Autowired
	private PaymentOrderDao paymentOrderDao;
	@Autowired
	private TradingAccountsService tradingAccountsService;
	@Autowired
	private DepositAgreementService depositAgreementService;
	@Autowired
	private RentContractService rentContractService;
	@Autowired
	private TradingAccountsDao tradingAccountsDao;
	@Autowired
	private PaymentTransDao paymentTransDao;
	@Autowired
	private PaymentTradeDao paymentTradeDao;
	
	@Scheduled(cron="0 */1 * * * ?")
	public void scanPaymentOrder() {
		log.info("------扫描未支付超时订单开始------");
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setOrderStatus("1");//未支付
		List<PaymentOrder> list = paymentOrderDao.findList(paymentOrder);
		Date dateNow = new Date();
		PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
		String orderTimeout = proper.getProperty("order.timeout");
		for(PaymentOrder tmpPaymentOrder : list) {
			if((dateNow.getTime()-tmpPaymentOrder.getOrderDate().getTime())/1000/60>Long.valueOf(orderTimeout)) {
				paymentOrderDao.delete(tmpPaymentOrder);
				
				String[] tradeIds = tmpPaymentOrder.getTradeId().split(",");
				for(String tradeId : tradeIds) {
					//定金协议/合同删除
					if(StringUtils.isNoneBlank(tradeId)) {
						TradingAccounts tradingAccounts = tradingAccountsService.get(tradeId);
						if(null != tradingAccounts) {
							String depositAgreementId = tradingAccounts.getTradeId();
							if(StringUtils.isNoneBlank(depositAgreementId)) {
								DepositAgreement depositAgreement = depositAgreementService.get(depositAgreementId);
								if(null != depositAgreement) {
									//depositAgreementService.delete(depositAgreement);
									AuditHis auditHis = new AuditHis();
									auditHis.setObjectId(depositAgreementId);
									auditHis.setAuditStatus("2");
									depositAgreementService.audit(auditHis);
								} else {
									RentContract rentContract = this.rentContractService.get(depositAgreementId);
									if(null != rentContract && StringUtils.isBlank(rentContract.getContractBusiStatus())) {
										//this.rentContractService.delete(rentContract);
										AuditHis auditHis = new AuditHis();
										auditHis.setObjectId(depositAgreementId);
										auditHis.setAuditStatus("3");
										this.rentContractService.audit(auditHis);
									} else {
										tradingAccounts.setTradeStatus("2");
										tradingAccountsDao.update(tradingAccounts);
										
										PaymentTrade paymentTrade = new PaymentTrade();
										paymentTrade.setTradeId(tradeId);
										List<PaymentTrade> paymentTradeList = paymentTradeDao.findList(paymentTrade);
										
										for(PaymentTrade tmpPaymentTrade : paymentTradeList) {
											PaymentTrans paymentTrans = paymentTransDao.get(tmpPaymentTrade.getTransId());
											paymentTrans.setTransStatus("0");
											paymentTrans.setTransAmount(0d);
											paymentTrans.setLastAmount(paymentTrans.getTradeAmount());
											paymentTransDao.update(paymentTrans);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		log.info("------扫描未支付超时订单结束------");
	}
}
