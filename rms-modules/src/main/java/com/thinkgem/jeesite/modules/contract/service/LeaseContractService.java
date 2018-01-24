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

import java.math.BigDecimal;
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
        LeaseContract leaseContract;
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
            List<LeaseContractDtl> leaseContractDtlList = leaseContractDtlDao.findList(leaseContractDtl);
            for (LeaseContractDtl tmpLeaseContractDtl : leaseContractDtlList) {
                Date currentStartDate = tmpLeaseContractDtl.getStartDate();//阶段开始日期
                Date currentEndDate = tmpLeaseContractDtl.getEndDate();//阶段结束日期
                Double depositRentAmt = tmpLeaseContractDtl.getDeposit();//阶段月承租价格
                int curMonthCountDiff = DateUtils.getMonthSpace(currentStartDate, currentEndDate);//阶段租期间隔月数
                if (currentEndDate.after(currentStartDate) && depositRentAmt != null && depositRentAmt > 0) {
                    if (curMonthCountDiff <= 0) {//日期间隔不足一个月
                        double dates = DateUtils.getDistanceOfTwoDate(currentStartDate, currentEndDate);
                        double dailyFee = depositRentAmt / 30;
                        double amount = dates * dailyFee;
                        double properAmt = new BigDecimal(amount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                        paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.LEASE_CONTRACT_TRADE.getValue(),
                                PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue(), leaseContract.getId(), TradeDirectionEnum.OUT.getValue(), properAmt, properAmt, 0d,
                                PaymentTransStatusEnum.NO_SIGN.getValue(), currentStartDate, currentEndDate, null);
                    } else {//日期间隔超过一个月
                        for (int i = 0; i < curMonthCountDiff; i++) {
                            if (i != (curMonthCountDiff - 1)) {
                                paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.LEASE_CONTRACT_TRADE.getValue(),
                                        PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue(), leaseContract.getId(), TradeDirectionEnum.OUT.getValue(), depositRentAmt, depositRentAmt, 0d,
                                        PaymentTransStatusEnum.NO_SIGN.getValue(), currentStartDate, DateUtils.dateAddMonth2(currentStartDate, 1), null);
                            } else {
                                paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.LEASE_CONTRACT_TRADE.getValue(),
                                        PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue(), leaseContract.getId(), TradeDirectionEnum.OUT.getValue(), depositRentAmt, depositRentAmt, 0d,
                                        PaymentTransStatusEnum.NO_SIGN.getValue(), currentStartDate, currentEndDate, null);

                            }
                            currentStartDate = DateUtils.dateAddMonth(currentStartDate, 1);
                        }
                    }
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
    private void processLeaseContractAttachmentFiles(String leaseContractId, String trusteeshipContr, String
            landlordId, String profile, String certificate, String relocation) {
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
