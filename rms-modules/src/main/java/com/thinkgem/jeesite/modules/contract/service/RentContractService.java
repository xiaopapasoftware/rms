package com.thinkgem.jeesite.modules.contract.service;

import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.app.enums.AlipayHousingSyncStatus;
import com.thinkgem.jeesite.modules.app.enums.UpEnum;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
import com.thinkgem.jeesite.modules.common.enums.ValidatorFlagEnum;
import com.thinkgem.jeesite.modules.common.service.AttachmentService;
import com.thinkgem.jeesite.modules.contract.dao.AgreementChangeDao;
import com.thinkgem.jeesite.modules.contract.dao.ContractTenantDao;
import com.thinkgem.jeesite.modules.contract.dao.DepositAgreementDao;
import com.thinkgem.jeesite.modules.contract.dao.RentContractDao;
import com.thinkgem.jeesite.modules.contract.entity.*;
import com.thinkgem.jeesite.modules.contract.enums.*;
import com.thinkgem.jeesite.modules.fee.dao.ElectricFeeDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.PaymenttransDtl;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.PaymenttransDtlService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.person.dao.TenantDao;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 出租合同Service
 *
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class RentContractService extends CrudService<RentContractDao, RentContract> {
    @Autowired
    private ContractTenantService contractTenantService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private AgreementChangeDao agreementChangeDao;
    @Autowired
    private HouseService houseService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private DepositAgreementDao depositAgreementDao;
    @Autowired
    private DepositAgreementService depositAgreementService;
    @Autowired
    private PaymentTransService paymentTransService;
    @Autowired
    private PaymenttransDtlService paymenttransDtlService;
    @Autowired
    private AuditHisService auditHisService;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private LeaseContractService leaseContractService;
    @Autowired
    private ElectricFeeDao electricFeeDao;
    @Autowired
    private RentContractDao rentContractDao;
    @Autowired
    private ContractTenantDao contractTenantDao;
    @Autowired
    private TenantDao tenantDao;

    @Override
    public List<RentContract> findList(RentContract entity) {
        areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
        return super.findList(entity);
    }

    @Override
    public Page<RentContract> findPage(Page<RentContract> page, RentContract entity) {
        areaScopeFilter(entity, "dsf", "tp.area_id=sua.area_id");
        return super.findPage(page, entity);
    }

    @Transactional(readOnly = false)
    public void audit(AuditHis auditHis) {
        auditHisService.saveAuditHis(auditHis, AuditTypeEnum.RENT_CONTRACT_CONTENT.getValue());
        String rentContractId = auditHis.getObjectId();
        RentContract rentContract = super.get(rentContractId);
        String actFlagFromView = auditHis.getType();
        if (AuditStatusEnum.PASS.getValue().equals(auditHis.getAuditStatus())) {
            Audit audit = new Audit();
            audit.setObjectId(rentContractId);
            audit.setNextRole("");
            auditService.update(audit);
            if ("2".equals(actFlagFromView)) {// 原出租合同的审核状态为：特殊退租内容待审核
                Accounting accounting = new Accounting();
                accounting.setRentContractId(rentContractId);
                accounting.setAccountingType(AccountingTypeEnum.SPECIAL_RETURN_ACCOUNT.getValue());
                List<Accounting> list = accountingService.findList(accounting);
                if (CollectionUtils.isNotEmpty(list)) {
                    for (Accounting tmpAccounting : list) {
                        Double feeAmt = tmpAccounting.getFeeAmount();
                        if (feeAmt != null && feeAmt > 0D) {
                            String feeType = tmpAccounting.getFeeType();
                            String paymentTransId = paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.SPECIAL_RETURN_RENT.getValue(), feeType, rentContractId, tmpAccounting.getFeeDirection(), feeAmt,
                                    feeAmt, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(), rentContract.getStartDate(), tmpAccounting.getFeeDate(), null);
                            tmpAccounting.setPaymentTransId(paymentTransId);
                            tmpAccounting.preUpdate();
                            accountingService.save(tmpAccounting);
                            if (TradeDirectionEnum.OUT.getValue().equals(tmpAccounting.getFeeDirection()) && PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue().equals(feeType)) {
                                shareRetirementAmt(tmpAccounting.getFeeDate(), tmpAccounting.getFeeAmount(), rentContract, paymentTransId, TradeDirectionEnum.OUT.getValue());
                            }
                            if (TradeDirectionEnum.IN.getValue().equals(tmpAccounting.getFeeDirection()) && PaymentTransTypeEnum.OVERDUE_RENT_AMOUNT.getValue().equals(feeType)) {
                                shareOverdueAmt(tmpAccounting.getFeeDate(), paymentTransService.analysisMaxIncomedTransDate(rentContract), tmpAccounting.getFeeAmount(), rentContract, paymentTransId,
                                        TradeDirectionEnum.IN.getValue());
                            }
                        }
                    }
                }
                paymentTransService.deleteNotSignPaymentTrans(rentContractId);// 删除未到账款项
                rentContract.setContractBusiStatus(ContractBusiStatusEnum.ACCOUNT_DONE_TO_SIGN.getValue());
            } else {// 原出租合同的审核状态为：到账收据完成合同内容待审核
                rentContract.setContractStatus(ContractAuditStatusEnum.INVOICE_TO_AUDITED.getValue());
            }
        } else {// 审核拒绝
            if (DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource())) {
                rentContract.setUpdateUser(auditHis.getUpdateUser());
            } else {
                rentContract.setUpdateUser(UserUtils.getUser().getId());
            }
            if ("2".equals(actFlagFromView)) {// 原出租合同的审核状态为：特殊退租内容待审核（被审核拒绝时）
                rentContract.setContractBusiStatus(ContractBusiStatusEnum.SPECIAL_RENTURN_CONTENT_AUDIT_REFUSE.getValue());
            } else {// 出租合同内容审核拒绝
                if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {
                    House house = houseService.get(rentContract.getHouse().getId());
                    houseService.cancelSign4WholeHouse(house);
                } else {// 释放单间的房源
                    Room room = roomService.get(rentContract.getRoom().getId());
                    houseService.cancelSign4SingleRoom(room);
                }
                paymentTransService.deletePaymentTransAndTradingAcctouns(rentContractId);
                rentContract.setContractStatus(ContractAuditStatusEnum.CONTENT_AUDIT_REFUSE.getValue());
            }
        }
        super.save(rentContract);
    }

    public List<Tenant> findTenant(RentContract rentContract) {
        List<Tenant> tenantList = new ArrayList<Tenant>();
        ContractTenant contractTenant = new ContractTenant();
        contractTenant.setLeaseContractId(rentContract.getId());
        List<ContractTenant> list = contractTenantService.findList(contractTenant);
        for (ContractTenant tmpContractTenant : list) {
            Tenant tenant = tenantService.get(tmpContractTenant.getTenantId());
            tenantList.add(tenant);
        }
        return tenantList;
    }

    public List<Tenant> findLiveTenant(RentContract rentContract) {
        List<Tenant> tenantList = new ArrayList<Tenant>();
        ContractTenant contractTenant = new ContractTenant();
        contractTenant.setContractId(rentContract.getId());
        List<ContractTenant> list = contractTenantService.findList(contractTenant);
        for (ContractTenant tmpContractTenant : list) {
            Tenant tenant = tenantService.get(tmpContractTenant.getTenantId());
            tenantList.add(tenant);
        }
        return tenantList;
    }

    public RentContract findContractByCode(String contractCode) {
        return dao.findContractByCode(contractCode);
    }

    @Transactional(readOnly = true)
    public List<RentContract> findAllValidRentContracts() {
        return dao.findAllList(new RentContract());
    }

    @Transactional(readOnly = true)
    public Integer getAllValidRentContractCounts() {
        return dao.getAllValidRentContractCounts();
    }

    /**
     * 在“退租核算”页面，点“保存”
     */
    @Transactional(readOnly = false)
    public void returnCheck(RentContract rentContract, String tradeType) {
        String returnRemark = rentContract.getReturnRemark();
        String returnDateStr = rentContract.getReturnDateStr();
        List<Accounting> accountList = rentContract.getAccountList();
        List<Accounting> outAccountList = rentContract.getOutAccountList();
        rentContract = super.get(rentContract.getId());
        if (StringUtils.isNotEmpty(returnDateStr)) {
            rentContract.setReturnDateStr(returnDateStr);
            rentContract.setReturnDate(DateUtils.parseDate(returnDateStr));
        }
        if (!TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {// 特殊退租与其他退租类型不同，需要人工先进行审核
            rentContract.setContractBusiStatus(ContractBusiStatusEnum.ACCOUNT_DONE_TO_SIGN.getValue());
        } else {
            rentContract.setContractBusiStatus(ContractBusiStatusEnum.SPECIAL_RENTURN_CONTENT_AUDIT.getValue());
        }
        rentContract.setReturnRemark(returnRemark);
        super.save(rentContract);
        accountingService.delRentContractAccountings(rentContract);// 删除核算记录
        // 特殊退租暂时不生成款项，在审核退租内容时再生成款项数据
        generatePaymentTransAndAccounts(accountList, rentContract, tradeType, TradeDirectionEnum.IN.getValue());
        generatePaymentTransAndAccounts(outAccountList, rentContract, tradeType, TradeDirectionEnum.OUT.getValue());
    }

    @Transactional(readOnly = false)
    public void saveAdditional(AgreementChange agreementChange) {
        String id = IdGen.uuid();
        /* 合同租客关联信息 */
        ContractTenant delContractTenant = new ContractTenant();
        delContractTenant.setLeasagremChangeId(id);// 承租的 变更协议
        delContractTenant.preUpdate();
        contractTenantService.delete(delContractTenant);
        List<Tenant> list = agreementChange.getTenantList();// 承租人
        if (null != list && list.size() > 0) {
            for (Tenant tenant : list) {
                ContractTenant contractTenant = new ContractTenant();
                contractTenant.setTenantId(tenant.getId());
                contractTenant.setLeasagremChangeId(id);
                contractTenantService.save(contractTenant);
            }
        }
        ContractTenant delContractTenant2 = new ContractTenant();
        delContractTenant2.setAgreementChangeId(id);// 入住的 变更协议
        delContractTenant2.preUpdate();
        contractTenantService.delete(delContractTenant2);
        list = agreementChange.getLiveList();// 入住人
        if (null != list && list.size() > 0) {
            for (Tenant tenant : list) {
                ContractTenant contractTenant = new ContractTenant();
                contractTenant.setTenantId(tenant.getId());
                contractTenant.setAgreementChangeId(id);
                contractTenantService.save(contractTenant);
            }
        }
        agreementChange.setCreateDate(new Date());
        agreementChange.setCreateBy(UserUtils.getUser());
        agreementChange.setUpdateDate(new Date());
        agreementChange.setUpdateBy(UserUtils.getUser());
        agreementChange.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
        agreementChange.setId(id);
        agreementChange.setAgreementStatus("0");// 待审核
        agreementChange.setRentContract(super.get(agreementChange.getContractId()));
        agreementChangeDao.insert(agreementChange);
        auditService.insert(AuditTypeEnum.RENT_CONTRACT_CHANGE.getValue(), "change_agreement_role", id, "");
    }

    /**
     * 新签、续签、定金转合同，需首先根据不同请求类型锁定房源状态
     *
     * @return 0=成功；-1=房源已出租；-2=出租合同结束日期不能晚于承租合同截止日期；
     */
    @Transactional(readOnly = false)
    public int saveContract(RentContract rentContract) {
        // 出租合同的结束时间不能超过承租合同的结束时间
        LeaseContract leaseContract = new LeaseContract();
        leaseContract.setHouse(rentContract.getHouse());
        List<LeaseContract> list = leaseContractService.findList(leaseContract);
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.get(0).getExpiredDate().before(rentContract.getExpiredDate())) {
                return -2;
            }
        }
        String curHouseId = rentContract.getHouse().getId();
        String curRoomId = rentContract.getRoom() == null ? "" : rentContract.getRoom().getId();
        curRoomId = StringUtils.isBlank(curRoomId) ? "" : curRoomId;
        if (rentContract.getIsNewRecord()) {// 手机APP签约、后台的合同新增
            return persistentHouseInfo(rentContract, curHouseId, curRoomId);
        } else {// 手机APP签约、后台的合同修改
            RentContract originalRentContract = super.get(rentContract.getId());
            String contractAuditStatus = originalRentContract.getContractStatus();
            String contractBusiStatus = originalRentContract.getContractBusiStatus();
            if (ContractAuditStatusEnum.TEMP_EXIST.getValue().equals(contractAuditStatus) || ContractAuditStatusEnum.FINISHED_TO_SIGN.getValue().equals(contractAuditStatus)) {
                return doProcessHouseRoomStatusChanged(originalRentContract, rentContract, curHouseId, curRoomId);
            }
            if (ContractAuditStatusEnum.CONTENT_AUDIT_REFUSE.getValue().equals(contractAuditStatus)) {
                return doRentHouseOrRoom(rentContract, curHouseId, curRoomId); // 出租新房源
            }
            // 如果合同业务状态为 有效 或者 账务审核拒绝，进行后门修改，则把新合同的业务状态置为空值
            if ((ContractAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(contractAuditStatus) && ContractBusiStatusEnum.VALID.getValue().equals(contractBusiStatus))
                    || (ContractAuditStatusEnum.INVOICE_AUDITED_REFUSE.getValue().equals(contractAuditStatus) && StringUtils.isBlank(contractBusiStatus))) {
                rentContract.setContractStatus(ContractAuditStatusEnum.FINISHED_TO_SIGN.getValue());
                rentContract.setContractBusiStatus("");
                return doProcessHouseRoomStatusChanged(originalRentContract, rentContract, curHouseId, curRoomId);
            }
            return -3;
        }
    }

    /**
     * 房源状态变更，如果选择的房源发生变化，需要把以前的房源状态回滚，改变后选的房源状态。
     */
    private int doProcessHouseRoomStatusChanged(RentContract originalRentContract, RentContract rentContract, String curHouseId, String curRoomId) {
        String originalHouseId = originalRentContract.getHouse().getId();
        String originalRoomId = originalRentContract.getRoom() == null ? "" : originalRentContract.getRoom().getId();
        originalRoomId = StringUtils.isBlank(originalRoomId) ? "" : originalRoomId;
        if (!originalHouseId.equals(curHouseId) || !originalRoomId.equals(curRoomId)) {// 修改了房屋或房间，回滚前房源状态
            if (StringUtils.isNotBlank(originalRoomId)) {// 合租，把单间从“已出租”改为“待出租可预订”
                houseService.returnSingleRoom(roomService.get(originalRoomId));
            } else {// 整租，把房屋从“完全出租”改为“待出租可预订”
                houseService.returnWholeHouse(houseService.get(originalHouseId));
            }
            return doRentHouseOrRoom(rentContract, curHouseId, curRoomId); // 出租新房源
        } else {// 未修改房源
            doSaveContractBusiness(rentContract);
            return 0;
        }
    }

    /**
     * 直接出租房源
     */
    private int doRentHouseOrRoom(RentContract rentContract, String curHouseId, String curRoomId) {
        // 出租新房源
        if (StringUtils.isNotBlank(curRoomId)) {// 合租，把单间从“待出租可预订”改为“已出租”
            boolean isLock = roomService.isLockSingleRoom4NewSign(curRoomId);
            if (isLock) {  //如该合租单间在支付宝客户端处于上架状态，则从支付宝进行下架
                Room room = roomService.get(curRoomId);
                if (Integer.valueOf(AlipayHousingSyncStatus.SUCCESS.getValue()) == room.getAlipayStatus() && UpEnum.UP.getValue() == room.getUp()) {
                    roomService.upDownRoom(curRoomId, UpEnum.DOWN.getValue(), houseService.get(roomService.get(curRoomId).getHouse().getId()).getBuilding().getType());
                }
            }
            return doProcessHouseStatusAfterLockRoom(isLock, curRoomId, rentContract);
        } else {// 整租，把房屋从“待出租可预订”改为“完全出租”
            boolean isLock = houseService.isLockWholeHouse4NewSign(curHouseId);
            if (isLock) { //如该整租房源在支付宝客户端处于上架状态，则从支付宝进行下架
                House house = houseService.get(curHouseId);
                if (Integer.valueOf(AlipayHousingSyncStatus.SUCCESS.getValue()) == house.getAlipayStatus() && UpEnum.UP.getValue() == house.getUp()) {
                    houseService.upDownHouse(curHouseId, UpEnum.DOWN.getValue());
                }
            }
            return doProcessAllRoomsAfterLockHouse(isLock, curHouseId, rentContract);
        }
    }

    private int persistentHouseInfo(RentContract rentContract, String houseId, String roomId) {
        if (ContractSignTypeEnum.NEW_SIGN.getValue().equals(rentContract.getSignType()) || StringUtils.isEmpty(rentContract.getSignType())) {
            if (StringUtils.isNotBlank(rentContract.getAgreementId())) { // 定金转合同，把房源状态从“已预定”变更为“已出租”
                if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {// 整租
                    boolean isLock = houseService.isLockWholeHouseFromDepositToContract(houseId);
                    return doProcessAllRoomsAfterLockHouse(isLock, houseId, rentContract);
                } else {// 合租
                    boolean isLock = roomService.isLockSingleRoomFromDepositToContract(roomId);
                    return doProcessHouseStatusAfterLockRoom(isLock, roomId, rentContract);
                }
            } else {// 非定金转合同，直接新签
                if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {// 整租，把房屋从“待出租可预订”变为“完全出租”
                    boolean isLock = houseService.isLockWholeHouse4NewSign(houseId);
                    if (isLock) { //如该整租房源在支付宝客户端处于上架状态，则从支付宝进行下架
                        House house = houseService.get(houseId);
                        if (Integer.valueOf(AlipayHousingSyncStatus.SUCCESS.getValue()) == house.getAlipayStatus() && UpEnum.UP.getValue() == house.getUp()) {
                            houseService.upDownHouse(houseId, UpEnum.DOWN.getValue());
                        }
                    }
                    return doProcessAllRoomsAfterLockHouse(isLock, houseId, rentContract);
                } else {
                    boolean isLock = roomService.isLockSingleRoom4NewSign(roomId);// 合租，把房间从“待出租可预订”变为“已出租”
                    //如该合租单间在支付宝客户端处于上架状态，则从支付宝进行下架
                    Room room = roomService.get(roomId);
                    if (Integer.valueOf(AlipayHousingSyncStatus.SUCCESS.getValue()) == room.getAlipayStatus() && UpEnum.UP.getValue() == room.getUp()) {
                        roomService.upDownRoom(roomId, UpEnum.DOWN.getValue(), houseService.get(roomService.get(roomId).getHouse().getId()).getBuilding().getType());
                    }
                    return doProcessHouseStatusAfterLockRoom(isLock, roomId, rentContract);
                }
            }
        }
        if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(rentContract.getSignType())) {// 续签，把房源状态从“已出租”变更为“已出租”
            if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(rentContract.getRentMode())) {// 整租
                boolean isLock = houseService.isLockWholeHouse4RenewSign(houseId);
                return doProcessAllRoomsAfterLockHouse(isLock, houseId, rentContract);
            } else {// 合租
                boolean isLock = roomService.isLockSingleRoom4RenewSign(roomId);
                return doProcessHouseStatusAfterLockRoom(isLock, roomId, rentContract);
            }
        }
        return 0;
    }

    /**
     * 锁定房间后，处理房间所属房屋的状态及保存出租合同业务信息
     */
    private int doProcessHouseStatusAfterLockRoom(boolean isLock, String roomId, RentContract rentContract) {
        if (isLock) {
            houseService.calculateHouseStatus(roomId);
            doSaveContractBusiness(rentContract);
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 锁定房屋后，处理锁定房屋下所属房间的状态及保存出租合同业务信息
     */
    private int doProcessAllRoomsAfterLockHouse(boolean isLock, String houseId, RentContract rentContract) {
        if (isLock) {
            roomService.lockRooms(houseId);
            doSaveContractBusiness(rentContract);
            return 0;
        } else {
            return -1;
        }
    }

    public List<RentContract> queryHousesByHouseId(String houseId) {
        return dao.queryHousesByHouseId(houseId);
    }

    /**
     * 房源状态变更成功，做合同相关业务
     */
    private void doSaveContractBusiness(RentContract rentContract) {
        String id = saveAndReturnId(rentContract);
        String contractSignType = rentContract.getSignType();
        if (ValidatorFlagEnum.SAVE.getValue().equals(rentContract.getValidatorFlag())) {
            paymentTransService.deletePaymentTransAndTradingAcctouns(id);// 删除相关款项记录
            String tradeType = "";// 交易类型
            Date startDate = rentContract.getStartDate();
            Date expireDate = rentContract.getExpiredDate();
            if (ContractSignTypeEnum.NEW_SIGN.getValue().equals(contractSignType) || StringUtils.isEmpty(contractSignType)) {
                tradeType = TradeTypeEnum.SIGN_NEW_CONTRACT.getValue();
            } else if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(contractSignType)) {
                tradeType = TradeTypeEnum.NORMAL_RENEW.getValue();
            }
            Double electricAmount = rentContract.getDepositElectricAmount();
            if (electricAmount != null && electricAmount > 0) {
                String paymentType = "";
                // 生成水电费押金
                if (TradeTypeEnum.SIGN_NEW_CONTRACT.getValue().equals(tradeType)) {
                    paymentType = PaymentTransTypeEnum.WATER_ELECT_DEPOSIT.getValue();
                }
                // 生成水电费押金差额
                if (TradeTypeEnum.NORMAL_RENEW.getValue().equals(tradeType)) {
                    paymentType = PaymentTransTypeEnum.SUPPLY_WATER_ELECT_DEPOSIT.getValue();
                }
                paymentTransService.generateAndSavePaymentTrans(tradeType, paymentType, id, TradeDirectionEnum.IN.getValue(), electricAmount, electricAmount, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(),
                        startDate, expireDate, null);
            }
            Double rentDepositAmt = rentContract.getDepositAmount();
            if (rentDepositAmt != null && rentDepositAmt > 0) {
                String paymentType = "";
                // 生成房租押金
                if (TradeTypeEnum.SIGN_NEW_CONTRACT.getValue().equals(tradeType)) {
                    paymentType = PaymentTransTypeEnum.RENT_DEPOSIT.getValue();
                }
                // 生成房租押金差额
                if (TradeTypeEnum.NORMAL_RENEW.getValue().equals(tradeType)) {
                    paymentType = PaymentTransTypeEnum.SUPPLY_RENT_DEPOSIT.getValue();
                }
                paymentTransService.generateAndSavePaymentTrans(tradeType, paymentType, id, TradeDirectionEnum.IN.getValue(), rentDepositAmt, rentDepositAmt, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(),
                        startDate, expireDate, null);
            }
            // 如果有电费充值金额，需要同时生成电费充值款项和电费充值记录
            Double eleRechargeAmount = rentContract.getEleRechargeAmount();
            if (eleRechargeAmount != null && eleRechargeAmount > 0) {
                Date nowDate = new Date();
                Date endDate = DateUtils.parseDate(DateUtils.lastDateOfCurrentMonth());
                String paymentTransId = paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.ELECT_SELF_AMOUNT.getValue(), id, TradeDirectionEnum.IN.getValue(), eleRechargeAmount,
                        eleRechargeAmount, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(), nowDate, endDate, null);
                ElectricFee ef = new ElectricFee();
                ef.preInsert();
                ef.setChargeAmount(eleRechargeAmount);
                ef.setChargeDate(new Date());
                ef.setChargeStatus(ElectricChargeStatusEnum.PROCESSING.getValue());
                ef.setStartDate(nowDate);
                ef.setEndDate(endDate);
                ef.setPaymentTransId(paymentTransId);
                ef.setRentContractId(id);
                ef.setSettleStatus(FeeSettlementStatusEnum.NOT_SETTLED.getValue());
                electricFeeDao.insert(ef);
            }
            int monthCountDiff = DateUtils.getMonthSpace(startDate, expireDate);// 合同日期间隔月数
            if (monthCountDiff > 0) {
                Double rentalAmt = rentContract.getRental();
                if (rentalAmt != null && rentalAmt > 0 && !AwardRentAmtTypeEnum.Y.getValue().equals(rentContract.getDerateRentFlag())) { // 不免房租
                    genContractRentalPayTrans(tradeType, id, rentContract, monthCountDiff, rentalAmt);//生成房租款项
                    if (rentContract.getHasFree() != null && AwardRentAmtTypeEnum.Y.getValue().equals(rentContract.getHasFree())) {// 若有返租促销
                        genFreePayTrans(id, rentContract.getFreeMonths());
                    }
                }
                genContractFeesPayTrans(tradeType, id, rentContract, monthCountDiff); // 生成合同期内所有的费用款项
            }
            // 定金协议转合同，更新原定金协议状态
            if (StringUtils.isNotBlank(rentContract.getAgreementId())) {
                DepositAgreement depositAgreement = depositAgreementService.get(rentContract.getAgreementId());
                depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.BE_CONVERTED_CONTRACT.getValue());// 已转合同
                depositAgreement.preUpdate();
                depositAgreementDao.update(depositAgreement);
            }
            // 续签合同，则把原合同业务状态改成“已续签”
            if (ContractSignTypeEnum.RENEW_SIGN.getValue().equals(contractSignType)) {
                RentContract rentContractOld = super.get(rentContract.getContractId());
                rentContractOld.setContractBusiStatus(ContractBusiStatusEnum.NORMAL_RENEW.getValue());
                super.save(rentContractOld);
            }
            auditService.delete(id);
            auditService.insert(AuditTypeEnum.RENT_CONTRACT_CONTENT.getValue(), "rent_contract_role", id, "");
        }
        // 处理承租人/入住人关系
        contractTenantService.doProcess4RentContract(rentContract);
        // 修改合同，首先清空所有的合同附件
        if (!rentContract.getIsNewRecord()) {
            Attachment attachment = new Attachment();
            attachment.setRentContractId(id);
            attachmentService.delete(attachment);
        }
        // 出租合同文件
        if (StringUtils.isNotBlank(rentContract.getRentContractFile())) {
            Attachment attachment = new Attachment();
            attachment.setRentContractId(id);
            attachment.setAttachmentType(FileType.RENTCONTRACT_FILE.getValue());
            attachment.setAttachmentPath(rentContract.getRentContractFile());
            attachmentService.save(attachment);
        }
        // 租客身份证
        if (StringUtils.isNotBlank(rentContract.getRentContractCusIDFile())) {
            Attachment attachment = new Attachment();
            attachment.setRentContractId(id);
            attachment.setAttachmentType(FileType.TENANT_ID.getValue());
            attachment.setAttachmentPath(rentContract.getRentContractCusIDFile());
            attachmentService.save(attachment);
        }
        // 出租合同其他附件
        if (StringUtils.isNotBlank(rentContract.getRentContractOtherFile())) {
            Attachment attachment = new Attachment();
            attachment.setRentContractId(id);
            attachment.setAttachmentType(FileType.RENTCONTRACT_FILE_OTHER.getValue());
            attachment.setAttachmentPath(rentContract.getRentContractOtherFile());
            attachmentService.save(attachment);
        }
    }

    /**
     * 生成合同期内的所有房租款项
     */
    private void genContractRentalPayTrans(String tradeType, String transObjId, RentContract rentContract, int monthCountDiff, Double rentalAmt) {
        if (rentalAmt != null && rentalAmt > 0) {
            Date startD = rentContract.getStartDate();// 开始日期
            boolean depositTransContractFlag = false;// 是否定金转合同
            Double depositAgreementAmount = 0d;// 定金金额
            if (StringUtils.isNotBlank(rentContract.getAgreementId())) {
                DepositAgreement depositAgreement = depositAgreementDao.get(rentContract.getAgreementId());
                depositAgreementAmount = depositAgreement.getDepositAmount();
                depositTransContractFlag = true;
            }
            // 生成房租款项列表
            for (int i = 0; i < monthCountDiff; i++) {
                PaymentTrans paymentTrans = new PaymentTrans();
                paymentTrans.setTradeType(tradeType);
                paymentTrans.setPaymentType(PaymentTransTypeEnum.RENT_AMOUNT.getValue());
                paymentTrans.setTransId(transObjId);
                paymentTrans.setTradeDirection(TradeDirectionEnum.IN.getValue());
                paymentTrans.setTradeAmount(rentalAmt);
                paymentTrans.setStartDate(startD);
                if (i != (monthCountDiff - 1)) {
                    paymentTrans.setExpiredDate(DateUtils.dateAddMonth2(startD, 1));
                } else {
                    paymentTrans.setExpiredDate(rentContract.getExpiredDate());
                }
                if (depositTransContractFlag) {// 定金转合同
                    if (depositAgreementAmount >= rentalAmt) {
                        paymentTrans.setLastAmount(0D);
                        paymentTrans.setTransAmount(rentalAmt);
                        paymentTrans.setTransStatus(PaymentTransStatusEnum.WHOLE_SIGN.getValue());
                        paymentTrans.setTransferDepositAmount(rentalAmt);
                        depositAgreementAmount = depositAgreementAmount - rentalAmt;
                    } else if (depositAgreementAmount > 0 && depositAgreementAmount < rentalAmt) {
                        paymentTrans.setLastAmount(rentalAmt - depositAgreementAmount);
                        paymentTrans.setTransAmount(depositAgreementAmount);
                        paymentTrans.setTransStatus(PaymentTransStatusEnum.PART_SIGN.getValue());
                        paymentTrans.setTransferDepositAmount(depositAgreementAmount);
                        depositAgreementAmount = 0d;
                    } else {
                        paymentTrans.setLastAmount(rentalAmt);
                        paymentTrans.setTransAmount(0D);
                        paymentTrans.setTransStatus(PaymentTransStatusEnum.NO_SIGN.getValue());
                    }
                } else {// 正常保存，无定金
                    paymentTrans.setLastAmount(rentalAmt);
                    paymentTrans.setTransAmount(0D);
                    paymentTrans.setTransStatus(PaymentTransStatusEnum.NO_SIGN.getValue());
                    paymentTrans.setTransferDepositAmount(0d);
                }
                paymentTransService.save(paymentTrans);
                startD = DateUtils.dateAddMonth(startD, 1);
            }
        }
    }

    /**
     * 生成合同期内的所有费用款项
     */
    private void genContractFeesPayTrans(String tradeType, String transObjId, RentContract rentContract, int monthCountDiff) {
        if (FeeChargeTypeEnum.PRE_CHARGE.getValue().equals(rentContract.getChargeType())) {// 预付
            Date startD = rentContract.getStartDate();// 开始日期
            for (int i = 0; i < monthCountDiff; i++) {
                Date expiredDate;
                if (i != (monthCountDiff - 1)) {
                    expiredDate = DateUtils.dateAddMonth2(startD, 1);
                } else {
                    expiredDate = rentContract.getExpiredDate();
                }
                // 水费
                Double waterFeeAmt = rentContract.getWaterFee();
                if (null != waterFeeAmt && waterFeeAmt > 0) {
                    paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.WATER_AMOUNT.getValue(), transObjId, TradeDirectionEnum.IN.getValue(), waterFeeAmt, waterFeeAmt, 0D,
                            PaymentTransStatusEnum.NO_SIGN.getValue(), startD, expiredDate, null);
                }
                // 燃气费
                Double gasFeeAmt = rentContract.getGasFee();
                if (null != gasFeeAmt && gasFeeAmt > 0) {
                    paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.GAS_AMOUNT.getValue(), transObjId, TradeDirectionEnum.IN.getValue(), gasFeeAmt, gasFeeAmt, 0D,
                            PaymentTransStatusEnum.NO_SIGN.getValue(), startD, expiredDate, null);
                }
                // 电视费
                Double tvFeeAmt = rentContract.getTvFee();
                if ("1".equals(rentContract.getHasTv()) && null != tvFeeAmt && tvFeeAmt > 0) {
                    paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.TV_AMOUNT.getValue(), transObjId, TradeDirectionEnum.IN.getValue(), tvFeeAmt, tvFeeAmt, 0D,
                            PaymentTransStatusEnum.NO_SIGN.getValue(), startD, expiredDate, null);
                }
                // 宽带费
                Double netFee = rentContract.getNetFee();
                if ("1".equals(rentContract.getHasNet()) && null != netFee && netFee > 0) {
                    paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.NET_AMOUNT.getValue(), transObjId, TradeDirectionEnum.IN.getValue(), netFee, netFee, 0D,
                            PaymentTransStatusEnum.NO_SIGN.getValue(), startD, expiredDate, null);
                }

                // 服务费，按照比例计算
                Double serviceFee = rentContract.getServiceFee();
                if (null != serviceFee && serviceFee > 0) {
                    Double tradeAmt = serviceFee * rentContract.getRental();
                    paymentTransService.generateAndSavePaymentTrans(tradeType, PaymentTransTypeEnum.SERVICE_AMOUNT.getValue(), transObjId, TradeDirectionEnum.IN.getValue(), tradeAmt, tradeAmt, 0D,
                            PaymentTransStatusEnum.NO_SIGN.getValue(), startD, expiredDate, null);
                }
                startD = DateUtils.dateAddMonth(startD, 1);
            }
        }
    }

    //把新签合同/续签合同交易类型下所有未到账的房租类型的款项按照时间正序排列后，再依次把前N个月的款项到账，同时生成对应的促销赠送房租款项
    private void genFreePayTrans(String rentContractId, Integer freeMonths) {
        float i = ((float) freeMonths) / 100;
        int intNum = (int) i; //整数部分
        float floatNum = Float.valueOf(String.format("%.2f", i - intNum));//小数部分，保留两位
        List<PaymentTrans> transList = paymentTransService.queryNoSignPaymentsByTransId(rentContractId);
        if (transList.size() >= intNum + (floatNum > 0 && floatNum < 1 ? 1 : 0)) {
            transList.stream().limit(intNum).forEach(trans -> {
                paymentTransService.freePaymentById(trans.getId());
                PaymentTrans freeTrans = new PaymentTrans();
                BeanUtils.copyProperties(trans, freeTrans);
                freeTrans.setPaymentType(PaymentTransTypeEnum.AWARD_RENT_AMT.getValue());
                freeTrans.setTradeDirection(TradeDirectionEnum.OUT.getValue());
                freeTrans.setTransAmount(trans.getTradeAmount());
                freeTrans.setLastAmount(0d);
                freeTrans.setTransStatus(PaymentTransStatusEnum.WHOLE_SIGN.getValue());
                freeTrans.setId(null);
                paymentTransService.save(freeTrans);
            });
            if (floatNum > 0 && floatNum < 1) {//单独处理小数部分
                PaymentTrans p = transList.get(intNum);//部分到账
                double amt = Double.valueOf(String.format("%.2f", p.getTradeAmount() * floatNum));
                p.setTransAmount(amt);
                double lastAmt = Double.valueOf(String.format("%.2f", p.getTradeAmount() - amt));
                p.setLastAmount(lastAmt);
                if (lastAmt > 0 && lastAmt < p.getTradeAmount()) {
                    p.setTransStatus(PaymentTransStatusEnum.PART_SIGN.getValue());
                } else {
                    p.setTransStatus(PaymentTransStatusEnum.WHOLE_SIGN.getValue());
                }
                paymentTransService.save(p);
                PaymentTrans freeTrans = new PaymentTrans();
                BeanUtils.copyProperties(p, freeTrans);
                freeTrans.setPaymentType(PaymentTransTypeEnum.AWARD_RENT_AMT.getValue());
                freeTrans.setTradeDirection(TradeDirectionEnum.OUT.getValue());
                freeTrans.setTradeAmount(amt);
                freeTrans.setTransAmount(amt);
                freeTrans.setLastAmount(0d);
                freeTrans.setTransStatus(PaymentTransStatusEnum.WHOLE_SIGN.getValue());
                freeTrans.setId(null);
                paymentTransService.save(freeTrans);
            }
        }
    }

    @Transactional(readOnly = true)
    public RentContract getByHouseId(RentContract rentContract) {
        return dao.getByHouseId(rentContract);
    }

    @Transactional(readOnly = false)
    protected void saveAccounting(Date feeDate, String paymentTransId, String tradeType, Accounting accounting, RentContract rentContract, String tradeDirection) {
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
        accounting.setFeeDate(feeDate);
        accounting.setFeeDirection(tradeDirection);
        accounting.setUser(UserUtils.getUser());
        accounting.setPaymentTransId(paymentTransId);
        accountingService.save(accounting);
    }

    @Transactional(readOnly = false)
    public void generatePaymentTransAndAccounts(List<Accounting> accountList, RentContract rentContract, String tradeType, String feeDirection) {
        for (Accounting accounting : accountList) {
            Double feeAmt = accounting.getFeeAmount();
            if (accounting != null && feeAmt != null && feeAmt > 0) {
                String feeType = accounting.getFeeType();
                String paymentTransId = "";
                Date feeDate;
                // 特殊退租不生成款项
                if (!TradeTypeEnum.SPECIAL_RETURN_RENT.getValue().equals(tradeType)) {
                    feeDate = new Date();
                    Date transEndDate = null;
                    if (TradeTypeEnum.NORMAL_RETURN_RENT.getValue().equals(tradeType)) {
                        transEndDate = rentContract.getExpiredDate();
                    }
                    if (TradeTypeEnum.ADVANCE_RETURN_RENT.getValue().equals(tradeType) || TradeTypeEnum.OVERDUE_RETURN_RENT.getValue().equals(tradeType)) {
                        transEndDate = feeDate;
                    }
                    paymentTransId = paymentTransService.generateAndSavePaymentTrans(tradeType, feeType, rentContract.getId(), feeDirection, feeAmt, feeAmt, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(),
                            rentContract.getStartDate(), transEndDate, null);
                    // 如果是提前退租，把应退房租金额分摊到明细表,便于毛利报表的统计
                    if (TradeTypeEnum.ADVANCE_RETURN_RENT.getValue().equals(tradeType) && TradeDirectionEnum.OUT.getValue().equals(feeDirection)
                            && PaymentTransTypeEnum.RETURN_RENT_AMOUNT.getValue().equals(feeType)) {
                        shareRetirementAmt(feeDate, feeAmt, rentContract, paymentTransId, feeDirection);
                    }
                    // 如果是逾期退租，把逾赔房租金额分摊到明细表，便于毛利报表统计
                    if (TradeTypeEnum.OVERDUE_RETURN_RENT.getValue().equals(tradeType) && TradeDirectionEnum.IN.getValue().equals(feeDirection)
                            && PaymentTransTypeEnum.OVERDUE_RENT_AMOUNT.getValue().equals(feeType)) {
                        shareOverdueAmt(feeDate, rentContract.getExpiredDate(), feeAmt, rentContract, paymentTransId, feeDirection);
                    }
                } else {
                    feeDate = DateUtils.parseDate(rentContract.getReturnDateStr());
                }
                saveAccounting(feeDate, paymentTransId, tradeType, accounting, rentContract, feeDirection);
            }
        }
    }

    /**
     * 逾期退租/特殊退租，把逾期的房租或特殊退租收的房租金额分摊到各个时间段内
     * 逾期退租 expiredDate 为合同结束日期， 特殊退expiredDate为已经交过的房租最后一天
     */
    @Transactional(readOnly = false)
    public void shareOverdueAmt(Date feeDate, Date expiredDate, Double feeAmount, RentContract rentContract, String paymentTransId, String direction) {
        if (feeDate.after(expiredDate)) {
            List<PaymenttransDtl> paymenttransDtls = new ArrayList<PaymenttransDtl>();
            Date curDateBegin = DateUtils.dateAddDay(expiredDate, 1);
            Date curDateEnd = DateUtils.dateAddMonth2(curDateBegin, 1);
            while (curDateEnd.before(feeDate)) {
                PaymenttransDtl pdl = new PaymenttransDtl();
                pdl.setRentContractId(rentContract.getId());
                pdl.setTransId(paymentTransId);
                pdl.setAmount(rentContract.getRental());
                pdl.setActDate(feeDate);
                pdl.setStartDate(curDateBegin);
                pdl.setExpiredDate(curDateEnd);
                pdl.setDirection(direction);
                paymenttransDtls.add(pdl);
                curDateBegin = DateUtils.dateAddDay(curDateEnd, 1);
                curDateEnd = DateUtils.dateAddMonth2(curDateBegin, 1);
            }
            PaymenttransDtl lastPDL = new PaymenttransDtl();
            lastPDL.setTransId(paymentTransId);
            if (feeAmount > rentContract.getRental()) {
                BigDecimal[] numbers = new BigDecimal(feeAmount).divideAndRemainder(new BigDecimal(rentContract.getRental()));
                lastPDL.setAmount(numbers[1].setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());// 取余数
            } else {
                lastPDL.setAmount(feeAmount);
            }
            lastPDL.setActDate(feeDate);
            lastPDL.setStartDate(curDateBegin);
            lastPDL.setExpiredDate(feeDate);
            lastPDL.setRentContractId(rentContract.getId());
            lastPDL.setDirection(direction);
            paymenttransDtls.add(lastPDL);
            if (CollectionUtils.isNotEmpty(paymenttransDtls)) {
                for (PaymenttransDtl pd : paymenttransDtls) {
                    paymenttransDtlService.save(pd);
                }
            }
        }
    }


    /**
     * 提前退租/特殊退租，把应退房租总金额按照租房合同周期分摊到各个时间段内
     */
    @Transactional(readOnly = false)
    public void shareRetirementAmt(Date feeDate, Double feeAmount, RentContract rentContract, String paymentTransId, String direction) {
        Date contractBeginDate = rentContract.getStartDate();
        Date paidExpiredDate = paymentTransService.analysisMaxIncomedTransDate(rentContract);
        if (paidExpiredDate != null) {
            if (feeDate.after(contractBeginDate) && paidExpiredDate.after(feeDate)) {
                List<PaymenttransDtl> paymenttransDtls = new ArrayList<PaymenttransDtl>();
                Date curDate = contractBeginDate;
                while (curDate.before(paidExpiredDate)) {
                    Date curEndDate = DateUtils.dateAddMonth2(curDate, 1);
                    if (DateUtils.checkYearMonthDaySame(feeDate, curDate) || DateUtils.checkYearMonthDaySame(feeDate, curEndDate) || (feeDate.after(curDate) && feeDate.before(curEndDate))) {
                        PaymenttransDtl pdl = new PaymenttransDtl();
                        pdl.setTransId(paymentTransId);
                        if (feeAmount > rentContract.getRental()) {
                            BigDecimal[] numbers = new BigDecimal(feeAmount).divideAndRemainder(new BigDecimal(rentContract.getRental()));
                            pdl.setAmount(numbers[1].setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());// 取余数
                        } else {
                            pdl.setAmount(feeAmount);
                        }
                        pdl.setStartDate(feeDate);
                        pdl.setExpiredDate(curEndDate);
                        pdl.setActDate(feeDate);
                        pdl.setRentContractId(rentContract.getId());
                        pdl.setDirection(direction);
                        paymenttransDtls.add(pdl);
                    }
                    if (curDate.after(feeDate)) {
                        PaymenttransDtl pdl = new PaymenttransDtl();
                        pdl.setTransId(paymentTransId);
                        pdl.setAmount(rentContract.getRental());
                        pdl.setActDate(feeDate);
                        pdl.setStartDate(curDate);
                        pdl.setExpiredDate(curEndDate);
                        pdl.setRentContractId(rentContract.getId());
                        pdl.setDirection(direction);
                        paymenttransDtls.add(pdl);
                    }
                    curDate = DateUtils.dateAddMonth(curDate, 1);
                }
                if (CollectionUtils.isNotEmpty(paymenttransDtls)) {
                    for (PaymenttransDtl pd : paymenttransDtls) {
                        paymenttransDtlService.save(pd);
                    }
                }
            }
        }
    }

    /**
     * 查询某个时间段下合同列表
     *
     * @param projectId
     * @param startDate
     * @param endDate
     * @return
     */
    public List<RentContract> queryContractListByProjectIdAndDate(@Param("projectId") String projectId, @Param("startDate") Date startDate, @Param("endDate") Date endDate) {
        return rentContractDao.queryContractListByProjectIdAndDate(projectId, startDate, endDate);
    }

    /**
     * 判断房屋是否整租
     *
     * @param houseId
     * @return
     */
    public boolean isWholeRentHouse(String houseId) {
        return dao.isWholeRentHouse(houseId) > 0;
    }

    public RentContract getByRoomId(String roomId) {
        return dao.getByRoomId(roomId);
    }

    public List<String> getTenantPhoneByRoomId(String roomId) {
        RentContract contract = dao.getByRoomId(roomId);
        if (contract != null) {
            List<ContractTenant> contractTenantList = contractTenantDao.getByContractId(contract.getId());
            if (CollectionUtils.isNotEmpty(contractTenantList)) {
                return contractTenantList.stream().map(contractTenant -> tenantDao.get(contractTenant.getTenantId())).map(Tenant::getCellPhone).collect(Collectors.toList());
            }
        }
        return null;
    }

    public List<RentContract> getByRentContract(RentContract rentContract) {
        return dao.getByRentContract(rentContract);
    }

}
