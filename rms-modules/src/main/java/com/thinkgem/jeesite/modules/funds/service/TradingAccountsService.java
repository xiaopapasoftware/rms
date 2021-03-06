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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thinkgem.jeesite.common.config.Global;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
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
import com.thinkgem.jeesite.modules.contract.enums.ElectricChargeStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.FeeSettlementStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.contract.enums.PaymentOrderStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.PublicFeePayStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradingAccountsStatusEnum;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.fee.entity.PostpaidFee;
import com.thinkgem.jeesite.modules.fee.service.PostpaidFeeService;
import com.thinkgem.jeesite.modules.funds.dao.PaymentOrderDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.dao.ReceiptDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentOrder;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.utils.UserUtils;

/**
 * 账务交易Service
 *
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
    private PaymentOrderDao paymentOrderDao;
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
    @Autowired
    private PostpaidFeeService postpaidFeeService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private RoomService roomService;

    private static final String TRADING_ACCOUNTS_ROLE = "trading_accounts_role";// 账务审批

    @Override
    public List<TradingAccounts> findList(TradingAccounts entity) {
        areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
        return super.findList(entity);
    }

    @Override
    public Page<TradingAccounts> findPage(Page<TradingAccounts> page, TradingAccounts entity) {
        areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
        return super.findPage(page, entity);
    }

    public List<Receipt> findReceiptList(TradingAccounts tradingAccounts) {
        Receipt receipt = new Receipt();
        receipt.setTradingAccounts(tradingAccounts);
        areaScopeFilter(receipt, "dsf", "tp.area_id=sua.area_id");
        return receiptDao.findList(receipt);
    }

    /**
     * 退回已到账的款项、删除收据
     */
    @Transactional(readOnly = false)
    public void remoke(String id) {
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
            tmpPaymentTrade.preUpdate();
            paymentTradeDao.delete(tmpPaymentTrade);
        }
        Receipt receipt = new Receipt();
        receipt.setTradingAccounts(tradingAccounts);
        receipt.preUpdate();
        receiptDao.delete(receipt);
        tradingAccounts.preUpdate();
        tradingAccountsDao.delete(tradingAccounts);
    }

    @Transactional(readOnly = false)
    public void audit(AuditHis auditHis) {
        String auditStatus = auditHis.getAuditStatus();
        AuditHis saveAuditHis = new AuditHis();
        saveAuditHis.preInsert();
        saveAuditHis.setObjectType(AuditTypeEnum.TRADING_ACCOUNT.getValue());
        saveAuditHis.setObjectId(auditHis.getObjectId());
        saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
        saveAuditHis.setAuditStatus(auditStatus);
        saveAuditHis.setAuditTime(new Date());
        saveAuditHis.setAuditUser(UserUtils.getUser().getId());
        auditHisDao.insert(saveAuditHis);

        TradingAccounts tradingAccounts = tradingAccountsDao.get(auditHis.getObjectId());
        tradingAccounts.preUpdate();
        tradingAccounts.setTradeStatus(auditStatus);
        tradingAccountsDao.update(tradingAccounts);

        RentContract rentContract = rentContractDao.get(tradingAccounts.getTradeId());
        if (rentContract != null) {
            if (DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource())) { // 手机APP端处理
                if (AuditStatusEnum.PASS.getValue().equals(auditStatus)) {
                    PaymentTrade paymentTrade = new PaymentTrade();
                    paymentTrade.setTradeId(tradingAccounts.getId());
                    List<PaymentTrade> paymentTradeList = paymentTradeDao.findList(paymentTrade);
                    if (CollectionUtils.isNotEmpty(paymentTradeList)) {
                        for (PaymentTrade tmpPaymentTradeList : paymentTradeList) {
                            PaymentTrans paymentTrans = paymentTransDao.get(tmpPaymentTradeList.getTransId());
                            paymentTrans.setTransStatus(PaymentTransStatusEnum.WHOLE_SIGN.getValue());
                            paymentTrans.setLastAmount(0d);
                            paymentTrans.setTransAmount(paymentTrans.getTradeAmount());
                            paymentTrans.preUpdate();
                            paymentTransDao.update(paymentTrans);
                        }
                    }
                } else {// APP移动端合同特殊处理
                    PaymentTrade paymentTrade = new PaymentTrade();
                    paymentTrade.setTradeId(tradingAccounts.getId());
                    List<PaymentTrade> paymentTradeList = paymentTradeDao.findList(paymentTrade);
                    if (CollectionUtils.isNotEmpty(paymentTradeList)) {
                        for (PaymentTrade tmpPaymentTradeList : paymentTradeList) {
                            PaymentTrans paymentTrans = paymentTransDao.get(tmpPaymentTradeList.getTransId());
                            if (PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue().equals(paymentTrans.getPaymentType())) {
                                paymentTrans.preUpdate();
                                paymentTransDao.delete(paymentTrans);
                            }
                        }
                    }
                }
            }
        }
        String tradeType = tradingAccounts.getTradeType();
        if (TradeTypeEnum.DEPOSIT_AGREEMENT.getValue().equals(tradeType)) {
            DepositAgreement depositAgreement = depositAgreementDao.get(tradingAccounts.getTradeId());
            if (!AgreementAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(depositAgreement.getAgreementStatus())) {
                depositAgreement.preUpdate();
                depositAgreement.setAgreementStatus(
                        AuditStatusEnum.PASS.getValue().equals(auditStatus) ? AgreementAuditStatusEnum.INVOICE_AUDITED_PASS.getValue() : AgreementAuditStatusEnum.INVOICE_AUDITED_REFUSE.getValue());
                if (AuditStatusEnum.PASS.getValue().equals(auditStatus)) {
                    depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.TOBE_CONVERTED.getValue());
                }
                depositAgreement.preUpdate();
                depositAgreementDao.update(depositAgreement);
            }
            // 如果是APP端生成的定金，但是从线下管理系统审核付款的，需要把订单状态也同步更新为已付款
            if (DataSourceEnum.FRONT_APP.getValue().equals(depositAgreement.getDataSource()) && AuditStatusEnum.PASS.getValue().equals(auditStatus)) {
                doProcessWithPaymentOrder(auditHis.getObjectId());
            }
        } else if (TradeTypeEnum.DEPOSIT_TO_BREAK.getValue().equals(tradeType)) {
            DepositAgreement depositAgreement = depositAgreementDao.get(tradingAccounts.getTradeId());
            depositAgreement.preUpdate();
            if (AuditStatusEnum.PASS.getValue().equals(auditStatus)) {
                depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.BE_CONVERTED_BREAK.getValue());
                houseService.cancelDepositHouseAndRoomDepositState(depositAgreement.getRentMode(), depositAgreement.getHouse().getId(),
                        Optional.ofNullable(depositAgreement.getRoom()).map(Room::getId).orElse(null));
            } else {
                depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.CONVERTBREAK_AUDIT_REFUSE.getValue());
            }
            depositAgreementDao.update(depositAgreement);
        } else if (TradeTypeEnum.SIGN_NEW_CONTRACT.getValue().equals(tradeType) || TradeTypeEnum.NORMAL_RENEW.getValue().equals(tradeType)) {
            if (!ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(rentContract.getContractStatus())) {
                if (AuditStatusEnum.PASS.getValue().equals(auditStatus)) {
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
            // 如果是APP端生成的合同，但是从线下管理系统审核付款的，需要把订单状态也同步更新为已付款
            if (DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource()) && AuditStatusEnum.PASS.getValue().equals(auditStatus)) {
                doProcessWithPaymentOrder(auditHis.getObjectId());
            }
            // 如果同时有电费充值的交易类型
            processElectricCharge(tradeType, tradingAccounts.getId(), auditStatus, rentContract);
        } else if (TradeTypeEnum.NORMAL_RETURN_RENT.getValue().equals(tradeType)) {
            processReturnAuditBusi(auditStatus, rentContract, ContractBusiStatusEnum.NORMAL_RETURN.getValue(), ContractBusiStatusEnum.RETURN_TRANS_AUDIT_REFUSE.getValue());
        } else if (TradeTypeEnum.ADVANCE_RETURN_RENT.getValue().equals(tradeType)) {
            processReturnAuditBusi(auditStatus, rentContract, ContractBusiStatusEnum.EARLY_RETURN.getValue(), ContractBusiStatusEnum.RETURN_TRANS_AUDIT_REFUSE.getValue());
        } else if (TradeTypeEnum.OVERDUE_RETURN_RENT.getValue().equals(tradeType)) {
            processReturnAuditBusi(auditStatus, rentContract, ContractBusiStatusEnum.LATE_RETURN.getValue(), ContractBusiStatusEnum.RETURN_TRANS_AUDIT_REFUSE.getValue());
        } else if (TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {
            processReturnAuditBusi(auditStatus, rentContract, ContractBusiStatusEnum.SPECIAL_RETURN.getValue(), ContractBusiStatusEnum.RETURN_TRANS_AUDIT_REFUSE.getValue());
        } else if (TradeTypeEnum.ELECTRICITY_CHARGE.getValue().equals(tradeType)) {
            processElectricCharge(tradeType, tradingAccounts.getId(), auditStatus, rentContract);
        } else if (TradeTypeEnum.PUB_FEE_POSTPAID.getValue().equals(tradeType)) {
            PaymentTrade paymentTrade = new PaymentTrade();
            paymentTrade.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
            paymentTrade.setTradeId(tradingAccounts.getId());
            List<PaymentTrade> ptList = paymentTradeDao.findList(paymentTrade);
            for (PaymentTrade tmpPaymentTrade : ptList) {
                PaymentTrans pt = paymentTransDao.get(tmpPaymentTrade.getTransId());
                if (StringUtils.isNotEmpty(pt.getPostpaidFeeId())) {
                    PostpaidFee postpaidFee = postpaidFeeService.get(pt.getPostpaidFeeId());
                    if (AuditStatusEnum.PASS.getValue().equals(auditStatus)) {
                        postpaidFee.setPayStatus(PublicFeePayStatusEnum.AUDITED_PASS.getValue());
                    } else {
                        postpaidFee.setPayStatus(PublicFeePayStatusEnum.AUDITED_REFUSE.getValue());
                    }
                    postpaidFeeService.save(postpaidFee);
                    break;
                }
            }
        }
    }


    private void processReturnAuditBusi(String auditStatus, RentContract rentContract, String passBusiStatus, String refuseBusiStatus) {
        if (AuditStatusEnum.PASS.getValue().equals(auditStatus)) {
            rentContract.setContractBusiStatus(passBusiStatus);
            if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {
                House house = houseService.get(rentContract.getHouse().getId());
                houseService.returnWholeHouse(house);
            } else {
                Room room = roomService.get(rentContract.getRoom().getId());
                houseService.returnSingleRoom(room);
            }
        } else {
            rentContract.setContractBusiStatus(refuseBusiStatus);
        }
        rentContract.preUpdate();
        rentContractDao.update(rentContract);
    }

    /**
     * 电费充值
     */
    private void processElectricCharge(String tradeType, String tradingAccountsId, String auditStatus, RentContract rentContract) {
        PaymentTrade paymentTrade = new PaymentTrade();
        paymentTrade.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
        paymentTrade.setTradeId(tradingAccountsId);
        List<PaymentTrade> list = paymentTradeDao.findList(paymentTrade);
        for (PaymentTrade tmpPaymentTrade : list) {
            PaymentTrans paymentTrans = paymentTransDao.get(tmpPaymentTrade.getTransId());
            if (PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue().equals(paymentTrans.getPaymentType())) {
                List<ElectricFee> electricFees = electricFeeDao.getElectricFeeByPaymentTransId(paymentTrans.getId());
                if (CollectionUtils.isNotEmpty(electricFees)) {
                    ElectricFee electricFee = electricFees.get(0);
                    if (AuditStatusEnum.PASS.getValue().equals(auditStatus)) {
                        if (null != rentContract && RentModelTypeEnum.JOINT_RENT.getValue().equals(rentContract.getRentMode())) {
                            Room room = roomDao.get(rentContract.getRoom());
                            String id = charge(room.getMeterNo(), new DecimalFormat("0").format(electricFee.getChargeAmount()));
                            if (StringUtils.isNotBlank(id) && !"0".equals(id)) {
                                Pattern pattern = Pattern.compile("[0-9]*");
                                Matcher isNum = pattern.matcher(id);
                                if (isNum.matches()) {
                                    electricFee.setChargeId(id);
                                    electricFee.setSettleStatus(FeeSettlementStatusEnum.AUDIT_PASSED.getValue());
                                    electricFee.setChargeStatus(ElectricChargeStatusEnum.SUCCESSED.getValue());
                                    electricFee.preUpdate();
                                    electricFeeDao.update(electricFee);
                                }
                            } else {
                                electricFee.setSettleStatus(FeeSettlementStatusEnum.AUDIT_REFUSED.getValue());
                                electricFee.setChargeStatus(ElectricChargeStatusEnum.FAILED.getValue());
                                electricFee.preUpdate();
                                electricFeeDao.update(electricFee);
                            }
                        }
                    } else {
                        if (TradeTypeEnum.ELECTRICITY_CHARGE.getValue().equals(tradeType)) {
                            paymentTrans.preUpdate();
                            paymentTransDao.delete(paymentTrans);
                            tmpPaymentTrade.preUpdate();
                            paymentTradeDao.delete(tmpPaymentTrade);
                        }
                        electricFee.setSettleStatus(FeeSettlementStatusEnum.AUDIT_REFUSED.getValue());
                        electricFee.setChargeStatus(ElectricChargeStatusEnum.FAILED.getValue());
                        electricFee.preUpdate();
                        electricFeeDao.update(electricFee);
                    }
                }
            }
        }
    }

    /**
     * 审核成功后，如果是APP端的定金或合同，同时要把订单状态更新，防止审核到账后被自动取消房源
     */
    private void doProcessWithPaymentOrder(String tradeAccountsId) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setTradeId(tradeAccountsId);
        List<PaymentOrder> pos = paymentOrderDao.findList(paymentOrder);
        if (CollectionUtils.isNotEmpty(pos)) {
            for (PaymentOrder po : pos) {
                po.setOrderStatus(PaymentOrderStatusEnum.PAID.getValue());
                paymentOrderDao.update(po);
            }
        }
    }

    /**
     * 到账登记，保存功能
     */
    @Transactional(readOnly = false)
    public void save(TradingAccounts tradingAccounts) {
        String id = super.saveAndReturnId(tradingAccounts);
        boolean isChoosedEleFlag = false;// 有可能用户同时选中了电费充值和新签合同的款项，设立一个是否有选中的电费充值的款项标示。
        String elePaymentTransId = "";// 电费充值的那笔款项
        if (StringUtils.isNotEmpty(tradingAccounts.getTransIds())) {
            String[] transIds = tradingAccounts.getTransIds().split(",");
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
        } else if (TradeTypeEnum.SIGN_NEW_CONTRACT.getValue().equals(tradeType) || TradeTypeEnum.NORMAL_RENEW.getValue().equals(tradeType)) {
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
                processElectricFeeState(elePaymentTransId);
            }
        } else if (TradeTypeEnum.NORMAL_RETURN_RENT.getValue().equals(tradeType) || TradeTypeEnum.OVERDUE_RETURN_RENT.getValue().equals(tradeType)
                || TradeTypeEnum.ADVANCE_RETURN_RENT.getValue().equals(tradeType) || TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {
            RentContract rentContract = rentContractDao.get(tradeId);
            if (ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(rentContract.getContractStatus())) {
                rentContract.setContractBusiStatus(ContractBusiStatusEnum.RETURN_TRANS_TO_AUDIT.getValue());
                rentContract.preUpdate();
                rentContractDao.update(rentContract);
            }
        } else if (TradeTypeEnum.ELECTRICITY_CHARGE.getValue().equals(tradeType)) {
            if (StringUtils.isNotEmpty(tradingAccounts.getTransIds())) {
                String[] transIds = tradingAccounts.getTransIds().split(",");
                List<String> eleTransIds = new ArrayList<String>();// 电费充值的款项ID列表
                boolean isChoosed = false;// 除了电费充值外，是否还有其他的款项
                for (String transId : transIds) {
                    PaymentTrans paymentTrans = paymentTransDao.get(transId);
                    if (!PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue().equals(paymentTrans.getPaymentType())) {// 含有别的款项，说明是有新签合同和电费充值混合的款项
                        isChoosed = true;
                    } else {
                        eleTransIds.add(transId);
                    }
                }
                if (isChoosed) {
                    RentContract rentContract = rentContractDao.get(tradeId);
                    rentContract.preUpdate();
                    if (ContractAuditStatusEnum.FINISHED_TO_SIGN.getValue().equals(rentContract.getContractStatus())) {
                        rentContract.setContractStatus(ContractAuditStatusEnum.SIGNED_TO_AUDIT_CONTENT.getValue());
                        rentContractDao.update(rentContract);
                    } else {
                        if (!ContractAuditStatusEnum.INVOICE_TO_AUDITED.getValue().equals(rentContract.getContractStatus())
                                && !ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(rentContract.getContractStatus())) {//
                            rentContract.setContractStatus(ContractAuditStatusEnum.INVOICE_TO_AUDITED.getValue());
                            rentContractDao.update(rentContract);
                        }
                    }
                }
                for (String eleTransId : eleTransIds) {
                    processElectricFeeState(eleTransId);
                }
            }
        } else if (TradeTypeEnum.PUB_FEE_POSTPAID.getValue().equals(tradeType)) {
            String postpaidFeeId = null;
            if (StringUtils.isNotBlank(tradingAccounts.getTransIds())) {
                for (String transId : tradingAccounts.getTransIds().split(",")) {
                    PaymentTrans paymentTrans = paymentTransDao.get(transId);
                    if (StringUtils.isNotBlank(paymentTrans.getPostpaidFeeId())) {
                        postpaidFeeId = paymentTrans.getPostpaidFeeId();
                        break;
                    }
                }
                PostpaidFee ppf = postpaidFeeService.get(postpaidFeeId);
                if (ppf != null) {
                    ppf.preUpdate();
                    ppf.setPayStatus(PublicFeePayStatusEnum.TO_AUDIT.getValue());
                    postpaidFeeService.save(ppf);
                }
            }
        }

        // 审核
        Audit audit = new Audit();
        audit.preInsert();
        audit.setObjectId(id);
        audit.setObjectType(AuditTypeEnum.TRADING_ACCOUNT.getValue());
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
        if (!tradingAccounts.getIsNewRecord() && !TradeTypeEnum.LEASE_CONTRACT_TRADE.getValue().equals(tradingAccounts.getTradeType())) {
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

        // 电费充值收据附件
        if (!StringUtils.isBlank(tradingAccounts.getElectricChargeFile())) {
            Attachment attachment = new Attachment();
            attachment.preInsert();
            attachment.setTradingAccountsId(id);
            attachment.setAttachmentType(FileType.ELECTIRC_RECHARGE_RECEIPT_FILE.getValue());
            attachment.setAttachmentPath(tradingAccounts.getElectricChargeFile());
            attachmentDao.insert(attachment);
        }

        // 公共事业费后付收据附件
        if (!StringUtils.isBlank(tradingAccounts.getCommonPostFeeFile())) {
            Attachment attachment = new Attachment();
            attachment.preInsert();
            attachment.setTradingAccountsId(id);
            attachment.setAttachmentType(FileType.COMMON_FEE_POST_RECEIPT_FILE.getValue());
            attachment.setAttachmentPath(tradingAccounts.getCommonPostFeeFile());
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
        // 续签，到账金额至少满足水电押金差额+房租押金差额+1个月房租
        if (ContractSignTypeEnum.NEW_SIGN.getValue().equals(rentContract.getSignType()) || ContractSignTypeEnum.RENEW_SIGN.getValue().equals(rentContract.getSignType())) {
            if (DateUtils.getMonthSpace(rentContract.getStartDate(), rentContract.getExpiredDate()) < 1) {// 如果合同期不足一个月
                needBeAmount = new BigDecimal(rentContract.getDepositElectricAmount() + rentContract.getDepositAmount());
            } else {// 如果合同期超过一个月
                needBeAmount = new BigDecimal(rentContract.getDepositElectricAmount() + rentContract.getDepositAmount() + rentContract.getRental());
            }
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
     * @param value   充值金额
     */
    public String charge(String meterNo, String value) {
        String result = "";
        String meterurl = Global.getInstance().getConfig("meter.remain.url") + "pay.action?addr=" + meterNo + "&pay_value=" + value;
        logger.info("electric charge full request meterURL is:{}", meterurl);
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

    @Transactional(readOnly = true)
    public List<TradingAccounts> queryIncomeTradeAccountsByTradeId(String tradeId) {
        return tradingAccountsDao.queryIncomeTradeAccountsByTradeId(tradeId);
    }

    @Transactional(readOnly = true)
    public List<TradingAccounts> queryCostTradeAccountsByTradeId(String tradeId) {
        return tradingAccountsDao.queryCostTradeAccountsByTradeId(tradeId);
    }

    private void processElectricFeeState(String eleTransId) {
        List<ElectricFee> upFees = electricFeeDao.getElectricFeeByPaymentTransId(eleTransId);
        ElectricFee upFee = upFees.get(0);
        upFee.setChargeStatus(ElectricChargeStatusEnum.PROCESSING.getValue());
        upFee.setSettleStatus(FeeSettlementStatusEnum.NOT_AUDITED.getValue());
        upFee.preUpdate();
        electricFeeDao.update(upFee);
    }

}
