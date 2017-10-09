/**
 * 小爬爬工作室 Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.fee.common.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.common.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeElectricityBillDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeElectricityBillVo;
import com.thinkgem.jeesite.modules.fee.enums.FeeBillStatusEnum;
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

    public List<FeeElectricityBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity) {
        areaScopeFilter(feeCriteriaEntity, "dsf", "tpp.area_id=sua.area_id");
        return this.dao.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
    }

    public Double getTotalAmount(FeeCriteriaEntity feeCriteriaEntity) {
        return this.dao.getTotalAmount(feeCriteriaEntity);
    }

    public FeeElectricityBillVo getWithProperty(String id, String houseId) {
        FeeElectricityBillVo feeElectricityBillVo = this.dao.getWithProperty(id);
        if (Optional.ofNullable(feeElectricityBillVo).isPresent()) {
            House house = feeCommonService.getHouseById(houseId);
            if (Optional.ofNullable(house).isPresent()) {
                feeElectricityBillVo.setHouseId(house.getHouseId());
                feeElectricityBillVo.setHouseEleNum(house.getEleAccountNum());
                feeElectricityBillVo.setProjectAddress(house.getProjectAddr());
            }
        }
        return feeElectricityBillVo;
    }

    @Transactional(readOnly = false)
    public void saveFeeElectricityBill(FeeElectricityBill feeElectricityBill) {
        House house = feeCommonService.getHouseById(feeElectricityBill.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[id={}]不存在,请确认", feeElectricityBill.getHouseEleNum());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }
        FeeElectricityBill query = new FeeElectricityBill();
        query.setEleBillDate(feeElectricityBill.getEleBillDate());
        query.setHouseEleNum(feeElectricityBill.getHouseEleNum());
        List<FeeElectricityBill> feeElectricityBills = this.findList(query);
        if (Optional.ofNullable(feeElectricityBills).isPresent()
                && feeElectricityBills.size() > 0) {
            FeeElectricityBill existEleBill = feeElectricityBills.get(0);
            feeElectricityBill.setId(existEleBill.getId());
            if (existEleBill.getBillStatus() != null && existEleBill.getBillStatus() != FeeBillStatusEnum.APP.getValue()
                    && existEleBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                logger.error("当前账单已经提交不能修改");
                throw new IllegalArgumentException("当前账单已经提交不能修改");
            }
        }
        feeElectricityBill.setPropertyId(house.getPropertyProject().getId());
        this.save(feeElectricityBill);

        // 判断房屋是否整组，如果整组生成抄表流水记录
        if (StringUtils.equals(house.getIntentMode(), HouseRentMethod.FULL_RENT.value())) {
            logger.info("生成抄表流水");
            feeEleReadFlowService.saveFeeEleReadFlowByFeeEleBill(feeElectricityBill);
            //TODO 生成收款流水

        }
    }

    @Transactional(readOnly = false)
    public void feeElectricityBillAudit(String status, String... ids) {
        String batchNo = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHMMssSSS");
        for (String id : ids) {
            FeeElectricityBill feeElectricityBill = dao.get(id);
            if (Optional.ofNullable(feeElectricityBill).isPresent()) {
                switch (status) {
                    case "1":
                        if (feeElectricityBill.getBillStatus() != FeeBillStatusEnum.APP.getValue() && feeElectricityBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                            logger.error("户号{}当前状态为{},不能提交", feeElectricityBill.getHouseEleNum(), FeeBillStatusEnum.fromValue(feeElectricityBill.getBillStatus()).getName());
                            throw new IllegalArgumentException("户号[" + feeElectricityBill.getHouseEleNum() + "]不可提交");
                        }
                        break;
                    case "2":
                        if (feeElectricityBill.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                            logger.error("户号{}当前状态为{},不能同意", feeElectricityBill.getHouseEleNum(), FeeBillStatusEnum.fromValue(feeElectricityBill.getBillStatus()).getName());
                            throw new IllegalArgumentException("户号[" + feeElectricityBill.getHouseEleNum() + "]不可同意");
                        }
                        break;
                    case "3":
                        if (feeElectricityBill.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                            logger.error("户号{}当前状态为{},不能驳回", feeElectricityBill.getHouseEleNum(), FeeBillStatusEnum.fromValue(feeElectricityBill.getBillStatus()).getName());
                            throw new IllegalArgumentException("户号[" + feeElectricityBill.getHouseEleNum() + "]不可驳回");
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("户号[" + feeElectricityBill.getHouseEleNum() + "]不在处理状态");
                }
                FeeElectricityBill updFeeEleBill = new FeeElectricityBill();
                if (StringUtils.isBlank(feeElectricityBill.getBatchNo())) {
                    updFeeEleBill.setBatchNo(batchNo);
                }
                updFeeEleBill.setBillStatus(Integer.valueOf(status));
                updFeeEleBill.setId(feeElectricityBill.getId());
                this.save(updFeeEleBill);
                //TODO 记录审核日志
            }
        }
    }
}
