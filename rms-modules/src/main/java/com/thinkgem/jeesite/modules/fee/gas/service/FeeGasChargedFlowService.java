/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGenerator;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleChargedFlow;
import com.thinkgem.jeesite.modules.fee.enums.*;
import com.thinkgem.jeesite.modules.fee.gas.dao.FeeGasChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasChargedFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasChargedFlowVo;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrder;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.GAS_UNIT, feeGasBill.getHouseId(), null);

        if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            logger.info("房屋[houseId={}]为固定模式,不生成收费流水", feeGasBill.getHouseId());
            throw new IllegalArgumentException("当前房屋为固定模式,不能生成收费记录");
        }

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
            logger.error("当前房屋[houseId={}]没有初始化电表数据", feeGasBill.getHouseId());
            throw new IllegalArgumentException("当前房屋没有初始化燃气表数据");
        }

        BigDecimal amount = new BigDecimal(feeGasBill.getGasDegree()).subtract(new BigDecimal(lastReadFlow.getGasDegree())).multiply(new BigDecimal(feeConfig.getConfigValue()));

        saveChargedFlow.setGasAmount(amount);

        House house = feeCommonService.getHouseById(feeGasBill.getHouseId());
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
        FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.GAS_UNIT, feeGasReadFlow.getHouseId(), null);
        if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            logger.error("当前房屋[houseId={}]为固定模式,不能生成收费记录", feeGasReadFlow.getHouseId());
            return;
        }

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

        House house = feeCommonService.getHouseById(feeGasReadFlow.getHouseId());
        BigDecimal amount = new BigDecimal(feeGasReadFlow.getGasDegree()).subtract(new BigDecimal(lastReadFlow.getGasDegree())).multiply(new BigDecimal(feeConfig.getConfigValue()));
        /*合租的时候,燃气费为公摊数*/
        if (StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.JOINT_RENT.getValue())) {
            logger.info("当前房屋为合租,费用为公摊数");
            List<Room> rooms = feeCommonService.getRoomByHouseId(house.getId());
            /*正在出租的房间数*/
            Long rentRoomSize = rooms.stream().filter(room -> StringUtils.equals(room.getRoomStatus(), RoomStatusEnum.RENTED.getValue())).count();
            amount = amount.divide(new BigDecimal(rentRoomSize));
            saveFeeGasChargeFlow.setGasAmount(amount);
            for (Room room : rooms) {
                try {
                    FeeGasChargedFlow feeGasChargedFlow = (FeeGasChargedFlow) saveFeeGasChargeFlow.clone();
                    if (StringUtils.equals(room.getRoomStatus(), RoomStatusEnum.RENTED.getValue())) {
                        saveFeeGasChargeFlow.setPayer(PayerEnum.RENT_USER.getValue());
                    } else {
                        saveFeeGasChargeFlow.setPayer(PayerEnum.COMPANY.getValue());
                    }
                    save(feeGasChargedFlow);
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    logger.error("生成租客收费异常");
                    throw new IllegalArgumentException("生成租客收费异常");
                }
            }
        } else {
            saveFeeGasChargeFlow.setGasAmount(amount);
            if (StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.PART_RENT.getValue())
                    || StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.WHOLE_RENT.getValue())) {
                saveFeeGasChargeFlow.setPayer(PayerEnum.RENT_USER.getValue());
            } else {
                saveFeeGasChargeFlow.setPayer(PayerEnum.COMPANY.getValue());
            }
            save(saveFeeGasChargeFlow);
        }
    }

    @Transactional(readOnly = false)
    public void deleteFeeGasChargedFlowByBusinessIdAndFromSource(String feeGasBillId, int fromSource) {
        FeeGasChargedFlow existGasChargedFlow = this.dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasBillId, fromSource, null);
        if (Optional.ofNullable(existGasChargedFlow).isPresent()) {
            FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
            feeGasChargedFlow.setId(existGasChargedFlow.getId());
            feeGasChargedFlow.setDelFlag(Constants.DEL_FLAG_YES);
            save(feeGasChargedFlow);
        }
    }

    public List<FeeGasChargedFlowVo> getFeeGasChargedFee(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeGasChargedFlow(feeCriteriaEntity);
    }

    public void generatorFlow() {
        /**
         * 1.去上次生成的记录，如果没有按本月一号开始计算
         * 2.本次记录的日期减上次生成的日期的天数，如果是同一天怎更新
         * 3.计算金额 = 配置金额/30*天数
         */
        List<FeeGasChargedFlow> feeGasChargedFlows = Lists.newArrayList();
        List<Room> rooms = feeCommonService.getJoinRentAllRoom();
        rooms.forEach(r -> {
            FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.GAS_UNIT, r.getHouse().getId(), r.getId());
            if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
                FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
                feeGasChargedFlow.setFromSource(FeeFromSourceEnum.SYSTEM_FIX_SET.getValue());
                feeGasChargedFlow.setGasAmount(new BigDecimal(feeConfig.getConfigValue()));
                feeGasChargedFlow.setGasCalculateDate(new Date());
                feeGasChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
                feeGasChargedFlow.setHouseId(r.getHouse().getId());
                feeGasChargedFlow.setRoomId(r.getId());
                feeGasChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()));
                feeGasChargedFlow.setHouseGasNum(r.getHouse().getGasAccountNum());
                feeGasChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
                feeGasChargedFlow.setPropertyId(r.getHouse().getPropertyProject().getId());
                feeGasChargedFlow.preInsert();
                feeGasChargedFlows.add(feeGasChargedFlow);
            }
        });
        dao.batchInsert(feeGasChargedFlows);
        logger.info("总共生成{}条收费流水记录", feeGasChargedFlows.size());
    }

    public void generatorOrder() {
        FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
        feeGasChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        List<FeeGasChargedFlow> feeGasChargedFlows = this.findList(feeGasChargedFlow);

        /*按房屋分组*/
        Map<String,List<FeeGasChargedFlow>> feeGasChargedMap = feeGasChargedFlows.stream()
                .collect(Collectors.groupingBy(FeeGasChargedFlow::getHouseId));

        List<FeeGasChargedFlow> updEleCharges = Lists.newArrayList();
        List<FeeOrder> feeOrders = Lists.newArrayList();

        feeGasChargedMap.forEach((String k,List<FeeGasChargedFlow> v) ->{
            FeeGasChargedFlow judgeCharge = v.get(0);
            if(StringUtils.equals("" + judgeCharge.getRentType(),RentModelTypeEnum.WHOLE_RENT.getValue())){
                String batchNo = new IdGenerator().nextId();
                FeeOrder feeOrder = feeGasChargedFlowToFeeOrder(judgeCharge);
                feeOrder.setBatchNo(batchNo);
                v.stream().forEach(f -> {
                    f.setGenerateOrder(GenerateOrderEnum.YES.getValue());
                    f.setOrderNo(batchNo);
                    updEleCharges.add(f);

                    feeOrder.setAmount(feeOrder.getAmount().add(f.getGasAmount()));
                });
                feeOrders.add(feeOrder);
            }else{
                 /*按房间分组*/
                Map<String, List<FeeGasChargedFlow>> chargedFlowMap = v.stream()
                        .collect(Collectors.groupingBy(FeeGasChargedFlow::getRoomId));

                /*计算每个房间的收费情况*/
                chargedFlowMap.forEach((String key,List<FeeGasChargedFlow> value) ->{
                    String batchNo = new IdGenerator().nextId();
                    FeeOrder feeOrder = feeGasChargedFlowToFeeOrder(value.get(0));
                    feeOrder.setBatchNo(batchNo);
                    value.stream().forEach(f -> {
                        f.setOrderNo(batchNo);
                        f.setGenerateOrder(GenerateOrderEnum.YES.getValue());
                        updEleCharges.add(f);

                        feeOrder.setAmount(feeOrder.getAmount().add(f.getGasAmount()));
                    });
                    feeOrders.add(feeOrder);
                });
            }
        });
        /*更新收费流水表*/
    }

    private FeeOrder feeGasChargedFlowToFeeOrder(FeeGasChargedFlow feeGasChargedFlow){
        FeeOrder feeOrder = new FeeOrder();
        feeOrder.setPayer(feeGasChargedFlow.getPayer());
        feeOrder.setHouseId(feeGasChargedFlow.getHouseId());
        feeOrder.setOrderDate(new Date());
        feeOrder.setOrderStatus(OrderStatusEnum.COMMIT.getValue());
        feeOrder.setPropertyId(feeGasChargedFlow.getPropertyId());
        feeOrder.setOrderType(OrderTypeEnum.GAS.getValue());
        feeOrder.setRoomId(feeGasChargedFlow.getRoomId());
        feeOrder.preInsert();
        return feeOrder;
    }
}