/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.dao.AgreementChangeDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.person.dao.TenantDao;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 协议变更Service
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class AgreementChangeService extends CrudService<AgreementChangeDao, AgreementChange> {
    @Autowired
    private AuditHisService auditHisService;
    @Autowired
    private AuditDao auditDao;
    @Autowired
    private ContractTenantDao contractTenantDao;
    @Autowired
    private TenantDao tenantDao;

    @Transactional(readOnly = false)
    public void save(AgreementChange agreementChange) {
	String id = super.saveAndReturnId(agreementChange);

	/* 合同变更协议租客关联信息 */
	ContractTenant delContractTenant = new ContractTenant();
	delContractTenant.preUpdate();
	delContractTenant.setAgreementChangeId(id);// 入住的变更协议
	contractTenantDao.delete(delContractTenant);
	List<Tenant> list = agreementChange.getLiveList();// 入住人
	if (null != list && list.size() > 0) {
	    for (Tenant tenant : list) {
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.preInsert();
		contractTenant.setTenantId(tenant.getId());
		contractTenant.setAgreementChangeId(id);// 入住的变更协议
		contractTenantDao.insert(contractTenant);
	    }
	}

	ContractTenant delContractTenant2 = new ContractTenant();
	delContractTenant2.setLeasagremChangeId(id);// 承租的变更协议
	delContractTenant2.preUpdate();
	contractTenantDao.delete(delContractTenant2);
	List<Tenant> list2 = agreementChange.getTenantList();// 承租人
	if (null != list2 && list2.size() > 0) {
	    for (Tenant tenant : list2) {
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.preInsert();
		contractTenant.setTenantId(tenant.getId());
		contractTenant.setLeasagremChangeId(id);// 承租的变更协议
		contractTenantDao.insert(contractTenant);
	    }
	}
    }

    /**
     * 入住人信息
     */
    @Transactional(readOnly = true)
    public List<Tenant> findLiveTenant(AgreementChange agreementChange) {
	List<Tenant> tenantList = new ArrayList<Tenant>();
	ContractTenant contractTenant = new ContractTenant();
	contractTenant.setAgreementChangeId(agreementChange.getId());
	List<ContractTenant> list = contractTenantDao.findList(contractTenant);
	for (ContractTenant tmpContractTenant : list) {
	    Tenant tenant = tenantDao.get(tmpContractTenant.getTenantId());
	    tenantList.add(tenant);
	}
	return tenantList;
    }

    /**
     * 承租人信息
     */
    @Transactional(readOnly = true)
    public List<Tenant> findTenant(AgreementChange agreementChange) {
	List<Tenant> tenantList = new ArrayList<Tenant>();
	ContractTenant contractTenant = new ContractTenant();
	contractTenant.setLeasagremChangeId(agreementChange.getId());
	List<ContractTenant> list = contractTenantDao.findList(contractTenant);
	for (ContractTenant tmpContractTenant : list) {
	    Tenant tenant = tenantDao.get(tmpContractTenant.getTenantId());
	    tenantList.add(tenant);
	}
	return tenantList;
    }

    @Transactional(readOnly = false)
    public void audit(AuditHis auditHis) {
	AgreementChange agreementChange = get(auditHis.getObjectId());
	AuditHis saveAuditHis = new AuditHis();
	saveAuditHis.setObjectType("3");// 变更协议
	saveAuditHis.setObjectId(auditHis.getObjectId());
	saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
	saveAuditHis.setAuditStatus(auditHis.getAuditStatus());// 1:通过 2:拒绝
	saveAuditHis.setAuditTime(new Date());
	saveAuditHis.setAuditUser(UserUtils.getUser().getId());
	auditHisService.save(saveAuditHis);

	if ("1".equals(auditHis.getAuditStatus())) {
	    // 审核
	    Audit audit = new Audit();
	    audit.setObjectId(auditHis.getObjectId());
	    audit.setNextRole("");
	    audit.preUpdate();
	    auditDao.update(audit);

	    /* 合同变更协议租客关联信息 */
	    ContractTenant delContractTenant = new ContractTenant();
	    delContractTenant.setAgreementChangeId(agreementChange.getId());// 入住的变更协议
	    delContractTenant.preUpdate();
	    contractTenantDao.delete(delContractTenant);
	    List<Tenant> list = agreementChange.getLiveList();// 入住人
	    if (null != list && list.size() > 0) {
		for (Tenant tenant : list) {
		    ContractTenant contractTenant = new ContractTenant();
		    contractTenant.preInsert();
		    contractTenant.setTenantId(tenant.getId());
		    contractTenant.setAgreementChangeId(agreementChange.getId());// 入住的变更协议
		    contractTenantDao.insert(contractTenant);
		}
	    }

	    ContractTenant delContractTenant2 = new ContractTenant();
	    delContractTenant2.setLeasagremChangeId(agreementChange.getId());// 承租的变更协议
	    delContractTenant2.preUpdate();
	    contractTenantDao.delete(delContractTenant2);
	    List<Tenant> list2 = agreementChange.getTenantList();// 承租人
	    if (null != list2 && list2.size() > 0) {
		for (Tenant tenant : list2) {
		    ContractTenant contractTenant = new ContractTenant();
		    contractTenant.preInsert();
		    contractTenant.setTenantId(tenant.getId());
		    contractTenant.setLeasagremChangeId(agreementChange.getId());// 承租的变更协议
		    contractTenantDao.insert(contractTenant);
		}
	    }
	}

	agreementChange.setAgreementStatus(auditHis.getAuditStatus());
	agreementChange.preUpdate();
	dao.update(agreementChange);
    }
}