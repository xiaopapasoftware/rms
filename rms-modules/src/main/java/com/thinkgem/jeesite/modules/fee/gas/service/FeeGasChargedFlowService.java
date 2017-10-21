/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.service;

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
import com.thinkgem.jeesite.modules.fee.gas.dao.FeeGasChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasChargedFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasChargedFlowVo;
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
 * <p>煤气收取流水实现类 service</p>
 * <p>Table: fee_gas_charged_flow - 煤气收取流水</p>
 * @since 2017-10-20 06:26:33
 * @author generator code
 */
@Service
@Transactional(readOnly = true)
public class FeeGasChargedFlowService extends CrudService<FeeGasChargedFlowDao, FeeGasChargedFlow>{
    @Autowired
    private FeeConfigService feeConfigService;

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeGasReadFlowService feeGasReadFlowService;

    @Transactional(readOnly = false)
    public void saveFeeGasChargedFlowByFeeGasBill(FeeGasBill feeGasBill) {
        FeeGasChargedFlow saveChargedFlow = new FeeGasChargedFlow();
        saveChargedFlow.setGasAmount(feeGasBill.getGasBillAmount());
        saveChargedFlow.setCreateDate(feeGasBill.getGasBillDate());
        saveChargedFlow.setPropertyId(feeGasBill.getPropertyId());
        saveChargedFlow.setHouseId(feeGasBill.getHouseId());
        saveChargedFlow.setRoomId("0");
        saveChargedFlow.setHouseGasNum(feeGasBill.getHouseGasNum());
        saveChargedFlow.setBusinessId(feeGasBill.getId());
        saveChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        saveChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        saveChargedFlow.setFromSource(FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        saveChargedFlow.setGasCalculateDate(new Date());

        FeeGasChargedFlow existGasChargedFlow = this.dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        if (Optional.ofNullable(existGasChargedFlow).isPresent()) {
            if (existGasChargedFlow.getGenerateOrder() == GenerateOrderEnum.YES.getValue()) {
                throw new IllegalArgumentException("该房屋已经生成订单,不可修改");
            }
            saveChargedFlow.setId(existGasChargedFlow.getId());
        }
        save(saveChargedFlow);
    }

    @Transactional(readOnly = false)
    public void saveFeeGasChargedFlowByFeeGasReadFlow(FeeGasReadFlow feeGasReadFlow) {
        FeeGasChargedFlow feeGasChargedFlow = readFlow2ChargedFlow(feeGasReadFlow);
        FeeGasChargedFlow existChargeFlow = dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue());
        if (Optional.ofNullable(existChargeFlow).isPresent()) {
            feeGasChargedFlow.setId(existChargeFlow.getId());
        }
        save(feeGasChargedFlow);
    }

    @Transactional(readOnly = false)
    public void deleteFeeGasChargedFlowByBusinessIdAndFromSource(String feeGasBillId, int fromSource) {
        FeeGasChargedFlow existGasChargedFlow = this.dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasBillId, fromSource);
        FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
        feeGasChargedFlow.setId(existGasChargedFlow.getId());
        feeGasChargedFlow.setDelFlag(Constants.DEL_FLAG_YES);
        save(feeGasChargedFlow);
    }

    public List<FeeGasChargedFlowVo> getFeeGasChargedFee(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeGasChargedFlow(feeCriteriaEntity);
    }


    private FeeGasChargedFlow readFlow2ChargedFlow(FeeGasReadFlow feeGasReadFlow) {
        FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
        feeGasChargedFlow.setBusinessId(feeGasReadFlow.getId());
        feeGasChargedFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());
        feeGasChargedFlow.setGasCalculateDate(new Date());
        feeGasChargedFlow.setHouseGasNum(feeGasReadFlow.getHouseGasNum());
        feeGasChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        feeGasChargedFlow.setPropertyId(feeGasReadFlow.getPropertyId());
        feeGasChargedFlow.setHouseId(feeGasReadFlow.getHouseId());
        feeGasChargedFlow.setRoomId("0");

        FeeGasReadFlow lastReadFlow = feeGasReadFlowService.getLastReadFlow(feeGasReadFlow);
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            lastReadFlow = new FeeGasReadFlow();
            lastReadFlow.setGasDegree(0F);
        }

        Map<String, FeeConfig> feeConfigMap = feeConfigService.getFeeConfig();
        House house = feeCommonService.getHouseById(feeGasReadFlow.getHouseId());
        String rangeId = feeCommonService.getRangeIdByHouseId(house.getId());
        feeGasChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        FeeConfig feeConfig = feeCommonService.getFeeConfig(feeConfigMap, rangeId, FeeTypeEnum.GAS_UNIT);
        double amount = (feeGasReadFlow.getGasDegree() - lastReadFlow.getGasDegree()) * Float.valueOf(feeConfig.getConfigValue());
        feeGasChargedFlow.setGasAmount(new BigDecimal(amount));
        return feeGasChargedFlow;
    }
}