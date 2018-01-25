package com.thinkgem.jeesite.task.service;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.AuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentOrderStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradingAccountsStatusEnum;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.funds.dao.PaymentOrderDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentOrder;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

//@Service
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
  private PaymentTransService paymentTransService;
  @Autowired
  private PaymentTradeDao paymentTradeDao;

  @Scheduled(cron = "0 */1 * * * ?")
  public void scanPaymentOrder() {
    log.info("------扫描未支付超时订单开始------");
    PaymentOrder paymentOrder = new PaymentOrder();
    paymentOrder.setOrderStatus(PaymentOrderStatusEnum.TOBEPAY.getValue());
    List<PaymentOrder> allNotPaidOrders = paymentOrderDao.findList(paymentOrder);// 所有未支付成功的订单
    String orderTimeout = Global.getInstance().getConfig("order.timeout");
    for (PaymentOrder tmpPaymentOrder : allNotPaidOrders) {
      if ((new Date().getTime() - tmpPaymentOrder.getOrderDate().getTime()) / 1000 / 60 > Long.valueOf(orderTimeout)) {
        tmpPaymentOrder.preUpdate();
        paymentOrderDao.delete(tmpPaymentOrder);
        String[] tradeIds = tmpPaymentOrder.getTradeId().split(",");
        for (String tradeId : tradeIds) {// 定金协议/合同删除
          if (StringUtils.isNotBlank(tradeId)) {
            TradingAccounts tradingAccounts = tradingAccountsService.get(tradeId);
            if (null != tradingAccounts) {
              String objectId = tradingAccounts.getTradeId();// 账务交易对象
              if (StringUtils.isNotBlank(objectId)) {
                DepositAgreement depositAgreement = depositAgreementService.get(objectId);
                if (null != depositAgreement) {
                  AuditHis auditHis = new AuditHis();
                  auditHis.setObjectId(objectId);
                  auditHis.setAuditStatus(AuditStatusEnum.REFUSE.getValue());
                  depositAgreementService.audit(auditHis);
                } else {
                  RentContract rentContract = rentContractService.get(objectId);
                  if (null != rentContract && StringUtils.isBlank(rentContract.getContractBusiStatus())) {
                    AuditHis auditHis = new AuditHis();
                    auditHis.setObjectId(objectId);
                    auditHis.setAuditStatus(AuditStatusEnum.REFUSE.getValue());
                    rentContractService.audit(auditHis);
                  } else {
                    tradingAccounts.setTradeStatus(TradingAccountsStatusEnum.AUDIT_REFUSE.getValue());
                    tradingAccountsDao.update(tradingAccounts);
                    PaymentTrade paymentTrade = new PaymentTrade();
                    paymentTrade.setTradeId(tradeId);
                    List<PaymentTrade> paymentTradeList = paymentTradeDao.findList(paymentTrade);
                    for (PaymentTrade tmpPaymentTrade : paymentTradeList) {
                      PaymentTrans paymentTrans = paymentTransService.get(tmpPaymentTrade.getTransId());
                      paymentTrans.setTransStatus(PaymentTransStatusEnum.NO_SIGN.getValue());
                      paymentTrans.setTransAmount(0d);
                      paymentTrans.setLastAmount(paymentTrans.getTradeAmount());
                      paymentTransService.save(paymentTrans);
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
