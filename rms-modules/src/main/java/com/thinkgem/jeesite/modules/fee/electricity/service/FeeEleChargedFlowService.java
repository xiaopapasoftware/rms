/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.common.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.config.service.FeeConfigService;
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeEleChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleChargedFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleChargedFlowVo;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
import com.thinkgem.jeesite.modules.fee.enums.FeeTypeEnum;
import com.thinkgem.jeesite.modules.fee.enums.GenerateOrderEnum;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>电费收取流水实现类 service</p>
 * <p>Table: fee_ele_charged_flow - 电费收取流水</p>
 *
 * @author generator code
 * @since 2017-09-18 08:24:32
 */
@Service
@Transactional(readOnly = true)
public class FeeEleChargedFlowService extends CrudService<FeeEleChargedFlowDao, FeeEleChargedFlow> {

    @Autowired
    private FeeConfigService feeConfigService;

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeEleReadFlowService feeEleReadFlowService;

    @Transactional(readOnly = false)
    public void saveFeeEleChargedFlowByFeeEleBill(FeeElectricityBill feeElectricityBill) {
        FeeEleChargedFlow saveChargedFlow = new FeeEleChargedFlow();
        saveChargedFlow.setEleAmount(feeElectricityBill.getEleBillAmount());
        saveChargedFlow.setCreateDate(feeElectricityBill.getEleBillDate());
        saveChargedFlow.setPropertyId(feeElectricityBill.getPropertyId());
        saveChargedFlow.setHouseId(feeElectricityBill.getHouseId());
        saveChargedFlow.setRoomId("0");
        saveChargedFlow.setHouseEleNum(feeElectricityBill.getHouseEleNum());
        saveChargedFlow.setBusinessId(feeElectricityBill.getId());
        saveChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        saveChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        saveChargedFlow.setFromSource(FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        saveChargedFlow.setEleCalculateDate(new Date());

        FeeEleChargedFlow existEleChargedFlow = this.dao.getFeeEleChargedFlowByBusinessIdAndFromSource(feeElectricityBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        if (Optional.ofNullable(existEleChargedFlow).isPresent()) {
            if (existEleChargedFlow.getGenerateOrder() == GenerateOrderEnum.YES.getValue()) {
                throw new IllegalArgumentException("该房屋已经生成订单,不可修改");
            }
            saveChargedFlow.setId(existEleChargedFlow.getId());
        }
        save(saveChargedFlow);
    }

    @Transactional(readOnly = false)
    public void saveFeeEleChargedFlowByFeeEleReadFlow(FeeEleReadFlow feeEleReadFlow) {
        FeeEleChargedFlow feeEleChargedFlow = readFlow2ChargedFlow(feeEleReadFlow);
        FeeEleChargedFlow existChargeFlow = dao.getFeeEleChargedFlowByBusinessIdAndFromSource(feeEleReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue());
        if (Optional.ofNullable(existChargeFlow).isPresent()) {
            feeEleChargedFlow.setId(existChargeFlow.getId());
        }
        save(feeEleChargedFlow);
    }

    @Transactional(readOnly = false)
    public void deleteFeeEleChargedFlowByBusinessIdAndFromSource(String feeEleBillId, int fromSource) {
        FeeEleChargedFlow existEleChargedFlow = this.dao.getFeeEleChargedFlowByBusinessIdAndFromSource(feeEleBillId, fromSource);
        FeeEleChargedFlow feeEleChargedFlow = new FeeEleChargedFlow();
        feeEleChargedFlow.setId(existEleChargedFlow.getId());
        feeEleChargedFlow.setDelFlag(Constants.DEL_FLAG_YES);
        save(feeEleChargedFlow);
    }

    public List<FeeEleChargedFlowVo> getFeeEleChargedFee(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeEleChargedFlow(feeCriteriaEntity);
    }


    private FeeEleChargedFlow readFlow2ChargedFlow(FeeEleReadFlow feeEleReadFlow) {
        FeeEleChargedFlow feeEleChargedFlow = new FeeEleChargedFlow();
        feeEleChargedFlow.setBusinessId(feeEleReadFlow.getId());
        feeEleChargedFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());
        feeEleChargedFlow.setEleCalculateDate(new Date());
        feeEleChargedFlow.setHouseEleNum(feeEleReadFlow.getHouseEleNum());
        feeEleChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        feeEleChargedFlow.setPropertyId(feeEleReadFlow.getPropertyId());
        feeEleChargedFlow.setHouseId(feeEleReadFlow.getHouseId());
        feeEleChargedFlow.setRoomId(feeEleReadFlow.getRoomId());

        FeeEleReadFlow lastReadFlow = feeEleReadFlowService.getLastReadFlow(feeEleReadFlow);
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            lastReadFlow = new FeeEleReadFlow();
            lastReadFlow.setEleDegree(0F);
            lastReadFlow.setElePeakDegree(0F);
            lastReadFlow.setEleValleyDegree(0F);
        }

        Map<String, FeeConfig> feeConfigMap = feeConfigService.getFeeConfig();
        House house = feeCommonService.getHouseById(feeEleReadFlow.getHouseId());
        String rangeId;
        if (house.getIntentMode() == RentModelTypeEnum.WHOLE_RENT.getValue()) {
            feeEleChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
            rangeId = dao.getRangeIdByHouseId(house.getId());
            FeeConfig peakFeeConfig = feeCommonService.getFeeConfig(feeConfigMap, rangeId, FeeTypeEnum.ELE_VALLEY_UNIT);
            FeeConfig valleyFeeConfig = feeCommonService.getFeeConfig(feeConfigMap, rangeId, FeeTypeEnum.ELE_VALLEY_UNIT);
            double peakAmount = (feeEleReadFlow.getElePeakDegree() - lastReadFlow.getElePeakDegree()) * Float.valueOf(peakFeeConfig.getConfigValue());
            double valleyAmount = (feeEleReadFlow.getEleValleyDegree() - lastReadFlow.getEleValleyDegree()) * Float.valueOf(valleyFeeConfig.getConfigValue());
            feeEleChargedFlow.setElePeakAmount(new BigDecimal(peakAmount));
            feeEleChargedFlow.setEleValleyAmount(new BigDecimal(valleyAmount));
            feeEleChargedFlow.setEleAmount(new BigDecimal(peakAmount).add(new BigDecimal(valleyAmount)));
        } else {
            feeEleChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()));
            if (StringUtils.isBlank(feeEleReadFlow.getRoomId()) || StringUtils.equals(feeEleReadFlow.getRoomId(), "0")) {
                rangeId = dao.getRangeIdByHouseId(house.getId());
            } else {
                rangeId = dao.getRangeIdByRoomId(feeEleReadFlow.getRoomId());
            }
            FeeConfig feeConfig = feeCommonService.getFeeConfig(feeConfigMap, rangeId, FeeTypeEnum.ELECTRICITY_UNIT);
            double amount = (feeEleReadFlow.getEleDegree() - lastReadFlow.getEleDegree()) * Float.valueOf(feeConfig.getConfigValue());
            feeEleChargedFlow.setEleAmount(new BigDecimal(amount));
        }
        return feeEleChargedFlow;
    }
}