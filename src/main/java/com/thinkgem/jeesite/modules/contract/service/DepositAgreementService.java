/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
import com.thinkgem.jeesite.modules.common.enums.ValidatorFlagEnum;
import com.thinkgem.jeesite.modules.common.service.AttachmentService;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.enums.AgreementAuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.AuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.AuditTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 定金协议Service
 * 
 * @author huangsc @version 2015-06-09
 * @author wangshujin @version 2016-08-22
 */
@Service
@Transactional(readOnly = true)
public class DepositAgreementService extends CrudService<DepositAgreementDao, DepositAgreement> {
  @Autowired
  private PaymentTransService paymentTransService;
  @Autowired
  private AuditService auditService;
  @Autowired
  private AuditHisService auditHisService;
  @Autowired
  private ContractTenantService contractTenantService;
  @Autowired
  private TenantService tenantService;
  @Autowired
  private HouseService houseService;
  @Autowired
  private RoomService roomService;
  @Autowired
  private AttachmentService attachmentService;

  public List<Tenant> findTenant(DepositAgreement depositAgreement) {
    List<Tenant> tenantList = new ArrayList<Tenant>();
    ContractTenant contractTenant = new ContractTenant();
    contractTenant.setDepositAgreementId(depositAgreement.getId());
    List<ContractTenant> list = contractTenantService.findList(contractTenant);
    if (CollectionUtils.isNotEmpty(list)) {
      for (ContractTenant tmpContractTenant : list) {
        tenantList.add(tenantService.get(tmpContractTenant.getTenantId()));
      }
    }
    return tenantList;
  }

  @Transactional(readOnly = false)
  public void audit(AuditHis auditHis) {
    auditHisService.saveAuditHis(auditHis, AuditTypeEnum.DEPOSIT_AGREEMENT_CONTENT.getValue());
    String depositAgreemId = auditHis.getObjectId();
    DepositAgreement depositAgreement = dao.get(depositAgreemId);
    if (AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus())) {// 审核通过
      Audit audit = new Audit();
      audit.setObjectId(depositAgreemId);
      audit.setNextRole("");
      auditService.update(audit);
      depositAgreement.setAgreementStatus(AgreementAuditStatusEnum.INVOICE_TO_AUDITED.getValue());
    } else { // 审核拒绝时，需要把房屋和房间的状态回滚到原先状态,前提条件是房屋状态是已预定
      if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(depositAgreement.getRentMode())) {
        House house = houseService.get(depositAgreement.getHouse().getId());
        houseService.releaseWholeHouse(house);
      } else {// 合租,更新房间状态
        Room room = roomService.get(depositAgreement.getRoom().getId());
        houseService.releaseSingleRoom(room);
      }
      paymentTransService.deletePaymentTransAndTradingAcctouns(depositAgreemId); // 删除对象下所有的款项，账务，款项账务关联关系，以及相关收据
      depositAgreement.setAgreementStatus(AgreementAuditStatusEnum.CONTENT_AUDIT_REFUSE.getValue());
    }
    if (DataSourceEnum.FRONT_APP.getValue().equals(depositAgreement.getDataSource())) {
      depositAgreement.setUpdateUser(auditHis.getUpdateUser());
    } else {
      depositAgreement.setUpdateUser(UserUtils.getUser().getId());
    }
    depositAgreement.preUpdate();
    dao.update(depositAgreement);
  }

  @Transactional(readOnly = false)
  public void save(DepositAgreement depositAgreement) {
    // 保存定金协议记录
    String id = saveAndReturnId(depositAgreement);
    // 页面保存操作
    if (ValidatorFlagEnum.SAVE.getValue().equals(depositAgreement.getValidatorFlag())) {
      // 删除定金协议相关的款项，账务，款项账务关联关系，以及相关收据、附件信息
      paymentTransService.deletePaymentTransAndTradingAcctouns(id);
      // 生成定金协议款项
      if (depositAgreement.getDepositAmount() != null && depositAgreement.getDepositAmount() > 0D) {
        paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.DEPOSIT_AGREEMENT.getValue(), PaymentTransTypeEnum.RECEIVABLE_DEPOSIT.getValue(), id, TradeDirectionEnum.IN.getValue(),
            depositAgreement.getDepositAmount(), depositAgreement.getDepositAmount(), 0D, PaymentTransStatusEnum.NO_SIGN.getValue(), depositAgreement.getStartDate(),
            depositAgreement.getExpiredDate());
      }
      // 更新房屋/房间状态
      if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(depositAgreement.getRentMode())) {
        House house = houseService.get(depositAgreement.getHouse().getId());
        houseService.depositWholeHouse(house);
      } else {
        Room room = roomService.get(depositAgreement.getRoom().getId());
        houseService.depositSingleRoom(room);
      }
      // 审核
      auditService.delete(id);
      auditService.insert(AuditTypeEnum.DEPOSIT_AGREEMENT_CONTENT.getValue(), "deposit_agreement_role", id, "");
    }
    /* 保存定金-租客关联信息 */
    List<Tenant> tenants = depositAgreement.getTenantList();
    if (CollectionUtils.isNotEmpty(tenants)) {
      ContractTenant delTenant = new ContractTenant();
      delTenant.setDepositAgreementId(id);
      delTenant.preUpdate();
      contractTenantService.delete(delTenant);
      for (Tenant tenant : tenants) {
        ContractTenant contractTenant = new ContractTenant();
        contractTenant.setTenantId(tenant.getId());
        contractTenant.setDepositAgreementId(id);
        contractTenantService.save(contractTenant);
      }
    }
    // 保存或暂存，需要清空定金协议所有的附件信息,再新增定金协议相关的附件
    if (!depositAgreement.getIsNewRecord()) {
      Attachment attachment = new Attachment();
      attachment.setDepositAgreemId(depositAgreement.getId());
      attachmentService.delete(attachment);
    }
    if (StringUtils.isNotBlank(depositAgreement.getDepositAgreementFile())) {
      attachmentService.save(generateAttachment(id, depositAgreement.getDepositAgreementFile(), FileType.DEPOSITAGREEMENT_FILE.getValue()));
    }
    if (StringUtils.isNotBlank(depositAgreement.getDepositCustomerIDFile())) {
      attachmentService.save(generateAttachment(id, depositAgreement.getDepositCustomerIDFile(), FileType.TENANT_ID.getValue()));
    }
    if (StringUtils.isNotBlank(depositAgreement.getDepositOtherFile())) {
      attachmentService.save(generateAttachment(id, depositAgreement.getDepositOtherFile(), FileType.DEPOSITRECEIPT_FILE_OTHER.getValue()));
    }
  }

  @Transactional(readOnly = false)
  public void delete(DepositAgreement depositAgreement) {
    super.delete(depositAgreement);
  }

  public List<DepositAgreement> findAllValidAgreements() {
    return dao.findAllList(new DepositAgreement());
  }

  public Integer getTotalValidDACounts() {
    return dao.getTotalValidDACounts(new DepositAgreement());
  }

  private Attachment generateAttachment(String depositAgreemId, String filePath, String fileType) {
    Attachment attachment = new Attachment();
    attachment.setDepositAgreemId(depositAgreemId);
    attachment.setAttachmentPath(filePath);
    attachment.setAttachmentType(fileType);
    return attachment;
  }

  public DepositAgreement getByHouseId(DepositAgreement depositAgreement) {
    return dao.getByHouseId(depositAgreement);
  }

  @Transactional(readOnly = false)
  public void update(DepositAgreement depositAgreement) {
    depositAgreement.preUpdate();
    dao.update(depositAgreement);
  }
}
