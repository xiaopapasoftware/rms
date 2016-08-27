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
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
import com.thinkgem.jeesite.modules.contract.dao.AgreementChangeDao;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.Audit;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.AccountingTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.AuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.AuditTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractAuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractBusiStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
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
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.person.dao.TenantDao;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 出租合同Service
 * 
 * @author huangsc @version 2015-06-11
 * @author wangshujin @version 2016-08-22
 */
@Service
@Transactional(readOnly = true)
public class RentContractService extends CrudService<RentContractDao, RentContract> {
    @Autowired
    private ContractTenantDao contractTenantDao;
    @Autowired
    private AuditService auditService;
    @Autowired
    private PaymentTransDao paymentTransDao;
    @Autowired
    private TenantDao tenantDao;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private AgreementChangeDao agreementChangeDao;
    @Autowired
    private HouseDao houseDao;
    @Autowired
    private HouseService houseService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private DepositAgreementDao depositAgreementDao;
    @Autowired
    private TradingAccountsDao tradingAccountsDao;
    @Autowired
    private ReceiptDao receiptDao;
    @Autowired
    private PaymentTransService paymentTransService;
    @Autowired
    private AuditHisService auditHisService;
    @Autowired
    private AttachmentDao attachmentDao;
    @Autowired
    private PaymentTradeDao paymentTradeDao;

    public RentContract get(String id) {
	return dao.get(id);
    }

