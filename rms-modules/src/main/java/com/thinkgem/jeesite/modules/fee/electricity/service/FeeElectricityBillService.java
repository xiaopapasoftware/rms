/**
 * 小爬爬工作室 Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeElectricityBillDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeElectricityBillVo;
import com.thinkgem.jeesite.modules.fee.enums.FeeBillStatusEnum;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
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
 * 电费账单表实现类 service
 * Table: fee_electricity_bill - 电费账单表
 *
 * @author generator code
 * @since 2017-09-18 08:24:24
 */
@Service
@Transactional(readOnly = true)
public class FeeElectricityBillService extends CrudService<FeeElectricityBillDao, FeeElectricityBill> {

    private Logger logger = LoggerFactory.getLogger(FeeElectricityBillService.class);

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeEleReadFlowService feeEleReadFlowService;

    @Autowired
    private FeeEleChargedFlowService feeEleChargedFlowService;

    public List<FeeElectricityBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity) {
        areaScopeFilter(feeCriteriaEntity, "dsf", "tpp.area_id=sua.area_id");
        return this.dao.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
    }

    public Double getTotalAmount(FeeCriteriaEntity feeCriteriaEntity) {
        return this.dao.getTotalAmount(feeCriteriaEntity);
    }

    @Transactional(readOnly = false)
    public void saveFeeElectricityBill(FeeElectricityBill feeElectricityBill) {
        feeElectricityBill.setEleBillDate(DateUtils.lastDateLocalMouth(feeElectricityBill.getEleBillDate()));
        House house = feeCommonService.getHouseById(feeElectricityBill.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[houseId={}]不存在,请确认", feeElectricityBill.getHouseId());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        FeeElectricityBill existBill = dao.getCurrentBillByDateAndHouseNum(feeElectricityBill.getEleBillDate(), feeElectricityBill.getHouseEleNum());
        if (Optional.ofNullable(existBill).isPresent()) {
            feeElectricityBill.setId(existBill.getId());
            if (existBill.getBillStatus() != null && existBill.getBillStatus() != FeeBillStatusEnum.APP.getValue()
                    && existBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                logger.error("当前账单已经提交不能修改");
                throw new IllegalArgumentException("当前账单已经提交不能修改");
            }
        }

        FeeElectricityBill lastBill = dao.getLastRecord(feeElectricityBill.getId(), feeElectricityBill.getHouseId());
        if (Optional.ofNullable(lastBill).isPresent()) {
            if (lastBill.getElePeakDegree() > feeElectricityBill.getElePeakDegree()) {
                logger.error("当前账单峰值数不能小于上次峰值数");
                throw new IllegalArgumentException("当前账单峰值数不能小于上次峰值数");
            }

            if (lastBill.getEleValleyDegree() > feeElectricityBill.getEleValleyDegree()) {
                logger.error("当前账单谷值数不能小于上次谷值数");
                throw new IllegalArgumentException("当前账单谷值数不能小于上次谷值数");
            }

            if (lastBill.getEleBillDate().compareTo(feeElectricityBill.getEleBillDate()) > 0) {
                logger.error("下月账单已经生成不能修改");
                throw new IllegalArgumentException("下月账单已经生成不能修改");
            }
        }

        feeElectricityBill.setPropertyId(house.getPropertyProject().getId());
        this.save(feeElectricityBill);

        // 判断房屋是否整组，如果整组生成抄表流水记录
        if (StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
            logger.info("生成抄表流水");
            feeEleReadFlowService.saveFeeEleReadFlowByFeeEleBill(feeElectricityBill);
            logger.info("生成收款流水");
            feeEleChargedFlowService.saveFeeEleChargedFlowByFeeEleBill(feeElectricityBill);
        }
    }

    @Transactional(readOnly = false)
    public void deleteFeeElectricityBill(String id) {
        FeeElectricityBill feeElectricityBill = this.get(id);
        if (feeElectricityBill.getBillStatus() != FeeBillStatusEnum.APP.getValue()
                && feeElectricityBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
            throw new IllegalArgumentException("该账单已提交,不能删除");
        }

        FeeElectricityBill lastBill = dao.getLastRecord(feeElectricityBill.getId(), feeElectricityBill.getHouseId());
        if (Optional.ofNullable(lastBill).isPresent()) {
            if (lastBill.getEleBillDate().compareTo(feeElectricityBill.getEleBillDate()) > 0) {
                logger.error("下月账单已经生成不能删除");
                throw new IllegalArgumentException("下月账单已经生成不能删除");
            }
        }

        feeElectricityBill.setId(id);
        feeElectricityBill.setDelFlag(Constants.DEL_FLAG_YES);
        this.save(feeElectricityBill);
        //如果是整租，删除相应生成的记录
        House house = feeCommonService.getHouseById(feeElectricityBill.getHouseId());
        if (Optional.ofNullable(house).isPresent() && StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
            logger.info("删除抄表流水");
            feeEleReadFlowService.deleteFeeEleReadFlowByFeeEleBill(feeElectricityBill.getId());
            logger.info("删除收款流水");
            feeEleChargedFlowService.deleteFeeEleChargedFlowByBusinessIdAndFromSource(feeElectricityBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        }
    }

    @Transactional(readOnly = false)
    public void feeElectricityBillAudit(String status, String... ids) {
        String batchNo = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHMMssSSS");
        List<FeeElectricityBill> feeElectricityBills = dao.getEleBillByIds(ids);
        List<FeeElectricityBill> updEleBills = Lists.newArrayList();
        feeElectricityBills.forEach(f -> {
            switch (status) {
                case "1":
                    if (f.getBillStatus() != FeeBillStatusEnum.APP.getValue() && f.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                        logger.error("户号{}账单当前状态为{},不能提交", f.getHouseEleNum(), FeeBillStatusEnum.fromValue(f.getBillStatus()).getName());
                        throw new IllegalArgumentException("户号[" + f.getHouseEleNum() + "]账单不可提交");
                    }
                    break;
                case "2":
                    if (f.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                        logger.error("户号{}账单当前状态为{},不能同意", f.getHouseEleNum(), FeeBillStatusEnum.fromValue(f.getBillStatus()).getName());
                        throw new IllegalArgumentException("户号[" + f.getHouseEleNum() + "]账单不可同意");
                    }
                    break;
                case "3":
                    if (f.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                        logger.error("户号{}账单当前状态为{},不能驳回", f.getHouseEleNum(), FeeBillStatusEnum.fromValue(f.getBillStatus()).getName());
                        throw new IllegalArgumentException("户号[" + f.getHouseEleNum() + "]账单不可驳回");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("户号[" + f.getHouseEleNum() + "]账单不在处理状态");
            }
            if (StringUtils.isBlank(f.getBatchNo())) {
                f.setBatchNo(batchNo);
            }
            f.setBillStatus(Integer.valueOf(status));
            updEleBills.add(f);
        });

        int ret = dao.batchUpdate(updEleBills);
        logger.info("总共处理{}条数据", ret);
    }
}
