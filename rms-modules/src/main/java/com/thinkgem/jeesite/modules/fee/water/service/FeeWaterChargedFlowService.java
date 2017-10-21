/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.config.service.FeeConfigService;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
import com.thinkgem.jeesite.modules.fee.enums.FeeTypeEnum;
import com.thinkgem.jeesite.modules.fee.enums.GenerateOrderEnum;
import com.thinkgem.jeesite.modules.fee.water.dao.FeeWaterChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterChargedFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterReadFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterChargedFlowVo;
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
 * <p>水收取流水实现类 service</p>
 * <p>Table: fee_water_charged_flow - 水收取流水</p>
 * @since 2017-10-20 06:26:08
 * @author generator code
 */
@Service
@Transactional(readOnly = true)
public class FeeWaterChargedFlowService extends CrudService<FeeWaterChargedFlowDao, FeeWaterChargedFlow> {

    @Autowired
    private FeeConfigService feeConfigService;

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeWaterReadFlowService feeWaterReadFlowService;

    @Transactional(readOnly = false)
    public void saveFeeWaterChargedFlowByFeeWaterBill(FeeWaterBill feeWaterBill) {
        FeeWaterChargedFlow saveChargedFlow = new FeeWaterChargedFlow();
        saveChargedFlow.setWaterAmount(feeWaterBill.getWaterBillAmount());
        saveChargedFlow.setCreateDate(feeWaterBill.getWaterBillDate());
        saveChargedFlow.setPropertyId(feeWaterBill.getPropertyId());
        saveChargedFlow.setHouseId(feeWaterBill.getHouseId());
        saveChargedFlow.setRoomId("0");
        saveChargedFlow.setHouseWaterNum(feeWaterBill.getHouseWaterNum());
        saveChargedFlow.setBusinessId(feeWaterBill.getId());
        saveChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        saveChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        saveChargedFlow.setFromSource(FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        saveChargedFlow.setWaterCalculateDate(new Date());

        FeeWaterChargedFlow existWaterChargedFlow = this.dao.getFeeWaterChargedFlowByBusinessIdAndFromSource(feeWaterBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        if (Optional.ofNullable(existWaterChargedFlow).isPresent()) {
            if (existWaterChargedFlow.getGenerateOrder() == GenerateOrderEnum.YES.getValue()) {
                throw new IllegalArgumentException("该房屋已经生成订单,不可修改");
            }
            saveChargedFlow.setId(existWaterChargedFlow.getId());
        }
        save(saveChargedFlow);
    }

    @Transactional(readOnly = false)
    public void saveFeeWaterChargedFlowByFeeWaterReadFlow(FeeWaterReadFlow feeWaterReadFlow) {
        FeeWaterChargedFlow feeWaterChargedFlow = readFlow2ChargedFlow(feeWaterReadFlow);
        FeeWaterChargedFlow existChargeFlow = dao.getFeeWaterChargedFlowByBusinessIdAndFromSource(feeWaterReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue());
        if (Optional.ofNullable(existChargeFlow).isPresent()) {
            feeWaterChargedFlow.setId(existChargeFlow.getId());
        }
        save(feeWaterChargedFlow);
    }

    @Transactional(readOnly = false)
    public void deleteFeeWaterChargedFlowByBusinessIdAndFromSource(String feeWaterBillId, int fromSource) {
        FeeWaterChargedFlow existWaterChargedFlow = this.dao.getFeeWaterChargedFlowByBusinessIdAndFromSource(feeWaterBillId, fromSource);
        FeeWaterChargedFlow feeWaterChargedFlow = new FeeWaterChargedFlow();
        feeWaterChargedFlow.setId(existWaterChargedFlow.getId());
        feeWaterChargedFlow.setDelFlag(Constants.DEL_FLAG_YES);
        save(feeWaterChargedFlow);
    }

    public List<FeeWaterChargedFlowVo> getFeeWaterChargedFee(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeWaterChargedFlow(feeCriteriaEntity);
    }


    private FeeWaterChargedFlow readFlow2ChargedFlow(FeeWaterReadFlow feeWaterReadFlow) {
        FeeWaterChargedFlow feeWaterChargedFlow = new FeeWaterChargedFlow();
        feeWaterChargedFlow.setBusinessId(feeWaterReadFlow.getId());
        feeWaterChargedFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());
        feeWaterChargedFlow.setWaterCalculateDate(new Date());
        feeWaterChargedFlow.setHouseWaterNum(feeWaterReadFlow.getHouseWaterNum());
        feeWaterChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        feeWaterChargedFlow.setPropertyId(feeWaterReadFlow.getPropertyId());
        feeWaterChargedFlow.setHouseId(feeWaterReadFlow.getHouseId());
        feeWaterChargedFlow.setRoomId("0");
        FeeWaterReadFlow lastReadFlow = feeWaterReadFlowService.getLastReadFlow(feeWaterReadFlow);
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            lastReadFlow = new FeeWaterReadFlow();
            lastReadFlow.setWaterDegree(0F);
        }

        Map<String, FeeConfig> feeConfigMap = feeConfigService.getFeeConfig();
        House house = feeCommonService.getHouseById(feeWaterReadFlow.getHouseId());
        String rangeId = feeCommonService.getRangeIdByHouseId(house.getId());
        feeWaterChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        FeeConfig feeConfig = feeCommonService.getFeeConfig(feeConfigMap, rangeId, FeeTypeEnum.WATER_UNIT);
        double amount = (feeWaterReadFlow.getWaterDegree() - lastReadFlow.getWaterDegree()) * Float.valueOf(feeConfig.getConfigValue());
        feeWaterChargedFlow.setWaterAmount(new BigDecimal(amount));
        return feeWaterChargedFlow;
    }
}