    @Transactional(readOnly = false)
    public void audit(AuditHis auditHis) {
	auditHisService.saveAuditHis(auditHis, AuditTypeEnum.RENT_CONTRACT_CONTENT.getValue());
	String rentContractId = auditHis.getObjectId();
	RentContract rentContract = dao.get(rentContractId);
	String actFlagFromView = auditHis.getType();// 2=特殊退租；
	if (AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus())) {
	    Audit audit = new Audit();
	    audit.setObjectId(rentContractId);
	    audit.setNextRole("");
	    auditService.update(audit);
	    if ("2".equals(actFlagFromView)) {// 特殊退租审核通过后直接生成款项
		Accounting accounting = new Accounting();
		accounting.setRentContractId(rentContractId);
		accounting.setAccountingType(AccountingTypeEnum.SPECIAL_RETURN_ACCOUNT.getValue());
		List<Accounting> list = accountingService.findList(accounting);
		if (CollectionUtils.isNotEmpty(list)) {
		    for (Accounting tmpAccounting : list) {
			Double feeAmt = tmpAccounting.getFeeAmount();
			if (feeAmt != null && feeAmt > 0D) {
			    String feeType = tmpAccounting.getFeeType();
			    Date expiredDate = null;
			    if (PaymentTransTypeEnum.WATER_ELECT_DEPOSIT.getValue().equals(feeType) || PaymentTransTypeEnum.RENT_DEPOSIT.getValue().equals(feeType)) {
				expiredDate = rentContract.getExpiredDate();
			    } else {
				expiredDate = tmpAccounting.getFeeDate();
			    }
			    paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.SPECIAL_RETURN_RENT.getValue(), feeType, rentContractId, tmpAccounting.getFeeDirection(), feeAmt, feeAmt, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(), rentContract.getStartDate(), expiredDate);
			}
		    }
		}
		paymentTransService.deleteNotSignPaymentTrans(rentContractId);// 删除未到账款项
		rentContract.setContractBusiStatus(ContractBusiStatusEnum.SPECAIL_RETURN_ACCOUNT.getValue());
	    } else {
		rentContract.setContractStatus(ContractAuditStatusEnum.INVOICE_TO_AUDITED.getValue());
	    }
	} else {// 审核拒绝
	    if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {
		House house = houseService.get(rentContract.getHouse().getId());
		houseService.cancelSign4WholeHouse(house);
	    } else {// 单间
		Room room = roomService.get(rentContract.getRoom().getId());
		houseService.cancelSign4SingleRoom(room);
	    }
	    paymentTransService.deletePaymentTransAndTradingAcctouns(rentContractId);// 删除相关款项
	    if ("2".equals(actFlagFromView)) {// 特殊退租审核
		rentContract.setContractBusiStatus(ContractBusiStatusEnum.SPECIAL_RENTURN_CONTENT_AUDIT_REFUSE.getValue());
	    } else {
		rentContract.setContractStatus(ContractAuditStatusEnum.CONTENT_AUDIT_REFUSE.getValue());
	    }
	}
	if (DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource())) {
	    rentContract.setUpdateUser(auditHis.getUpdateUser());
	} else {
	    rentContract.setUpdateUser(UserUtils.getUser().getId());
	}
	rentContract.preUpdate();
	dao.update(rentContract);
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

    public RentContract findContractByCode(String contractCode) {
	return dao.findContractByCode(contractCode);
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
	commonReturnRent(rentContract, ContractBusiStatusEnum.NORMAL_RETURN_ACCOUNT.getValue());
    }

    /**
     * 提前退租
     */
    @Transactional(readOnly = false)
    public void earylReturnContract(RentContract rentContract) {
	commonReturnRent(rentContract, ContractBusiStatusEnum.EARLY_RETURN_ACCOUNT.getValue());
	/* 删除未到账款项 */
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.setTransId(rentContract.getId());
	paymentTrans.setTransStatus("0");// 未到账登记
	paymentTrans.setDelFlag("0");
	paymentTrans.preUpdate();
	paymentTransService.delete(paymentTrans);
    }

    /**
     * 逾期退租
     */
    @Transactional(readOnly = false)
    public void lateReturnContract(RentContract rentContract) {
	commonReturnRent(rentContract, ContractBusiStatusEnum.LATE_RETURN_ACCOUNT.getValue());
    }

    @Transactional(readOnly = true)
    public List<RentContract> findAllValidRentContracts() {
	return dao.findAllList(new RentContract());
    }

    @Transactional(readOnly = true)
    public Integer getAllValidRentContractCounts() {
	return dao.getAllValidRentContractCounts(new RentContract());
    }

    /**
     * 退租核算页面，点击保存按钮
     */
    @Transactional(readOnly = false)
    public void returnCheck(RentContract rentContract, String tradeType) {
	String returnRemark = rentContract.getReturnRemark();
	List<Accounting> accountList = rentContract.getAccountList();
	List<Accounting> outAccountList = rentContract.getOutAccountList();
	rentContract = dao.get(rentContract.getId());
	if (!"9".equals(tradeType)) {// 特殊退租与其他退租类型不同，需要人工先进行审核
	    rentContract.setContractBusiStatus("4");// 退租核算完成到账收据待登记
	} else {
	    rentContract.setContractBusiStatus("17");// 特殊退租内容待审核
	}
	rentContract.preUpdate();
	rentContract.setReturnRemark(returnRemark);
	dao.update(rentContract);

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
	accountingService.delByRent(delAccounting);
	generatePaymentTransAndAccounts(accountList, rentContract, tradeType, "1");// 收款
	generatePaymentTransAndAccounts(outAccountList, rentContract, tradeType, "0");// 出款
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
	delContractTenant.preUpdate();
	contractTenantDao.delete(delContractTenant);
	List<Tenant> list = agreementChange.getTenantList();// 承租人
	if (null != list && list.size() > 0) {
	    for (Tenant tenant : list) {
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.preInsert();
		contractTenant.setTenantId(tenant.getId());
		contractTenant.setLeasagremChangeId(id);
		contractTenantDao.insert(contractTenant);
	    }
	}

	ContractTenant delContractTenant2 = new ContractTenant();
	delContractTenant2.setAgreementChangeId(id);// 入住的 变更协议
	delContractTenant2.preUpdate();
	contractTenantDao.delete(delContractTenant2);
	list = agreementChange.getLiveList();// 入住人
	if (null != list && list.size() > 0) {
	    for (Tenant tenant : list) {
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.preInsert();
		contractTenant.setTenantId(tenant.getId());
		contractTenant.setAgreementChangeId(id);
		contractTenantDao.insert(contractTenant);
	    }
	}

	agreementChange.setId(id);
	agreementChange.setAgreementStatus("0");// 待审核
	agreementChange.setRentContract(dao.get(agreementChange.getContractId()));
	agreementChangeDao.insert(agreementChange);

	auditService.insert(AuditTypeEnum.RENT_CONTRACT_CHANGE.getValue(), "change_agreement_role", id, "");
    }

    @Transactional(readOnly = false)
    public void save(RentContract rentContract) {
	String id = super.saveAndReturnId(rentContract);
	if ("1".equals(rentContract.getValidatorFlag())) {// 正常保存，而非暂存
	    PaymentTrans delPaymentTrans = new PaymentTrans();// 款项
	    delPaymentTrans.setTransId(id);
	    delPaymentTrans.preUpdate();
	    paymentTransDao.delete(delPaymentTrans);

	    TradingAccounts delTradingAccounts = new TradingAccounts();
	    delTradingAccounts.setTradeId(id);
	    List<TradingAccounts> list = tradingAccountsDao.findList(delTradingAccounts);
	    for (TradingAccounts dTradingAccounts : list) {
		// 删除已经上传的收据附件
		Attachment attachment3 = new Attachment();
		attachment3.setTradingAccountsId(dTradingAccounts.getId());
		attachment3.preUpdate();
		attachmentDao.delete(attachment3);

		// 同时删除已经录入的收据记录
		Receipt r = new Receipt();
		r.setTradingAccounts(dTradingAccounts);
		r.preUpdate();
		receiptDao.delete(r);

		// 删除账务记录
		dTradingAccounts.preUpdate();
		tradingAccountsDao.delete(dTradingAccounts);

		// 删除账务记录款项关联信息
		PaymentTrade delPaymentTrade = new PaymentTrade();
		delPaymentTrade.setTradeId(dTradingAccounts.getId());
		delPaymentTrade.preUpdate();
		paymentTradeDao.delete(delPaymentTrade);
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

	    // 合同日期间隔月数
	    int monthCountDiff = DateUtils.getMonthSpace(rentContract.getStartDate(), rentContract.getExpiredDate());

	    // 生成合同期内所有的房租款项
	    genContractRentalPayTrans(tradeType, id, rentContract, monthCountDiff);

	    // 生成合同期内所有的费用款项
	    genContractFeesPayTrans(tradeType, id, rentContract, monthCountDiff);

	    /* 更新房屋/房间状态 */
	    if ("0".equals(rentContract.getRentMode())) {// 整租
		House house = houseDao.get(rentContract.getHouse().getId());
		if (house != null) {
		    house.setHouseStatus("4");// 完全出租
		    house.preUpdate();
		    houseDao.update(house);
		    // 同时把房间的状态都更新为“已出租”
		    Room parameterRoom = new Room();
		    parameterRoom.setHouse(house);
		    List<Room> rooms = roomService.findList(parameterRoom);
		    if (CollectionUtils.isNotEmpty(rooms)) {
			for (Room r : rooms) {
			    r.setRoomStatus("3");// 已出租
			    r.preUpdate();
			    roomService.update(r);
			}
		    }
		}
	    } else {// 单间
		Room room = roomService.get(rentContract.getRoom().getId());
		if (null != room) {
		    room.setRoomStatus("3");// 已出租
		    room.preUpdate();
		    roomService.update(room);
		}

		// 同时更新该房间所属房屋的状态
		if (room != null && room.getHouse() != null) {
		    House h = houseDao.get(room.getHouse().getId());
		    Room queryRoom = new Room();
		    queryRoom.setHouse(h);
		    List<Room> roomsOfHouse = roomService.findList(queryRoom);
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
			h.preUpdate();
			houseDao.update(h);
		    }
		}
	    }

	    auditService.delete(id);
	    auditService.insert(AuditTypeEnum.RENT_CONTRACT_CONTENT.getValue(), "rent_contract_role", id, "");
	}

	/* 合同承租人关联信息 */
	ContractTenant delContractTenant = new ContractTenant();
	delContractTenant.setLeaseContractId(id);
	delContractTenant.preUpdate();
	contractTenantDao.delete(delContractTenant);
	List<Tenant> list = rentContract.getTenantList();// 承租人
	if (null != list && list.size() > 0) {
	    for (Tenant tenant : list) {
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.preInsert();
		contractTenant.setTenantId(tenant.getId());
		contractTenant.setLeaseContractId(id);
		contractTenantDao.insert(contractTenant);
	    }
	}

	/* 合同入住人关联信息 */
	ContractTenant delContractTenant2 = new ContractTenant();
	delContractTenant2.setContractId(id);
	delContractTenant2.preUpdate();
	contractTenantDao.delete(delContractTenant2);
	list = rentContract.getLiveList();// 入住人
	if (null != list && list.size() > 0) {
	    for (Tenant tenant : list) {
		ContractTenant contractTenant = new ContractTenant();
		contractTenant.preInsert();
		contractTenant.setTenantId(tenant.getId());
		contractTenant.setContractId(id);
		contractTenantDao.insert(contractTenant);
	    }
	}

	if ("1".equals(rentContract.getSaveSource())) {// 定金协议转合同，更新原定金协议状态
	    DepositAgreement depositAgreement = depositAgreementDao.get(rentContract.getAgreementId());
	    depositAgreement.setAgreementBusiStatus("2");// 已转合同
	    depositAgreement.preUpdate();
	    depositAgreementDao.update(depositAgreement);
	}

	/* 当前合同为续签合同，则把原合同业务状态改成“正常人工续签” */
	if ("1".equals(rentContract.getSignType())) {
	    RentContract rentContractOld = dao.get(rentContract.getContractId());
	    rentContractOld.setContractBusiStatus("14");// 正常人工续签
	    rentContractOld.preUpdate();
	    dao.update(rentContractOld);
	}

	/* 当前合同为逾期自动续签，则把原合同业务状态改成“逾期自动续签” */
	if ("2".equals(rentContract.getSignType())) {
	    RentContract rentContractOld = dao.get(rentContract.getContractId());
	    rentContractOld.setContractBusiStatus("15");// 逾期自动续签
	    rentContractOld.preUpdate();
	    dao.update(rentContractOld);
	}

	// 非新增，修改合同，首先清空所有的合同附件
	if (!rentContract.getIsNewRecord()) {
	    Attachment attachment = new Attachment();
	    attachment.setRentContractId(rentContract.getId());
	    attachment.preUpdate();
	    attachmentDao.delete(attachment);
	}
	// 出租合同文件
	if (!StringUtils.isBlank(rentContract.getRentContractFile())) {
	    Attachment attachment = new Attachment();
	    attachment.preInsert();
	    attachment.setRentContractId(rentContract.getId());
	    attachment.setAttachmentType(FileType.RENTCONTRACT_FILE.getValue());
	    attachment.setAttachmentPath(rentContract.getRentContractFile());
	    attachmentDao.insert(attachment);
	}
	// 租客身份证
	if (!StringUtils.isBlank(rentContract.getRentContractCusIDFile())) {
	    Attachment attachment = new Attachment();
	    attachment.preInsert();
	    attachment.setRentContractId(rentContract.getId());
	    attachment.setAttachmentType(FileType.TENANT_ID.getValue());
	    attachment.setAttachmentPath(rentContract.getRentContractCusIDFile());
	    attachmentDao.insert(attachment);
	}
	// 出租合同其他附件
	if (!StringUtils.isBlank(rentContract.getRentContractOtherFile())) {
	    Attachment attachment = new Attachment();
	    attachment.preInsert();
	    attachment.setRentContractId(rentContract.getId());
	    attachment.setAttachmentType(FileType.RENTCONTRACT_FILE_OTHER.getValue());
	    attachment.setAttachmentPath(rentContract.getRentContractOtherFile());
	    attachmentDao.insert(attachment);
	}
    }

    @Transactional(readOnly = false)
    public void delete(RentContract rentContract) {
	rentContract.preUpdate();
	super.delete(rentContract);
    }

    /**
     * 生成水电费押金/水电费押金差额 款项
     */
    private void genDepositElectricPayTrans(String tradeType, String transObjId, RentContract rentContract) {
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.preInsert();
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
	if (rentContract.getDepositElectricAmount() > 0) {
	    paymentTransDao.insert(paymentTrans);
	}
    }

    /**
     * 生成房租押金/房租押金差额款项
     */
    private void genDepositAmountPayTrans(String tradeType, String transObjId, RentContract rentContract) {
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.preInsert();
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
	if (paymentTrans.getTradeAmount() > 0) {
	    paymentTransDao.insert(paymentTrans);
	}
    }

    /**
     * 生成合同期内的所有房租款项
     */
    private void genContractRentalPayTrans(String tradeType, String transObjId, RentContract rentContract, int monthCountDiff) {

	Date startD = rentContract.getStartDate();// 开始日期

	boolean depositTransContractFlag = false;
	Double depositAgreementAmount = 0d;
	if (!StringUtils.isBlank(rentContract.getAgreementId())) {
	    DepositAgreement depositAgreement = depositAgreementDao.get(rentContract.getAgreementId());
	    depositAgreementAmount = depositAgreement.getDepositAmount();
	    depositTransContractFlag = true;
	}

	// 先生成整数房租款项列表
	if (monthCountDiff > 0) {
	    for (int i = 0; i < monthCountDiff; i++) {
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.preInsert();
		paymentTrans.setTradeType(tradeType);
		paymentTrans.setPaymentType("6");// 房租金额
		paymentTrans.setTransId(transObjId);
		paymentTrans.setTradeDirection("1");// 收款
		paymentTrans.setTradeAmount(rentContract.getRental());
		paymentTrans.setStartDate(startD);
		if (i != (monthCountDiff - 1))
		    paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
		else
		    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
		if (depositTransContractFlag) {// 定金转合同
		    if (depositAgreementAmount >= rentContract.getRental()) {
			paymentTrans.setLastAmount(0D);
			paymentTrans.setTransAmount(rentContract.getRental());
			paymentTrans.setTransStatus("2");// 完全到账登记
			paymentTrans.setTransferDepositAmount(rentContract.getRental());
			depositAgreementAmount = depositAgreementAmount - rentContract.getRental();
		    } else if (depositAgreementAmount > 0 && depositAgreementAmount < rentContract.getRental()) {
			paymentTrans.setLastAmount(rentContract.getRental() - depositAgreementAmount);
			paymentTrans.setTransAmount(depositAgreementAmount);
			paymentTrans.setTransStatus("1");// 部分到账登记
			paymentTrans.setTransferDepositAmount(depositAgreementAmount);
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
		    paymentTrans.setTransferDepositAmount(0d);
		}
		if (paymentTrans.getTradeAmount() > 0) {
		    paymentTransDao.insert(paymentTrans);
		}
		startD = DateUtils.dateAddMonth(startD, 1);
	    }
	}
    }

    /**
     * 生成合同期内的所有费用款项
     */
    private void genContractFeesPayTrans(String tradeType, String transObjId, RentContract rentContract, int monthCountDiff) {
	if ("0".equals(rentContract.getChargeType())) {// 预付
	    Date startD = rentContract.getStartDate();// 开始日期
	    if (monthCountDiff > 0) { // 先生成整数费用款项列表
		for (int i = 0; i < monthCountDiff; i++) {
		    if (null != rentContract.getWaterFee() && rentContract.getWaterFee() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.preInsert();
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType("14");// 水费金额
			paymentTrans.setTransId(transObjId);
			paymentTrans.setTradeDirection("1");// 收款
			paymentTrans.setStartDate(startD);
			if (i != (monthCountDiff - 1))
			    paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
			else
			    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(rentContract.getWaterFee());
			paymentTrans.setLastAmount(rentContract.getWaterFee());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			if (paymentTrans.getTradeAmount() > 0) {
			    paymentTransDao.insert(paymentTrans);
			}
		    }
		    if ("1".equals(rentContract.getHasTv()) && null != rentContract.getTvFee() && rentContract.getTvFee() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.preInsert();
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType("18");// 有线电视费
			paymentTrans.setTransId(transObjId);
			paymentTrans.setTradeDirection("1");// 收款
			paymentTrans.setStartDate(startD);
			if (i != (monthCountDiff - 1))
			    paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
			else
			    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(rentContract.getTvFee());
			paymentTrans.setLastAmount(rentContract.getTvFee());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			if (paymentTrans.getTradeAmount() > 0) {
			    paymentTransDao.insert(paymentTrans);
			}
		    }
		    if ("1".equals(rentContract.getHasNet()) && null != rentContract.getNetFee() && rentContract.getNetFee() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.preInsert();
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType("20");// 宽带费
			paymentTrans.setTransId(transObjId);
			paymentTrans.setTradeDirection("1");// 收款
			paymentTrans.setStartDate(startD);
			if (i != (monthCountDiff - 1))
			    paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
			else
			    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(rentContract.getNetFee());
			paymentTrans.setLastAmount(rentContract.getNetFee());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			if (paymentTrans.getTradeAmount() > 0) {
			    paymentTransDao.insert(paymentTrans);
			}
		    }
		    if (null != rentContract.getServiceFee() && rentContract.getServiceFee() > 0) {
			PaymentTrans paymentTrans = new PaymentTrans();
			paymentTrans.preInsert();
			paymentTrans.setTradeType(tradeType);
			paymentTrans.setPaymentType("22");// 服务费
			paymentTrans.setTransId(transObjId);
			paymentTrans.setTradeDirection("1");// 收款
			paymentTrans.setStartDate(startD);
			if (i != (monthCountDiff - 1))
			    paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
			else
			    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
			paymentTrans.setTradeAmount(rentContract.getServiceFee() / 100 * rentContract.getRental());
			paymentTrans.setLastAmount(rentContract.getServiceFee() / 100 * rentContract.getRental());
			paymentTrans.setTransAmount(0D);
			paymentTrans.setTransStatus("0");// 未到账登记
			if (paymentTrans.getTradeAmount() > 0) {
			    paymentTransDao.insert(paymentTrans);
			}
		    }
		    startD = DateUtils.dateAddMonth(startD, 1);
		}
	    }
	}
    }

    @Transactional(readOnly = true)
    public RentContract getByHouseId(RentContract rentContract) {
	return dao.getByHouseId(rentContract);
    }

    /* 保存款项 */
    private void savePaymentTrans(String tradeType, Accounting accounting, RentContract rentContract, String tradeDirection) {
	PaymentTrans paymentTrans = new PaymentTrans();
	paymentTrans.preInsert();
	paymentTrans.setTradeType(tradeType);
	paymentTrans.setPaymentType(accounting.getFeeType());
	paymentTrans.setTransId(rentContract.getId());
	paymentTrans.setTradeDirection(tradeDirection);
	paymentTrans.setStartDate(rentContract.getStartDate());
	paymentTrans.setExpiredDate(rentContract.getExpiredDate());
	paymentTrans.setTradeAmount(accounting.getFeeAmount());
	paymentTrans.setLastAmount(accounting.getFeeAmount());
	paymentTrans.setTransAmount(0D);
	paymentTrans.setTransStatus("0");// 未到账登记
	paymentTransDao.insert(paymentTrans);

    }

    private void saveAccounting(String tradeType, Accounting accounting, RentContract rentContract, String tradeDirection) {
	accounting.setRentContract(rentContract);
	if (TradeTypeEnum.NORMAL_RETURN_RENT.getValue().equals(tradeType)) {
	    accounting.setAccountingType(AccountingTypeEnum.NORMAL_RETURN_ACCOUNT.getValue());
	} else if (TradeTypeEnum.ADVANCE_RETURN_RENT.getValue().equals(tradeType)) {
	    accounting.setAccountingType(AccountingTypeEnum.ADVANCE_RETURN_ACCOUNT.getValue());
	} else if (TradeTypeEnum.OVERDUE_RETURN_RENT.getValue().equals(tradeType)) {
	    accounting.setAccountingType(AccountingTypeEnum.LATE_RETURN_ACCOUNT.getValue());
	} else if (TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {
	    accounting.setAccountingType(AccountingTypeEnum.SPECIAL_RETURN_ACCOUNT.getValue());
	}
	accounting.setFeeDirection(tradeDirection);
	accounting.setUser(UserUtils.getUser());
	if (!"9".equals(tradeType)) {// 非特殊退租核算
	    accounting.setFeeDate(new Date());
	} else {
	    if (StringUtils.isNotEmpty(accounting.getFeeDateStr())) {
		accounting.setFeeDate(DateUtils.parseDate(accounting.getFeeDateStr()));
	    }
	}
	accountingService.save(accounting);
    }

    private void generatePaymentTransAndAccounts(List<Accounting> accountList, RentContract rentContract, String tradeType, String feeDirection) {
	for (Accounting accounting : accountList) {
	    if (accounting != null && accounting.getFeeAmount() != null && accounting.getFeeAmount() != 0) {
		if (!TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {// 特殊退租不生成款项
		    if (accounting.getFeeAmount() > 0) {
			savePaymentTrans(tradeType, accounting, rentContract, feeDirection);
		    }
		}
		saveAccounting(tradeType, accounting, rentContract, feeDirection);
	    }
	}
    }

    /**
     * 通用退租处理
     * 
     * @param contractBusiStatus
     *            合同业务状态
     */
    private void commonReturnRent(RentContract rentContract, String contractBusiStatus) {
	rentContract = dao.get(rentContract.getId());
	rentContract.setContractBusiStatus(contractBusiStatus);
	rentContract.preUpdate();
	dao.update(rentContract);
	boolean damagedFlag = "1".equals(rentContract.getBreakDown()) ? true : false;
	if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {// 整租
	    House house = houseDao.get(rentContract.getHouse().getId());
	    houseService.returnWholeHouse(house, damagedFlag);
	} else {// 单间
	    Room room = roomService.get(rentContract.getRoom().getId());
	    houseService.returnSingleRoom(room, damagedFlag);
	}
    }
}