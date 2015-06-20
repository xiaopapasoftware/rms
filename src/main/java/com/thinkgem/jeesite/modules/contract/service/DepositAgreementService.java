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
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 定金协议Service
 * @author huangsc
 * @version 2015-06-09
 */
@Service
@Transactional(readOnly = true)
public class DepositAgreementService extends CrudService<DepositAgreementDao, DepositAgreement> {
	@Autowired
	private PaymentTransDao paymentTransDao;
	@Autowired
	private DepositAgreementDao depositAgreementDao;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private AuditHisDao auditHisDao;
	
	private static final String DEPOSIT_AGREEMENT_ROLE = "deposit_agreement_role";//定金协议审批
	
	public DepositAgreement get(String id) {
		return super.get(id);
	}
	
	public List<DepositAgreement> findList(DepositAgreement depositAgreement) {
		return super.findList(depositAgreement);
	}
	
	public Page<DepositAgreement> findPage(Page<DepositAgreement> page, DepositAgreement depositAgreement) {
		return super.findPage(page, depositAgreement);
	}
	
	@Transactional(readOnly = false)
	public void breakContract(DepositAgreement depositAgreement) {
		depositAgreement = depositAgreementDao.get(depositAgreement.getId());
		
		/*1.生成款项*/
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setId(IdGen.uuid());
		paymentTrans.setTradeType("1");//定金协议
		paymentTrans.setPaymentType("1");//定金违约金
		paymentTrans.setTransId(depositAgreement.getId());
		paymentTrans.setTradeDirection("1");//收款
		paymentTrans.setStartDate(new Date());
		paymentTrans.setExpiredDate(new Date());
		paymentTrans.setTradeAmount(depositAgreement.getDepositAmount());
		paymentTrans.setLastAmount(0D);
		paymentTrans.setTransAmount(depositAgreement.getDepositAmount());
		paymentTrans.setTransStatus("2");//完全到账登记
		paymentTrans.setCreateDate(new Date());
		paymentTrans.setCreateBy(UserUtils.getUser());
		paymentTrans.setUpdateDate(new Date());
		paymentTrans.setUpdateBy(UserUtils.getUser());
		paymentTrans.setDelFlag("0");
		paymentTransDao.insert(paymentTrans);
		
		/*2.更新定金协议*/
		depositAgreement.setAgreementBusiStatus("1");//已转违约
		depositAgreement.setUpdateDate(new Date());
		depositAgreement.setUpdateBy(UserUtils.getUser());
		depositAgreementDao.update(depositAgreement);
		
		//TODO:
		/*3.更新房屋/房间状态*/
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
			
			DepositAgreement depositAgreement = depositAgreementDao.get(auditHis.getObjectId());
			depositAgreement.setAgreementStatus("1".equals(auditHis.getAuditStatus())?"3":auditHis.getAuditStatus());//2:内容审核拒绝 3:内容审核通过到账收据待审核
			depositAgreement.setUpdateDate(new Date());
			depositAgreement.setUpdateBy(UserUtils.getUser());
			depositAgreementDao.update(depositAgreement);
		}
	}
	
	@Transactional(readOnly = false)
	public void save(DepositAgreement depositAgreement) {
		depositAgreement.setAgreementStatus("0");//录入完成到账收据待登记
		depositAgreement.setAgreementBusiStatus("0");//待转合同
		
		String id = super.saveAndReturnId(depositAgreement);
		//生成款项
		PaymentTrans delPaymentTrans = new PaymentTrans();
		delPaymentTrans.setTransId(id);
		paymentTransDao.delete(delPaymentTrans);
		
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setId(IdGen.uuid());
		paymentTrans.setTradeType("1");//定金协议
		paymentTrans.setPaymentType("0");//应收定金
		paymentTrans.setTransId(id);
		paymentTrans.setTradeDirection("1");//收款
		paymentTrans.setStartDate(depositAgreement.getStartDate());
		paymentTrans.setExpiredDate(depositAgreement.getExpiredDate());
		paymentTrans.setTradeAmount(depositAgreement.getDepositAmount());
		paymentTrans.setLastAmount(depositAgreement.getDepositAmount());
		paymentTrans.setTransAmount(0D);
		paymentTrans.setTransStatus("0");//未到账登记
		paymentTrans.setCreateDate(new Date());
		paymentTrans.setCreateBy(UserUtils.getUser());
		paymentTrans.setUpdateDate(new Date());
		paymentTrans.setUpdateBy(UserUtils.getUser());
		paymentTrans.setDelFlag("0");
		paymentTransDao.insert(paymentTrans);
		
		//审核
		Audit audit = new Audit();
		audit.setId(IdGen.uuid());
		audit.setObjectId(id);
		audit.setObjectType("1");//预约定金
		audit.setNextRole(DEPOSIT_AGREEMENT_ROLE);
		audit.setCreateDate(new Date());
		audit.setCreateBy(UserUtils.getUser());
		audit.setUpdateDate(new Date());
		audit.setUpdateBy(UserUtils.getUser());
		audit.setDelFlag("0");
		auditDao.insert(audit);
	}
	
	@Transactional(readOnly = false)
	public void delete(DepositAgreement depositAgreement) {
		super.delete(depositAgreement);
	}
	
}