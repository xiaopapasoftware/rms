/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
import com.thinkgem.jeesite.modules.common.enums.ValidatorFlagEnum;
import com.thinkgem.jeesite.modules.common.service.AttachmentService;
import com.thinkgem.jeesite.modules.contract.dao.AgreementChangeDao;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.AccountingTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.AgreementBusiStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.AuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.AuditTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractAuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractBusiStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractSignTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.ElectricChargeStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.FeeChargeTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.FeeSettlementStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 出租合同Service
 * 
 * @author huangsc
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class RentContractService extends CrudService<RentContractDao, RentContract> {
  @Autowired
  private ContractTenantService contractTenantService;
  @Autowired
  private AuditService auditService;
  @Autowired
  private TenantService tenantService;
  @Autowired
  private AccountingService accountingService;
  @Autowired
  private AgreementChangeDao agreementChangeDao;
  @Autowired
  private HouseService houseService;
  @Autowired
  private RoomService roomService;
  @Autowired
  private DepositAgreementDao depositAgreementDao;
  @Autowired
  private DepositAgreementService depositAgreementService;
  @Autowired
  private PaymentTransService paymentTransService;
  @Autowired
  private AuditHisService auditHisService;
  @Autowired
  private AttachmentService attachmentService;
  @Autowired
  private LeaseContractService leaseContractService;
  @Autowired
  private ElectricFeeDao electricFeeDao;

  @Transactional(readOnly = false)
  public void audit(AuditHis auditHis) {
    auditHisService.saveAuditHis(auditHis, AuditTypeEnum.RENT_CONTRACT_CONTENT.getValue());
    String rentContractId = auditHis.getObjectId();
    RentContract rentContract = super.get(rentContractId);
    String actFlagFromView = auditHis.getType();// 2=特殊退租；
    if (AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus())) {
      Audit audit = new Audit();
      audit.setObjectId(rentContractId);
      audit.setNextRole("");
      auditService.update(audit);
      if ("2".equals(actFlagFromView)) {// 特殊退租审核通过后直接生成款项
        Accounting accounting = new Accounting();
        accounting.setRentContractId(rentContractId);
        accounting.setAccountingType(AccountingTypeEnum.SPECIAL_RETURN_ACCOUNT.getValue());
        List<Accounting> list = accountingService.findList(accounting);
        if (CollectionUtils.isNotEmpty(list)) {
          for (Accounting tmpAccounting : list) {
            Double feeAmt = tmpAccounting.getFeeAmount();
            if (feeAmt != null && feeAmt > 0D) {
              String feeType = tmpAccounting.getFeeType();
              Date expiredDate = null;
              if (PaymentTransTypeEnum.WATER_ELECT_DEPOSIT.getValue().equals(feeType) || PaymentTransTypeEnum.RENT_DEPOSIT.getValue().equals(feeType)) {
                expiredDate = rentContract.getExpiredDate();
              } else {
                expiredDate = tmpAccounting.getFeeDate();
              }
              paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.SPECIAL_RETURN_RENT.getValue(), feeType, rentContractId, tmpAccounting.getFeeDirection(), feeAmt, feeAmt, 0D,
                  PaymentTransStatusEnum.NO_SIGN.getValue(), rentContract.getStartDate(), expiredDate);
            }
          }
        }
        paymentTransService.deleteNotSignPaymentTrans(rentContractId);// 删除未到账款项
        rentContract.setContractBusiStatus(ContractBusiStatusEnum.SPECAIL_RETURN_ACCOUNT.getValue());
      } else {
        rentContract.setContractStatus(ContractAuditStatusEnum.INVOICE_TO_AUDITED.getValue());
      }
    } else {// 审核拒绝
      if (DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource())) {
        rentContract.setUpdateUser(auditHis.getUpdateUser());
        if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {
          House house = houseService.get(rentContract.getHouse().getId());
          houseService.cancelSign4WholeHouse(house);
        } else {// 单间
          Room room = roomService.get(rentContract.getRoom().getId());
          houseService.cancelSign4SingleRoom(room);
        }
      } else {// 管理系统后台审核拒绝往往不需要释放房屋，而是直接修改合同。
        rentContract.setUpdateUser(UserUtils.getUser().getId());
      }
      paymentTransService.deletePaymentTransAndTradingAcctouns(rentContractId);// 删除相关款项
      if ("2".equals(actFlagFromView)) {// 特殊退租审核
        rentContract.setContractBusiStatus(ContractBusiStatusEnum.SPECIAL_RENTURN_CONTENT_AUDIT_REFUSE.getValue());
      } else {
        rentContract.setContractStatus(ContractAuditStatusEnum.CONTENT_AUDIT_REFUSE.getValue());
      }
    }
    super.save(rentContract);
  }

  public List<Tenant> findTenant(RentContract rentContract) {
    List<Tenant> tenantList = new ArrayList<Tenant>();
    ContractTenant contractTenant = new ContractTenant();
    contractTenant.setLeaseContractId(rentContract.getId());
    List<ContractTenant> list = contractTenantService.findList(contractTenant);
    for (ContractTenant tmpContractTenant : list) {
      Tenant tenant = tenantService.get(tmpContractTenant.getTenantId());
      tenantList.add(tenant);
    }
    return tenantList;
  }

  public List<Tenant> findLiveTenant(RentContract rentContract) {
    List<Tenant> tenantList = new ArrayList<Tenant>();
    ContractTenant contractTenant = new ContractTenant();
    contractTenant.setContractId(rentContract.getId());
    List<ContractTenant> list = contractTenantService.findList(contractTenant);
    for (ContractTenant tmpContractTenant : list) {
      Tenant tenant = tenantService.get(tmpContractTenant.getTenantId());
      tenantList.add(tenant);
    }
    return tenantList;
  }

  public RentContract findContractByCode(String contractCode) {
    return dao.findContractByCode(contractCode);
  }

  @Transactional(readOnly = true)
  public List<RentContract> findAllValidRentContracts() {
    return dao.findAllList(new RentContract());
  }

  @Transactional(readOnly = true)
  public Integer getAllValidRentContractCounts() {
    return dao.getAllValidRentContractCounts();
  }

  /**
   * 在“退租核算”页面，点“保存”
   */
  @Transactional(readOnly = false)
  public void returnCheck(RentContract rentContract, String tradeType) {
    String returnRemark = rentContract.getReturnRemark();
    List<Accounting> accountList = rentContract.getAccountList();
    List<Accounting> outAccountList = rentContract.getOutAccountList();
    rentContract = super.get(rentContract.getId());
    if (!TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {// 特殊退租与其他退租类型不同，需要人工先进行审核
      rentContract.setContractBusiStatus(ContractBusiStatusEnum.ACCOUNT_DONE_TO_SIGN.getValue());
    } else {
      rentContract.setContractBusiStatus(ContractBusiStatusEnum.SPECIAL_RENTURN_CONTENT_AUDIT.getValue());
    }
    rentContract.setReturnRemark(returnRemark);
    super.save(rentContract);
    // 删除核算记录
    Accounting delAccounting = new Accounting();
    delAccounting.setRentContract(rentContract);
    delAccounting.preUpdate();
    if (TradeTypeEnum.NORMAL_RETURN_RENT.getValue().equals(tradeType)) {
      delAccounting.setAccountingType(AccountingTypeEnum.NORMAL_RETURN_ACCOUNT.getValue());
    } else if (TradeTypeEnum.ADVANCE_RETURN_RENT.getValue().equals(tradeType)) {
      delAccounting.setAccountingType(AccountingTypeEnum.ADVANCE_RETURN_ACCOUNT.getValue());
    } else if (TradeTypeEnum.OVERDUE_RETURN_RENT.getValue().equals(tradeType)) {
      delAccounting.setAccountingType(AccountingTypeEnum.LATE_RETURN_ACCOUNT.getValue());
    } else if (TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {
      delAccounting.setAccountingType(AccountingTypeEnum.SPECIAL_RETURN_ACCOUNT.getValue());
    }
    accountingService.delByRent(delAccounting);
    generatePaymentTransAndAccounts(accountList, rentContract, tradeType, TradeDirectionEnum.IN.getValue());
    generatePaymentTransAndAccounts(outAccountList, rentContract, tradeType, TradeDirectionEnum.OUT.getValue());
    // 变更房屋及房间的状态
    if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {// 整租
      House house = houseService.get(rentContract.getHouse().getId());
      houseService.returnWholeHouse(house);
    } else {// 单间
      Room room = roomService.get(rentContract.getRoom().getId());
      houseService.returnSingleRoom(room);
    }
  }

  @Transactional(readOnly = false)
  public void saveAdditional(AgreementChange agreementChange) {
    String id = IdGen.uuid();
    /* 合同租客关联信息 */
    ContractTenant delContractTenant = new ContractTenant();
    delContractTenant.setLeasagremChangeId(id);// 承租的 变更协议
    delContractTenant.preUpdate();
    contractTenantService.delete(delContractTenant);
    List<Tenant> list = agreementChange.getTenantList();// 承租人
    if (null != list && list.size() > 0) {
      for (Tenant tenant : list) {
        ContractTenant contractTenant = new ContractTenant();
        contractTenant.setTenantId(tenant.getId());
        contractTenant.setLeasagremChangeId(id);
        contractTenantService.save(contractTenant);
      }
    }
    ContractTenant delContractTenant2 = new ContractTenant();
    delContractTenant2.setAgreementChangeId(id);// 入住的 变更协议
    delContractTenant2.preUpdate();
    contractTenantService.delete(delContractTenant2);
    list = agreementChange.getLiveList();// 入住人
    if (null != list && list.size() > 0) {
      for (Tenant tenant : list) {
        ContractTenant contractTenant = new ContractTenant();
        contractTenant.setTenantId(tenant.getId());
        contractTenant.setAgreementChangeId(id);
        contractTenantService.save(contractTenant);
      }
    }
    agreementChange.setCreateDate(new Date());
    agreementChange.setCreateBy(UserUtils.getUser());
    agreementChange.setUpdateDate(new Date());
    agreementChange.setUpdateBy(UserUtils.getUser());
    agreementChange.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
    agreementChange.setId(id);
    agreementChange.setAgreementStatus("0");// 待审核
    agreementChange.setRentContract(super.get(agreementChange.getContractId()));
    agreementChangeDao.insert(agreementChange);
    auditService.insert(AuditTypeEnum.RENT_CONTRACT_CHANGE.getValue(), "change_agreement_role", id, "");
  }

  /**
   * 新签、正常人工续签、逾期自动续签、定金转合同，需首先根据不同请求类型锁定房源状态
   * 
   * @return 0=成功；-1=房源已出租；-2=出租合同结束日期不能晚于承租合同截止日期；
   */
  @Transactional(readOnly = false)
  public int saveContract(RentContract rentContract) {
    // 出租合同的结束时间不能超过承租合同的结束时间
    LeaseContract leaseContract = new LeaseContract();
    leaseContract.setHouse(rentContract.getHouse());
    List<LeaseContract> list = leaseContractService.findList(leaseContract);
    if (CollectionUtils.isNotEmpty(list)) {
      if (list.get(0).getExpiredDate().before(rentContract.getExpiredDate())) {
        return -2;
      }
    }
    String houseId = "";
    String roomId = "";
    if (rentContract.getHouse() != null) {
      houseId = rentContract.getHouse().getId();
    }
    if (rentContract.getRoom() != null) {
      roomId = rentContract.getRoom().getId();
    }
    if (rentContract.getIsNewRecord()) {// 新增后的保存,包括手机APP签约申请以及后台直接新增合同，需要锁定房源
      if (ValidatorFlagEnum.TEMP_SAVE.getValue().equals(rentContract.getValidatorFlag())) {
        doSaveContractBusiness(rentContract); // 后台暂存无须改变房源状态
      } else {// 保存
        return persistentHouseInfo(rentContract, houseId, roomId);
      }
    } else {// 点修改后的保存
      if (ValidatorFlagEnum.TEMP_SAVE.getValue().equals(rentContract.getValidatorFlag())) {// 点击暂存按钮
        doSaveContractBusiness(rentContract); // 后台暂存无须改变房源状态
      } else {// 点“保存”按钮
        RentContract originalRentContract = super.get(rentContract.getId());
        if (ContractAuditStatusEnum.TEMP_EXIST.getValue().equals(originalRentContract.getContractStatus())) {// 以前是暂存的合同，修改后点保存，要更新房源状态
          return persistentHouseInfo(rentContract, houseId, roomId);
        } else {
          doSaveContractBusiness(rentContract); // 修改合同，不需修改房源状态
        }
      }
    }
    return 0;
  }

  private int persistentHouseInfo(RentContract rentContract, String houseId, String roomId) {
    if (ContractSignTypeEnum.NEW_SIGN.getValue().equals(rentContract.getSignType()) || StringUtils.isEmpty(rentContract.getSignType())) {
      if (StringUtils.isNotBlank(rentContract.getAgreementId())) { // 定金转合同，把房源状态从“已预定”变更为“已出租”
        if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {// 整租
          boolean isLock = houseService.isLockWholeHouseFromDepositToContract(houseId);
          if (isLock) {
            roomService.lockRooms(houseId);
            doSaveContractBusiness(rentContract);
            return 0;
          } else {
            return -1;
          }
        } else {// 合租
          boolean isLock = roomService.isLockSingleRoomFromDepositToContract(roomId);
          if (isLock) {
            houseService.calculateHouseStatus(roomId);
            doSaveContractBusiness(rentContract);
            return 0;
          } else {
            return -1;
          }
        }
      } else {// 非定金转合同，直接新签
        if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {// 整租
          boolean isLock = houseService.isLockWholeHouse4NewSign(houseId);
          if (isLock) {
            roomService.lockRooms(houseId);
            doSaveContractBusiness(rentContract);
            return 0;
          } else {
            return -1;
          }
        } else {// 合租
          boolean isLock = roomService.isLockSingleRoom4NewSign(roomId);
          if (isLock) {
            houseService.calculateHouseStatus(roomId);
            doSaveContractBusiness(rentContract);
            return 0;
          } else {
            return -1;
          }
        }
      }
    }
    if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(rentContract.getSignType()) || ContractSignTypeEnum.LATE_RENEW_SIGN.getValue().equals(rentContract.getSignType())) {// 正常人工续签或逾期自动续签，把房源状态从“已出租”变更为“已出租”
      if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {// 整租
        boolean isLock = houseService.isLockWholeHouse4RenewSign(houseId);
        if (isLock) {
          roomService.lockRooms(houseId);
          doSaveContractBusiness(rentContract);
          return 0;
        } else {
          return -1;
        }
      } else {// 合租
        boolean isLock = roomService.isLockSingleRoom4RenewSign(roomId);
        if (isLock) {
          houseService.calculateHouseStatus(roomId);
          doSaveContractBusiness(rentContract);
          return 0;
        } else {
          return -1;
        }
      }
    }
    return 0;
  }

  /**
   * 获取某个日期的所有已经出租掉的房间数，包括合租的和整租的
   */
  public int queryValidSingleRoomCount(Date startDate, String propertyProjectId) {
    return dao.queryValidSingleRoomCount(startDate, propertyProjectId);
  }

  /**
   * 查询已出租的有效的单间合同列表
   */
  public List<RentContract> queryValidSingleRooms(Date startDate, Date endDate, String propertyProjectId) {
    return dao.queryValidSingleRooms(startDate, endDate, propertyProjectId);
  }

  /**
   * 查询指定日期所有部分出租+完全出租的房屋套数
   */
  public int queryValidEntireHouseCount(String propertyProjectId, Date startDate) {
    return dao.queryValidEntireHouseCount(propertyProjectId, startDate);
  }

  /**
   * 根据不同的房型查询有效的整租合同列表
   */
  public List<RentContract> queryValidConditionalEntireHouses(Date startDate, Date endDate, String propertyProjectId, Integer roomNum, Integer cusspacNum) {
    return dao.queryValidConditionalEntireHouses(startDate, endDate, propertyProjectId, roomNum, cusspacNum);
  }

  /**
   * 房源状态变更成功，做合同相关业务
   */
  private void doSaveContractBusiness(RentContract rentContract) {
    String id = saveAndReturnId(rentContract);
    String contractSignType = rentContract.getSignType();
    if (ValidatorFlagEnum.SAVE.getValue().equals(rentContract.getValidatorFlag())) {// 正常保存
      paymentTransService.deletePaymentTransAndTradingAcctouns(id);// 删除相关款项记录
      String tradeType = "";// 交易类型
      Date startDate = rentContract.getStartDate();
      Date expireDate = rentContract.getExpiredDate();
      if (ContractSignTypeEnum.NEW_SIGN.getValue().equals(contractSignType) || StringUtils.isEmpty(contractSignType)) {
        tradeType = TradeTypeEnum.SIGN_NEW_CONTRACT.getValue();
      } else if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(contractSignType)) {
        tradeType = TradeTypeEnum.NORMAL_RENEW.getValue();
      } else if (ContractSignTypeEnum.LATE_RENEW_SIGN.getValue().equals(contractSignType)) {
        tradeType = TradeTypeEnum.OVERDUE_AUTO_RENEW.getValue();
      }
      Double electricAmount = rentContract.getDepositElectricAmount();
      if (electricAmount != null && electricAmount > 0) {
        String paymentType = "";
        // 生成水电费押金
        if (TradeTypeEnum.SIGN_NEW_CONTRACT.getValue().equals(tradeType)) {
          paymentType = PaymentTransTypeEnum.WATER_ELECT_DEPOSIT.getValue();
        }
        // 生成水电费押金差额
        if (TradeTypeEnum.NORMAL_RENEW.getValue().equals(tradeType) || TradeTypeEnum.OVERDUE_AUTO_RENEW.getValue().equals(tradeType)) {
          paymentType = PaymentTransTypeEnum.SUPPLY_WATER_ELECT_DEPOSIT.getValue();
        }
        paymentTransService.generateAndSavePaymentTrans(tradeType, paymentType, id, TradeDirectionEnum.IN.getValue(), electricAmount, electricAmount, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(),
            startDate, expireDate);
      }
      Double rentDepositAmt = rentContract.getDepositAmount();
      if (rentDepositAmt != null && rentDepositAmt > 0) {
        String paymentType = "";
        // 生成房租押金
        if (TradeTypeEnum.SIGN_NEW_CONTRACT.getValue().equals(tradeType)) {
          paymentType = PaymentTransTypeEnum.RENT_DEPOSIT.getValue();
        }
        // 生成房租押金差额
        if (TradeTypeEnum.NORMAL_RENEW.getValue().equals(tradeType) || TradeTypeEnum.OVERDUE_AUTO_RENEW.getValue().equals(tradeType)) {
          paymentType = PaymentTransTypeEnum.SUPPLY_RENT_DEPOSIT.getValue();
        }
        paymentTransService.generateAndSavePaymentTrans(tradeType, paymentType, id, TradeDirectionEnum.IN.getValue(), rentDepositAmt, rentDepositAmt, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(),
            startDate, expireDate);
      }
      // 如果有电费充值金额，需要同时生成电费充值款项和电费充值记录
      Double eleRechargeAmount = rentContract.getEleRechargeAmount();
      if (eleRechargeAmount != null && eleRechargeAmount > 0) {
        Date nowDate = new Date();
        Date endDate = DateUtils.parseDate(DateUtils.lastDateOfCurrentMonth());
        String paymentTransId = paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue(), id, TradeDirectionEnum.IN.getValue(), eleRechargeAmount,
            eleRechargeAmount, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(), nowDate, endDate);
        ElectricFee ef = new ElectricFee();
        ef.preInsert();
        ef.setChargeAmount(eleRechargeAmount);
        ef.setChargeDate(new Date());
        ef.setChargeStatus(ElectricChargeStatusEnum.PROCESSING.getValue());
        ef.setStartDate(nowDate);
        ef.setEndDate(endDate);
        ef.setPaymentTransId(paymentTransId);
        ef.setRentContractId(id);
        ef.setSettleStatus(FeeSettlementStatusEnum.NOT_SETTLED.getValue());
        electricFeeDao.insert(ef);
      }
      int monthCountDiff = DateUtils.getMonthSpace(startDate, expireDate);// 合同日期间隔月数
      if (monthCountDiff > 0) {
        Double rentalAmt = rentContract.getRental();// 房租
        if (rentalAmt != null && rentalAmt > 0) {
          // 生成房租款项
          genContractRentalPayTrans(tradeType, id, rentContract, monthCountDiff, rentalAmt);
        }
        genContractFeesPayTrans(tradeType, id, rentContract, monthCountDiff); // 生成合同期内所有的费用款项
      }
      // 定金协议转合同，更新原定金协议状态
      if (StringUtils.isNotBlank(rentContract.getAgreementId())) {
        DepositAgreement depositAgreement = depositAgreementService.get(rentContract.getAgreementId());
        depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.BE_CONVERTED_CONTRACT.getValue());// 已转合同
        depositAgreement.preUpdate();
        depositAgreementDao.update(depositAgreement);
      }
      // 续签合同，则把原合同业务状态改成“正常人工续签”
      if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(contractSignType)) {
        RentContract rentContractOld = super.get(rentContract.getContractId());
        rentContractOld.setContractBusiStatus(ContractBusiStatusEnum.NORMAL_RENEW.getValue());
        super.save(rentContractOld);
      }
      // 逾期自动续签，则把原合同业务状态改成“逾期自动续签”
      if (ContractSignTypeEnum.LATE_RENEW_SIGN.getValue().equals(contractSignType)) {
        RentContract rentContractOld = super.get(rentContract.getContractId());
        rentContractOld.setContractBusiStatus(ContractBusiStatusEnum.LATE_AUTO_RENEW.getValue());
        super.save(rentContractOld);
      }
      auditService.delete(id);
      auditService.insert(AuditTypeEnum.RENT_CONTRACT_CONTENT.getValue(), "rent_contract_role", id, "");
    }
    // 处理承租人/入住人关系
    contractTenantService.doProcess4RentContract(rentContract);
    // 修改合同，首先清空所有的合同附件
    if (!rentContract.getIsNewRecord()) {
      Attachment attachment = new Attachment();
      attachment.setRentContractId(id);
      attachmentService.delete(attachment);
    }
    // 出租合同文件
    if (StringUtils.isNotBlank(rentContract.getRentContractFile())) {
      Attachment attachment = new Attachment();
      attachment.setRentContractId(id);
      attachment.setAttachmentType(FileType.RENTCONTRACT_FILE.getValue());
      attachment.setAttachmentPath(rentContract.getRentContractFile());
      attachmentService.save(attachment);
    }
    // 租客身份证
    if (StringUtils.isNotBlank(rentContract.getRentContractCusIDFile())) {
      Attachment attachment = new Attachment();
      attachment.setRentContractId(id);
      attachment.setAttachmentType(FileType.TENANT_ID.getValue());
      attachment.setAttachmentPath(rentContract.getRentContractCusIDFile());
      attachmentService.save(attachment);
    }
    // 出租合同其他附件
    if (StringUtils.isNotBlank(rentContract.getRentContractOtherFile())) {
      Attachment attachment = new Attachment();
      attachment.setRentContractId(id);
      attachment.setAttachmentType(FileType.RENTCONTRACT_FILE_OTHER.getValue());
      attachment.setAttachmentPath(rentContract.getRentContractOtherFile());
      attachmentService.save(attachment);
    }
  }

  /**
   * 生成合同期内的所有房租款项
   */
  private void genContractRentalPayTrans(String tradeType, String transObjId, RentContract rentContract, int monthCountDiff, Double rentalAmt) {
    Date startD = rentContract.getStartDate();// 开始日期
    boolean depositTransContractFlag = false;// 是否定金转合同
    Double depositAgreementAmount = 0d;// 定金金额
    if (StringUtils.isNotBlank(rentContract.getAgreementId())) {
      DepositAgreement depositAgreement = depositAgreementDao.get(rentContract.getAgreementId());
      depositAgreementAmount = depositAgreement.getDepositAmount();
      depositTransContractFlag = true;
    }
    // 生成房租款项列表
    for (int i = 0; i < monthCountDiff; i++) {
      PaymentTrans paymentTrans = new PaymentTrans();
      paymentTrans.setTradeType(tradeType);
      paymentTrans.setPaymentType(PaymentTransTypeEnum.RENT_AMOUNT.getValue());
      paymentTrans.setTransId(transObjId);
      paymentTrans.setTradeDirection(TradeDirectionEnum.IN.getValue());
      paymentTrans.setTradeAmount(rentalAmt);
      paymentTrans.setStartDate(startD);
      if (i != (monthCountDiff - 1)) {
        paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
      } else {
        paymentTrans.setExpiredDate(rentContract.getExpiredDate());
      }
      if (depositTransContractFlag) {// 定金转合同
        if (depositAgreementAmount >= rentalAmt) {
          paymentTrans.setLastAmount(0D);
          paymentTrans.setTransAmount(rentalAmt);
          paymentTrans.setTransStatus(PaymentTransStatusEnum.WHOLE_SIGN.getValue());
          paymentTrans.setTransferDepositAmount(rentalAmt);
          depositAgreementAmount = depositAgreementAmount - rentalAmt;
        } else if (depositAgreementAmount > 0 && depositAgreementAmount < rentalAmt) {
          paymentTrans.setLastAmount(rentalAmt - depositAgreementAmount);
          paymentTrans.setTransAmount(depositAgreementAmount);
          paymentTrans.setTransStatus(PaymentTransStatusEnum.PART_SIGN.getValue());
          paymentTrans.setTransferDepositAmount(depositAgreementAmount);
          depositAgreementAmount = 0d;
        } else {
          paymentTrans.setLastAmount(rentalAmt);
          paymentTrans.setTransAmount(0D);
          paymentTrans.setTransStatus(PaymentTransStatusEnum.NO_SIGN.getValue());
        }
      } else {// 正常保存，无定金
        paymentTrans.setLastAmount(rentalAmt);
        paymentTrans.setTransAmount(0D);
        paymentTrans.setTransStatus(PaymentTransStatusEnum.NO_SIGN.getValue());
        paymentTrans.setTransferDepositAmount(0d);
      }
      paymentTransService.save(paymentTrans);
      startD = DateUtils.dateAddMonth(startD, 1);
    }
  }

  /**
   * 生成合同期内的所有费用款项
   */
  private void genContractFeesPayTrans(String tradeType, String transObjId, RentContract rentContract, int monthCountDiff) {
    if (FeeChargeTypeEnum.PRE_CHARGE.getValue().equals(rentContract.getChargeType())) {// 预付
      Date startD = rentContract.getStartDate();// 开始日期
      for (int i = 0; i < monthCountDiff; i++) {
        Date expiredDate = null;
        if (i != (monthCountDiff - 1)) {
          expiredDate = DateUtils.dateAddMonth2(startD, 1);
        } else {
          expiredDate = rentContract.getExpiredDate();
        }
        // 水费
        Double waterFeeAmt = rentContract.getWaterFee();
        if (null != waterFeeAmt && waterFeeAmt > 0) {
          paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.WATER_AMOUNT.getValue(), transObjId, TradeDirectionEnum.IN.getValue(), waterFeeAmt, waterFeeAmt, 0D,
              PaymentTransStatusEnum.NO_SIGN.getValue(), startD, expiredDate);
        }
        // 电视费
        Double tvFeeAmt = rentContract.getTvFee();
        if ("1".equals(rentContract.getHasTv()) && null != tvFeeAmt && tvFeeAmt > 0) {
          paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.TV_AMOUNT.getValue(), transObjId, TradeDirectionEnum.IN.getValue(), tvFeeAmt, tvFeeAmt, 0D,
              PaymentTransStatusEnum.NO_SIGN.getValue(), startD, expiredDate);
        }
        // 宽带费
        Double netFee = rentContract.getNetFee();
        if ("1".equals(rentContract.getHasNet()) && null != netFee && netFee > 0) {
          paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.NET_AMOUNT.getValue(), transObjId, TradeDirectionEnum.IN.getValue(), netFee, netFee, 0D,
              PaymentTransStatusEnum.NO_SIGN.getValue(), startD, expiredDate);
        }

        // 服务费，按照比例计算
        Double serviceFee = rentContract.getServiceFee();
        if (null != serviceFee && serviceFee > 0) {
          Double tradeAmt = rentContract.getServiceFee() * rentContract.getRental();
          paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.SERVICE_AMOUNT.getValue(), transObjId, TradeDirectionEnum.IN.getValue(), tradeAmt, tradeAmt, 0D,
              PaymentTransStatusEnum.NO_SIGN.getValue(), startD, expiredDate);
        }
        startD = DateUtils.dateAddMonth(startD, 1);
      }
    }
  }

  @Transactional(readOnly = true)
  public RentContract getByHouseId(RentContract rentContract) {
    return dao.getByHouseId(rentContract);
  }

  /* 保存款项 */
  private void savePaymentTrans(String tradeType, Accounting accounting, RentContract rentContract, String tradeDirection) {
    PaymentTrans paymentTrans = new PaymentTrans();
    paymentTrans.setTradeType(tradeType);
    paymentTrans.setPaymentType(accounting.getFeeType());
    paymentTrans.setTransId(rentContract.getId());
    paymentTrans.setTradeDirection(tradeDirection);
    paymentTrans.setStartDate(rentContract.getStartDate());
    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
    paymentTrans.setTradeAmount(accounting.getFeeAmount());
    paymentTrans.setLastAmount(accounting.getFeeAmount());
    paymentTrans.setTransAmount(0D);
    paymentTrans.setTransStatus(PaymentTransStatusEnum.NO_SIGN.getValue());
    paymentTransService.save(paymentTrans);
  }

  private void saveAccounting(String tradeType, Accounting accounting, RentContract rentContract, String tradeDirection) {
    accounting.setRentContract(rentContract);
    if (TradeTypeEnum.NORMAL_RETURN_RENT.getValue().equals(tradeType)) {
      accounting.setAccountingType(AccountingTypeEnum.NORMAL_RETURN_ACCOUNT.getValue());
    } else if (TradeTypeEnum.ADVANCE_RETURN_RENT.getValue().equals(tradeType)) {
      accounting.setAccountingType(AccountingTypeEnum.ADVANCE_RETURN_ACCOUNT.getValue());
    } else if (TradeTypeEnum.OVERDUE_RETURN_RENT.getValue().equals(tradeType)) {
      accounting.setAccountingType(AccountingTypeEnum.LATE_RETURN_ACCOUNT.getValue());
    } else if (TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {
      accounting.setAccountingType(AccountingTypeEnum.SPECIAL_RETURN_ACCOUNT.getValue());
    }
    accounting.setFeeDirection(tradeDirection);
    accounting.setUser(UserUtils.getUser());
    if (!TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {
      accounting.setFeeDate(new Date());
    } else {
      if (StringUtils.isNotEmpty(accounting.getFeeDateStr())) {
        accounting.setFeeDate(DateUtils.parseDate(accounting.getFeeDateStr()));
      }
    }
    accountingService.save(accounting);
  }

  private void generatePaymentTransAndAccounts(List<Accounting> accountList, RentContract rentContract, String tradeType, String feeDirection) {
    for (Accounting accounting : accountList) {
      if (accounting != null && accounting.getFeeAmount() != null && accounting.getFeeAmount() != 0) {
        if (!TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {// 特殊退租不生成款项
          if (accounting.getFeeAmount() > 0) {
            savePaymentTrans(tradeType, accounting, rentContract, feeDirection);
          }
        }
        saveAccounting(tradeType, accounting, rentContract, feeDirection);
      }
    }
  }
}
