/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.enums.*;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasChargedFlow;
import com.thinkgem.jeesite.modules.fee.water.dao.FeeWaterChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterChargedFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterReadFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterChargedFlowVo;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.enums.HouseStatusEnum;
import com.thinkgem.jeesite.modules.inventory.enums.RoomStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeWaterReadFlowService feeWaterReadFlowService;

    @Transactional(readOnly = false)
    public void saveFeeWaterChargedFlowByFeeWaterBill(FeeWaterBill feeWaterBill) {
        FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.WATER_UNIT, feeWaterBill.getHouseId(), null);
        if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            logger.info("房屋[houseId={}]为固定模式,不生成收费流水", feeWaterBill.getHouseId());
            throw new IllegalArgumentException("当前房屋为固定模式,不能生成收费记录");
        }

        FeeWaterChargedFlow feeWaterChargedFlow = feeWaterBillToFeeWaterCharged(feeWaterBill);

        FeeWaterChargedFlow existWaterChargedFlow = this.dao.getFeeWaterChargedFlowByBusinessIdAndFromSourceAndRoom(feeWaterBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue(), null);
        if (Optional.ofNullable(existWaterChargedFlow).isPresent()) {
            if (existWaterChargedFlow.getGenerateOrder() == GenerateOrderEnum.YES.getValue()) {
                throw new IllegalArgumentException("该房屋已经生成订单,不可修改");
            }
            feeWaterChargedFlow.setId(existWaterChargedFlow.getId());
        }

        FeeWaterReadFlow queryFeeWaterReadFlow = new FeeWaterReadFlow();
        queryFeeWaterReadFlow.setHouseId(feeWaterBill.getHouseId());
        queryFeeWaterReadFlow.setId(feeWaterBill.getId());
        FeeWaterReadFlow lastReadFlow = feeWaterReadFlowService.getLastReadFlow(queryFeeWaterReadFlow);
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            logger.error("当前房屋[水户号={}]没有初始化电表数据", feeWaterBill.getHouseWaterNum());
            throw new IllegalArgumentException("当前房屋没有初始化水表数据");
        }

        feeWaterChargedFlow.setWaterAmount(new BigDecimal(feeWaterBill.getWaterDegree()).subtract(new BigDecimal(lastReadFlow.getWaterDegree())).multiply(new BigDecimal(feeConfig.getConfigValue())));

        House house = feeCommonService.getHouseById(feeWaterBill.getHouseId());
        if (StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.PART_RENT.getValue())
                || StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.WHOLE_RENT.getValue())) {
            feeWaterChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
        } else {
            feeWaterChargedFlow.setPayer(PayerEnum.COMPANY.getValue());
        }

        save(feeWaterChargedFlow);
    }

    @Transactional(readOnly = false)
    public void saveFeeWaterChargedFlowByFeeWaterReadFlow(FeeWaterReadFlow feeWaterReadFlow) {
        FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.WATER_UNIT, feeWaterReadFlow.getHouseId(), null);
        if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            logger.error("当前房屋[houseId={}]为固定模式,不能生成收费记录", feeWaterReadFlow.getHouseId());
            return;
        }

        FeeWaterChargedFlow feeWaterChargedFlow = readFlowToChargedFlow(feeWaterReadFlow);

        feeWaterChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        FeeWaterChargedFlow existChargeFlow = dao.getFeeWaterChargedFlowByBusinessIdAndFromSourceAndRoom(feeWaterReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue(), null);
        if (Optional.ofNullable(existChargeFlow).isPresent()) {
            feeWaterChargedFlow.setId(existChargeFlow.getId());

        }

        FeeWaterReadFlow lastReadFlow = feeWaterReadFlowService.getLastReadFlow(feeWaterReadFlow);
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            logger.error("当前房屋[水户号={}]没有初始化电表数据", feeWaterReadFlow.getHouseWaterNum());
            throw new IllegalArgumentException("当前房屋没有初始化水表数据");
        }

        House house = feeCommonService.getHouseById(feeWaterReadFlow.getHouseId());
        BigDecimal amount = new BigDecimal(feeWaterReadFlow.getWaterDegree()).subtract(new BigDecimal(lastReadFlow.getWaterDegree())).multiply(new BigDecimal(feeConfig.getConfigValue()));

        /*合租的时候,燃气费为公摊数*/
        if (StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.JOINT_RENT.getValue())) {
            logger.info("当前房屋为合租,费用为公摊数");
            List<Room> rooms = feeCommonService.getRoomByHouseId(house.getId());
            /*正在出租的房间数*/
            Long rentRoomSize = rooms.stream().filter(room -> StringUtils.equals(room.getRoomStatus(), RoomStatusEnum.RENTED.getValue())).count();
            amount = amount.divide(new BigDecimal(rentRoomSize));
            feeWaterChargedFlow.setWaterAmount(amount);
            for (Room room : rooms) {
                try {
                    FeeWaterChargedFlow roomFeeWaterCharge = (FeeWaterChargedFlow) feeWaterChargedFlow.clone();
                    if (StringUtils.equals(room.getRoomStatus(), RoomStatusEnum.RENTED.getValue())) {
                        roomFeeWaterCharge.setPayer(PayerEnum.RENT_USER.getValue());
                    } else {
                        roomFeeWaterCharge.setPayer(PayerEnum.COMPANY.getValue());
                    }
                    save(roomFeeWaterCharge);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    logger.error("生成租客收费异常");
                    throw new IllegalArgumentException("生成租客收费异常");
                }
            }
        } else {
            feeWaterChargedFlow.setWaterAmount(amount);

            if (StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.PART_RENT.getValue())
                    || StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.WHOLE_RENT.getValue())) {
                feeWaterChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
            } else {
                feeWaterChargedFlow.setPayer(PayerEnum.COMPANY.getValue());
            }

            save(feeWaterChargedFlow);
        }
    }

    @Transactional(readOnly = false)
    public void deleteFeeWaterChargedFlowByBusinessIdAndFromSource(String feeWaterBillId, int fromSource) {
        FeeWaterChargedFlow existWaterChargedFlow = this.dao.getFeeWaterChargedFlowByBusinessIdAndFromSourceAndRoom(feeWaterBillId, fromSource, null);
        if (Optional.ofNullable(existWaterChargedFlow).isPresent()) {
            FeeWaterChargedFlow updDeeWaterChargeFlow = new FeeWaterChargedFlow();
            updDeeWaterChargeFlow.setId(existWaterChargedFlow.getId());
            updDeeWaterChargeFlow.setDelFlag(Constants.DEL_FLAG_YES);
            save(updDeeWaterChargeFlow);
        }
    }

    public List<FeeWaterChargedFlowVo> getFeeWaterChargedFee(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeWaterChargedFlow(feeCriteriaEntity);
    }

    private FeeWaterChargedFlow readFlowToChargedFlow(FeeWaterReadFlow feeWaterReadFlow) {
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

    private FeeWaterChargedFlow feeWaterBillToFeeWaterCharged(FeeWaterBill feeWaterBill) {
        FeeWaterChargedFlow feeWaterChargedFlow = new FeeWaterChargedFlow();
        feeWaterChargedFlow.setWaterAmount(feeWaterBill.getWaterBillAmount());
        feeWaterChargedFlow.setCreateDate(feeWaterBill.getWaterBillDate());
        feeWaterChargedFlow.setPropertyId(feeWaterBill.getPropertyId());
        feeWaterChargedFlow.setHouseId(feeWaterBill.getHouseId());
        feeWaterChargedFlow.setRoomId("0");
        feeWaterChargedFlow.setHouseWaterNum(feeWaterBill.getHouseWaterNum());
        feeWaterChargedFlow.setBusinessId(feeWaterBill.getId());
        feeWaterChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        feeWaterChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        feeWaterChargedFlow.setFromSource(FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        feeWaterChargedFlow.setWaterCalculateDate(new Date());
        return feeWaterChargedFlow;
    }

}