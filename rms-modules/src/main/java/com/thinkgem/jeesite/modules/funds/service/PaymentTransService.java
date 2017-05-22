package com.thinkgem.jeesite.modules.funds.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thinkgem.jeesite.common.persistence.Page;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.service.AttachmentService;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;

/**
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class PaymentTransService extends CrudService<PaymentTransDao, PaymentTrans> {
  @Autowired
  private ElectricFeeDao electricFeeDao;
  @Autowired
  private TradingAccountsDao tradingAccountsDao;
  @Autowired
  private PaymentTradeDao paymentTradeDao;
  @Autowired
  private ReceiptService receiptService;
  @Autowired
  private AttachmentService attachmentService;

  @Override
  public List<PaymentTrans> findList(PaymentTrans entity) {
    areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
    return super.findList(entity);
  }

  @Override
  public Page<PaymentTrans> findPage(Page<PaymentTrans> page, PaymentTrans entity) {
    areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
    return super.findPage(page, entity);
  }

  @Transactional(readOnly = false)
  public String generateAndSavePaymentTrans(String tradeType, String paymentType, String transId, String tradeDirection, Double tradeAmount, Double lastAmount, Double transAmount, String transStatus,
      Date startDate, Date expiredDate, String postpaidFeeId) {
    String id = "";
    if (null != tradeAmount && tradeAmount > 0) {
      PaymentTrans paymentTrans = new PaymentTrans();
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
      paymentTrans.setPostpaidFeeId(postpaidFeeId);
      id = super.saveAndReturnId(paymentTrans);
    }
    return id;
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
    // 删除款项账务的关联关系、收据、附件
    TradingAccounts tradingAccounts = new TradingAccounts();
    tradingAccounts.setTradeId(objectID);
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
        receiptService.delete(receipt);
        Attachment attachment = new Attachment();
        attachment.setTradingAccountsId(tmpTradingAccounts.getId());
        attachmentService.delete(attachment);
      }
    }
    // 删除账务交易记录
    tradingAccounts.preUpdate();
    tradingAccountsDao.delete(tradingAccounts);
    // 如果合同有同步的电费充值金额，需要把同时生成的电费充值记录删除
    ElectricFee ele = new ElectricFee();
    ele.preUpdate();
    ele.setRentContractId(objectID);
    electricFeeDao.delete(ele);
  }

  /**
   * 退租，删除未到账的款项，包括： 交易类型【新签合同、正常人工续签、逾期自动续签】；款项类型【房租金额、水费金额、燃气金额、有线电视费、宽带费、服务费】。
   */
  @Transactional(readOnly = false)
  public void deleteNotSignPaymentTrans(String rentContractId) {
    super.delete(prepareNotSignPaymentCondition(rentContractId));
  }

  /**
   * 退租删除未到账的款项之回滚，包括： 交易类型【新签合同、正常人工续签、逾期自动续签】；款项类型【房租金额、水费金额、燃气金额、有线电视费、宽带费、服务费】。
   */
  @Transactional(readOnly = false)
  public void rollbackDeleteNotSignPaymentTrans(String rentContractId) {
    dao.rollbackDelete(prepareNotSignPaymentCondition(rentContractId));
  }

  private PaymentTrans prepareNotSignPaymentCondition(String rentContractId) {
    PaymentTrans p = new PaymentTrans();
    p.preUpdate();
    p.setTransId(rentContractId);
    p.setTradeDirection(TradeDirectionEnum.IN.getValue());
    p.setTransStatus(PaymentTransStatusEnum.NO_SIGN.getValue());
    List<String> paymentTypeList = new ArrayList<String>();
    paymentTypeList.add(PaymentTransTypeEnum.RENT_AMOUNT.getValue());
    paymentTypeList.add(PaymentTransTypeEnum.WATER_AMOUNT.getValue());
    paymentTypeList.add(PaymentTransTypeEnum.GAS_AMOUNT.getValue());
    paymentTypeList.add(PaymentTransTypeEnum.TV_AMOUNT.getValue());
    paymentTypeList.add(PaymentTransTypeEnum.NET_AMOUNT.getValue());
    paymentTypeList.add(PaymentTransTypeEnum.SERVICE_AMOUNT.getValue());
    p.setPaymentTypeList(paymentTypeList);
    List<String> tradeTypeList = new ArrayList<String>();
    tradeTypeList.add(TradeTypeEnum.SIGN_NEW_CONTRACT.getValue());
    tradeTypeList.add(TradeTypeEnum.NORMAL_RENEW.getValue());
    tradeTypeList.add(TradeTypeEnum.OVERDUE_AUTO_RENEW.getValue());
    p.setTradeTypeList(tradeTypeList);
    return p;
  }

  /**
   * 检查退租时，合同下是否还有未到账的款项，有返回true，无返回false
   */
  public boolean checkNotSignedPaymentTrans(String rentContractId) {
    boolean paymentTransFlag = false;
    PaymentTrans paymentTrans = new PaymentTrans();
    paymentTrans.setTransId(rentContractId);
    List<PaymentTrans> paymentTransList = super.findList(paymentTrans);
    if (CollectionUtils.isNotEmpty(paymentTransList)) {
      for (PaymentTrans tmpPaymentTrans : paymentTransList) {
        if (!PaymentTransStatusEnum.WHOLE_SIGN.getValue().equals(tmpPaymentTrans.getTransStatus())) {
          paymentTransFlag = true;
          break;
        }
      }
    }
    return paymentTransFlag;
  }

  /**
   * 删除后付费交易对应的所有的款项，账务，款项账务关联关系，以及相关收据 前提： 1、到账登记时，不能跟别的交易类型一起混合到账登记，必须只能是后付费这一种账务交易类型。 2、到账登记的款项必须是 同一笔后付费交易，不能跨两笔后付费交易。
   */
  @Transactional(readOnly = false)
  public void deletePaymentTransAndTradingAcctounsWithPostpaidFee(String postpaidFeeId) {
    // 根据后付费交易ID查询账务交易集合
    Set<String> tradingAccountsIdSet = new HashSet<String>();
    PaymentTrans delPaymentTrans = new PaymentTrans();
    delPaymentTrans.setPostpaidFeeId(postpaidFeeId);
    List<PaymentTrans> transList = super.findList(delPaymentTrans);
    if (CollectionUtils.isNotEmpty(transList)) {
      for (PaymentTrans tempTrans : transList) {
        PaymentTrade pt = new PaymentTrade();
        pt.setTransId(tempTrans.getId());
        List<PaymentTrade> paymentTrades = paymentTradeDao.findList(pt);
        if (CollectionUtils.isNotEmpty(paymentTrades)) {
          tradingAccountsIdSet.add(paymentTrades.get(0).getTradeId());
        }
      }
    }
    // 删除款项记录
    delPaymentTrans.preUpdate();
    super.delete(delPaymentTrans);
    // 删除款项账务的关联关系、收据、附件
    if (CollectionUtils.isNotEmpty(tradingAccountsIdSet)) {
      for (String tradingAccountsId : tradingAccountsIdSet) {
        TradingAccounts tradingAccount = tradingAccountsDao.get(tradingAccountsId);
        if (tradingAccount != null) {
          PaymentTrade pt = new PaymentTrade();
          pt.setTradeId(tradingAccountsId);
          pt.preUpdate();
          paymentTradeDao.delete(pt);
          Receipt receipt = new Receipt();
          receipt.setTradingAccounts(tradingAccount);
          receipt.preUpdate();
          receiptService.delete(receipt);
          Attachment attachment = new Attachment();
          attachment.setTradingAccountsId(tradingAccountsId);
          attachmentService.delete(attachment);
          // 删除账务交易记录
          tradingAccount.preUpdate();
          tradingAccountsDao.delete(tradingAccount);
        }
      }
    }
  }

  /**
   * 删除电费充值对应的所有的款项，账务，款项账务关联关系，以及相关收据 ，用于电费充值记录信息的修改
   */
  @Transactional(readOnly = false)
  public void deletePaymentTransAndTradingAcctounsWithChargeFee(String electricFeeId) {
    ElectricFee electricFee = electricFeeDao.get(electricFeeId);
    if (electricFee != null) {
      String paymentTransId = electricFee.getPaymentTransId();
      PaymentTrans pt = new PaymentTrans();
      pt.setId(paymentTransId);
      super.delete(pt);
      PaymentTrade ptd = new PaymentTrade();
      ptd.setTransId(paymentTransId);
      List<PaymentTrade> ptds = paymentTradeDao.findList(ptd);
      if (CollectionUtils.isNotEmpty(ptds)) {
        TradingAccounts tradingAccount = tradingAccountsDao.get(ptds.get(0).getTradeId());
        if (tradingAccount != null) {
          Receipt receipt = new Receipt();
          receipt.setTradingAccounts(tradingAccount);
          receipt.preUpdate();
          receiptService.delete(receipt);
          Attachment attachment = new Attachment();
          attachment.setTradingAccountsId(tradingAccount.getId());
          attachmentService.delete(attachment);
          tradingAccount.preUpdate();
          tradingAccountsDao.delete(tradingAccount);
        }
      }
      ptd.preUpdate();
      paymentTradeDao.delete(ptd);
    }
  }

  /**
   * 获取某出租合同下， 所有指定款项类型并且指定款项状态的款项记录
   */
  public List<PaymentTrans> getPaymentTransByTypeAndStatus(String paymentTransType, String rentContractId, String paymentTransStatus) {
    PaymentTrans pt = new PaymentTrans();
    pt.setPaymentType(paymentTransType);
    pt.setTransId(rentContractId);
    pt.setTransStatus(paymentTransStatus);
    return super.findList(pt);
  }

  /**
   * 删除合同下指定的账务交易类型集合的款项
   */
  @Transactional(readOnly = false)
  public void deleteRentContractTradeTypeList(String rentContractId, List<String> tradeTypeList) {
    PaymentTrans pts = new PaymentTrans();
    pts.preUpdate();
    pts.setTransId(rentContractId);
    pts.setTradeTypeList(tradeTypeList);
    super.delete(pts);
  }
}
