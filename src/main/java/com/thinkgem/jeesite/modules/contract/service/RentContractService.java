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

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.contract.dao.AccountingDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.person.dao.TenantDao;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 出租合同Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class RentContractService extends CrudService<RentContractDao, RentContract> {
	@Autowired
	private ContractTenantDao contractTenantDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private PaymentTransDao paymentTransDao;
	@Autowired
	private TenantDao tenantDao;
	@Autowired
	private AuditHisDao auditHisDao;
	@Autowired
	private RentContractDao rentContractDao;
	@Autowired
	private AccountingDao accountingDao;
	
	private static final String RENT_CONTRACT_ROLE = "rent_contract_role";//新签合同审批
	
	public RentContract get(String id) {
		return super.get(id);
	}
	
	@Transactional(readOnly = false)
	public void audit(AuditHis auditHis) {
		AuditHis saveAuditHis = new AuditHis();
		saveAuditHis.setId(IdGen.uuid());
		saveAuditHis.setObjectType("1");//定金协议
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
			
		}
		
		RentContract rentContract = rentContractDao.get(auditHis.getObjectId());
		rentContract.setContractStatus("1".equals(auditHis.getAuditStatus())?"4":"3");//4:内容审核通过到账收据待审核 3:内容审核拒绝
		rentContract.setUpdateDate(new Date());
		rentContract.setUpdateBy(UserUtils.getUser());
		rentContractDao.update(rentContract);
	}
	
	public List<Tenant> findTenant(RentContract rentContract) {
		List<Tenant> tenantList = new ArrayList<Tenant>();
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.setLeaseContractId(rentContract.getId());
		List<ContractTenant> list = contractTenantDao.findAllList(contractTenant);
		for(ContractTenant tmpContractTenant : list) {
			Tenant tenant = tenantDao.get(tmpContractTenant.getTenantId());
			tenantList.add(tenant);
		}
		return tenantList;
	}
	
	public List<Tenant> findLiveTenant(RentContract rentContract) {
		List<Tenant> tenantList = new ArrayList<Tenant>();
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.setContractId(rentContract.getId());
		List<ContractTenant> list = contractTenantDao.findAllList(contractTenant);
		for(ContractTenant tmpContractTenant : list) {
			Tenant tenant = tenantDao.get(tmpContractTenant.getTenantId());
			tenantList.add(tenant);
		}
		return tenantList;
	}
	
	public List<RentContract> findList(RentContract rentContract) {
		return super.findList(rentContract);
	}
	
	public Page<RentContract> findPage(Page<RentContract> page, RentContract rentContract) {
		return super.findPage(page, rentContract);
	}
	
	/**
	 * 正常退租
	 */
	@Transactional(readOnly = false)
	public void returnContract(RentContract rentContract) {
		rentContract = rentContractDao.get(rentContract.getId());
		rentContract.setContractBusiStatus("2");//正常退租待核算
		rentContract.setUpdateBy(UserUtils.getUser());
		rentContract.setUpdateDate(new Date());
		this.rentContractDao.update(rentContract);
	}
	
	/**
	 * 正常退租核算
	 */
	@Transactional(readOnly = false)
	public void returnCheck(RentContract rentContract) {
		List<Accounting> accountList = rentContract.getAccountList();
		List<Accounting> outAccountList = rentContract.getOutAccountList();
		
		rentContract = rentContractDao.get(rentContract.getId());
		rentContract.setContractBusiStatus("4");//退租核算完成到账收据待登记
		rentContract.setUpdateBy(UserUtils.getUser());
		rentContract.setUpdateDate(new Date());
		this.rentContractDao.update(rentContract);
		
		/*款项*/
		for(Accounting accounting : accountList) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType("7");//正常退租
			paymentTrans.setPaymentType(accounting.getFeeType());
			paymentTrans.setTransId(rentContract.getId());
			paymentTrans.setTradeDirection("1");//收款
			paymentTrans.setStartDate(rentContract.getExpiredDate());
			paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(accounting.getFeeAmount());
			paymentTrans.setLastAmount(accounting.getFeeAmount());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");//未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			paymentTransDao.insert(paymentTrans);
			
			/*核算记录*/
			accounting.setId(IdGen.uuid());
			accounting.setRentContract(rentContract);
			accounting.setAccountingType("1");//正常退租核算
			accounting.setFeeDirection("1");//应收
			accounting.setUser(UserUtils.getUser());
			accounting.setFeeDate(new Date());
			accounting.setCreateDate(new Date());
			accounting.setCreateBy(UserUtils.getUser());
			accounting.setUpdateDate(new Date());
			accounting.setUpdateBy(UserUtils.getUser());
			accounting.setDelFlag("0");
			accountingDao.insert(accounting);
		}
		
		for(Accounting accounting : outAccountList) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType("7");//正常退租
			paymentTrans.setPaymentType(accounting.getFeeType());
			paymentTrans.setTransId(rentContract.getId());
			paymentTrans.setTradeDirection("0");//出款
			paymentTrans.setStartDate(rentContract.getExpiredDate());
			paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(accounting.getFeeAmount());
			paymentTrans.setLastAmount(accounting.getFeeAmount());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");//未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			paymentTransDao.insert(paymentTrans);
			
			/*核算记录*/
			accounting.setId(IdGen.uuid());
			accounting.setRentContract(rentContract);
			accounting.setAccountingType("1");//正常退租核算
			accounting.setFeeDirection("0");//应出
			accounting.setUser(UserUtils.getUser());
			accounting.setFeeDate(new Date());
			accounting.setCreateDate(new Date());
			accounting.setCreateBy(UserUtils.getUser());
			accounting.setUpdateDate(new Date());
			accounting.setUpdateBy(UserUtils.getUser());
			accounting.setDelFlag("0");
			accountingDao.insert(accounting);
		}
	}
	
	@Transactional(readOnly = false)
	public void save(RentContract rentContract) {
		rentContract.setContractBusiStatus("0");//有效
		String id = super.saveAndReturnId(rentContract);
		
		if("1".equals(rentContract.getValidatorFlag())) {
			//款项
			PaymentTrans delPaymentTrans = new PaymentTrans();
			delPaymentTrans.setTransId(id);
			paymentTransDao.delete(delPaymentTrans);
			
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType("3");//新签合同
			paymentTrans.setPaymentType("2");//水电费押金
			paymentTrans.setTransId(id);
			paymentTrans.setTradeDirection("1");//收款
			paymentTrans.setStartDate(rentContract.getStartDate());
			paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(rentContract.getDepositElectricAmount());
			paymentTrans.setLastAmount(rentContract.getDepositElectricAmount());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");//未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			paymentTransDao.insert(paymentTrans);
			
			paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType("3");//新签合同
			paymentTrans.setPaymentType("4");//房租押金
			paymentTrans.setTransId(id);
			paymentTrans.setTradeDirection("1");//收款
			paymentTrans.setStartDate(rentContract.getStartDate());
			paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(rentContract.getDepositAmount()*rentContract.getDepositMonths());
			paymentTrans.setLastAmount(rentContract.getDepositAmount()*rentContract.getDepositMonths());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");//未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			paymentTransDao.insert(paymentTrans);
			
			/*生成合同期内所有的房租款项*/
			int month = DateUtils.getMonthSpace(rentContract.getStartDate(),rentContract.getExpiredDate());//合同总月数
			int transMonth = month-rentContract.getRenMonths();
			for(int i=0;i<transMonth;i++) {
				paymentTrans = new PaymentTrans();
				paymentTrans.setId(IdGen.uuid());
				paymentTrans.setTradeType("3");//新签合同
				paymentTrans.setPaymentType("6");//房租金额
				paymentTrans.setTransId(id);
				paymentTrans.setTradeDirection("1");//收款
				paymentTrans.setStartDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i+rentContract.getRenMonths()));
				paymentTrans.setExpiredDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i+1+rentContract.getRenMonths()));
				paymentTrans.setTradeAmount(rentContract.getRental());
				paymentTrans.setLastAmount(rentContract.getRental());
				paymentTrans.setTransAmount(0D);
				paymentTrans.setTransStatus("0");//未到账登记
				paymentTrans.setCreateDate(new Date());
				paymentTrans.setCreateBy(UserUtils.getUser());
				paymentTrans.setUpdateDate(new Date());
				paymentTrans.setUpdateBy(UserUtils.getUser());
				paymentTrans.setDelFlag("0");
				paymentTransDao.insert(paymentTrans);
			}
			
			/*押款项*/
			for(int i=0;i<rentContract.getRenMonths();i++) {
				paymentTrans = new PaymentTrans();
				paymentTrans.setId(IdGen.uuid());
				paymentTrans.setTradeType("3");//新签合同
				paymentTrans.setPaymentType("6");//房租金额
				paymentTrans.setTransId(id);
				paymentTrans.setTradeDirection("1");//收款
				paymentTrans.setStartDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i));
				paymentTrans.setExpiredDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i+1));
				paymentTrans.setTradeAmount(rentContract.getRental());
				paymentTrans.setLastAmount(rentContract.getRental());
				paymentTrans.setTransAmount(0D);
				paymentTrans.setTransStatus("0");//未到账登记
				paymentTrans.setCreateDate(new Date());
				paymentTrans.setCreateBy(UserUtils.getUser());
				paymentTrans.setUpdateDate(new Date());
				paymentTrans.setUpdateBy(UserUtils.getUser());
				paymentTrans.setDelFlag("0");
				paymentTransDao.insert(paymentTrans);
				
				if("0".equals(rentContract.getChargeType())) {//预付
					paymentTrans = new PaymentTrans();
					paymentTrans.setId(IdGen.uuid());
					paymentTrans.setTradeType("3");//新签合同
					paymentTrans.setPaymentType("14");//水费金额
					paymentTrans.setTransId(id);
					paymentTrans.setTradeDirection("1");//收款
					paymentTrans.setStartDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i));
					paymentTrans.setExpiredDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i+1));
					paymentTrans.setTradeAmount(rentContract.getWaterFee());
					paymentTrans.setLastAmount(rentContract.getWaterFee());
					paymentTrans.setTransAmount(0D);
					paymentTrans.setTransStatus("0");//未到账登记
					paymentTrans.setCreateDate(new Date());
					paymentTrans.setCreateBy(UserUtils.getUser());
					paymentTrans.setUpdateDate(new Date());
					paymentTrans.setUpdateBy(UserUtils.getUser());
					paymentTrans.setDelFlag("0");
					paymentTransDao.insert(paymentTrans);
					
					if("1".equals(rentContract.getHasTv())) {
						paymentTrans = new PaymentTrans();
						paymentTrans.setId(IdGen.uuid());
						paymentTrans.setTradeType("3");//新签合同
						paymentTrans.setPaymentType("18");//有线电视费
						paymentTrans.setTransId(id);
						paymentTrans.setTradeDirection("1");//收款
						paymentTrans.setStartDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i));
						paymentTrans.setExpiredDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i+1));
						paymentTrans.setTradeAmount(rentContract.getTvFee());
						paymentTrans.setLastAmount(rentContract.getTvFee());
						paymentTrans.setTransAmount(0D);
						paymentTrans.setTransStatus("0");//未到账登记
						paymentTrans.setCreateDate(new Date());
						paymentTrans.setCreateBy(UserUtils.getUser());
						paymentTrans.setUpdateDate(new Date());
						paymentTrans.setUpdateBy(UserUtils.getUser());
						paymentTrans.setDelFlag("0");
						paymentTransDao.insert(paymentTrans);
					}
					
					if("1".equals(rentContract.getHasNet())) {
						paymentTrans = new PaymentTrans();
						paymentTrans.setId(IdGen.uuid());
						paymentTrans.setTradeType("3");//新签合同
						paymentTrans.setPaymentType("20");//宽带费
						paymentTrans.setTransId(id);
						paymentTrans.setTradeDirection("1");//收款
						paymentTrans.setStartDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i));
						paymentTrans.setExpiredDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i+1));
						paymentTrans.setTradeAmount(rentContract.getNetFee());
						paymentTrans.setLastAmount(rentContract.getNetFee());
						paymentTrans.setTransAmount(0D);
						paymentTrans.setTransStatus("0");//未到账登记
						paymentTrans.setCreateDate(new Date());
						paymentTrans.setCreateBy(UserUtils.getUser());
						paymentTrans.setUpdateDate(new Date());
						paymentTrans.setUpdateBy(UserUtils.getUser());
						paymentTrans.setDelFlag("0");
						paymentTransDao.insert(paymentTrans);
					}
					
					if(null != rentContract.getServiceFee()) {
						paymentTrans = new PaymentTrans();
						paymentTrans.setId(IdGen.uuid());
						paymentTrans.setTradeType("3");//新签合同
						paymentTrans.setPaymentType("22");//服务费
						paymentTrans.setTransId(id);
						paymentTrans.setTradeDirection("1");//收款
						paymentTrans.setStartDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i));
						paymentTrans.setExpiredDate(DateUtils.dateAddMonth(rentContract.getStartDate(),i+1));
						paymentTrans.setTradeAmount(rentContract.getServiceFee()/100*rentContract.getRental());
						paymentTrans.setLastAmount(rentContract.getServiceFee()/100*rentContract.getRental());
						paymentTrans.setTransAmount(0D);
						paymentTrans.setTransStatus("0");//未到账登记
						paymentTrans.setCreateDate(new Date());
						paymentTrans.setCreateBy(UserUtils.getUser());
						paymentTrans.setUpdateDate(new Date());
						paymentTrans.setUpdateBy(UserUtils.getUser());
						paymentTrans.setDelFlag("0");
						paymentTransDao.insert(paymentTrans);
					}
				}
			}
			
			//审核
			Audit audit = new Audit();
			audit.setId(IdGen.uuid());
			audit.setObjectId(id);
			auditDao.delete(audit);
			audit.setObjectType("3");//新签合同
			audit.setNextRole(RENT_CONTRACT_ROLE);
			audit.setCreateDate(new Date());
			audit.setCreateBy(UserUtils.getUser());
			audit.setUpdateDate(new Date());
			audit.setUpdateBy(UserUtils.getUser());
			audit.setDelFlag("0");
			auditDao.insert(audit);
		}
		
		/*合同租客关联信息*/
		ContractTenant delContractTenant = new ContractTenant();
		delContractTenant.setTenantId(id);
		contractTenantDao.delete(delContractTenant);
		List<Tenant> list = rentContract.getTenantList();//承租人
		if(null != list && list.size()>0) {
			for(Tenant tenant : list) {
				ContractTenant contractTenant = new ContractTenant();
				contractTenant.setId(IdGen.uuid());
				contractTenant.setTenantId(tenant.getId());
				contractTenant.setLeaseContractId(id);
				contractTenant.setCreateDate(new Date());
				contractTenant.setCreateBy(UserUtils.getUser());
				contractTenant.setUpdateDate(new Date());
				contractTenant.setUpdateBy(UserUtils.getUser());
				contractTenant.setDelFlag("0");
				contractTenantDao.insert(contractTenant);
			}
		}
		list = rentContract.getLiveList();//入住人
		if(null != list && list.size()>0) {
			for(Tenant tenant : list) {
				ContractTenant contractTenant = new ContractTenant();
				contractTenant.setId(IdGen.uuid());
				contractTenant.setTenantId(tenant.getId());
				contractTenant.setContractId(id);
				contractTenant.setCreateDate(new Date());
				contractTenant.setCreateBy(UserUtils.getUser());
				contractTenant.setUpdateDate(new Date());
				contractTenant.setUpdateBy(UserUtils.getUser());
				contractTenant.setDelFlag("0");
				contractTenantDao.insert(contractTenant);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(RentContract rentContract) {
		super.delete(rentContract);
	}
	
}