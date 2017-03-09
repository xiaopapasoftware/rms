package com.thinkgem.jeesite.modules.contract.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
import com.thinkgem.jeesite.modules.common.enums.ValidatorFlagEnum;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.ContractSignTypeEnum;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.TenantService;

/**
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class ContractTenantService extends CrudService<ContractTenantDao, ContractTenant> {

  @Autowired
  private TenantService tenantService;

  /**
   * 根据定金协议查询其下租客列表
   */
  public List<Tenant> getDepositAgreementTenantList(String depositAgreementId) {
    List<Tenant> tenants = new ArrayList<Tenant>();
    ContractTenant contractTenant = new ContractTenant();
    contractTenant.setDepositAgreementId(depositAgreementId);
    List<ContractTenant> cts = super.findList(contractTenant);
    if (CollectionUtils.isNotEmpty(cts)) {
      for (ContractTenant ct : cts) {
        Tenant t = tenantService.get(ct.getTenantId());
        if (t != null) {
          tenants.add(t);
        }
      }
    }
    return tenants;
  }

  /**
   * 保存或修改定金协议（包括前端APP的定金）
   */
  @Transactional(readOnly = false)
  public void doProcess4DepositAgreement(DepositAgreement depositAgreement) {
    if (DataSourceEnum.FRONT_APP.getValue().equals(depositAgreement.getDataSource()) && ValidatorFlagEnum.TEMP_SAVE.getValue().equals(depositAgreement.getValidatorFlag())) {
      List<Tenant> leaseTenants = depositAgreement.getTenantList();
      if (CollectionUtils.isNotEmpty(leaseTenants)) {
        Tenant leaseTenat = leaseTenants.get(0);
        Tenant queryTenant = new Tenant();
        queryTenant.setCellPhone(leaseTenat.getCellPhone());
        List<Tenant> tempTenantList = tenantService.findTenantByPhone(queryTenant);
        if (CollectionUtils.isEmpty(tempTenantList)) {
          String tenatId = tenantService.saveAndReturnId(leaseTenat);
          ContractTenant contractTenant = new ContractTenant();
          contractTenant.setTenantId(tenatId);
          contractTenant.setDepositAgreementId(depositAgreement.getId());
          super.save(contractTenant);
        } else {
          ContractTenant contractTenant = new ContractTenant();
          contractTenant.setTenantId(tempTenantList.get(0).getId());
          contractTenant.setDepositAgreementId(depositAgreement.getId());
          List<ContractTenant> cts = super.findList(contractTenant);
          if (CollectionUtils.isEmpty(cts)) {
            super.save(contractTenant);
          }
        }
      }
    } else { /* 保存定金-租客关联信息 */
      List<Tenant> tenants = depositAgreement.getTenantList();
      if (CollectionUtils.isNotEmpty(tenants)) {
        ContractTenant delTenant = new ContractTenant();
        delTenant.setDepositAgreementId(depositAgreement.getId());
        delTenant.preUpdate();
        super.delete(delTenant);
        for (Tenant tenant : tenants) {
          ContractTenant contractTenant = new ContractTenant();
          contractTenant.setTenantId(tenant.getId());
          contractTenant.setDepositAgreementId(depositAgreement.getId());
          super.save(contractTenant);
        }
      }
    }
  }

  /**
   * 保存或修改出租合同（包括前端APP的合同）
   */
  @Transactional(readOnly = false)
  public void doProcess4RentContract(RentContract rentContract) {
    // 合同承租人关联信，如果是从APP过来的需要新增租客信息
    if (ContractSignTypeEnum.NEW_SIGN.getValue().equals(rentContract.getSignType()) && DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource())
        && ValidatorFlagEnum.TEMP_SAVE.getValue().equals(rentContract.getValidatorFlag())) {
      List<Tenant> leaseTenants = rentContract.getTenantList();
      if (CollectionUtils.isNotEmpty(leaseTenants)) {
        Tenant leaseTenat = leaseTenants.get(0);
        Tenant queryTenant = new Tenant();
        queryTenant.setCellPhone(leaseTenat.getCellPhone());
        List<Tenant> tempTenantList = tenantService.findTenantByPhone(queryTenant);
        if (CollectionUtils.isEmpty(tempTenantList)) {
          String tenatId = tenantService.saveAndReturnId(leaseTenat);
          ContractTenant contractTenant = new ContractTenant();
          contractTenant.setTenantId(tenatId);
          contractTenant.setLeaseContractId(rentContract.getId());
          contractTenant.setContractId(rentContract.getId());
          super.save(contractTenant);
        } else {
          Tenant tt = tempTenantList.get(0);
          ContractTenant contractTenant = new ContractTenant();
          contractTenant.setTenantId(tt.getId());
          contractTenant.setLeaseContractId(rentContract.getId());
          contractTenant.setContractId(rentContract.getId());
          List<ContractTenant> cts = super.findList(contractTenant);
          if (CollectionUtils.isEmpty(cts)) {
            super.save(contractTenant);
          }
        }
      }
    } else {// 后台管理系统直接保存合同
      updateTenantList(rentContract.getId(), rentContract.getTenantList());
      updateLiveList(rentContract.getId(), rentContract.getLiveList());
    }
  }

  /**
   * 更新出租合同的承租人列表
   */
  @Transactional(readOnly = false)
  public void updateTenantList(String rentContractId, List<Tenant> tenantList) {
    ContractTenant delContractTenant = new ContractTenant();
    delContractTenant.setLeaseContractId(rentContractId);
    delContractTenant.preUpdate();
    super.delete(delContractTenant);
    if (CollectionUtils.isNotEmpty(tenantList)) {
      for (Tenant tenant : tenantList) {
        ContractTenant contractTenant = new ContractTenant();
        contractTenant.setTenantId(tenant.getId());
        contractTenant.setLeaseContractId(rentContractId);
        super.save(contractTenant);
      }
    }
  }

  /**
   * 更新出租合同的入住人列表
   */
  @Transactional(readOnly = false)
  public void updateLiveList(String rentContractId, List<Tenant> LiveList) {
    ContractTenant delContractTenant = new ContractTenant();
    delContractTenant.setContractId(rentContractId);
    delContractTenant.preUpdate();
    super.delete(delContractTenant);
    if (CollectionUtils.isNotEmpty(LiveList)) {
      for (Tenant tenant : LiveList) {
        ContractTenant contractTenant = new ContractTenant();
        contractTenant.setTenantId(tenant.getId());
        contractTenant.setContractId(rentContractId);
        super.save(contractTenant);
      }
    }
  }
}
