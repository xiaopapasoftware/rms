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
import com.thinkgem.jeesite.modules.fee.enums.ChargeMethodEnum;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
import com.thinkgem.jeesite.modules.fee.enums.FeeTypeEnum;
import com.thinkgem.jeesite.modules.fee.enums.GenerateOrderEnum;
import com.thinkgem.jeesite.modules.fee.water.dao.FeeWaterChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterChargedFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterReadFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterChargedFlowVo;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
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
 *
 * @author generator code
 * @since 2017-10-20 06:26:08
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
        Map<String, FeeConfig> feeConfigMap = feeConfigService.getFeeConfig();
        House house = feeCommonService.getHouseById(feeWaterBill.getHouseId());
        String rangeId = feeCommonService.getRangeIdByHouseId(house.getId());
        FeeConfig feeConfig = feeCommonService.getFeeConfig(feeConfigMap, rangeId, FeeTypeEnum.GAS_UNIT);
        if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            return;
        }

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

        List<FeeWaterChargedFlow> existWaterChargedFlows = this.dao.getFeeWaterChargedFlowByBusinessIdAndFromSourceAndRoom(feeWaterBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue(), null);
        if (Optional.ofNullable(existWaterChargedFlows).isPresent() && existWaterChargedFlows.size() > 0) {
            if (existWaterChargedFlows.get(0).getGenerateOrder() == GenerateOrderEnum.YES.getValue()) {
                throw new IllegalArgumentException("该房屋已经生成订单,不可修改");
            }
            saveChargedFlow.setId(existWaterChargedFlows.get(0).getId());
        }
        save(saveChargedFlow);
    }

    @Transactional(readOnly = false)
    public void saveFeeWaterChargedFlowByFeeWaterReadFlow(FeeWaterReadFlow feeWaterReadFlow) {
        Map<String, FeeConfig> feeConfigMap = feeConfigService.getFeeConfig();
        House house = feeCommonService.getHouseById(feeWaterReadFlow.getHouseId());
        String rangeId = feeCommonService.getRangeIdByHouseId(house.getId());
        FeeConfig feeConfig = feeCommonService.getFeeConfig(feeConfigMap, rangeId, FeeTypeEnum.WATER_UNIT);
        if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            return;
        }

        double chargeFee = calculateCharge(feeWaterReadFlow, feeConfig);
        if (house.getIntentMode() == RentModelTypeEnum.JOINT_RENT.getValue()) {
            List<Room> rooms = feeCommonService.getRoomByHouseId(feeWaterReadFlow.getHouseId());
            if (Optional.ofNullable(rooms).isPresent() && rooms.size() > 0) {
                for (Room room : rooms) {
                    FeeWaterChargedFlow saveFeeWaterChargeFlow = readFlow2ChargedFlow(feeWaterReadFlow);
                    saveFeeWaterChargeFlow.setRoomId(room.getId());
                    saveFeeWaterChargeFlow.setRentType(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()));
                    saveFeeWaterChargeFlow.setWaterAmount(new BigDecimal(chargeFee).divide(new BigDecimal(rooms.size())));
                    List<FeeWaterChargedFlow> existChargeFlows = dao.getFeeWaterChargedFlowByBusinessIdAndFromSourceAndRoom(feeWaterReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue(), room.getId());
                    if (Optional.ofNullable(existChargeFlows).isPresent() && existChargeFlows.size() > 0) {
                        saveFeeWaterChargeFlow.setId(existChargeFlows.get(0).getId());

                    }
                    save(saveFeeWaterChargeFlow);
                }
            } else {
                throw new IllegalArgumentException("房屋[" + house.getHouseCode() + "]是整租,却没有找到房间");
            }
        } else {
            FeeWaterChargedFlow saveFeeWaterChargeFlow = readFlow2ChargedFlow(feeWaterReadFlow);
            saveFeeWaterChargeFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
            List<FeeWaterChargedFlow> existChargeFlows = dao.getFeeWaterChargedFlowByBusinessIdAndFromSourceAndRoom(feeWaterReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue(), null);
            if (Optional.ofNullable(existChargeFlows).isPresent() && existChargeFlows.size() > 0) {
                saveFeeWaterChargeFlow.setId(existChargeFlows.get(0).getId());

            }
            saveFeeWaterChargeFlow.setWaterAmount(new BigDecimal(chargeFee));
            save(saveFeeWaterChargeFlow);
        }
    }

    @Transactional(readOnly = false)
    public void deleteFeeWaterChargedFlowByBusinessIdAndFromSource(String feeWaterBillId, int fromSource) {
        List<FeeWaterChargedFlow> existWaterChargedFlows = this.dao.getFeeWaterChargedFlowByBusinessIdAndFromSourceAndRoom(feeWaterBillId, fromSource, null);
        if (Optional.ofNullable(existWaterChargedFlows).isPresent() && existWaterChargedFlows.size() > 0) {
            for (FeeWaterChargedFlow feeWaterChargedFlow : existWaterChargedFlows) {
                FeeWaterChargedFlow updDeeWaterChargeFlow = new FeeWaterChargedFlow();
                updDeeWaterChargeFlow.setId(feeWaterChargedFlow.getId());
                updDeeWaterChargeFlow.setDelFlag(Constants.DEL_FLAG_YES);
                save(updDeeWaterChargeFlow);
            }
        }
    }

    public List<FeeWaterChargedFlowVo> getFeeWaterChargedFee(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeWaterChargedFlow(feeCriteriaEntity);
    }

    private double calculateCharge(FeeWaterReadFlow feeWaterReadFlow, FeeConfig feeConfig) {
        FeeWaterReadFlow lastReadFlow = feeWaterReadFlowService.getLastReadFlow(feeWaterReadFlow);
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            lastReadFlow = new FeeWaterReadFlow();
            lastReadFlow.setWaterDegree(0F);
        }
        double amount = (feeWaterReadFlow.getWaterDegree() - lastReadFlow.getWaterDegree()) * Float.valueOf(feeConfig.getConfigValue());
        return amount;
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
        return feeWaterChargedFlow;
    }
}