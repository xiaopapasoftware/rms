/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.contract.dao.AgreementChangeDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 协议变更Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class AgreementChangeService extends CrudService<AgreementChangeDao, AgreementChange> {
	@Autowired
	private AuditHisDao auditHisDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private AgreementChangeDao agreementChangeDao;
	@Autowired
	private ContractTenantDao contractTenantDao;
	@Autowired
	private RentContractDao rentContractDao;

	public AgreementChange get(String id) {
		return super.get(id);
	}
	
	public List<AgreementChange> findList(AgreementChange agreementChange) {
		return super.findList(agreementChange);
	}
	
	public Page<AgreementChange> findPage(Page<AgreementChange> page, AgreementChange agreementChange) {
		return super.findPage(page, agreementChange);
	}
	
	@Transactional(readOnly = false)
	public void save(AgreementChange agreementChange) {
		super.save(agreementChange);
	}
	
	@Transactional(readOnly = false)
	public void delete(AgreementChange agreementChange) {
		super.delete(agreementChange);
	}
	
	@Transactional(readOnly = false)
	public void audit(AuditHis auditHis) {
		AgreementChange agreementChange = agreementChangeDao.get(auditHis.getObjectId());
		
		AuditHis saveAuditHis = new AuditHis();
		saveAuditHis.setId(IdGen.uuid());
		saveAuditHis.setObjectType("3");//变更协议
		saveAuditHis.setObjectId(auditHis.getObjectId());
		saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
		saveAuditHis.setAuditStatus(auditHis.getAuditStatus());//1:通过 2:拒绝
		saveAuditHis.setCreateDate(new Date());
		saveAuditHis.setCreateBy(UserUtils.getUser());
		saveAuditHis.setUpdateDate(new Date());
		saveAuditHis.setUpdateBy(UserUtils.getUser());
		saveAuditHis.setAuditTime(new Date());
		saveAuditHis.setAuditUser(UserUtils.getUser().getId());
		saveAuditHis.setDelFlag("0");
		auditHisDao.insert(saveAuditHis);
				
		if("1".equals(auditHis.getAuditStatus())) {
			//审核
			Audit audit = new Audit();
			audit.setObjectId(auditHis.getObjectId());
			audit.setNextRole("");
			audit.setUpdateDate(new Date());
			audit.setUpdateBy(UserUtils.getUser());
			auditDao.update(audit);
			
			/*合同租客关联信息*/
			ContractTenant delContractTenant = new ContractTenant();
			delContractTenant.setContractId(agreementChange.getContractId());
			contractTenantDao.delete(delContractTenant);
			List<Tenant> list = agreementChange.getTenantList();//承租人
			if(null != list && list.size()>0) {
				for(Tenant tenant : list) {
					ContractTenant contractTenant = new ContractTenant();
					contractTenant.setId(IdGen.uuid());
					contractTenant.setTenantId(tenant.getId());
					contractTenant.setLeaseContractId(agreementChange.getContractId());
					contractTenant.setCreateDate(new Date());
					contractTenant.setCreateBy(UserUtils.getUser());
					contractTenant.setUpdateDate(new Date());
					contractTenant.setUpdateBy(UserUtils.getUser());
					contractTenant.setDelFlag("0");
					contractTenantDao.insert(contractTenant);
				}
			}
			list = agreementChange.getLiveList();//入住人
			if(null != list && list.size()>0) {
				for(Tenant tenant : list) {
					ContractTenant contractTenant = new ContractTenant();
					contractTenant.setId(IdGen.uuid());
					contractTenant.setTenantId(tenant.getId());
					contractTenant.setContractId(agreementChange.getContractId());
					contractTenant.setCreateDate(new Date());
					contractTenant.setCreateBy(UserUtils.getUser());
					contractTenant.setUpdateDate(new Date());
					contractTenant.setUpdateBy(UserUtils.getUser());
					contractTenant.setDelFlag("0");
					contractTenantDao.insert(contractTenant);
				}
			}
		}
		
		agreementChange.setAgreementStatus(auditHis.getAuditStatus());
		agreementChange.setUpdateDate(new Date());
		agreementChange.setUpdateBy(UserUtils.getUser());
		agreementChangeDao.update(agreementChange);
		
		/*更新合同*/
		RentContract rentContract = rentContractDao.get(agreementChange.getRentContract().getId());
		rentContract.setRentMode(agreementChange.getRentMode());
		rentContract.setUpdateDate(new Date());
		rentContract.setUpdateBy(UserUtils.getUser());
		rentContractDao.update(rentContract);
	}
}