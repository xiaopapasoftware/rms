/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.ArrayList;
import java.util.List;

import com.thinkgem.jeesite.common.persistence.Page;
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
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
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
import com.thinkgem.jeesite.modules.utils.UserUtils;

/**
 * 定金协议Service
 * 
 * @author wangshujin
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
  @Autowired
  private LeaseContractService leaseContractService;

  @Override
  public List<DepositAgreement> findList(DepositAgreement entity) {
    areaScopeFilter(entity,"dsf","tp.area_id=sua.area_id");
    return super.findList(entity);
  }

  @Override
  public Page<DepositAgreement> findPage(Page<DepositAgreement> page, DepositAgreement entity) {
    areaScopeFilter(entity,"dsf","tp.area_id=sua.area_id");
    return super.findPage(page, entity);
  }

  /**
   * 根据定金协议设置其对应的承租人姓名和手机号列表
   */
  public void setDepositNameAndPhoneList(DepositAgreement depositAgreement) {}

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
    DepositAgreement depositAgreement = super.get(depositAgreemId);
    if (AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus())) {
      Audit audit = new Audit();
      audit.setObjectId(depositAgreemId);
      audit.setNextRole("");
      auditService.update(audit);
      depositAgreement.setAgreementStatus(AgreementAuditStatusEnum.INVOICE_TO_AUDITED.getValue());
    } else { // 审核拒绝
      if (DataSourceEnum.FRONT_APP.getValue().equals(depositAgreement.getDataSource())) {
        depositAgreement.setUpdateUser(auditHis.getUpdateUser());
      } else {
        depositAgreement.setUpdateUser(UserUtils.getUser().getId());
      }
      if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(depositAgreement.getRentMode())) {
        House house = houseService.get(depositAgreement.getHouse().getId());
        houseService.releaseWholeHouse(house);
      } else {
        Room room = roomService.get(depositAgreement.getRoom().getId());
        houseService.releaseSingleRoom(room);
      }
      paymentTransService.deletePaymentTransAndTradingAcctouns(depositAgreemId); // 删除对象下所有的款项，账务，款项账务关联关系，以及相关收据
      depositAgreement.setAgreementStatus(AgreementAuditStatusEnum.CONTENT_AUDIT_REFUSE.getValue());
    }
    super.save(depositAgreement);
  }

  /**
   * 预定房源，需首先根据不同请求类型锁定房源状态
   * 
   * @return 0=成功；-1=房源已预定；-2=出租合同结束日期不能晚于承租合同截止日期；
   */
  @Transactional(readOnly = false)
  public int saveDepositAgreement(DepositAgreement depositAgreement) {
    // 预定的结束时间不能超过承租合同的结束时间
    LeaseContract leaseContract = new LeaseContract();
    leaseContract.setHouse(depositAgreement.getHouse());
    List<LeaseContract> list = leaseContractService.findList(leaseContract);
    if (CollectionUtils.isNotEmpty(list)) {
      if (list.get(0).getExpiredDate().before(depositAgreement.getExpiredDate())) {
        return -2;
      }
    }
    String curHouseId = depositAgreement.getHouse().getId();
    String curRoomId = depositAgreement.getRoom() == null ? "" : depositAgreement.getRoom().getId();
    curRoomId = StringUtils.isBlank(curRoomId) ? "" : curRoomId;
    if (depositAgreement.getIsNewRecord()) {// 新增,包括手机APP在线预订申请以及后台直接新增预订，需要锁定房源
      return proccessHouseRoomStatus(depositAgreement, curHouseId, curRoomId);
    } else { // 手机APP签约、后台 修改- 保存/暂存
      DepositAgreement originalDepositAgreement = super.get(depositAgreement.getId());
      // 如果选择的房源发生变化，需要把以前的房源状态回滚，改变后选的房源状态
      String originalHouseId = originalDepositAgreement.getHouse().getId();
      String originalRoomId = originalDepositAgreement.getRoom() == null ? "" : originalDepositAgreement.getRoom().getId();
      originalRoomId = StringUtils.isBlank(originalRoomId) ? "" : originalRoomId;
      if (!originalHouseId.equals(curHouseId) || !originalRoomId.equals(curRoomId)) {// 定金协议修改了房屋或房间
        // 回滚前房源
        if (StringUtils.isNotBlank(originalRoomId)) {// 合租，把单间从“已预定”改为“待出租可预订”
          houseService.releaseSingleRoom(roomService.get(originalRoomId));
        } else {// 整租，把房屋从“已预定”改为“待出租可预订”
          houseService.releaseWholeHouse(houseService.get(originalHouseId));
        }
        // 预定新房源
        return proccessHouseRoomStatus(depositAgreement, curHouseId, curRoomId);
      } else {
        doSaveDepositAgreement(depositAgreement);
        return 0;
      }
    }
  }

  private int proccessHouseRoomStatus(DepositAgreement depositAgreement, String houseId, String roomId) {
    if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(depositAgreement.getRentMode())) {// 整租
      boolean isLock = houseService.isLockWholeHouse4Deposit(houseId);
      if (isLock) {
        roomService.depositAllRooms(houseId);
        doSaveDepositAgreement(depositAgreement);
        return 0;
      } else {
        return -1;
      }
    } else {// 合租
      boolean isLock = roomService.isLockSingleRoom4Deposit(roomId);
      if (isLock) {
        houseService.calculateHouseStatus(roomId);
        doSaveDepositAgreement(depositAgreement);
        return 0;
      } else {
        return -1;
      }
    }
  }

  /**
   * 房源状态变更成功，做预定相关业务
   */
  private void doSaveDepositAgreement(DepositAgreement depositAgreement) {
    String id = saveAndReturnId(depositAgreement);// 保存定金协议记录
    if (ValidatorFlagEnum.SAVE.getValue().equals(depositAgreement.getValidatorFlag())) { // 页面保存操作
      paymentTransService.deletePaymentTransAndTradingAcctouns(id);// 删除定金协议相关的款项，账务，款项账务关联关系，以及相关收据、附件信息
      Double depositAmount = depositAgreement.getDepositAmount();
      if (depositAmount != null && depositAmount > 0D) { // 生成定金协议款项
        paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.DEPOSIT_AGREEMENT.getValue(), PaymentTransTypeEnum.RECEIVABLE_DEPOSIT.getValue(), id, TradeDirectionEnum.IN.getValue(),
            depositAmount, depositAmount, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(), depositAgreement.getStartDate(), depositAgreement.getExpiredDate(), null);
      }
      auditService.delete(id);// 审核
      auditService.insert(AuditTypeEnum.DEPOSIT_AGREEMENT_CONTENT.getValue(), "deposit_agreement_role", id, "");
    }
    contractTenantService.doProcess4DepositAgreement(depositAgreement); // 处理承租人关系
    if (!depositAgreement.getIsNewRecord()) { // 保存或暂存，需要清空定金协议所有的附件信息,再新增定金协议相关的附件
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
