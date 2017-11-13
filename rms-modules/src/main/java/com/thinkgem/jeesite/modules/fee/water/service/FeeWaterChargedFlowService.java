/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.service;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.IdGenerator;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.enums.*;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrder;
import com.thinkgem.jeesite.modules.fee.order.service.FeeOrderService;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private FeeOrderService feeOrderService;

    @Transactional(readOnly = false)
    public void saveFeeWaterChargedFlowByFeeWaterBill(FeeWaterBill feeWaterBill) {
        FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.WATER_UNIT, feeWaterBill.getHouseId(), null);
        if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            logger.info("房屋[houseId={}]为固定模式,不生成收费流水", feeWaterBill.getHouseId());
            throw new IllegalArgumentException("当前房屋为固定模式,不能生成收费记录");
        }

        FeeWaterChargedFlow feeWaterChargedFlow = feeWaterBillToFeeWaterCharged(feeWaterBill);

        List<FeeWaterChargedFlow> existWaterChargedFlows = this.dao.getFeeWaterChargedFlowByBusinessIdAndFromSource(feeWaterBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        if (Optional.ofNullable(existWaterChargedFlows).isPresent() && existWaterChargedFlows.size() > 0) {
            if (existWaterChargedFlows.get(0).getGenerateOrder() == GenerateOrderEnum.YES.getValue()) {
                throw new IllegalArgumentException("该房屋已经生成订单,不可修改");
            }
            feeWaterChargedFlow.setId(existWaterChargedFlows.get(0).getId());
        }

        /*查询除当前的上一条记录*/
        String id = null;
        FeeWaterReadFlow existRead = feeWaterReadFlowService.getCurrentReadByDateAndHouseId(feeWaterBill.getWaterBillDate(), feeWaterBill.getHouseId());
        if (Optional.ofNullable(existRead).isPresent()) {
            id = existRead.getId();
        }
        FeeWaterReadFlow lastReadFlow = feeWaterReadFlowService.getLastReadFlow(id, feeWaterBill.getHouseId());
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            logger.error("当前房屋[houseId={}]没有初始化电表数据", feeWaterBill.getHouseId());
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

        FeeWaterReadFlow lastReadFlow = feeWaterReadFlowService.getLastReadFlow(feeWaterReadFlow.getId(), feeWaterChargedFlow.getHouseId());
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
            feeWaterChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()));
            for (Room room : rooms) {
                FeeWaterChargedFlow roomFeeWaterCharge = feeWaterChargedFlow.clone();
                roomFeeWaterCharge.setRoomId(room.getId());

                /*如果存在则获取ID*/
                List<FeeWaterChargedFlow> existChargeFlows = dao.getFeeWaterChargedFlowByBusinessIdAndFromSource(feeWaterReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue());
                if (Optional.ofNullable(existChargeFlows).isPresent() && existChargeFlows.size() > 0) {
                    String id = existChargeFlows.stream().filter(f -> StringUtils.equals(f.getRoomId(), room.getId())).map(FeeWaterChargedFlow::getId).findFirst().get();
                    roomFeeWaterCharge.setId(id);
                }

                if (StringUtils.equals(room.getRoomStatus(), RoomStatusEnum.RENTED.getValue())) {
                    roomFeeWaterCharge.setPayer(PayerEnum.RENT_USER.getValue());
                } else {
                    roomFeeWaterCharge.setPayer(PayerEnum.COMPANY.getValue());
                }
                save(roomFeeWaterCharge);
            }
        } else {
            List<FeeWaterChargedFlow> existChargeFlows = dao.getFeeWaterChargedFlowByBusinessIdAndFromSource(feeWaterReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue());
            if (Optional.ofNullable(existChargeFlows).isPresent() && existChargeFlows.size() > 0) {
                feeWaterChargedFlow.setId(existChargeFlows.get(0).getId());
            }

            feeWaterChargedFlow.setWaterAmount(amount);
            feeWaterChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));

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
        List<FeeWaterChargedFlow> existWaterChargedFlows = this.dao.getFeeWaterChargedFlowByBusinessIdAndFromSource(feeWaterBillId, fromSource);
        if (Optional.ofNullable(existWaterChargedFlows).isPresent() && existWaterChargedFlows.size() > 0) {
            existWaterChargedFlows.forEach(f -> {
                FeeWaterChargedFlow updDeeWaterChargeFlow = new FeeWaterChargedFlow();
                updDeeWaterChargeFlow.setId(f.getId());
                updDeeWaterChargeFlow.setDelFlag(Constants.DEL_FLAG_YES);
                save(updDeeWaterChargeFlow);
            });
        }
    }

    public List<FeeWaterChargedFlowVo> getFeeWaterChargedFee(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeWaterChargedFlow(feeCriteriaEntity);
    }

    public void generatorFlow() {
        List<FeeWaterChargedFlow> feeWaterChargedFlows = Lists.newArrayList();
        List<Room> rooms = feeCommonService.getJoinRentAllRoom();
        rooms.forEach(r -> {
            FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.WATER_UNIT, r.getHouse().getId(), r.getId());
            if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
                /*创建新增对象*/
                FeeWaterChargedFlow feeWaterChargedFlow = new FeeWaterChargedFlow();
                feeWaterChargedFlow.setFromSource(FeeFromSourceEnum.SYSTEM_FIX_SET.getValue());
                feeWaterChargedFlow.setWaterCalculateDate(new Date());
                feeWaterChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
                feeWaterChargedFlow.setHouseId(r.getHouse().getId());
                feeWaterChargedFlow.setRoomId(r.getId());
                feeWaterChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()));
                feeWaterChargedFlow.setHouseWaterNum(r.getHouse().getGasAccountNum());
                feeWaterChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
                feeWaterChargedFlow.setPropertyId(r.getHouse().getPropertyProject().getId());

                /*计算金额*/
                double days;
                FeeWaterChargedFlow lastCharged = dao.getLastRecord(r.getHouse().getHouseId(), r.getId());
                if (Optional.ofNullable(lastCharged).isPresent()) {
                    days = DateUtils.getDistanceOfTwoDate(new Date(), lastCharged.getWaterCalculateDate());
                } else {
                    days = Double.valueOf(DateUtils.getDay());
                }
                if (days > 0) {
                    /*金额 = 固定金额/30*上月生成日至今的天数*/
                    BigDecimal amount = new BigDecimal(feeConfig.getConfigValue()).divide(new BigDecimal(FeeCommonService.FULL_MOUTH_DAYS)).multiply(new BigDecimal(days));
                    feeWaterChargedFlow.setWaterAmount(amount);
                    feeWaterChargedFlow.preInsert();
                    feeWaterChargedFlows.add(feeWaterChargedFlow);
                }
            }
        });
        dao.batchInsert(feeWaterChargedFlows);
        logger.info("总共生成{}条收费流水记录", feeWaterChargedFlows.size());
    }

    public void generatorOrder() {
        FeeWaterChargedFlow feeWaterChargedFlow = new FeeWaterChargedFlow();
        feeWaterChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        List<FeeWaterChargedFlow> feeWaterChargedFlows = this.findList(feeWaterChargedFlow);

        /*按房屋分组*/
        Map<String, List<FeeWaterChargedFlow>> feeWaterChargedMap = feeWaterChargedFlows.stream()
                .collect(Collectors.groupingBy(FeeWaterChargedFlow::getHouseId));

        List<FeeWaterChargedFlow> updWaterCharges = Lists.newArrayList();
        List<FeeOrder> feeOrders = Lists.newArrayList();

        feeWaterChargedMap.forEach((String k, List<FeeWaterChargedFlow> v) -> {
            FeeWaterChargedFlow judgeCharge = v.get(0);
            if (StringUtils.equals("" + judgeCharge.getRentType(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
                String orderNo = new IdGenerator().nextId();
                FeeOrder feeOrder = feeWaterChargedFlowToFeeOrder(judgeCharge);
                feeOrder.setOrderNo(orderNo);
                v.stream().forEach(f -> {
                    f.setGenerateOrder(GenerateOrderEnum.YES.getValue());
                    f.setOrderNo(orderNo);
                    updWaterCharges.add(f);

                    feeOrder.setAmount(feeOrder.getAmount().add(f.getWaterAmount()));
                });
                feeOrders.add(feeOrder);
            } else {
                 /*按房间分组*/
                Map<String, List<FeeWaterChargedFlow>> chargedFlowMap = v.stream()
                        .collect(Collectors.groupingBy(FeeWaterChargedFlow::getRoomId));

                /*计算每个房间的收费情况*/
                chargedFlowMap.forEach((String key, List<FeeWaterChargedFlow> value) -> {
                    String orderNo = new IdGenerator().nextId();
                    FeeOrder feeOrder = feeWaterChargedFlowToFeeOrder(value.get(0));
                    feeOrder.setOrderNo(orderNo);
                    value.stream().forEach(f -> {
                        f.setOrderNo(orderNo);
                        f.setGenerateOrder(GenerateOrderEnum.YES.getValue());
                        updWaterCharges.add(f);

                        feeOrder.setAmount(feeOrder.getAmount().add(f.getWaterAmount()));
                    });
                    feeOrders.add(feeOrder);
                });
            }
        });

        /*更新收费流水表*/
        dao.batchInsert(updWaterCharges);
        logger.info("生成订单信息");
        feeOrderService.batchInsert(feeOrders);
    }

    private FeeOrder feeWaterChargedFlowToFeeOrder(FeeWaterChargedFlow feeWaterChargedFlow) {
        FeeOrder feeOrder = new FeeOrder();
        feeOrder.setPayer(feeWaterChargedFlow.getPayer());
        feeOrder.setHouseId(feeWaterChargedFlow.getHouseId());
        feeOrder.setOrderDate(new Date());
        feeOrder.setOrderStatus(OrderStatusEnum.COMMIT.getValue());
        feeOrder.setPropertyId(feeWaterChargedFlow.getPropertyId());
        feeOrder.setOrderType(OrderTypeEnum.WATER.getValue());
        feeOrder.setRoomId(feeWaterChargedFlow.getRoomId());
        feeOrder.preInsert();
        return feeOrder;
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