/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.enums.*;
import com.thinkgem.jeesite.modules.fee.gas.dao.FeeGasChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasChargedFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasChargedFlowVo;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.enums.HouseStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>煤气收取流水实现类 service</p>
 * <p>Table: fee_gas_charged_flow - 煤气收取流水</p>
 *
 * @author generator code
 * @since 2017-10-20 06:26:33
 */
@Service
@Transactional(readOnly = true)
public class FeeGasChargedFlowService extends CrudService<FeeGasChargedFlowDao, FeeGasChargedFlow> {

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeGasReadFlowService feeGasReadFlowService;

    private FeeGasChargedFlow feeGasBillToFeeGasCharged(FeeGasBill feeGasBill) {
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
        return saveChargedFlow;
    }

    private FeeGasChargedFlow feeGasReadToFeeGasCharged(FeeGasReadFlow feeGasReadFlow) {
        FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
        feeGasChargedFlow.setBusinessId(feeGasReadFlow.getId());
        feeGasChargedFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());
        feeGasChargedFlow.setGasCalculateDate(new Date());
        feeGasChargedFlow.setHouseGasNum(feeGasReadFlow.getHouseGasNum());
        feeGasChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        feeGasChargedFlow.setPropertyId(feeGasReadFlow.getPropertyId());
        feeGasChargedFlow.setHouseId(feeGasReadFlow.getHouseId());
        feeGasChargedFlow.setRoomId("0");
        return feeGasChargedFlow;
    }

    @Transactional(readOnly = false)
    public void saveFeeGasChargedFlowByFeeGasBill(FeeGasBill feeGasBill) {
        House house = feeCommonService.getHouseById(feeGasBill.getHouseId());
        FeeGasChargedFlow saveChargedFlow = feeGasBillToFeeGasCharged(feeGasBill);

        FeeGasChargedFlow existGasChargedFlow = this.dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue(), null);
        if (Optional.ofNullable(existGasChargedFlow).isPresent()) {
            if (existGasChargedFlow.getGenerateOrder() == GenerateOrderEnum.YES.getValue()) {
                throw new IllegalArgumentException("该房屋已经生成订单,不可修改");
            }
            saveChargedFlow.setId(existGasChargedFlow.getId());
        }

        FeeGasReadFlow queryFeeGasReadFlow = new FeeGasReadFlow();
        queryFeeGasReadFlow.setHouseId(feeGasBill.getHouseId());
        queryFeeGasReadFlow.setId(feeGasBill.getId());
        FeeGasReadFlow lastReadFlow = feeGasReadFlowService.getLastReadFlow(queryFeeGasReadFlow);
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            logger.error("当前房屋[燃气户号={}]没有初始化电表数据", feeGasBill.getHouseGasNum());
            throw new IllegalArgumentException("当前房屋没有初始化燃气表数据");
        }

        FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.GAS_UNIT, feeGasBill.getHouseId(), null, ChargeMethodEnum.ACCOUNT_MODEL);

        saveChargedFlow.setGasAmount(new BigDecimal(feeGasBill.getGasDegree()).subtract(new BigDecimal(lastReadFlow.getGasDegree())).multiply(new BigDecimal(feeConfig.getConfigValue())));

        if (StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.PART_RENT.getValue())
                || StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.WHOLE_RENT.getValue())) {
            saveChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
        } else {
            saveChargedFlow.setPayer(PayerEnum.COMPANY.getValue());
        }
        save(saveChargedFlow);
    }

    @Transactional(readOnly = false)
    public void saveFeeGasChargedFlowByFeeGasReadFlow(FeeGasReadFlow feeGasReadFlow) {
        House house = feeCommonService.getHouseById(feeGasReadFlow.getHouseId());
        FeeGasChargedFlow saveFeeGasChargeFlow = feeGasReadToFeeGasCharged(feeGasReadFlow);

        saveFeeGasChargeFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        FeeGasChargedFlow existChargeFlow = dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue(), null);
        if (Optional.ofNullable(existChargeFlow).isPresent()) {
            saveFeeGasChargeFlow.setId(existChargeFlow.getId());

        }

        FeeGasReadFlow lastReadFlow = feeGasReadFlowService.getLastReadFlow(feeGasReadFlow);
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            logger.error("当前房屋[户号={}]没有初始化电表数据", feeGasReadFlow.getHouseGasNum());
            throw new IllegalArgumentException("当前房屋没有初始化电表数据");
        }

        FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.GAS_UNIT, feeGasReadFlow.getHouseId(), null, ChargeMethodEnum.ACCOUNT_MODEL);
        saveFeeGasChargeFlow.setGasAmount(new BigDecimal(feeGasReadFlow.getGasDegree()).subtract(new BigDecimal(lastReadFlow.getGasDegree())).multiply(new BigDecimal(feeConfig.getConfigValue())));

        if (StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.PART_RENT.getValue())
                || StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.WHOLE_RENT.getValue())) {
            saveFeeGasChargeFlow.setPayer(PayerEnum.RENT_USER.getValue());
        } else {
            saveFeeGasChargeFlow.setPayer(PayerEnum.COMPANY.getValue());
        }
        save(saveFeeGasChargeFlow);
    }

    @Transactional(readOnly = false)
    public void deleteFeeGasChargedFlowByBusinessIdAndFromSource(String feeGasBillId, int fromSource) {
        FeeGasChargedFlow existGasChargedFlow = this.dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasBillId, fromSource, null);
        if(Optional.ofNullable(existGasChargedFlow).isPresent()){
            FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
            feeGasChargedFlow.setId(existGasChargedFlow.getId());
            feeGasChargedFlow.setDelFlag(Constants.DEL_FLAG_YES);
            save(feeGasChargedFlow);
        }
    }

    public List<FeeGasChargedFlowVo> getFeeGasChargedFee(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeGasChargedFlow(feeCriteriaEntity);
    }
}