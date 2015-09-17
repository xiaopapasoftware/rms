/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.dao.AccountingDao;
import com.thinkgem.jeesite.modules.contract.dao.AgreementChangeDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditDao;
import com.thinkgem.jeesite.modules.contract.dao.AuditHisDao;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.FileType;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTransDao;
import com.thinkgem.jeesite.modules.funds.dao.ReceiptDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.inventory.dao.HouseDao;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.person.dao.TenantDao;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 出租合同Service
 * 
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
    @Autowired
    private AgreementChangeDao agreementChangeDao;
    @Autowired
    private HouseDao houseDao;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private DepositAgreementDao depositAgreementDao;
    @Autowired
    private TradingAccountsDao tradingAccountsDao;
    @Autowired
    private ReceiptDao receiptDao;
    @Autowired
    private PaymentTransService paymentTransService;
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private PaymentTradeDao paymentTradeDao;

    private static final String RENT_CONTRACT_ROLE = "rent_contract_role";// 新签合同审批
    private static final String CHANGE_AGREEMENT_ROLE = "change_agreement_role";// 变更协议审批

    public RentContract get(String id) {
	return super.get(id);
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

	    if ("2".equals(auditHis.getType())) {// 特殊退租生成款项
		Accounting accounting = new Accounting();
		accounting.setRentContractId(auditHis.getObjectId());
		accounting.setAccountingType("3");// 特殊退租核算
		accounting.setDelFlag("0");
		List<Accounting> list = accountingDao.findList(accounting);

		for (Accounting tmpAccounting : list) {
		    PaymentTrans paymentTrans = new PaymentTrans();
		    paymentTrans.setId(IdGen.uuid());
		    paymentTrans.setTradeType("9");// 特殊退租
		    paymentTrans.setPaymentType(tmpAccounting.getFeeType());
		    paymentTrans.setTransId(auditHis.getObjectId());
		    paymentTrans.setTradeDirection(tmpAccounting.getFeeDirection());
		    paymentTrans.setStartDate(tmpAccounting.getFeeDate());
		    paymentTrans.setExpiredDate(tmpAccounting.getFeeDate());
		    paymentTrans.setTradeAmount(tmpAccounting.getFeeAmount());
		    paymentTrans.setLastAmount(tmpAccounting.getFeeAmount());
		    paymentTrans.setTransAmount(0D);
		    paymentTrans.setTransStatus("0");// 未到账登记
		    paymentTrans.setCreateDate(new Date());
		    paymentTrans.setCreateBy(UserUtils.getUser());
		    paymentTrans.setUpdateDate(new Date());
		    paymentTrans.setUpdateBy(UserUtils.getUser());
		    paymentTrans.setDelFlag("0");
		    if (0 != tmpAccounting.getFeeAmount())
			paymentTransDao.insert(paymentTrans);
		}
	    }
	} else {// 审核失败的时候，需要把房屋状态回滚到原先状态
	    /* 更新房屋/房间状态 */
	    RentContract rentContract = this.rentContractDao.get(auditHis.getObjectId());
	    if ("0".equals(rentContract.getRentMode())) {// 整租
		House house = houseDao.get(rentContract.getHouse().getId());
		house.setHouseStatus("1");// 待出租可预订
		house.setCreateBy(UserUtils.getUser());
		house.setUpdateDate(new Date());
		houseDao.update(house);
	    } else {// 单间
		Room room = roomDao.get(rentContract.getRoom().getId());
		if (null != room) {
		    room.setRoomStatus("1");// 待出租可预订
		    room.setCreateBy(UserUtils.getUser());
		    room.setUpdateDate(new Date());
		    roomDao.update(room);
		}
		// 更新房屋的状态
		House h = houseDao.get(room.getHouse().getId());
		Room queryRoom = new Room();
		queryRoom.setHouse(h);
		List<Room> roomsOfHouse = roomDao.findList(queryRoom);
		if (CollectionUtils.isNotEmpty(roomsOfHouse)) {
		    int rentedRoomCount = 0;
		    for (Room rentedRoom : roomsOfHouse) {
			if ("3".equals(rentedRoom.getRoomStatus())) {// 房间已出租
			    rentedRoomCount = rentedRoomCount + 1;
			}
		    }
		    String updatedHouseSts = "";
		    if (rentedRoomCount > 0) {
			updatedHouseSts = "3";// 房屋为部分出租状态
		    } else if (rentedRoomCount == 0) {
			updatedHouseSts = "1";// 房屋为待出租可预订
		    }
		    h.setHouseStatus(updatedHouseSts);
		    h.setUpdateBy(UserUtils.getUser());
		    h.setUpdateDate(new Date());
		    houseDao.update(h);
		}
	    }

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

	RentContract rentContract = rentContractDao.get(auditHis.getObjectId());
	if ("2".equals(auditHis.getType())) {
	    rentContract.setContractBusiStatus("1".equals(auditHis.getAuditStatus()) ? "10" : "18");// 10:特殊退租待结算
												    // 18:特殊退租内容审核拒绝
	} else {
	    rentContract.setContractStatus("1".equals(auditHis.getAuditStatus()) ? "4" : "3");// 4:内容审核通过到账收据待审核
											      // 3:内容审核拒绝
	}
	rentContract.setUpdateDate(new Date());
	rentContract.setUpdateBy(UserUtils.getUser());
	rentContractDao.update(rentContract);
    }

    public List<Tenant> findTenant(RentContract rentContract) {
	List<Tenant> tenantList = new ArrayList<Tenant>();
	ContractTenant contractTenant = new ContractTenant();
	contractTenant.setLeaseContractId(rentContract.getId());
	List<ContractTenant> list = contractTenantDao.findList(contractTenant);
	for (ContractTenant tmpContractTenant : list) {
	    Tenant tenant = tenantDao.get(tmpContractTenant.getTenantId());
	    tenantList.add(tenant);
	}
	return tenantList;
    }

    public List<Tenant> findLiveTenant(RentContract rentContract) {
	List<Tenant> tenantList = new ArrayList<Tenant>();
	ContractTenant contractTenant = new ContractTenant();
	contractTenant.setContractId(rentContract.getId());
	List<ContractTenant> list = contractTenantDao.findList(contractTenant);
	for (ContractTenant tmpContractTenant : list) {
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

    public Page<RentContract> findContractList(Page<RentContract> page, RentContract rentContract) {
	rentContract.setPage(page);
	page.setList(dao.findContractList(rentContract));
	return page;
    }

    /**
     * 正常退租
     */
    @Transactional(readOnly = false)
    public void returnContract(RentContract rentContract) {
	rentContract = rentContractDao.get(rentContract.getId());
	rentContract.setContractBusiStatus("2");// 正常退租待核算
	rentContract.setUpdateBy(UserUtils.getUser());
	rentContract.setUpdateDate(new Date());
	this.rentContractDao.update(rentContract);

	// 退租后更改房屋/房间状态
	changeHouseOrRoomStatusByReturn(rentContract);
    }

    /**
     * 提前退租
     */
    @Transactional(readOnly = false)
    public void earylReturnContract(RentContract rentContract) {
	rentContract = rentContractDao.get(rentContract.getId());
	rentContract.setContractBusiStatus("1");// 提前退租待核算
	rentContract.setUpdateBy(UserUtils.getUser());
	rentContract.setUpdateDate(new Date());
	this.rentContractDao.update(rentContract);

	// 退租后更改房屋/房间状态
	changeHouseOrRoomStatusByReturn(rentContract);

	/* 删除未到账款项 */
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.setTransId(rentContract.getId());
	paymentTrans.setTransStatus("0");// 未到账登记
	paymentTrans.setDelFlag("0");
	paymentTransService.delete(paymentTrans);
    }

    /**
     * 逾期退租
     */
    @Transactional(readOnly = false)
    public void lateReturnContract(RentContract rentContract) {
	rentContract = rentContractDao.get(rentContract.getId());
	rentContract.setContractBusiStatus("3");// 逾期退租待核算
	rentContract.setUpdateBy(UserUtils.getUser());
	rentContract.setUpdateDate(new Date());
	this.rentContractDao.update(rentContract);

	// 退租后更改房屋/房间状态
	changeHouseOrRoomStatusByReturn(rentContract);
    }

    @Transactional(readOnly = true)
    public List<RentContract> findAllValidRentContracts() {
	return rentContractDao.findAllList(new RentContract());
    }

    /**
     * 正常退租核算
     */
    @Transactional(readOnly = false)
    public void returnCheck(RentContract rentContract, String tradeType) {
	String breakDown = rentContract.getBreakDown();
	String returnRemark = rentContract.getReturnRemark();
	List<Accounting> accountList = rentContract.getAccountList();
	List<Accounting> outAccountList = rentContract.getOutAccountList();

	rentContract = rentContractDao.get(rentContract.getId());
	if (!"9".equals(tradeType))
	    rentContract.setContractBusiStatus("4");// 退租核算完成到账收据待登记
	else
	    rentContract.setContractBusiStatus("17");// 特殊退租内容待审核
	rentContract.setUpdateBy(UserUtils.getUser());
	rentContract.setUpdateDate(new Date());
	rentContract.setReturnRemark(returnRemark);
	this.rentContractDao.update(rentContract);

	/* 款项 */
	Accounting delAccounting = new Accounting();
	delAccounting.setRentContract(rentContract);
	delAccounting.setDelFlag("0");
	if ("7".equals(tradeType))
	    delAccounting.setAccountingType("1");// 正常退租核算
	else if ("6".equals(tradeType))
	    delAccounting.setAccountingType("0");// 提前退租核算
	else if ("8".equals(tradeType))
	    delAccounting.setAccountingType("2");// 逾期退租核算
	else if ("9".equals(tradeType))
	    delAccounting.setAccountingType("3");// 特殊退租核算
	delAccounting.setUpdateDate(new Date());
	delAccounting.setUpdateBy(UserUtils.getUser());
	accountingDao.delByRent(delAccounting);

	for (Accounting accounting : accountList) {
	    if (accounting != null && accounting.getFeeAmount() != null) {
		if (!"9".equals(tradeType)) {// 特殊退租不生成款项
		    if (accounting.getFeeAmount() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType(accounting.getFeeType());
			paymentTrans.setTransId(rentContract.getId());
			paymentTrans.setTradeDirection("1");// 收款
			paymentTrans.setStartDate(rentContract.getStartDate());
			paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(accounting.getFeeAmount());
			paymentTrans.setLastAmount(accounting.getFeeAmount());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			paymentTransDao.insert(paymentTrans);
		    }
		}

		/* 核算记录 */
		accounting.setId(IdGen.uuid());
		accounting.setRentContract(rentContract);
		if ("7".equals(tradeType))
		    accounting.setAccountingType("1");// 正常退租核算
		else if ("6".equals(tradeType))
		    accounting.setAccountingType("0");// 提前退租核算
		else if ("8".equals(tradeType))
		    accounting.setAccountingType("2");// 逾期退租核算
		else if ("9".equals(tradeType))
		    accounting.setAccountingType("3");// 特殊退租核算
		accounting.setFeeDirection("1");// 应收
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

	for (Accounting accounting : outAccountList) {
	    if (accounting != null && accounting.getFeeAmount() != null) {
		if (!"9".equals(tradeType)) {// 特殊退租不生成款项
		    if (accounting.getFeeAmount() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType(accounting.getFeeType());
			paymentTrans.setTransId(rentContract.getId());
			paymentTrans.setTradeDirection("0");// 出款
			paymentTrans.setStartDate(rentContract.getStartDate());
			paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(accounting.getFeeAmount());
			paymentTrans.setLastAmount(accounting.getFeeAmount());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			paymentTransDao.insert(paymentTrans);
		    }
		}

		/* 核算记录 */
		accounting.setId(IdGen.uuid());
		accounting.setRentContract(rentContract);
		if ("7".equals(tradeType))
		    accounting.setAccountingType("1");// 正常退租核算
		else if ("6".equals(tradeType))
		    accounting.setAccountingType("0");// 提前退租核算
		else if ("8".equals(tradeType))
		    accounting.setAccountingType("2");// 逾期退租核算
		else if ("9".equals(tradeType))
		    accounting.setAccountingType("3");// 特殊退租核算
		accounting.setFeeDirection("0");// 应出
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

	/* 更新房屋/房间状态 */
	if ("0".equals(rentContract.getRentMode())) {// 整租
	    if ("1".equals(breakDown)) {
		House house = houseDao.get(rentContract.getHouse().getId());
		house.setHouseStatus("6");// 已损坏
		house.setCreateBy(UserUtils.getUser());
		house.setUpdateDate(new Date());
		houseDao.update(house);
	    }
	} else {// 单间
	    Room room = roomDao.get(rentContract.getRoom().getId());
	    if (null != room) {
		if ("1".equals(breakDown)) {
		    room.setRoomStatus("5");// 已损坏
		    room.setCreateBy(UserUtils.getUser());
		    room.setUpdateDate(new Date());
		    roomDao.update(room);
		}
	    }
	}
    }

    @Transactional(readOnly = false)
    public void saveAdditional(AgreementChange agreementChange) {
	String id = IdGen.uuid();

	agreementChange.setAgreementStatus("0");// 待审核
	agreementChange.setCreateDate(new Date());
	agreementChange.setCreateBy(UserUtils.getUser());
	agreementChange.setUpdateDate(new Date());
	agreementChange.setUpdateBy(UserUtils.getUser());
	agreementChange.setDelFlag("0");

	/* 合同租客关联信息 */
	ContractTenant delContractTenant = new ContractTenant();
	delContractTenant.setLeasagremChangeId(id);// 承租的 变更协议
	contractTenantDao.delete(delContractTenant);
	List<Tenant> list = agreementChange.getTenantList();// 承租人
	if (null != list && list.size() > 0) {
	    for (Tenant tenant : list) {
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.setId(IdGen.uuid());
		contractTenant.setTenantId(tenant.getId());
		contractTenant.setLeasagremChangeId(id);
		contractTenant.setCreateDate(new Date());
		contractTenant.setCreateBy(UserUtils.getUser());
		contractTenant.setUpdateDate(new Date());
		contractTenant.setUpdateBy(UserUtils.getUser());
		contractTenant.setDelFlag("0");
		contractTenantDao.insert(contractTenant);
	    }
	}

	ContractTenant delContractTenant2 = new ContractTenant();
	delContractTenant2.setAgreementChangeId(id);// 入住的 变更协议
	contractTenantDao.delete(delContractTenant2);
	list = agreementChange.getLiveList();// 入住人
	if (null != list && list.size() > 0) {
	    for (Tenant tenant : list) {
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.setId(IdGen.uuid());
		contractTenant.setTenantId(tenant.getId());
		contractTenant.setAgreementChangeId(id);
		contractTenant.setCreateDate(new Date());
		contractTenant.setCreateBy(UserUtils.getUser());
		contractTenant.setUpdateDate(new Date());
		contractTenant.setUpdateBy(UserUtils.getUser());
		contractTenant.setDelFlag("0");
		contractTenantDao.insert(contractTenant);
	    }
	}

	agreementChange.setId(id);
	agreementChange.setAgreementStatus("0");// 待审核
	agreementChange.setRentContract(rentContractDao.get(agreementChange.getContractId()));
	agreementChangeDao.insert(agreementChange);

	// 审核
	Audit audit = new Audit();
	audit.setId(IdGen.uuid());
	audit.setObjectId(id);
	audit.setObjectType("0");// 变更协议
	audit.setNextRole(CHANGE_AGREEMENT_ROLE);
	audit.setCreateDate(new Date());
	audit.setCreateBy(UserUtils.getUser());
	audit.setUpdateDate(new Date());
	audit.setUpdateBy(UserUtils.getUser());
	audit.setDelFlag("0");
	auditDao.insert(audit);
    }

    @Transactional(readOnly = false)
    public void save(RentContract rentContract) {
	String id = super.saveAndReturnId(rentContract);
	if ("1".equals(rentContract.getValidatorFlag())) {// 正常保存，而非暂存

	    PaymentTrans delPaymentTrans = new PaymentTrans();// 款项
	    delPaymentTrans.setTransId(id);
	    paymentTransDao.delete(delPaymentTrans);

	    TradingAccounts delTradingAccounts = new TradingAccounts();
	    delTradingAccounts.setTradeId(id);
	    List<TradingAccounts> list = tradingAccountsDao.findList(delTradingAccounts);
	    for (TradingAccounts dTradingAccounts : list) {
		this.tradingAccountsDao.delete(dTradingAccounts);
		PaymentTrade delPaymentTrade = new PaymentTrade();
		delPaymentTrade.setTradeId(dTradingAccounts.getId());
		this.paymentTradeDao.delete(delPaymentTrade);
	    }

	    String tradeType = "";// 交易类型
	    if ("0".equals(rentContract.getSignType()) || StringUtils.isEmpty(rentContract.getSignType())) {
		tradeType = "3";// 新签合同
	    } else if ("1".equals(rentContract.getSignType())) {
		tradeType = "4";// 正常人工续签
	    } else if ("2".equals(rentContract.getSignType())) {
		tradeType = "5";// 逾期自动续签
	    }

	    // 新签合同、正常人工续签合同时，才需要生成水电费押金/水电押金差额
	    if ("3".equals(tradeType) || "4".equals(tradeType)) {
		genDepositElectricPayTrans(tradeType, id, rentContract);
	    }

	    // 新签合同、正常人工续签合同时，才需要生成房租押金/房租押金差额
	    if ("3".equals(tradeType) || "4".equals(tradeType)) {
		genDepositAmountPayTrans(tradeType, id, rentContract);
	    }

	    // 合同日期间隔月数，带小数
	    double monthCountDiff = DateUtils.getMonthSpace(rentContract.getStartDate(), rentContract.getExpiredDate());
	    logger.info("monthDiff : " + monthCountDiff);

	    // 生成合同期内所有的房租款项
	    genContractRentalPayTrans(tradeType, id, rentContract, monthCountDiff);

	    // 生成合同期内所有的费用款项
	    genContractFeesPayTrans(tradeType, id, rentContract, monthCountDiff);

	    // 审核
	    Audit audit = new Audit();
	    audit.setId(IdGen.uuid());
	    audit.setObjectId(id);
	    auditDao.delete(audit);
	    audit.setNextRole(RENT_CONTRACT_ROLE);
	    audit.setCreateDate(new Date());
	    audit.setCreateBy(UserUtils.getUser());
	    audit.setUpdateDate(new Date());
	    audit.setUpdateBy(UserUtils.getUser());
	    audit.setDelFlag("0");
	    auditDao.insert(audit);

	    /* 更新房屋/房间状态 */
	    if ("0".equals(rentContract.getRentMode())) {// 整租
		House house = houseDao.get(rentContract.getHouse().getId());
		house.setHouseStatus("4");// 完全出租
		house.setUpdateBy(UserUtils.getUser());
		house.setUpdateDate(new Date());
		houseDao.update(house);
	    } else {// 单间
		Room room = roomDao.get(rentContract.getRoom().getId());
		if (null != room) {
		    room.setRoomStatus("3");// 已出租
		    room.setUpdateBy(UserUtils.getUser());
		    room.setUpdateDate(new Date());
		    roomDao.update(room);
		}

		// 同时更新该房间所属房屋的状态
		if (room != null && room.getHouse() != null) {
		    House h = houseDao.get(room.getHouse().getId());
		    Room queryRoom = new Room();
		    queryRoom.setHouse(h);
		    List<Room> roomsOfHouse = roomDao.findList(queryRoom);
		    if (CollectionUtils.isNotEmpty(roomsOfHouse)) {
			int rentedRoomCount = 0;
			for (Room rentedRoom : roomsOfHouse) {
			    if ("3".equals(rentedRoom.getRoomStatus())) {// 房间已出租
				rentedRoomCount = rentedRoomCount + 1;
			    }
			}
			String updatedHouseSts = "";
			if (rentedRoomCount < roomsOfHouse.size()) {
			    updatedHouseSts = "3";// 房屋为部分出租状态
			} else if (rentedRoomCount == roomsOfHouse.size()) {
			    updatedHouseSts = "4";// 房屋为完全出租
			}
			h.setHouseStatus(updatedHouseSts);
			h.setUpdateBy(UserUtils.getUser());
			h.setUpdateDate(new Date());
			houseDao.update(h);
		    }
		}
	    }
	}

	/* 合同租客关联信息 */
	ContractTenant delContractTenant = new ContractTenant();
	delContractTenant.setLeaseContractId(id);
	contractTenantDao.delete(delContractTenant);
	List<Tenant> list = rentContract.getTenantList();// 承租人
	if (null != list && list.size() > 0) {
	    for (Tenant tenant : list) {
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

	ContractTenant delContractTenant2 = new ContractTenant();
	delContractTenant2.setContractId(id);
	contractTenantDao.delete(delContractTenant2);
	list = rentContract.getLiveList();// 入住人
	if (null != list && list.size() > 0) {
	    for (Tenant tenant : list) {
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

	if ("1".equals(rentContract.getSaveSource())) {// 定金协议转合同
	    DepositAgreement depositAgreement = depositAgreementDao.get(rentContract.getAgreementId());
	    depositAgreement.setAgreementBusiStatus("2");// 已转合同
	    depositAgreement.setUpdateBy(UserUtils.getUser());
	    depositAgreement.setUpdateDate(new Date());
	    depositAgreementDao.update(depositAgreement);
	}

	/* 更改原合同 */
	if ("1".equals(rentContract.getSignType())) {
	    RentContract rentContractOld = this.rentContractDao.get(rentContract.getContractId());
	    rentContractOld.setContractBusiStatus("14");// 正常人工续签
	    rentContractOld.setUpdateBy(UserUtils.getUser());
	    rentContractOld.setUpdateDate(new Date());
	    rentContractDao.update(rentContractOld);
	}
	if ("2".equals(rentContract.getSignType())) {
	    RentContract rentContractOld = this.rentContractDao.get(rentContract.getContractId());
	    rentContractOld.setContractBusiStatus("15");// 逾期自动续签
	    rentContractOld.setUpdateBy(UserUtils.getUser());
	    rentContractOld.setUpdateDate(new Date());
	    rentContractDao.update(rentContractOld);
	}

	// 非新增，首先清空所有的合同附件
	if (!rentContract.getIsNewRecord()) {
	    Attachment attachment = new Attachment();
	    attachment.setRentContractId(rentContract.getId());
	    attachmentDao.delete(attachment);
	}
	// 出租合同文件
	if (!StringUtils.isBlank(rentContract.getRentContractFile())) {
	    Attachment attachment = new Attachment();
	    attachment.setId(IdGen.uuid());
	    attachment.setRentContractId(rentContract.getId());
	    attachment.setAttachmentType(FileType.RENTCONTRACT_FILE.getValue());
	    attachment.setAttachmentPath(rentContract.getRentContractFile());
	    attachment.setCreateDate(new Date());
	    attachment.setCreateBy(UserUtils.getUser());
	    attachment.setUpdateDate(new Date());
	    attachment.setUpdateBy(UserUtils.getUser());
	    attachment.setDelFlag("0");
	    attachmentDao.insert(attachment);
	}
	// 租客身份证
	if (!StringUtils.isBlank(rentContract.getRentContractCusIDFile())) {
	    Attachment attachment = new Attachment();
	    attachment.setId(IdGen.uuid());
	    attachment.setRentContractId(rentContract.getId());
	    attachment.setAttachmentType(FileType.TENANT_ID.getValue());
	    attachment.setAttachmentPath(rentContract.getRentContractCusIDFile());
	    attachment.setCreateDate(new Date());
	    attachment.setCreateBy(UserUtils.getUser());
	    attachment.setUpdateDate(new Date());
	    attachment.setUpdateBy(UserUtils.getUser());
	    attachment.setDelFlag("0");
	    attachmentDao.insert(attachment);
	}
	// 出租合同其他收据
	if (!StringUtils.isBlank(rentContract.getRentContractOtherFile())) {
	    Attachment attachment = new Attachment();
	    attachment.setId(IdGen.uuid());
	    attachment.setRentContractId(rentContract.getId());
	    attachment.setAttachmentType(FileType.RENTCONTRACT_FILE_OTHER.getValue());
	    attachment.setAttachmentPath(rentContract.getRentContractOtherFile());
	    attachment.setCreateDate(new Date());
	    attachment.setCreateBy(UserUtils.getUser());
	    attachment.setUpdateDate(new Date());
	    attachment.setUpdateBy(UserUtils.getUser());
	    attachment.setDelFlag("0");
	    attachmentDao.insert(attachment);
	}
    }

    @Transactional(readOnly = false)
    public void delete(RentContract rentContract) {
	super.delete(rentContract);
    }

    /**
     * 生成水电费押金/水电费押金差额 款项
     */
    private void genDepositElectricPayTrans(String tradeType, String transObjId, RentContract rentContract) {
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.setId(IdGen.uuid());
	paymentTrans.setTradeType(tradeType);
	paymentTrans.setTradeAmount(rentContract.getDepositElectricAmount());// 应该交易金额
	paymentTrans.setLastAmount(rentContract.getDepositElectricAmount());// 剩余交易金额
	paymentTrans.setTransAmount(0D);// 实际交易金额
	paymentTrans.setTransStatus("0");// 未到账登记
	if ("3".equals(tradeType)) {// 新签合同
	    paymentTrans.setPaymentType("2");// 水电费押金
	}
	if ("4".equals(tradeType)) {// 续签合同
	    paymentTrans.setPaymentType("3");// 续补水电费押金
	}
	paymentTrans.setTransId(transObjId);
	paymentTrans.setTradeDirection("1");// 收款
	paymentTrans.setStartDate(rentContract.getStartDate());
	paymentTrans.setExpiredDate(rentContract.getExpiredDate());
	paymentTrans.setCreateDate(new Date());
	paymentTrans.setCreateBy(UserUtils.getUser());
	paymentTrans.setUpdateDate(new Date());
	paymentTrans.setUpdateBy(UserUtils.getUser());
	paymentTrans.setDelFlag("0");
	if (rentContract.getDepositElectricAmount() > 0) {
	    paymentTransDao.insert(paymentTrans);
	}
    }

    /**
     * 生成房租押金/房租押金差额款项
     */
    private void genDepositAmountPayTrans(String tradeType, String transObjId, RentContract rentContract) {
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.setId(IdGen.uuid());
	paymentTrans.setTradeType(tradeType);
	paymentTrans.setTransId(transObjId);
	paymentTrans.setTradeDirection("1");// 收款
	paymentTrans.setStartDate(rentContract.getStartDate());
	paymentTrans.setExpiredDate(rentContract.getExpiredDate());
	paymentTrans.setTradeAmount(rentContract.getDepositAmount());
	paymentTrans.setLastAmount(rentContract.getDepositAmount());
	paymentTrans.setTransAmount(0D);
	paymentTrans.setTransStatus("0");// 未到账登记
	if ("3".equals(tradeType)) {// 新签合同
	    paymentTrans.setPaymentType("4");// 房租押金
	}
	if ("4".equals(tradeType)) {// 续签合同
	    paymentTrans.setPaymentType("5");// 续补房租押金
	}
	paymentTrans.setCreateDate(new Date());
	paymentTrans.setCreateBy(UserUtils.getUser());
	paymentTrans.setUpdateDate(new Date());
	paymentTrans.setUpdateBy(UserUtils.getUser());
	paymentTrans.setDelFlag("0");
	if (paymentTrans.getTradeAmount() > 0) {
	    paymentTransDao.insert(paymentTrans);
	}
    }

    /**
     * 生成合同期内的所有房租款项
     */
    private void genContractRentalPayTrans(String tradeType, String transObjId, RentContract rentContract, double monthCountDiff) {

	Date startD = rentContract.getStartDate();// 开始日期

	boolean depositTransContractFlag = false;
	/*
	 * Double depositAgreementAmount =
	 * rentContract.getDepositAgreementAmount();// 已经缴纳的定金金额，只适用于定金转合同的业务场景
	 * if (depositAgreementAmount != null && depositAgreementAmount > 0) {//
	 * 定金转合同 depositTransContractFlag = true; }
	 */
	Double depositAgreementAmount = 0d;
	if (!StringUtils.isBlank(rentContract.getAgreementId())) {
	    DepositAgreement depositAgreement = this.depositAgreementDao.get(rentContract.getAgreementId());
	    depositAgreementAmount = depositAgreement.getDepositAmount();
	    depositTransContractFlag = true;
	}

	// 先生成整数房租款项列表
	int intergeredMonthCounts = (int) monthCountDiff;// 合同期间间隔的月份整数个数
	if (intergeredMonthCounts > 0) {
	    for (int i = 0; i < intergeredMonthCounts; i++) {
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setId(IdGen.uuid());
		paymentTrans.setTradeType(tradeType);
		paymentTrans.setPaymentType("6");// 房租金额
		paymentTrans.setTransId(transObjId);
		paymentTrans.setTradeDirection("1");// 收款
		paymentTrans.setTradeAmount(rentContract.getRental());
		paymentTrans.setStartDate(startD);
		paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
		if (depositTransContractFlag) {// 定金转合同
		    if (depositAgreementAmount >= rentContract.getRental()) {
			paymentTrans.setLastAmount(0D);
			paymentTrans.setTransAmount(rentContract.getRental());
			paymentTrans.setTransStatus("2");// 完全到账登记
			depositAgreementAmount = depositAgreementAmount - rentContract.getRental();
		    } else if (depositAgreementAmount > 0 && depositAgreementAmount < rentContract.getRental()) {
			paymentTrans.setLastAmount(rentContract.getRental() - depositAgreementAmount);
			paymentTrans.setTransAmount(depositAgreementAmount);
			paymentTrans.setTransStatus("1");// 部分到账登记
			depositAgreementAmount = 0d;
		    } else {
			paymentTrans.setLastAmount(rentContract.getRental());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
		    }
		} else {// 正常保存，无定金
		    paymentTrans.setLastAmount(rentContract.getRental());
		    paymentTrans.setTransAmount(0D);
		    paymentTrans.setTransStatus("0");// 未到账登记
		}
		paymentTrans.setCreateDate(new Date());
		paymentTrans.setCreateBy(UserUtils.getUser());
		paymentTrans.setUpdateDate(new Date());
		paymentTrans.setUpdateBy(UserUtils.getUser());
		paymentTrans.setDelFlag("0");
		if (paymentTrans.getTradeAmount() > 0) {
		    paymentTransDao.insert(paymentTrans);

		    if ("2".equals(paymentTrans.getTransStatus())) {
			// 生成账务交易
			TradingAccounts tradingAccounts = new TradingAccounts();
			tradingAccounts.setId(IdGen.uuid());
			tradingAccounts.setTradeId(transObjId);
			tradingAccounts.setTradeType("3");// 新签合同
			tradingAccounts.setTradeDirection("1");// 入账
			tradingAccounts.setTradeAmount(paymentTrans.getTransAmount());

			List<Tenant> tenants = rentContract.getTenantList();
			if (CollectionUtils.isNotEmpty(tenants)) {
			    String tenantId = tenants.get(0).getId();
			    Tenant tenant = tenantDao.get(tenantId);
			    tradingAccounts.setPayeeName(tenant.getTenantName());
			    String tenantType = tenant.getTenantType();// 租客类型
			    if ("0".equals(tenantType)) {// 个人租客
				tradingAccounts.setPayeeType("1");// 交易人类型为“个人”
			    }
			    if ("1".equals(tenantType)) {// 企业租客
				tradingAccounts.setPayeeType("0");// 交易人类型为“单位”
			    }
			}

			tradingAccounts.setTradeStatus("0");// 待审核
			tradingAccounts.setCreateDate(new Date());
			tradingAccounts.setCreateBy(UserUtils.getUser());
			tradingAccounts.setUpdateDate(new Date());
			tradingAccounts.setUpdateBy(UserUtils.getUser());
			tradingAccounts.setDelFlag("0");
			tradingAccountsDao.insert(tradingAccounts);

			PaymentTrade paymentTrade = new PaymentTrade();
			paymentTrade.setTradeId(tradingAccounts.getId());
			paymentTrade.setTransId(paymentTrans.getId());
			paymentTrade.setId(IdGen.uuid());
			paymentTrade.setCreateDate(new Date());
			paymentTrade.setCreateBy(UserUtils.getUser());
			paymentTrade.setUpdateDate(new Date());
			paymentTrade.setUpdateBy(UserUtils.getUser());
			paymentTrade.setDelFlag("0");
			paymentTradeDao.insert(paymentTrade);
		    }
		}
		startD = DateUtils.dateAddMonth(startD, 1);
	    }
	}

	// 生成零头的房租款项
	BigDecimal doubledMonthCounts = new BigDecimal(monthCountDiff - intergeredMonthCounts).setScale(2, BigDecimal.ROUND_HALF_UP);// 合同期间间隔的月份零头数

	// 特殊预先处理
	if (doubledMonthCounts.compareTo(BigDecimal.ZERO) > 0 && doubledMonthCounts.compareTo(new BigDecimal(1)) < 0) {
	    doubledMonthCounts = specialProcess(doubledMonthCounts, rentContract.getStartDate(), rentContract.getExpiredDate());
	}

	if (doubledMonthCounts.compareTo(BigDecimal.ZERO) > 0 && doubledMonthCounts.compareTo(new BigDecimal(1)) < 0) {
	    PaymentTrans paymentTrans = new PaymentTrans();
	    paymentTrans.setId(IdGen.uuid());
	    paymentTrans.setTradeType(tradeType);
	    paymentTrans.setPaymentType("6");// 房租金额
	    paymentTrans.setTransId(transObjId);
	    paymentTrans.setTradeDirection("1");// 收款
	    paymentTrans.setTradeAmount(rentContract.getRental() * doubledMonthCounts.doubleValue());
	    paymentTrans.setStartDate(startD);
	    paymentTrans.setExpiredDate(rentContract.getExpiredDate());

	    if (depositTransContractFlag) {// 定金转合同
		if (depositAgreementAmount <= 0) {// 定金已经被分配完毕
		    paymentTrans.setLastAmount(rentContract.getRental() * doubledMonthCounts.doubleValue());
		    paymentTrans.setTransAmount(0D);
		    paymentTrans.setTransStatus("0");// 未到账登记
		} else {// 定金还有剩余
		    if (depositAgreementAmount >= rentContract.getRental() * doubledMonthCounts.doubleValue()) {
			paymentTrans.setLastAmount(0D);
			paymentTrans.setTransAmount(rentContract.getRental() * doubledMonthCounts.doubleValue());
			paymentTrans.setTransStatus("2");// 完全到账登记
			depositAgreementAmount = depositAgreementAmount - rentContract.getRental() * doubledMonthCounts.doubleValue();
		    } else {
			paymentTrans.setLastAmount(rentContract.getRental() * doubledMonthCounts.doubleValue() - depositAgreementAmount);
			paymentTrans.setTransAmount(depositAgreementAmount);
			paymentTrans.setTransStatus("1");// 部分到账登记
			depositAgreementAmount = 0d;
		    }
		}
	    } else {// 正常保存，无定金
		paymentTrans.setLastAmount(rentContract.getRental() * doubledMonthCounts.doubleValue());
		paymentTrans.setTransAmount(0D);
		paymentTrans.setTransStatus("0");// 未到账登记
	    }
	    paymentTrans.setCreateDate(new Date());
	    paymentTrans.setCreateBy(UserUtils.getUser());
	    paymentTrans.setUpdateDate(new Date());
	    paymentTrans.setUpdateBy(UserUtils.getUser());
	    paymentTrans.setDelFlag("0");
	    if (paymentTrans.getTradeAmount() > 0) {
		paymentTransDao.insert(paymentTrans);

		if ("2".equals(paymentTrans.getTransStatus())) {
		    // 生成账务交易
		    TradingAccounts tradingAccounts = new TradingAccounts();
		    tradingAccounts.setId(IdGen.uuid());
		    tradingAccounts.setTradeId(transObjId);
		    tradingAccounts.setTradeType("3");// 新签合同
		    tradingAccounts.setTradeDirection("1");// 入账
		    tradingAccounts.setTradeAmount(paymentTrans.getTransAmount());

		    List<Tenant> tenants = rentContract.getTenantList();
		    if (CollectionUtils.isNotEmpty(tenants)) {
			String tenantId = tenants.get(0).getId();
			Tenant tenant = tenantDao.get(tenantId);
			tradingAccounts.setPayeeName(tenant.getTenantName());
			String tenantType = tenant.getTenantType();// 租客类型
			if ("0".equals(tenantType)) {// 个人租客
			    tradingAccounts.setPayeeType("1");// 交易人类型为“个人”
			}
			if ("1".equals(tenantType)) {// 企业租客
			    tradingAccounts.setPayeeType("0");// 交易人类型为“单位”
			}
		    }

		    tradingAccounts.setTradeStatus("0");// 待审核
		    tradingAccounts.setCreateDate(new Date());
		    tradingAccounts.setCreateBy(UserUtils.getUser());
		    tradingAccounts.setUpdateDate(new Date());
		    tradingAccounts.setUpdateBy(UserUtils.getUser());
		    tradingAccounts.setDelFlag("0");
		    tradingAccountsDao.insert(tradingAccounts);

		    PaymentTrade paymentTrade = new PaymentTrade();
		    paymentTrade.setTradeId(tradingAccounts.getId());
		    paymentTrade.setTransId(paymentTrans.getId());
		    paymentTrade.setId(IdGen.uuid());
		    paymentTrade.setCreateDate(new Date());
		    paymentTrade.setCreateBy(UserUtils.getUser());
		    paymentTrade.setUpdateDate(new Date());
		    paymentTrade.setUpdateBy(UserUtils.getUser());
		    paymentTrade.setDelFlag("0");
		    paymentTradeDao.insert(paymentTrade);
		}
	    }
	}
    }

    /**
     * 生成合同期内的所有费用款项
     */
    private void genContractFeesPayTrans(String tradeType, String transObjId, RentContract rentContract, double monthCountDiff) {

	if ("0".equals(rentContract.getChargeType())) {// 预付

	    Date startD = rentContract.getStartDate();// 开始日期

	    // 先生成整数费用款项列表
	    int intergeredMonthCounts = (int) monthCountDiff;
	    if (intergeredMonthCounts > 0) {
		for (int i = 0; i < intergeredMonthCounts; i++) {

		    if (null != rentContract.getWaterFee() && rentContract.getWaterFee() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType("14");// 水费金额
			paymentTrans.setTransId(transObjId);
			paymentTrans.setTradeDirection("1");// 收款
			paymentTrans.setStartDate(startD);
			paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
			paymentTrans.setTradeAmount(rentContract.getWaterFee());
			paymentTrans.setLastAmount(rentContract.getWaterFee());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			if (paymentTrans.getTradeAmount() > 0) {
			    paymentTransDao.insert(paymentTrans);
			}
		    }

		    if ("1".equals(rentContract.getHasTv()) && null != rentContract.getTvFee() && rentContract.getTvFee() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType("18");// 有线电视费
			paymentTrans.setTransId(transObjId);
			paymentTrans.setTradeDirection("1");// 收款
			paymentTrans.setStartDate(startD);
			paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
			paymentTrans.setTradeAmount(rentContract.getTvFee());
			paymentTrans.setLastAmount(rentContract.getTvFee());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			if (paymentTrans.getTradeAmount() > 0) {
			    paymentTransDao.insert(paymentTrans);
			}
		    }

		    if ("1".equals(rentContract.getHasNet()) && null != rentContract.getNetFee() && rentContract.getNetFee() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType("20");// 宽带费
			paymentTrans.setTransId(transObjId);
			paymentTrans.setTradeDirection("1");// 收款
			paymentTrans.setStartDate(startD);
			paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
			paymentTrans.setTradeAmount(rentContract.getNetFee());
			paymentTrans.setLastAmount(rentContract.getNetFee());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			if (paymentTrans.getTradeAmount() > 0) {
			    paymentTransDao.insert(paymentTrans);
			}
		    }

		    if (null != rentContract.getServiceFee() && rentContract.getServiceFee() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.setId(IdGen.uuid());
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType("22");// 服务费
			paymentTrans.setTransId(transObjId);
			paymentTrans.setTradeDirection("1");// 收款
			paymentTrans.setStartDate(startD);
			paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
			paymentTrans.setTradeAmount(rentContract.getServiceFee() / 100 * rentContract.getRental());
			paymentTrans.setLastAmount(rentContract.getServiceFee() / 100 * rentContract.getRental());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			paymentTrans.setCreateDate(new Date());
			paymentTrans.setCreateBy(UserUtils.getUser());
			paymentTrans.setUpdateDate(new Date());
			paymentTrans.setUpdateBy(UserUtils.getUser());
			paymentTrans.setDelFlag("0");
			if (paymentTrans.getTradeAmount() > 0) {
			    paymentTransDao.insert(paymentTrans);
			}
		    }

		    startD = DateUtils.dateAddMonth(startD, 1);
		}
	    }

	    // 生成零头的费用款项
	    BigDecimal doubledMonthCounts = new BigDecimal(monthCountDiff - intergeredMonthCounts).setScale(2, BigDecimal.ROUND_HALF_UP);// 合同期间间隔的月份零头数

	    // 特殊预先处理
	    if (doubledMonthCounts.compareTo(BigDecimal.ZERO) > 0 && doubledMonthCounts.compareTo(new BigDecimal(1)) < 0) {
		doubledMonthCounts = specialProcess(doubledMonthCounts, rentContract.getStartDate(), rentContract.getExpiredDate());
	    }

	    if (doubledMonthCounts.compareTo(BigDecimal.ZERO) > 0 && doubledMonthCounts.compareTo(new BigDecimal(1)) < 0) {

		if (null != rentContract.getWaterFee() && rentContract.getWaterFee() > 0) {
		    PaymentTrans paymentTrans = new PaymentTrans();
		    paymentTrans.setId(IdGen.uuid());
		    paymentTrans.setTradeType(tradeType);
		    paymentTrans.setPaymentType("14");// 水费金额
		    paymentTrans.setTransId(transObjId);
		    paymentTrans.setTradeDirection("1");// 收款
		    paymentTrans.setStartDate(startD);
		    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
		    paymentTrans.setTradeAmount(rentContract.getWaterFee() * doubledMonthCounts.doubleValue());
		    paymentTrans.setLastAmount(rentContract.getWaterFee() * doubledMonthCounts.doubleValue());
		    paymentTrans.setTransAmount(0D);
		    paymentTrans.setTransStatus("0");// 未到账登记
		    paymentTrans.setCreateDate(new Date());
		    paymentTrans.setCreateBy(UserUtils.getUser());
		    paymentTrans.setUpdateDate(new Date());
		    paymentTrans.setUpdateBy(UserUtils.getUser());
		    paymentTrans.setDelFlag("0");
		    if (paymentTrans.getTradeAmount() > 0) {
			paymentTransDao.insert(paymentTrans);
		    }
		}

		if ("1".equals(rentContract.getHasTv()) && null != rentContract.getTvFee() && rentContract.getTvFee() > 0) {
		    PaymentTrans paymentTrans = new PaymentTrans();
		    paymentTrans.setId(IdGen.uuid());
		    paymentTrans.setTradeType(tradeType);
		    paymentTrans.setPaymentType("18");// 有线电视费
		    paymentTrans.setTransId(transObjId);
		    paymentTrans.setTradeDirection("1");// 收款
		    paymentTrans.setStartDate(startD);
		    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
		    paymentTrans.setTradeAmount(rentContract.getTvFee() * doubledMonthCounts.doubleValue());
		    paymentTrans.setLastAmount(rentContract.getTvFee() * doubledMonthCounts.doubleValue());
		    paymentTrans.setTransAmount(0D);
		    paymentTrans.setTransStatus("0");// 未到账登记
		    paymentTrans.setCreateDate(new Date());
		    paymentTrans.setCreateBy(UserUtils.getUser());
		    paymentTrans.setUpdateDate(new Date());
		    paymentTrans.setUpdateBy(UserUtils.getUser());
		    paymentTrans.setDelFlag("0");
		    if (paymentTrans.getTradeAmount() > 0) {
			paymentTransDao.insert(paymentTrans);
		    }
		}

		if ("1".equals(rentContract.getHasNet()) && null != rentContract.getNetFee() && rentContract.getNetFee() > 0) {
		    PaymentTrans paymentTrans = new PaymentTrans();
		    paymentTrans.setId(IdGen.uuid());
		    paymentTrans.setTradeType(tradeType);
		    paymentTrans.setPaymentType("20");// 宽带费
		    paymentTrans.setTransId(transObjId);
		    paymentTrans.setTradeDirection("1");// 收款
		    paymentTrans.setStartDate(startD);
		    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
		    paymentTrans.setTradeAmount(rentContract.getNetFee() * doubledMonthCounts.doubleValue());
		    paymentTrans.setLastAmount(rentContract.getNetFee() * doubledMonthCounts.doubleValue());
		    paymentTrans.setTransAmount(0D);
		    paymentTrans.setTransStatus("0");// 未到账登记
		    paymentTrans.setCreateDate(new Date());
		    paymentTrans.setCreateBy(UserUtils.getUser());
		    paymentTrans.setUpdateDate(new Date());
		    paymentTrans.setUpdateBy(UserUtils.getUser());
		    paymentTrans.setDelFlag("0");
		    if (paymentTrans.getTradeAmount() > 0) {
			paymentTransDao.insert(paymentTrans);
		    }
		}

		if (null != rentContract.getServiceFee() && rentContract.getServiceFee() > 0) {
		    PaymentTrans paymentTrans = new PaymentTrans();
		    paymentTrans.setId(IdGen.uuid());
		    paymentTrans.setTradeType(tradeType);
		    paymentTrans.setPaymentType("22");// 服务费
		    paymentTrans.setTransId(transObjId);
		    paymentTrans.setTradeDirection("1");// 收款
		    paymentTrans.setStartDate(startD);
		    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
		    paymentTrans.setTradeAmount((rentContract.getServiceFee() / 100 * rentContract.getRental()) * doubledMonthCounts.doubleValue());
		    paymentTrans.setLastAmount((rentContract.getServiceFee() / 100 * rentContract.getRental()) * doubledMonthCounts.doubleValue());
		    paymentTrans.setTransAmount(0D);
		    paymentTrans.setTransStatus("0");// 未到账登记
		    paymentTrans.setCreateDate(new Date());
		    paymentTrans.setCreateBy(UserUtils.getUser());
		    paymentTrans.setUpdateDate(new Date());
		    paymentTrans.setUpdateBy(UserUtils.getUser());
		    paymentTrans.setDelFlag("0");
		    if (paymentTrans.getTradeAmount() > 0) {
			paymentTransDao.insert(paymentTrans);
		    }
		}
	    }
	}
    }

    /**
     * 特殊处理月份间隔的小数部分 如果开始、结束日期都不含2月份，则误差范围在0.01 如果开始、结束日期 含有2月份，则误差范围在0.05
     */
    private BigDecimal specialProcess(BigDecimal doubledMonthCounts, Date startDate, Date expiredDate) {
	Calendar stratC = Calendar.getInstance();
	stratC.setTime(startDate);

	Calendar expiredC = Calendar.getInstance();
	expiredC.setTime(expiredDate);

	if (stratC.get(Calendar.MONTH) == 1 || expiredC.get(Calendar.MONTH) == 1) {// 含有2月份
	    if (doubledMonthCounts.compareTo(new BigDecimal(0.06)) < 0) {
		return BigDecimal.ZERO;
	    }
	}
	if (stratC.get(Calendar.MONTH) != 1 && expiredC.get(Calendar.MONTH) != 1) {// 都不含有2月份
	    if (doubledMonthCounts.compareTo(new BigDecimal(0.02)) < 0) {
		return BigDecimal.ZERO;
	    }
	}
	return doubledMonthCounts;
    }

    /**
     * 退租后更新房屋或者房间的状态
     */
    private void changeHouseOrRoomStatusByReturn(RentContract rentContract) {
	/* 更新房屋/房间状态 */
	if ("0".equals(rentContract.getRentMode())) {// 整租
	    House house = houseDao.get(rentContract.getHouse().getId());
	    if ("1".equals(rentContract.getBreakDown()))
		house.setHouseStatus("6");// 已损坏
	    else
		house.setHouseStatus("5");// 已退待租
	    house.setCreateBy(UserUtils.getUser());
	    house.setUpdateDate(new Date());
	    houseDao.update(house);
	} else {// 单间
	    Room room = roomDao.get(rentContract.getRoom().getId());
	    if ("1".equals(rentContract.getBreakDown()))
		room.setRoomStatus("5");// 已损坏
	    else
		room.setRoomStatus("4");// 已退租可预订
	    room.setCreateBy(UserUtils.getUser());
	    room.setUpdateDate(new Date());
	    roomDao.update(room);

	    if (room != null && room.getHouse() != null) {
		House h = houseDao.get(room.getHouse().getId());
		Room queryRoom = new Room();
		queryRoom.setHouse(h);
		List<Room> roomsOfHouse = roomDao.findList(queryRoom);
		if (CollectionUtils.isNotEmpty(roomsOfHouse)) {
		    int rentedRoomCount = 0;
		    for (Room rentedRoom : roomsOfHouse) {
			if ("3".equals(rentedRoom.getRoomStatus())) {// 房间已出租
			    rentedRoomCount = rentedRoomCount + 1;
			}
		    }
		    String updatedHouseSts = "";
		    if (0 == rentedRoomCount) {
			updatedHouseSts = "5";// 房屋为已退待租状态
		    } else if (rentedRoomCount < roomsOfHouse.size()) {
			updatedHouseSts = "3";// 房屋为部分出租状态
		    }
		    h.setHouseStatus(updatedHouseSts);
		    h.setUpdateBy(UserUtils.getUser());
		    h.setUpdateDate(new Date());
		    houseDao.update(h);
		}
	    }
	}
    }
}