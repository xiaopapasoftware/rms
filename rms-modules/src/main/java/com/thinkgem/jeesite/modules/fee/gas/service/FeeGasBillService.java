/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.enums.FeeBillStatusEnum;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
import com.thinkgem.jeesite.modules.fee.gas.dao.FeeGasBillDao;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasBillVo;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>燃气账单表实现类 service</p>
 * <p>Table: fee_gas_bill - 燃气账单表</p>
 *
 * @author generator code
 * @since 2017-10-20 06:26:27
 */
@Service
@Transactional(readOnly = true)
public class FeeGasBillService extends CrudService<FeeGasBillDao, FeeGasBill> {

    private Logger logger = LoggerFactory.getLogger(FeeGasBillService.class);

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeGasReadFlowService feeGasReadFlowService;

    @Autowired
    private FeeGasChargedFlowService feeGasChargedFlowService;

    public List<FeeGasBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity) {
        areaScopeFilter(feeCriteriaEntity, "dsf", "tpp.area_id=sua.area_id");
        return this.dao.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
    }

    public Double getTotalAmount(FeeCriteriaEntity feeCriteriaEntity) {
        return this.dao.getTotalAmount(feeCriteriaEntity);
    }

    @Transactional(readOnly = false)
    public void saveFeeGasBill(FeeGasBill feeGasBill) {
        feeGasBill.setGasBillDate(DateUtils.lastDateLocalMouth(feeGasBill.getGasBillDate()));
        House house = feeCommonService.getHouseById(feeGasBill.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[id={}]不存在,请确认", feeGasBill.getHouseId());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        FeeGasBill existBill = dao.getCurrentBillByDateAndHouseId(feeGasBill.getGasBillDate(), feeGasBill.getHouseId());
        if (Optional.ofNullable(existBill).isPresent()) {
            feeGasBill.setId(existBill.getId());
            if (existBill.getBillStatus() != null && existBill.getBillStatus() != FeeBillStatusEnum.APP.getValue()
                    && existBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                logger.error("当前账单已经提交不能修改");
                throw new IllegalArgumentException("当前账单已经提交不能修改");
            }
        }

        FeeGasBill lastBill = dao.getLastRecord(feeGasBill.getId(), feeGasBill.getHouseId());
        if (Optional.ofNullable(lastBill).isPresent()) {
            if (lastBill.getGasDegree() > feeGasBill.getGasDegree()) {
                logger.error("当前账单抄表数不能小于上次抄表数");
                throw new IllegalArgumentException("当前账单峰值数不能小于上次峰值数");
            }

            if (lastBill.getGasBillDate().compareTo(feeGasBill.getGasBillDate()) > 0) {
                logger.error("下月账单已经生成不能修改");
                throw new IllegalArgumentException("下月账单已经生成不能修改");
            }
        }

        feeGasBill.setPropertyId(house.getPropertyProject().getId());
        this.save(feeGasBill);

        /*判断房屋是否整组，如果整组生成抄表流水记录*/
        if (StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
            logger.info("生成抄表流水");
            feeGasReadFlowService.saveFeeGasReadFlowByFeeGasBill(feeGasBill);
            logger.info("生成收款流水");
            feeGasChargedFlowService.saveFeeGasChargedFlowByFeeGasBill(feeGasBill);
        }
    }

    @Transactional(readOnly = false)
    public void deleteFeeGasBill(String id) {
        FeeGasBill feeGasBill = this.get(id);
        if (feeGasBill.getBillStatus() != FeeBillStatusEnum.APP.getValue()
                && feeGasBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
            logger.error("该账单[id={}]已提交,不能删除", id);
            throw new IllegalArgumentException("该账单已提交,不能删除");
        }

        FeeGasBill lastBill = dao.getLastRecord(feeGasBill.getId(), feeGasBill.getHouseId());
        if (Optional.ofNullable(lastBill).isPresent()) {
            if (lastBill.getGasBillDate().compareTo(feeGasBill.getGasBillDate()) > 0) {
                logger.error("下月账单已经生成不能删除");
                throw new IllegalArgumentException("下月账单已经生成不能删除");
            }
        }

        feeGasBill.setId(id);
        feeGasBill.setDelFlag(Constants.DEL_FLAG_YES);
        this.save(feeGasBill);
        /*如果是整租，删除相应生成的记录*/
        House house = feeCommonService.getHouseById(feeGasBill.getHouseId());
        if (Optional.ofNullable(house).isPresent() && StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
            logger.info("删除抄表流水");
            feeGasReadFlowService.deleteFeeGasReadFlowByFeeGasBill(feeGasBill.getId());
            logger.info("删除收款流水");
            feeGasChargedFlowService.deleteFeeGasChargedFlowByBusinessIdAndFromSource(feeGasBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        }
    }

    @Transactional(readOnly = false)
    public void feeGasBillAudit(String status, String... ids) {
        String batchNo = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHMMssSSS");
        List<FeeGasBill> feeGasBills = dao.getGasBillByIds(ids);
        List<FeeGasBill> updGasBills = Lists.newArrayList();
        feeGasBills.forEach(f -> {
            switch (status) {
                case "1":
                    if (f.getBillStatus() != FeeBillStatusEnum.APP.getValue() && f.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                        logger.error("户号{}账单当前状态为{},不能提交", f.getHouseGasNum(), FeeBillStatusEnum.fromValue(f.getBillStatus()).getName());
                        throw new IllegalArgumentException("户号[" + f.getHouseGasNum() + "]账单不可提交");
                    }
                    break;
                case "2":
                    if (f.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                        logger.error("户号{}账单当前状态为{},不能同意", f.getHouseGasNum(), FeeBillStatusEnum.fromValue(f.getBillStatus()).getName());
                        throw new IllegalArgumentException("户号[" + f.getHouseGasNum() + "]账单不可同意");
                    }
                    break;
                case "3":
                    if (f.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                        logger.error("户号{}账单当前状态为{},不能驳回", f.getHouseGasNum(), FeeBillStatusEnum.fromValue(f.getBillStatus()).getName());
                        throw new IllegalArgumentException("户号[" + f.getHouseGasNum() + "]账单不可驳回");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("户号[" + f.getHouseGasNum() + "]账单不在处理状态");

            }
            if (StringUtils.isBlank(f.getBatchNo())) {
                f.setBatchNo(batchNo);
            }
            f.setBillStatus(Integer.valueOf(status));
            f.preUpdate();
            updGasBills.add(f);
        });
        int ret = dao.batchUpdate(updGasBills);
        logger.info("总共处理{}条数据", ret);
    }
}