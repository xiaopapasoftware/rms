/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.enums.FeeBillStatusEnum;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
import com.thinkgem.jeesite.modules.fee.water.dao.FeeWaterBillDao;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterBillVo;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.enums.HouseRentMethod;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>水费账单表实现类 service</p>
 * <p>Table: fee_water_bill - 水费账单表</p>
 * @since 2017-10-20 06:25:59
 * @author generator code
 */
@Service
@Transactional(readOnly = true)
public class FeeWaterBillService extends CrudService<FeeWaterBillDao, FeeWaterBill>{

    private Logger logger = LoggerFactory.getLogger(FeeWaterBillService.class);

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeWaterReadFlowService feeWaterReadFlowService;

    @Autowired
    private FeeWaterChargedFlowService feeWaterChargedFlowService;

    public List<FeeWaterBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity) {
        areaScopeFilter(feeCriteriaEntity, "dsf", "tpp.area_id=sua.area_id");
        return this.dao.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
    }

    public Double getTotalAmount(FeeCriteriaEntity feeCriteriaEntity) {
        return this.dao.getTotalAmount(feeCriteriaEntity);
    }

    public FeeWaterBillVo getWithProperty(String id, String houseId) {
        FeeWaterBillVo feeWaterBillVo = this.dao.getWithProperty(id);
        if (Optional.ofNullable(feeWaterBillVo).isPresent()) {
            House house = feeCommonService.getHouseById(houseId);
            if (Optional.ofNullable(house).isPresent()) {
                feeWaterBillVo.setHouseId(house.getHouseId());
                feeWaterBillVo.setHouseWaterNum(house.getWaterAccountNum());
                feeWaterBillVo.setProjectAddress(house.getProjectAddr());
            }
        }
        return feeWaterBillVo;
    }

    @Transactional(readOnly = false)
    public void saveFeeWaterBill(FeeWaterBill feeWaterBill) {
        House house = feeCommonService.getHouseById(feeWaterBill.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[id={}]不存在,请确认", feeWaterBill.getHouseWaterNum());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        FeeWaterBill query = new FeeWaterBill();
        query.setWaterBillDate(feeWaterBill.getWaterBillDate());
        query.setHouseWaterNum(feeWaterBill.getHouseWaterNum());
        List<FeeWaterBill> feeWaterBills = this.findList(query);
        if (Optional.ofNullable(feeWaterBills).isPresent()
                && feeWaterBills.size() > 0) {
            FeeWaterBill existWaterBill = feeWaterBills.get(0);
            feeWaterBill.setId(existWaterBill.getId());
            if (existWaterBill.getBillStatus() != null && existWaterBill.getBillStatus() != FeeBillStatusEnum.APP.getValue()
                    && existWaterBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                logger.error("当前账单已经提交不能修改");
                throw new IllegalArgumentException("当前账单已经提交不能修改");
            }
        }

        FeeWaterBill lastBill = dao.getLastWaterBill(feeWaterBill);
        if(Optional.ofNullable(lastBill).isPresent()){
            if(lastBill.getWaterDegree() > feeWaterBill.getWaterDegree()){
                logger.error("当前账单抄表数不能小于上次抄表数");
                throw new IllegalArgumentException("当前账单峰值数不能小于上次峰值数");
            }

            if(lastBill.getWaterBillDate().compareTo(feeWaterBill.getWaterBillDate()) > 0){
                logger.error("下月账单已经生成不能修改");
                throw new IllegalArgumentException("下月账单已经生成不能修改");
            }
        }

        feeWaterBill.setPropertyId(house.getPropertyProject().getId());
        this.save(feeWaterBill);

        // 判断房屋是否整组，如果整组生成抄表流水记录
        if (StringUtils.equals(house.getIntentMode(), HouseRentMethod.FULL_RENT.value())) {
            logger.info("生成抄表流水");
            feeWaterReadFlowService.saveFeeWaterReadFlowByFeeWaterBill(feeWaterBill);
            logger.info("生成收款流水");
            feeWaterChargedFlowService.saveFeeWaterChargedFlowByFeeWaterBill(feeWaterBill);
        }
    }

    @Transactional(readOnly = false)
    public void deleteFeeWaterBill(String id) {
        FeeWaterBill feeWaterBill = this.get(id);
        if (feeWaterBill.getBillStatus() != FeeBillStatusEnum.COMMIT.getValue()) {
            throw new IllegalArgumentException("该账单已提交,不能删除");
        }

        FeeWaterBill lastBill = dao.getLastWaterBill(feeWaterBill);
        if(Optional.ofNullable(lastBill).isPresent()){
            if(lastBill.getWaterBillDate().compareTo(feeWaterBill.getWaterBillDate()) > 0){
                logger.error("下月账单已经生成不能删除");
                throw new IllegalArgumentException("下月账单已经生成不能删除");
            }
        }

        feeWaterBill.setId(id);
        feeWaterBill.setDelFlag(Constants.DEL_FLAG_YES);
        this.save(feeWaterBill);
        //如果是整租，删除相应生成的记录
        House house = feeCommonService.getHouseById(feeWaterBill.getHouseId());
        if (Optional.ofNullable(house).isPresent() && StringUtils.equals(house.getIntentMode(), HouseRentMethod.FULL_RENT.value())) {
            logger.info("删除抄表流水");
            feeWaterReadFlowService.deleteFeeWaterReadFlowByFeeWaterBill(feeWaterBill.getId());
            logger.info("删除收款流水");
            feeWaterChargedFlowService.deleteFeeWaterChargedFlowByBusinessIdAndFromSource(feeWaterBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        }
    }

    @Transactional(readOnly = false)
    public void feeWaterBillAudit(String status, String... ids) {
        String batchNo = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHMMssSSS");
        for (String id : ids) {
            FeeWaterBill feeWaterBill = dao.get(id);
            if (Optional.ofNullable(feeWaterBill).isPresent()) {
                switch (status) {
                    case "1":
                        if (feeWaterBill.getBillStatus() != FeeBillStatusEnum.APP.getValue() && feeWaterBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                            logger.error("户号{}当前状态为{},不能提交", feeWaterBill.getHouseWaterNum(), FeeBillStatusEnum.fromValue(feeWaterBill.getBillStatus()).getName());
                            throw new IllegalArgumentException("户号[" + feeWaterBill.getHouseWaterNum() + "]不可提交");
                        }
                        break;
                    case "2":
                        if (feeWaterBill.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                            logger.error("户号{}当前状态为{},不能同意", feeWaterBill.getHouseWaterNum(), FeeBillStatusEnum.fromValue(feeWaterBill.getBillStatus()).getName());
                            throw new IllegalArgumentException("户号[" + feeWaterBill.getHouseWaterNum() + "]不可同意");
                        }
                        break;
                    case "3":
                        if (feeWaterBill.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                            logger.error("户号{}当前状态为{},不能驳回", feeWaterBill.getHouseWaterNum(), FeeBillStatusEnum.fromValue(feeWaterBill.getBillStatus()).getName());
                            throw new IllegalArgumentException("户号[" + feeWaterBill.getHouseWaterNum() + "]不可驳回");
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("户号[" + feeWaterBill.getHouseWaterNum() + "]不在处理状态");
                }
                FeeWaterBill updFeeWaterBill = new FeeWaterBill();
                if (StringUtils.isBlank(feeWaterBill.getBatchNo())) {
                    updFeeWaterBill.setBatchNo(batchNo);
                }
                updFeeWaterBill.setBillStatus(Integer.valueOf(status));
                updFeeWaterBill.setId(feeWaterBill.getId());
                this.save(updFeeWaterBill);
                //TODO 记录审核日志
            }
        }
    }
}