/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.AgreementAuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.AgreementBusiStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.AuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.AuditTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractAuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractBusiStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractSignTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradingAccountsStatusEnum;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.dao.ReceiptDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 账务交易Service
 * 
 * @author huangsc
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class TradingAccountsService extends CrudService<TradingAccountsDao, TradingAccounts> {

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
  private PaymentTransDao paymentTransDao;
  @Autowired
  private ElectricFeeDao electricFeeDao;
  @Autowired
  private AttachmentDao attachmentDao;
  @Autowired
  private RoomDao roomDao;

  private static final String TRADING_ACCOUNTS_ROLE = "trading_accounts_role";// 账务审批

  public List<Receipt> findReceiptList(TradingAccounts tradingAccounts) {
    Receipt receipt = new Receipt();
    receipt.setTradingAccounts(tradingAccounts);
    return receiptDao.findList(receipt);
  }

  @Transactional(readOnly = false)
  public void remoke(String id) {/* 退回已到账的款项、删除收据 */
    TradingAccounts tradingAccounts = tradingAccountsDao.get(id);
    PaymentTrade paymentTrade = new PaymentTrade();
    paymentTrade.setTradeId(tradingAccounts.getId());
    List<PaymentTrade> listPaymentTrade = paymentTradeDao.findList(paymentTrade);
    for (PaymentTrade tmpPaymentTrade : listPaymentTrade) {
      PaymentTrans paymentTrans = new PaymentTrans();
      paymentTrans.setId(tmpPaymentTrade.getTransId());
      paymentTrans = paymentTransDao.get(paymentTrans);
      Double transedDepositAmount = paymentTrans.getTransferDepositAmount();
      if (transedDepositAmount != null && transedDepositAmount > 0) {// 定金转过来的部分,特殊处理
        paymentTrans.setTransAmount(transedDepositAmount);
        if (transedDepositAmount < paymentTrans.getTradeAmount()) {
          paymentTrans.setTransStatus(PaymentTransStatusEnum.PART_SIGN.getValue());
        } else {
          paymentTrans.setTransStatus(PaymentTransStatusEnum.WHOLE_SIGN.getValue());
        }
        paymentTrans.setLastAmount(paymentTrans.getTradeAmount() - transedDepositAmount);
      } else {
        paymentTrans.setTransAmount(0D);
        paymentTrans.setTransStatus(PaymentTransStatusEnum.NO_SIGN.getValue());
        paymentTrans.setLastAmount(paymentTrans.getTradeAmount());
      }
      paymentTrans.preUpdate();
      paymentTransDao.update(paymentTrans);
    }
    Receipt receipt = new Receipt();
    receipt.setTradingAccounts(tradingAccounts);
    receipt.preUpdate();
    this.receiptDao.delete(receipt);
    tradingAccounts.preUpdate();
    tradingAccountsDao.delete(tradingAccounts);
  }

  @Transactional(readOnly = false)
  public void audit(AuditHis auditHis) {
    AuditHis saveAuditHis = new AuditHis();
    saveAuditHis.preInsert();
    saveAuditHis.setObjectType(AuditTypeEnum.TRADING_ACCOUNT.getValue());
    saveAuditHis.setObjectId(auditHis.getObjectId());
    saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
    saveAuditHis.setAuditStatus(auditHis.getAuditStatus());// 1:通过 2:拒绝
    saveAuditHis.setAuditTime(new Date());
    saveAuditHis.setAuditUser(UserUtils.getUser().getId());
    auditHisDao.insert(saveAuditHis);

    TradingAccounts tradingAccounts = tradingAccountsDao.get(auditHis.getObjectId());
    tradingAccounts.preUpdate();
    tradingAccounts.setTradeStatus(auditHis.getAuditStatus());
    tradingAccountsDao.update(tradingAccounts);

    if ("1".equals(auditHis.getAuditStatus())) {// 审核通过
      // 款项做到账
      PaymentTrade paymentTrade = new PaymentTrade();
      paymentTrade.setTradeId(tradingAccounts.getId());
      List<PaymentTrade> paymentTradeList = paymentTradeDao.findList(paymentTrade);
      if (null != paymentTradeList && paymentTradeList.size() > 0) {
        for (PaymentTrade tmpPaymentTradeList : paymentTradeList) {
          String transId = tmpPaymentTradeList.getTransId();
          PaymentTrans paymentTrans = paymentTransDao.get(transId);
          paymentTrans.setTransStatus(PaymentTransStatusEnum.WHOLE_SIGN.getValue());
          paymentTrans.setLastAmount(0d);// 剩余交易金额
          paymentTrans.setTransAmount(paymentTrans.getTradeAmount());// 实际交易金额
          paymentTrans.preUpdate();
          paymentTransDao.update(paymentTrans);
        }
      }
    } else {
      PaymentTrade paymentTrade = new PaymentTrade();
      paymentTrade.setTradeId(tradingAccounts.getId());
      List<PaymentTrade> paymentTradeList = paymentTradeDao.findList(paymentTrade);
      if (null != paymentTradeList && paymentTradeList.size() > 0) {
        for (PaymentTrade tmpPaymentTradeList : paymentTradeList) {
          String transId = tmpPaymentTradeList.getTransId();
          PaymentTrans paymentTrans = paymentTransDao.get(transId);
          if (PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue().equals(paymentTrans.getPaymentType())) {
            paymentTrans.preUpdate();
            paymentTransDao.delete(paymentTrans);
          }
        }
      }
    }
    if (TradeTypeEnum.DEPOSIT_AGREEMENT.getValue().equals(tradingAccounts.getTradeType())) {
      DepositAgreement depositAgreement = depositAgreementDao.get(tradingAccounts.getTradeId());
      if (!AgreementAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(depositAgreement.getAgreementStatus())) {
        depositAgreement.preUpdate();
        depositAgreement.setAgreementStatus(
            AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus()) ? AgreementAuditStatusEnum.INVOICE_AUDITED_PASS.getValue() : AgreementAuditStatusEnum.INVOICE_AUDITED_REFUSE.getValue());
        if (AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus())) {
          depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.TOBE_CONVERTED.getValue());
        }
        depositAgreement.preUpdate();
        depositAgreementDao.update(depositAgreement);
      }
    } else if (TradeTypeEnum.DEPOSIT_TO_BREAK.getValue().equals(tradingAccounts.getTradeType())) {
      DepositAgreement depositAgreement = depositAgreementDao.get(tradingAccounts.getTradeId());
      depositAgreement.preUpdate();
      depositAgreement.setAgreementBusiStatus(
          AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus()) ? AgreementBusiStatusEnum.BE_CONVERTED_BREAK.getValue() : AgreementBusiStatusEnum.CONVERTBREAK_AUDIT_REFUSE.getValue());
      depositAgreementDao.update(depositAgreement);
    } else if (TradeTypeEnum.SIGN_NEW_CONTRACT.getValue().equals(tradingAccounts.getTradeType()) || TradeTypeEnum.NORMAL_RENEW.getValue().equals(tradingAccounts.getTradeType())
        || TradeTypeEnum.OVERDUE_AUTO_RENEW.getValue().equals(tradingAccounts.getTradeType())) {
      RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
      if (!ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(rentContract.getContractStatus())) {
        if (AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus())) {
          if (checkRentContractTransAmountEnough(rentContract)) {
            rentContract.setContractStatus(ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue());
            rentContract.setContractBusiStatus(ContractBusiStatusEnum.VALID.getValue());
            rentContract.preUpdate();
            rentContractDao.update(rentContract);
          }
        } else {
          rentContract.setContractStatus(ContractAuditStatusEnum.INVOICE_AUDITED_REFUSE.getValue());
          rentContract.preUpdate();
          rentContractDao.update(rentContract);
        }
      }
      // 如果同时有电费充值的交易类型
      PaymentTrade paymentTrade = new PaymentTrade();
      paymentTrade.setDelFlag("0");
      paymentTrade.setTradeId(tradingAccounts.getId());
      List<PaymentTrade> list = paymentTradeDao.findList(paymentTrade);
      for (PaymentTrade tmpPaymentTrade : list) { /* 更新充值记录 */
        PaymentTrans paymentTrans = paymentTransDao.get(tmpPaymentTrade.getTransId());
        if ("11".equals(paymentTrans.getPaymentType())) {// 电费自用金额类型
          ElectricFee electricFee = new ElectricFee();
          electricFee.setPaymentTransId(paymentTrans.getId());
          electricFee.setDelFlag("0");
          electricFee = electricFeeDao.get(electricFee);
          if (null != electricFee) {
            if ("1".equals(auditHis.getAuditStatus())) {// 审核通过
              if (null != rentContract && "1".equals(rentContract.getRentMode())) {// 单间
                Room room = rentContract.getRoom();
                room = roomDao.get(room);
                String meterNo = room.getMeterNo();
                DecimalFormat df = new DecimalFormat("0");
                String id = charge(meterNo, df.format(electricFee.getChargeAmount()));
                if (!StringUtils.isBlank(id)) {// 充值成功
                  Pattern pattern = Pattern.compile("[0-9]*");
                  Matcher isNum = pattern.matcher(id);
                  if (isNum.matches()) {
                    electricFee.setChargeId(id);
                    electricFee.setSettleStatus("3");// 3=审核通过；2=审核拒绝
                    electricFee.setChargeStatus("1");// 1=充值成功；2=充值失败；
                    electricFee.preUpdate();
                    electricFeeDao.update(electricFee);
                  }
                } else {// 充值失败
                  electricFee.setSettleStatus("2");// 3=审核通过；2=审核拒绝
                  electricFee.setChargeStatus("2");// 1=充值成功；2=充值失败；
                  electricFee.preUpdate();
                  electricFeeDao.update(electricFee);
                }
              }
            } else {// 审核不通过
              electricFee.setSettleStatus("2");// 3=审核通过；2=审核拒绝
              electricFee.setChargeStatus("2");// 1=充值成功；2=充值失败；
              electricFee.preUpdate();
              electricFeeDao.update(electricFee);
            }
          }
        }
      }
    } else if ("7".equals(tradingAccounts.getTradeType())) {
      // 正常退租 8:正常退租 6:退租款项审核拒绝
      RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
      rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus()) ? "8" : "6");
      rentContract.preUpdate();
      rentContractDao.update(rentContract);
    } else if ("6".equals(tradingAccounts.getTradeType())) {
      // 提前退租 7:提前退租 6:退租款项审核拒绝
      RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
      rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus()) ? "7" : "6");
      rentContract.preUpdate();
      rentContractDao.update(rentContract);
    } else if ("8".equals(tradingAccounts.getTradeType())) {
      // 逾期退租 9:逾期退租 6:退租款项审核拒绝
      RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
      rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus()) ? "9" : "6");
      rentContract.preUpdate();
      rentContractDao.update(rentContract);
    } else if ("9".equals(tradingAccounts.getTradeType())) {
      // 特殊退租 16:特殊退租 6:退租款项审核拒绝
      RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
      rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus()) ? "16" : "6");
      rentContract.preUpdate();
      rentContractDao.update(rentContract);
    } else if ("11".equals(tradingAccounts.getTradeType())) {// 电费充值
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
          if ("1".equals(auditHis.getAuditStatus())) {// 审核通过
            RentContract rentContract = rentContractDao.get(electricFee.getRentContractId());/* 智能电表充值 */
            if (null != rentContract && "1".equals(rentContract.getRentMode())) {// 单间
              Room room = rentContract.getRoom();
              room = roomDao.get(room);
              String meterNo = room.getMeterNo();
              DecimalFormat df = new DecimalFormat("0");
              String id = charge(meterNo, df.format(electricFee.getChargeAmount()));
              if (!StringUtils.isBlank(id)) {
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(id);
                if (isNum.matches()) {
                  electricFee.setChargeId(id);
                  electricFee.setSettleStatus("3");// 3=审核通过；2=审核拒绝
                  electricFee.setChargeStatus("1");// 1=充值成功；2=充值失败；
                  electricFee.preUpdate();
                  electricFeeDao.update(electricFee);
                }
              } else {// 充值失败
                electricFee.setSettleStatus("2");// 3=审核通过；2=审核拒绝
                electricFee.setChargeStatus("2");// 1=充值成功；2=充值失败；
                electricFee.preUpdate();
                electricFeeDao.update(electricFee);
              }
            }
          } else {// 审核不通过
            electricFee.setSettleStatus("2");// 3=审核通过；2=审核拒绝
            electricFee.setChargeStatus("2");// 1=充值成功；2=充值失败；
            electricFee.preUpdate();
            electricFeeDao.update(electricFee);
          }
        }
      }
    }
  }

  @Transactional(readOnly = false)
  public void save(TradingAccounts tradingAccounts) {
    String id = super.saveAndReturnId(tradingAccounts);
    boolean isChoosedEleFlag = false;// 有可能用户同时选中了电费充值和新签合同的款项，设立一个是否有选中的电费充值的款项标示。
    String elePaymentTransId = "";// 电费充值的那笔款项
    /* 更新款项状态 */
    if (StringUtils.isNotEmpty(tradingAccounts.getTransIds())) {
      String[] transIds = tradingAccounts.getTransIds().split(",");
      /* 款项账务关联 */
      PaymentTrade paymentTrade = new PaymentTrade();
      paymentTrade.setTradeId(id);
      paymentTrade.preUpdate();
      paymentTradeDao.delete(paymentTrade);
      for (int i = 0; i < transIds.length; i++) {
        PaymentTrans paymentTrans = paymentTransDao.get(transIds[i]);
        paymentTrans.setTransStatus(PaymentTransStatusEnum.WHOLE_SIGN.getValue());
        paymentTrans.setTransAmount(paymentTrans.getTradeAmount());
        paymentTrans.setLastAmount(0D);// 剩余交易金额
        paymentTrans.preUpdate();
        paymentTransDao.update(paymentTrans);
        if (PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue().equals(paymentTrans.getPaymentType())) {// 如果有电费充值的款项
          isChoosedEleFlag = true;
          elePaymentTransId = paymentTrans.getId();
        }
        paymentTrade.setTransId(paymentTrans.getId());
        paymentTrade.preInsert();
        paymentTradeDao.insert(paymentTrade);
      }
    }
    String tradeId = tradingAccounts.getTradeId();
    String tradeType = tradingAccounts.getTradeType();
    if (TradeTypeEnum.DEPOSIT_AGREEMENT.getValue().equals(tradeType)) {
      DepositAgreement depositAgreement = depositAgreementDao.get(tradeId);
      depositAgreement.preUpdate();
      if (AgreementAuditStatusEnum.FINISHED_TO_SIGN.getValue().equals(depositAgreement.getAgreementStatus())) {
        depositAgreement.setAgreementStatus(AgreementAuditStatusEnum.SIGNED_TO_AUDIT_CONTENT.getValue());
        depositAgreementDao.update(depositAgreement);
      } else {
        if (!AgreementAuditStatusEnum.INVOICE_TO_AUDITED.getValue().equals(depositAgreement.getAgreementStatus())
            && !AgreementAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(depositAgreement.getAgreementStatus())) {
          depositAgreement.setAgreementStatus(AgreementAuditStatusEnum.INVOICE_TO_AUDITED.getValue());
          depositAgreementDao.update(depositAgreement);
        }
      }
    } else if (TradeTypeEnum.DEPOSIT_TO_BREAK.getValue().equals(tradeType)) {
      DepositAgreement depositAgreement = depositAgreementDao.get(tradeId);
      if (AgreementBusiStatusEnum.CONVERTBREAK_TO_SIGN.getValue().equals(depositAgreement.getAgreementBusiStatus())
          || AgreementBusiStatusEnum.CONVERTBREAK_AUDIT_REFUSE.getValue().equals(depositAgreement.getAgreementBusiStatus())) {
        depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.CONVERTBREAK_TO_AUDIT.getValue());
      }
      depositAgreement.preUpdate();
      depositAgreementDao.update(depositAgreement);
    } else if (TradeTypeEnum.SIGN_NEW_CONTRACT.getValue().equals(tradeType) || TradeTypeEnum.NORMAL_RENEW.getValue().equals(tradeType)
        || TradeTypeEnum.OVERDUE_AUTO_RENEW.getValue().equals(tradeType)) {
      RentContract rentContract = rentContractDao.get(tradeId);
      if (ContractAuditStatusEnum.FINISHED_TO_SIGN.getValue().equals(rentContract.getContractStatus())) {
        rentContract.setContractStatus(ContractAuditStatusEnum.SIGNED_TO_AUDIT_CONTENT.getValue());
        rentContract.preUpdate();
        rentContractDao.update(rentContract);
      } else {
        if (!ContractAuditStatusEnum.INVOICE_TO_AUDITED.getValue().equals(rentContract.getContractStatus())
            && !ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(rentContract.getContractStatus())) {
          rentContract.setContractStatus(ContractAuditStatusEnum.INVOICE_TO_AUDITED.getValue());
          rentContract.preUpdate();
          rentContractDao.update(rentContract);
        }
      }
      // 如果带有电费充值的款项
      if (isChoosedEleFlag) {
        ElectricFee fee = new ElectricFee();
        fee.setPaymentTransId(elePaymentTransId);
        ElectricFee upFee = electricFeeDao.get(fee);
        upFee.setChargeStatus("0");// 0=充值中
        upFee.setSettleStatus("1");// '1'='结算待审核'
        upFee.preUpdate();
        electricFeeDao.update(upFee);
      }
    } else if (TradeTypeEnum.NORMAL_RETURN_RENT.getValue().equals(tradeType) || TradeTypeEnum.OVERDUE_RETURN_RENT.getValue().equals(tradeType)
        || TradeTypeEnum.ADVANCE_RETURN_RENT.getValue().equals(tradeType)) {
      RentContract rentContract = rentContractDao.get(tradeId);
      if (ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(rentContract.getContractStatus())) {
        rentContract.setContractBusiStatus(ContractBusiStatusEnum.RETURN_TRANS_TO_AUDIT.getValue());
        rentContract.preUpdate();
        rentContractDao.update(rentContract);
      }
    } else if (TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {
      RentContract rentContract = rentContractDao.get(tradeId);
      rentContract.preUpdate();
      rentContract.setContractBusiStatus(ContractBusiStatusEnum.SPECAIL_RETURN_ACCOUNT_AUDIT.getValue());
      rentContractDao.update(rentContract);
    } else if (TradeTypeEnum.ELECTRICITY_CHARGE.getValue().equals(tradeType)) {
      if (!StringUtils.isEmpty(tradingAccounts.getTransIds())) {
        String[] transIds = tradingAccounts.getTransIds().split(",");
        List<String> eleTransIds = new ArrayList<String>();// 电费充值的款项ID列表
        boolean isChoosed = false;// 除了电费充值外，是否还有其他的款项
        for (String transId : transIds) {
          PaymentTrans paymentTrans = paymentTransDao.get(transId);
          if (!"11".equals(paymentTrans.getPaymentType())) {// 含有别的款项，说明是有新签合同和电费充值混合的款项
            isChoosed = true;
          } else {
            eleTransIds.add(transId);
          }
        }
        if (isChoosed) {
          RentContract rentContract = rentContractDao.get(tradeId);
          rentContract.preUpdate();
          if ("1".equals(rentContract.getContractStatus())) {// '1':'录入完成到账收据待登记'
            rentContract.setContractStatus("2");// '2':'到账收据完成合同内容待审核'
            rentContractDao.update(rentContract);
          } else {
            if (!"4".equals(rentContract.getContractStatus()) && !"6".equals(rentContract.getContractStatus())) {// '4':'内容审核通过到账收据待审核',"6":到账收据审核通过
              rentContract.setContractStatus("4");// 内容审核通过到账收据待审核
              rentContractDao.update(rentContract);
            }
          }
        }
        for (String eleTransId : eleTransIds) {//
          ElectricFee fee = new ElectricFee();
          fee.setPaymentTransId(eleTransId);
          ElectricFee upFee = electricFeeDao.get(fee);
          upFee.setChargeStatus("0");// 0=充值中
          upFee.setSettleStatus("1");// '1'='结算待审核'
          upFee.preUpdate();
          electricFeeDao.update(upFee);
        }
      }
    }

    // 审核
    Audit audit = new Audit();
    audit.preInsert();
    audit.setObjectId(id);
    audit.setObjectType("2");// 账务
    audit.setNextRole(TRADING_ACCOUNTS_ROLE);
    auditDao.insert(audit);

    /* 收据 */
    if (CollectionUtils.isNotEmpty(tradingAccounts.getReceiptList())) {
      Receipt delReceipt = new Receipt();
      TradingAccounts delTradingAccounts = new TradingAccounts();
      delTradingAccounts.setId(id);
      delReceipt.setTradingAccounts(delTradingAccounts);
      delReceipt.preUpdate();
      receiptDao.delete(delReceipt);
      for (Receipt receipt : tradingAccounts.getReceiptList()) {
        receipt.preInsert();
        receipt.setTradingAccounts(tradingAccounts);
        receiptDao.insert(receipt);
      }
    }

    // 非新增，首先清空所有的账务交易记录的附件信息，非承租合同，承租合同批量到账登记就直接生成账务记录，不需要审核
    if (!tradingAccounts.getIsNewRecord() && !"0".equals(tradingAccounts.getTradeType())) {
      Attachment attachment = new Attachment();
      attachment.setTradingAccountsId(id);
      attachment.preUpdate();
      attachmentDao.delete(attachment);
    }

    // 出租合同收据附件
    if (!StringUtils.isBlank(tradingAccounts.getRentContractReceiptFile())) {
      Attachment attachment = new Attachment();
      attachment.preInsert();
      attachment.setTradingAccountsId(id);
      attachment.setAttachmentType(FileType.RENTCONTRACTRECEIPT_FILE.getValue());
      attachment.setAttachmentPath(tradingAccounts.getRentContractReceiptFile());
      attachmentDao.insert(attachment);
    }

    // 定金协议收据附件
    if (!StringUtils.isBlank(tradingAccounts.getDepositReceiptFile())) {
      Attachment attachment = new Attachment();
      attachment.preInsert();
      attachment.setTradingAccountsId(id);
      attachment.setAttachmentType(FileType.DEPOSITRECEIPT_FILE.getValue());
      attachment.setAttachmentPath(tradingAccounts.getDepositReceiptFile());
      attachmentDao.insert(attachment);
    }
  }

  /**
   * 校验新签合同（直接新签、定金转合同）已经成功到账的金额是否超过合同的水电押金+房租押金+1个月房租之和 校验续签合同已成功到账的金额是否已超过水电押金差额+房租押金差额+1个月房租之和
   */
  private boolean checkRentContractTransAmountEnough(RentContract rentContract) {
    // 计算合同成功到账的总金额
    TradingAccounts ta = new TradingAccounts();
    ta.setTradeId(rentContract.getId());
    ta.setTradeStatus(TradingAccountsStatusEnum.AUDIT_PASS.getValue());
    if (ContractSignTypeEnum.NEW_SIGN.getValue().equals(rentContract.getSignType())) {
      ta.setTradeType(TradeTypeEnum.SIGN_NEW_CONTRACT.getValue());
    }
    if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(rentContract.getSignType())) {
      ta.setTradeType(TradeTypeEnum.NORMAL_RENEW.getValue());
    }
    if (ContractSignTypeEnum.LATE_RENEW_SIGN.getValue().equals(rentContract.getSignType())) {
      ta.setTradeType(TradeTypeEnum.OVERDUE_AUTO_RENEW.getValue());
    }
    BigDecimal totalAmount = BigDecimal.ZERO;// 合同已经被审核通过的总已到账款项
    List<TradingAccounts> tradingAccounts = tradingAccountsDao.findList(ta);
    if (tradingAccounts == null) {
      tradingAccounts = new ArrayList<TradingAccounts>();
    }
    // 新签还需要考虑一种情况就是：这笔合同如果由定金协议转的，则还需要加上定金的金额
    if (ContractSignTypeEnum.NEW_SIGN.getValue().equals(rentContract.getSignType())) {
      if (StringUtils.isNotEmpty(rentContract.getAgreementId())) {
        TradingAccounts ta2 = new TradingAccounts();
        ta2.setTradeId(rentContract.getAgreementId());
        ta2.setTradeStatus(TradingAccountsStatusEnum.AUDIT_PASS.getValue());
        ta2.setTradeType(TradeTypeEnum.DEPOSIT_AGREEMENT.getValue());
        List<TradingAccounts> tradingAccounts2 = tradingAccountsDao.findList(ta2);
        if (tradingAccounts2 == null) {
          tradingAccounts2 = new ArrayList<TradingAccounts>();
        }
        tradingAccounts.addAll(tradingAccounts2);
      }
    }

    if (CollectionUtils.isNotEmpty(tradingAccounts)) {
      for (TradingAccounts tempTA : tradingAccounts) {
        if (TradeDirectionEnum.IN.getValue().equals(tempTA.getTradeDirection())) {
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
        needBeAmount = new BigDecimal(rentContract.getDepositElectricAmount() + rentContract.getDepositAmount() + rentContract.getRental());
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

  /**
   * 电表充值
   * 
   * @param meterNo 电表表号
   * @param value 充值金额
   */
  public String charge(String meterNo, String value) {
    PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
    String meterurl = proper.getProperty("meter.url") + "pay.action?addr=" + meterNo + "&pay_value=" + value;
    String result = "";
    BufferedReader read = null;
    try {
      URLConnection connection = new URL(meterurl).openConnection();
      connection.connect();
      read = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
      String line;
      while ((line = read.readLine()) != null) {
        result += line;
      }
      logger.info("call meter charge result:" + result);
    } catch (Exception e) {
      this.logger.error("call meter charge error:", e);// 捕获到异常，充值失败，则更新充值记录状态为
    } finally {
      try {
        if (null != read) read.close();
      } catch (IOException e) {
        logger.error("close io error:", e);
      }
    }
    return result;
  }
}
