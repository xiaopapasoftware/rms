/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.dao.LeaseContractDao;
import com.thinkgem.jeesite.modules.contract.dao.LeaseContractDtlDao;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContractDtl;
import com.thinkgem.jeesite.modules.contract.enums.*;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 承租合同Service
 *
 * @author huangsc
 * @version 2015-06-06
 */
@Service
@Transactional(readOnly = true)
public class LeaseContractService extends CrudService<LeaseContractDao, LeaseContract> {
  @Autowired
  private AttachmentDao attachmentDao;
  @Autowired
  private LeaseContractDtlDao leaseContractDtlDao;
  @Autowired
  private AuditDao auditDao;
  @Autowired
  private AuditHisDao auditHisDao;
  @Autowired
  private LeaseContractDao leaseContractDao;
  @Autowired
  private PaymentTransDao paymentTransDao;
  @Autowired
  private PaymentTransService paymentTransService;

  private static final String LEASE_CONTRACT_ROLE = "lease_contract_role";// 承租合同审批

  public LeaseContract get(String id) {
    LeaseContract leaseContract = super.get(id);
    if (null != leaseContract) {
      LeaseContractDtl deaseContractDtl = new LeaseContractDtl();
      deaseContractDtl.setLeaseContractId(id);
      deaseContractDtl.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
      List<LeaseContractDtl> leaseContractDtlList = leaseContractDtlDao.findList(deaseContractDtl);
      leaseContract.setLeaseContractDtlList(leaseContractDtlList);
    }
    return leaseContract;
  }

  @Override
  public List<LeaseContract> findList(LeaseContract entity) {
    areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
    return super.findList(entity);
  }

  @Override
  public Page<LeaseContract> findPage(Page<LeaseContract> page, LeaseContract entity) {
    areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
    return super.findPage(page, entity);
  }

