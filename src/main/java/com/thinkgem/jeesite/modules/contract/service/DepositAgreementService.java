/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
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

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.FileType;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.dao.ReceiptDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.inventory.dao.HouseDao;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.person.dao.TenantDao;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 定金协议Service
 * 
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
    @Autowired
    private ContractTenantDao contractTenantDao;
    @Autowired
    private TenantDao tenantDao;
    @Autowired
    private HouseDao houseDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private TradingAccountsDao tradingAccountsDao;
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private ReceiptDao receiptDao;
    @Autowired
    private PaymentTradeDao paymentTradeDao;
    @Autowired
	private ContractBookService contractBookService;

    private static final String DEPOSIT_AGREEMENT_ROLE = "deposit_agreement_role";// 定金协议审批

    public DepositAgreement get(String id) {
	return super.get(id);
    }

    public List<DepositAgreement> findList(DepositAgreement depositAgreement) {
	return super.findList(depositAgreement);
    }

    public Page<DepositAgreement> findPage(Page<DepositAgreement> page, DepositAgreement depositAgreement) {
	return super.findPage(page, depositAgreement);
    }

    public List<Tenant> findTenant(DepositAgreement depositAgreement) {
	List<Tenant> tenantList = new ArrayList<Tenant>();
	ContractTenant contractTenant = new ContractTenant();
	contractTenant.setDepositAgreementId(depositAgreement.getId());
	List<ContractTenant> list = contractTenantDao.findList(contractTenant);
	for (ContractTenant tmpContractTenant : list) {
	    Tenant tenant = tenantDao.get(tmpContractTenant.getTenantId());
	    tenantList.add(tenant);
	}
	return tenantList;
    }

    @Transactional(readOnly = false)
    public void breakContract(DepositAgreement depositAgreement) {
	Double refundAmount = depositAgreement.getRefundAmount();
	depositAgreement = depositAgreementDao.get(depositAgreement.getId());
	if (refundAmount != null && refundAmount > 0) {
	    depositAgreement.setRefundAmount(refundAmount);
	}
	
	/* 1.生成款项--定金转违约退费 */
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.setId(IdGen.uuid());
	paymentTrans.setTradeType("2");// 定金转违约
	paymentTrans.setPaymentType("26");// '26'='定金转违约退费'
	paymentTrans.setTransId(depositAgreement.getId());
	paymentTrans.setTradeDirection("0");// 出款
	paymentTrans.setStartDate(new Date());
	paymentTrans.setExpiredDate(new Date());
	paymentTrans.setTradeAmount(depositAgreement.getRefundAmount());
	paymentTrans.setLastAmount(depositAgreement.getRefundAmount());
	paymentTrans.setTransAmount(0D);
	paymentTrans.setTransStatus("0");// 未到账登记
	paymentTrans.setCreateDate(new Date());
	paymentTrans.setCreateBy(UserUtils.getUser());
	paymentTrans.setUpdateDate(new Date());
	paymentTrans.setUpdateBy(UserUtils.getUser());
	paymentTrans.setDelFlag("0");
	if (null != depositAgreement.getRefundAmount() && depositAgreement.getRefundAmount() > 0)
	    paymentTransDao.insert(paymentTrans);

	/* 2.更新定金协议为“定金转违约到账待登记” */
	depositAgreement.setAgreementBusiStatus("3");// '3'='定金转违约到账待登记'
	depositAgreement.setUpdateDate(new Date());
	depositAgreement.setUpdateBy(UserUtils.getUser());
	depositAgreementDao.update(depositAgreement);

	/* 3.更新房屋/房间状态 */
	if ("0".equals(depositAgreement.getRentMode())) {// 整租
	    House house = houseDao.get(depositAgreement.getHouse().getId());
	    house.setHouseStatus("1");// 待出租可预订
	    house.setCreateBy(UserUtils.getUser());
	    house.setUpdateDate(new Date());
	    houseDao.update(house);
	} else {// 单间
	    Room room = roomDao.get(depositAgreement.getRoom().getId());
	    room.setRoomStatus("1");// 待出租可预订
	    room.setCreateBy(UserUtils.getUser());
	    room.setUpdateDate(new Date());
	    roomDao.update(room);
	}
    }

    @Transactional(readOnly = false)
    public void audit(AuditHis auditHis) {
	AuditHis saveAuditHis = new AuditHis();
	saveAuditHis.setId(IdGen.uuid());
	saveAuditHis.setObjectType("1");// 定金协议
	saveAuditHis.setObjectId(auditHis.getObjectId());
	saveAuditHis.setAuditMsg(auditHis.getAuditMsg());
	saveAuditHis.setAuditStatus(auditHis.getAuditStatus());// 1:通过 2:拒绝
	saveAuditHis.setCreateDate(new Date());
	saveAuditHis.setCreateBy(UserUtils.getUser());
	saveAuditHis.setUpdateDate(new Date());
	saveAuditHis.setUpdateBy(UserUtils.getUser());
	saveAuditHis.setAuditTime(new Date());
	saveAuditHis.setAuditUser(UserUtils.getUser().getId());
	saveAuditHis.setDelFlag("0");
	auditHisDao.insert(saveAuditHis);

	if ("1".equals(auditHis.getAuditStatus())) {
	    // 审核
	    Audit audit = new Audit();
	    audit.setObjectId(auditHis.getObjectId());
	    audit.setNextRole("");
	    audit.setUpdateDate(new Date());
	    audit.setUpdateBy(UserUtils.getUser());
	    auditDao.update(audit);
	} else {// 审核拒绝时，需要把房屋和房间的状态回滚到原先状态
	    /* 更新房屋/房间状态 */
	    DepositAgreement depositAgreement = this.depositAgreementDao.get(auditHis.getObjectId());
	    if ("0".equals(depositAgreement.getRentMode())) {// 整租
		House house = houseDao.get(depositAgreement.getHouse().getId());
		house.setHouseStatus("1");// 待出租可预订
		house.setUpdateBy(UserUtils.getUser());
		house.setUpdateDate(new Date());
		houseDao.update(house);
	    } else {// 单间
		Room room = roomDao.get(depositAgreement.getRoom().getId());
		room.setRoomStatus("1");// 待出租可预订
		room.setUpdateBy(UserUtils.getUser());
		room.setUpdateDate(new Date());
		roomDao.update(room);
		// 更新房屋状态
		House h = houseDao.get(room.getHouse().getId());
		if ("2".equals(h.getHouseStatus())) {// 如果房屋状态是“已预定”
		    h.setHouseStatus("1");// 待出租可预订
		    h.setUpdateBy(UserUtils.getUser());
		    h.setUpdateDate(new Date());
		    houseDao.update(h);
		}
	    }

	    // 删除生成的款项
	    PaymentTrans delPaymentTrans = new PaymentTrans();
	    delPaymentTrans.setTransId(auditHis.getObjectId());
	    paymentTransDao.delete(delPaymentTrans);

	    /* 删除账务交易 */
	    TradingAccounts tradingAccounts = new TradingAccounts();
	    tradingAccounts.setTradeId(auditHis.getObjectId());
	    tradingAccounts.setTradeStatus("0");// 待审核
	    List<TradingAccounts> list = tradingAccountsDao.findList(tradingAccounts);
	    if (null != list && list.size() > 0) {
		for (TradingAccounts tmpTradingAccounts : list) {
		    tmpTradingAccounts.setUpdateBy(UserUtils.getUser());
		    tmpTradingAccounts.setUpdateDate(new Date());
		    tmpTradingAccounts.setDelFlag("1");
		    tradingAccountsDao.delete(tradingAccounts);

		    /* 删除收据 */
		    Receipt receipt = new Receipt();
		    TradingAccounts delTradingAccounts = new TradingAccounts();
		    delTradingAccounts.setId(tmpTradingAccounts.getId());
		    receipt.setTradingAccounts(delTradingAccounts);
		    receipt.setUpdateBy(UserUtils.getUser());
		    receipt.setUpdateDate(new Date());
		    this.receiptDao.delete(receipt);
		}
	    }
	}

	DepositAgreement depositAgreement = depositAgreementDao.get(auditHis.getObjectId());
	depositAgreement.setAgreementStatus("1".equals(auditHis.getAuditStatus()) ? "3" : auditHis.getAuditStatus());// 2:内容审核拒绝
														     // 3:内容审核通过到账收据待审核
	depositAgreement.setUpdateDate(new Date());
	depositAgreement.setUpdateBy(UserUtils.getUser());
	depositAgreementDao.update(depositAgreement);
    }

    @Transactional(readOnly = false)
    public void save(DepositAgreement depositAgreement) {
	String id = super.saveAndReturnId(depositAgreement);

	if ("1".equals(depositAgreement.getValidatorFlag())) {// 正常保存，而非暂存,暂存为0
	    // 生成款项
	    PaymentTrans delPaymentTrans = new PaymentTrans();
	    delPaymentTrans.setTransId(id);
	    paymentTransDao.delete(delPaymentTrans);

	    TradingAccounts delTradingAccounts = new TradingAccounts();
	    delTradingAccounts.setTradeId(id);
	    List<TradingAccounts> list = tradingAccountsDao.findList(delTradingAccounts);

	    for (TradingAccounts dTradingAccounts : list) {
		// 删除已经上传的收据附件
		Attachment attachment3 = new Attachment();
		attachment3.setTradingAccountsId(dTradingAccounts.getId());
		attachmentDao.delete(attachment3);

		// 同时删除已经录入的收据记录
		Receipt r = new Receipt();
		r.setTradingAccounts(dTradingAccounts);
		receiptDao.delete(r);

		// 删除账务记录
		tradingAccountsDao.delete(dTradingAccounts);

		// 删除账务记录款项关联信息
		PaymentTrade delPaymentTrade = new PaymentTrade();
		delPaymentTrade.setTradeId(dTradingAccounts.getId());
		paymentTradeDao.delete(delPaymentTrade);
	    }

	    if (null != depositAgreement.getStartDate() && null != depositAgreement.getExpiredDate() && null != depositAgreement.getDepositAmount()) {
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setId(IdGen.uuid());
		paymentTrans.setTradeType("1");// 定金协议
		paymentTrans.setPaymentType("0");// 应收定金
		paymentTrans.setTransId(id);
		paymentTrans.setTradeDirection("1");// 收款
		paymentTrans.setStartDate(depositAgreement.getStartDate());
		paymentTrans.setExpiredDate(depositAgreement.getExpiredDate());
		paymentTrans.setTradeAmount(depositAgreement.getDepositAmount());
		paymentTrans.setLastAmount(depositAgreement.getDepositAmount());
		paymentTrans.setTransAmount(0D);
		paymentTrans.setTransStatus("0");// 未到账登记
		paymentTrans.setCreateDate(new Date());
		paymentTrans.setCreateBy(UserUtils.getUser());
		paymentTrans.setUpdateDate(new Date());
		paymentTrans.setUpdateBy(UserUtils.getUser());
		paymentTrans.setDelFlag("0");
		if (0 != depositAgreement.getDepositAmount())
		    paymentTransDao.insert(paymentTrans);
	    }

	    /* 更新房屋/房间状态 */
	    if ("0".equals(depositAgreement.getRentMode())) {// 整租
		House house = houseDao.get(depositAgreement.getHouse().getId());
		if (house != null) {
		    house.setHouseStatus("2");// 已预定
		    house.setCreateBy(UserUtils.getUser());
		    house.setUpdateDate(new Date());
		    houseDao.update(house);
		}
	    } else {// 单间
		if (null != depositAgreement.getRoom() && !StringUtils.isBlank(depositAgreement.getRoom().getId())) {
		    Room room = roomDao.get(depositAgreement.getRoom().getId());
		    if (room != null) {
			room.setRoomStatus("2");// 已预定
			room.setCreateBy(UserUtils.getUser());
			room.setUpdateDate(new Date());
			roomDao.update(room);
			// 同时更新该房间所属房屋的状态
			House h = houseDao.get(room.getHouse().getId());
			if ("1".equals(h.getHouseStatus())) {// 待出租可预订
			    Room queryRoom = new Room();
			    queryRoom.setHouse(h);
			    List<Room> roomsOfHouse = roomDao.findList(queryRoom);
			    if (CollectionUtils.isNotEmpty(roomsOfHouse)) {
				int depositCount = 0;// 预定或出租的数量
				for (Room depositedRoom : roomsOfHouse) {
				    if ("2".equals(depositedRoom.getRoomStatus()) || "3".equals(depositedRoom.getRoomStatus())) {// 房间已预定
					depositCount = depositCount + 1;
				    }
				}
				if (depositCount == roomsOfHouse.size()) {// 房屋内房间全部出租或预定，房屋状态更新为“已预定”
				    h.setHouseStatus("2");// 已预定
				    h.setCreateBy(UserUtils.getUser());
				    h.setUpdateDate(new Date());
				    houseDao.update(h);
				}
			    }
			}
		    }
		}
	    }

	    // 审核
	    Audit audit = new Audit();
	    audit.setId(IdGen.uuid());
	    audit.setObjectId(id);
	    auditDao.delete(audit);
	    audit.setObjectType("1");// 预约定金
	    audit.setNextRole(DEPOSIT_AGREEMENT_ROLE);
	    audit.setCreateDate(new Date());
	    audit.setCreateBy(UserUtils.getUser());
	    audit.setUpdateDate(new Date());
	    audit.setUpdateBy(UserUtils.getUser());
	    audit.setDelFlag("0");
	    auditDao.insert(audit);

	    /* 更新房屋/房间状态 */
	    if ("0".equals(depositAgreement.getRentMode())) {// 整租
		House house = houseDao.get(depositAgreement.getHouse().getId());
		house.setHouseStatus("2");// 已预定
		house.setCreateBy(UserUtils.getUser());
		house.setUpdateDate(new Date());
		houseDao.update(house);
	    } else {// 单间
		if (null != depositAgreement.getRoom() && !StringUtils.isBlank(depositAgreement.getRoom().getId())) {
		    Room room = roomDao.get(depositAgreement.getRoom().getId());
		    room.setRoomStatus("2");// 已预定
		    room.setCreateBy(UserUtils.getUser());
		    room.setUpdateDate(new Date());
		    roomDao.update(room);
		    // 同时更新该房间所属房屋的状态
		    House h = houseDao.get(room.getHouse().getId());
		    if ("1".equals(h.getHouseStatus())) {// 待出租可预订
			Room queryRoom = new Room();
			queryRoom.setHouse(h);
			List<Room> roomsOfHouse = roomDao.findList(queryRoom);
			if (CollectionUtils.isNotEmpty(roomsOfHouse)) {
			    int depositCount = 0;// 预定或出租的数量
			    for (Room depositedRoom : roomsOfHouse) {
				if ("2".equals(depositedRoom.getRoomStatus()) || "3".equals(depositedRoom.getRoomStatus())) {// 房间已预定
				    depositCount = depositCount + 1;
				}
			    }
			    if (depositCount == roomsOfHouse.size()) {// 房屋内房间全部出租或预定，房屋状态更新为“已预定”
				h.setHouseStatus("2");// 已预定
				h.setCreateBy(UserUtils.getUser());
				h.setUpdateDate(new Date());
				houseDao.update(h);
			    }
			}
		    }
		}
	    }
	    
	    /*APP来的数据还是暂存状态???*/
	    if("2".equals(depositAgreement.getDataSource())) {
	    	//TODO:
	    }
	}

	if (null != depositAgreement.getTenantList() && depositAgreement.getTenantList().size() > 0) {
	    /* 合同租客关联信息 */
	    ContractTenant delTenant = new ContractTenant();
	    delTenant.setDepositAgreementId(id);
	    contractTenantDao.delete(delTenant);
	    List<Tenant> list = depositAgreement.getTenantList();
	    if (null != list && list.size() > 0) {
		for (Tenant tenant : list) {
		    ContractTenant contractTenant = new ContractTenant();
		    contractTenant.setId(IdGen.uuid());
		    contractTenant.setTenantId(tenant.getId());
		    contractTenant.setDepositAgreementId(id);
		    contractTenant.setCreateDate(new Date());
		    contractTenant.setCreateBy(UserUtils.getUser());
		    contractTenant.setUpdateDate(new Date());
		    contractTenant.setUpdateBy(UserUtils.getUser());
		    contractTenant.setDelFlag("0");
		    contractTenantDao.insert(contractTenant);
		}
	    }
	}

	if (!depositAgreement.getIsNewRecord()) {// 新增清空定金协议所有的附件信息
	    Attachment attachment = new Attachment();
	    attachment.setDepositAgreemId(depositAgreement.getId());
	    attachmentDao.delete(attachment);
	}

	if (!StringUtils.isBlank(depositAgreement.getDepositAgreementFile())) {
	    Attachment attachment = new Attachment();
	    attachment.setId(IdGen.uuid());
	    attachment.setDepositAgreemId(depositAgreement.getId());
	    attachment.setAttachmentType(FileType.DEPOSITAGREEMENT_FILE.getValue());
	    attachment.setAttachmentPath(depositAgreement.getDepositAgreementFile());
	    attachment.setCreateDate(new Date());
	    attachment.setCreateBy(UserUtils.getUser());
	    attachment.setUpdateDate(new Date());
	    attachment.setUpdateBy(UserUtils.getUser());
	    attachment.setDelFlag("0");
	    attachmentDao.insert(attachment);
	}

	if (!StringUtils.isBlank(depositAgreement.getDepositCustomerIDFile())) {
	    Attachment attachment = new Attachment();
	    attachment.setId(IdGen.uuid());
	    attachment.setDepositAgreemId(depositAgreement.getId());
	    attachment.setAttachmentType(FileType.TENANT_ID.getValue());
	    attachment.setAttachmentPath(depositAgreement.getDepositCustomerIDFile());
	    attachment.setCreateDate(new Date());
	    attachment.setCreateBy(UserUtils.getUser());
	    attachment.setUpdateDate(new Date());
	    attachment.setUpdateBy(UserUtils.getUser());
	    attachment.setDelFlag("0");
	    attachmentDao.insert(attachment);
	}

	if (!StringUtils.isBlank(depositAgreement.getDepositOtherFile())) {
	    Attachment attachment = new Attachment();
	    attachment.setId(IdGen.uuid());
	    attachment.setDepositAgreemId(depositAgreement.getId());
	    attachment.setAttachmentType(FileType.DEPOSITRECEIPT_FILE_OTHER.getValue());
	    attachment.setAttachmentPath(depositAgreement.getDepositOtherFile());
	    attachment.setCreateDate(new Date());
	    attachment.setCreateBy(UserUtils.getUser());
	    attachment.setUpdateDate(new Date());
	    attachment.setUpdateBy(UserUtils.getUser());
	    attachment.setDelFlag("0");
	    attachmentDao.insert(attachment);
	}
    }

    @Transactional(readOnly = false)
    public void delete(DepositAgreement depositAgreement) {
	super.delete(depositAgreement);
    }

    @Transactional(readOnly = true)
    public List<DepositAgreement> findAllValidAgreements() {
	return depositAgreementDao.findAllList(new DepositAgreement());
    }

    @Transactional(readOnly = true)
    public Integer getTotalValidDACounts() {
	return depositAgreementDao.getTotalValidDACounts(new DepositAgreement());
    }
    
    public DepositAgreement getByHouseId(DepositAgreement depositAgreement) {
    	return this.depositAgreementDao.getByHouseId(depositAgreement);
    }
}