  @Transactional(readOnly = false)
  public void audit(AuditHis auditHis) {
    AuditHis saveAuditHis = new AuditHis();
    saveAuditHis.preInsert();
    saveAuditHis.setObjectType(AuditTypeEnum.LEASE_CONTRACT_CONTENT.getValue());
    saveAuditHis.setObjectId(auditHis.getObjectId());
    saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
    saveAuditHis.setAuditStatus(auditHis.getAuditStatus());
    saveAuditHis.setAuditTime(new Date());
    saveAuditHis.setAuditUser(UserUtils.getUser().getId());
    auditHisDao.insert(saveAuditHis);
    LeaseContract leaseContract = new LeaseContract();
    leaseContract = leaseContractDao.get(auditHis.getObjectId());
    leaseContract.setContractStatus(auditHis.getAuditStatus());
    leaseContract.preUpdate();
    leaseContractDao.update(leaseContract);
    if (AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus())) {// 审核通过，生成款项
      Audit audit = new Audit();
      audit.setObjectId(auditHis.getObjectId());
      audit.setNextRole("");
      audit.preUpdate();
      auditDao.update(audit);
      PaymentTrans delPaymentTrans = new PaymentTrans();
      delPaymentTrans.setTransId(leaseContract.getId());
      delPaymentTrans.preUpdate();
      paymentTransDao.delete(delPaymentTrans);
      // 1.押金款项
      paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.LEASE_CONTRACT_TRADE.getValue(), PaymentTransTypeEnum.RENT_DEPOSIT.getValue(), leaseContract.getId(),
          TradeDirectionEnum.OUT.getValue(), leaseContract.getDeposit(), leaseContract.getDeposit(), 0d, PaymentTransStatusEnum.NO_SIGN.getValue(), leaseContract.getEffectiveDate(),
          leaseContract.getExpiredDate(), null);
      // 2.房租款项
      LeaseContractDtl leaseContractDtl = new LeaseContractDtl();
      leaseContractDtl.setLeaseContractId(leaseContract.getId());
      List<LeaseContractDtl> list = leaseContractDtlDao.findList(leaseContractDtl);
      int monthSpace = leaseContract.getMonthSpace();// 打款月份间隔
      List<PaymentTrans> listPaymentTrans = new ArrayList<PaymentTrans>();
      for (LeaseContractDtl tmpLeaseContractDtl : list) {// 计算开始日期与结束日期之间的月数
        int month = DateUtils.getMonthSpace(tmpLeaseContractDtl.getStartDate(), tmpLeaseContractDtl.getEndDate());
        month = (month == 0 ? month++ : month);
        for (int i = 1; i <= month; i++) {
          PaymentTrans paymentTrans = new PaymentTrans();
          paymentTrans.setId(IdGen.uuid());
          paymentTrans.setTradeType(TradeTypeEnum.LEASE_CONTRACT_TRADE.getValue());
          paymentTrans.setPaymentType(PaymentTransTypeEnum.RENT_AMOUNT.getValue());
          paymentTrans.setTransId(leaseContract.getId());
          paymentTrans.setTradeDirection(TradeDirectionEnum.OUT.getValue());
          Date startDate = i == 1 ? tmpLeaseContractDtl.getStartDate() : DateUtils.dateAddMonth2(tmpLeaseContractDtl.getStartDate(), i - 1);
          paymentTrans.setStartDate(startDate);
          Date endDate = i == month ? tmpLeaseContractDtl.getEndDate() : DateUtils.dateAddMonth2(tmpLeaseContractDtl.getStartDate(), i);
          paymentTrans.setExpiredDate(endDate);
          paymentTrans.setTradeAmount(tmpLeaseContractDtl.getDeposit());
          paymentTrans.setTransAmount(0d);
          paymentTrans.setLastAmount(tmpLeaseContractDtl.getDeposit());
          paymentTrans.setTransStatus(PaymentTransStatusEnum.NO_SIGN.getValue());
          paymentTrans.setCreateDate(new Date());
          paymentTrans.setCreateBy(UserUtils.getUser());
          paymentTrans.setUpdateDate(new Date());
          paymentTrans.setUpdateBy(UserUtils.getUser());
          paymentTrans.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
          if (tmpLeaseContractDtl.getDeposit() != null && tmpLeaseContractDtl.getDeposit() > 0) {
            listPaymentTrans.add(paymentTrans);
          }
        }
      }
      int split = listPaymentTrans.size() / monthSpace;
      for (int i = 0; i < split; i++) {
        int end = (i + 1) * monthSpace;
        List<PaymentTrans> tmpList = listPaymentTrans.subList(i * monthSpace, i != (split - 1) ? end : listPaymentTrans.size());
        PaymentTrans tmpPaymentTrans = tmpList.get(0);
        double tradeAmount = 0d;
        for (PaymentTrans p : tmpList) {
          tradeAmount += p.getTradeAmount();
        }
        tmpPaymentTrans.setTradeAmount(tradeAmount);
        tmpPaymentTrans.setTransAmount(0d);
        tmpPaymentTrans.setLastAmount(tmpPaymentTrans.getTradeAmount());
        if (i != 0) {
          tmpPaymentTrans.setStartDate(DateUtils.dateAddDay(tmpPaymentTrans.getStartDate(), 1));
        }
        Date endDate = DateUtils.dateAddMonth2(tmpPaymentTrans.getStartDate(), monthSpace);
        tmpPaymentTrans.setExpiredDate(endDate);
        if (tradeAmount > 0) {
          paymentTransDao.insert(tmpPaymentTrans);
        }
      }
      if (0 != listPaymentTrans.size() % monthSpace) {
        List<PaymentTrans> tmpList = listPaymentTrans.subList(listPaymentTrans.size() - listPaymentTrans.size() % monthSpace - 1, listPaymentTrans.size() - 1);
        PaymentTrans tmpPaymentTrans = tmpList.get(0);
        tmpPaymentTrans.setId(IdGen.uuid());
        double tradeAmount = 0d;
        for (PaymentTrans p : tmpList) {
          tradeAmount += p.getTradeAmount();
        }
        tmpPaymentTrans.setTradeAmount(tradeAmount);
        tmpPaymentTrans.setTransAmount(0d);
        tmpPaymentTrans.setLastAmount(tmpPaymentTrans.getTradeAmount());
        tmpPaymentTrans.setStartDate(DateUtils.dateAddDay(tmpPaymentTrans.getStartDate(), 1));
        Date endDate = DateUtils.dateAddMonth2(tmpPaymentTrans.getStartDate(), monthSpace);
        tmpPaymentTrans.setExpiredDate(endDate);
        if (tradeAmount > 0) {
          paymentTransDao.insert(tmpPaymentTrans);
        }
      }
    }
  }

  @Transactional(readOnly = false)
  public void save(LeaseContract leaseContract) {
    leaseContract.setContractStatus(LeaseContractAuditStatusEnum.TO_BE_AUDIT.getValue());// 待审核
    String id = super.saveAndReturnId(leaseContract);
    if (StringUtils.isEmpty(leaseContract.getId())) {
      Audit audit = new Audit();
      audit.preInsert();
      audit.setObjectId(id);
      audit.setObjectType(AuditTypeEnum.LEASE_CONTRACT_CONTENT.getValue());
      audit.setNextRole(LEASE_CONTRACT_ROLE);
      auditDao.insert(audit);
      processLeaseContractAttachmentFiles(id, leaseContract.getTrusteeshipContr(), leaseContract.getLandlordId(), leaseContract.getProfile(), leaseContract.getCertificate(),
          leaseContract.getRelocation()); // 保存附件
      // 承租合同明细
      List<LeaseContractDtl> leaseContractDtlList = leaseContract.getLeaseContractDtlList();
      if (CollectionUtils.isNotEmpty(leaseContractDtlList)) {
        for (LeaseContractDtl leaseContractDtl : leaseContractDtlList) {
          leaseContractDtl.preInsert();
          leaseContractDtl.setLeaseContractId(id);
          leaseContractDtlDao.insert(leaseContractDtl);
        }
      }
    } else {
      Audit audit = new Audit();
      audit.setObjectId(leaseContract.getId());
      audit.setNextRole(LEASE_CONTRACT_ROLE);
      audit.preUpdate();
      auditDao.update(audit);
      // 保存附件
      Attachment attachment = new Attachment();
      attachment.setLeaseContractId(leaseContract.getId());
      attachment.preUpdate();
      attachmentDao.delete(attachment);
      processLeaseContractAttachmentFiles(leaseContract.getId(), leaseContract.getTrusteeshipContr(), leaseContract.getLandlordId(), leaseContract.getProfile(), leaseContract.getCertificate(),
          leaseContract.getRelocation());
      // 承租合同明细，由于删除手法的特殊性，此处需做修改.
      List<LeaseContractDtl> leaseContractDtlList = leaseContract.getLeaseContractDtlList();// 总提交数据集
      List<LeaseContractDtl> addLeaseContractDtlList = Lists.newArrayList();// 新增的list
      List<LeaseContractDtl> delLeaseContractDtlList = Lists.newArrayList();// 删除的list
      if (CollectionUtils.isNotEmpty(leaseContractDtlList)) {
        for (LeaseContractDtl lcd : leaseContractDtlList) {
          if (StringUtils.isEmpty(lcd.getId()) && BaseEntity.DEL_FLAG_NORMAL.equals(lcd.getDelFlag())) {
            addLeaseContractDtlList.add(lcd);
          } else if (StringUtils.isNotEmpty(lcd.getId()) && BaseEntity.DEL_FLAG_DELETE.equals(lcd.getDelFlag())) {
            delLeaseContractDtlList.add(lcd);
          } else {// 更新
            leaseContractDtlDao.update(lcd);
          }
        }
      }
      if (CollectionUtils.isNotEmpty(delLeaseContractDtlList)) {
        for (LeaseContractDtl l : delLeaseContractDtlList) {
          l.preUpdate();
          leaseContractDtlDao.delete(l);
        }
      }
      if (CollectionUtils.isNotEmpty(addLeaseContractDtlList)) {
        for (LeaseContractDtl l : addLeaseContractDtlList) {
          l.preInsert();
          l.setLeaseContractId(leaseContract.getId());
          leaseContractDtlDao.insert(l);
        }
      }
    }
  }

  /**
   * 处理承租合同的附件信息
   */
  private void processLeaseContractAttachmentFiles(String leaseContractId, String trusteeshipContr, String landlordId, String profile, String certificate, String relocation) {
    Attachment attachment = new Attachment();
    attachment.preInsert();
    attachment.setLeaseContractId(leaseContractId);
    if (!StringUtils.isBlank(trusteeshipContr)) {
      attachment.setAttachmentType(FileType.TRUSTEESHIP_CONTRACT_FILE.getValue());
      attachment.setAttachmentPath(trusteeshipContr);
      attachmentDao.insert(attachment);
    }
    if (!StringUtils.isBlank(landlordId)) {
      attachment.setAttachmentType(FileType.OWNER_ID.getValue());
      attachment.setAttachmentPath(landlordId);
      attachmentDao.insert(attachment);
    }
    if (!StringUtils.isBlank(profile)) {
      attachment.setAttachmentType(FileType.CERTIFICATE_FILE.getValue());
      attachment.setAttachmentPath(profile);
      attachmentDao.insert(attachment);
    }
    if (!StringUtils.isBlank(certificate)) {
      attachment.setAttachmentType(FileType.HOUSE_CERTIFICATE.getValue());
      attachment.setAttachmentPath(certificate);
      attachmentDao.insert(attachment);
    }
    if (!StringUtils.isBlank(relocation)) {
      attachment.setAttachmentType(FileType.HOUSE_AGREEMENT_CERTIFICATE.getValue());
      attachment.setAttachmentPath(relocation);
      attachmentDao.insert(attachment);
    }
  }

  @Transactional(readOnly = false)
  public void delete(LeaseContract leaseContract) {
    super.delete(leaseContract);
  }

  @Transactional(readOnly = true)
  public List<LeaseContract> findAllValidLeaseContracts() {
    return leaseContractDao.findAllList(new LeaseContract());
  }

  @Transactional(readOnly = true)
  public Integer getTotalValidLeaseContractCounts() {
    return leaseContractDao.getTotalValidLeaseContractCounts(new LeaseContract());
  }

  public List<LeaseContract> findLeaseContractListByHouseId(String houseId) {
    List<LeaseContract> contractList = leaseContractDao.getLeaseContractListByHouseId(houseId);
    if (CollectionUtils.isNotEmpty(contractList)) {
      contractList.forEach(contract -> contract.setLeaseContractDtlList(leaseContractDtlDao.getLeaseContractDtlListByContractId(contract.getId())));
    }
    return contractList;
  }
}
