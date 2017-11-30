/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.service;

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
import com.thinkgem.jeesite.modules.fee.gas.dao.FeeGasChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasChargedFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasChargedFlowVo;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrder;
import com.thinkgem.jeesite.modules.fee.order.service.FeeOrderService;
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

    @Autowired
    private FeeOrderService feeOrderService;

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
        FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeUnitEnum.GAS_UNIT, feeGasBill.getHouseId(), null);
        if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            logger.info("房屋[houseId={}]为固定模式,不生成收费流水", feeGasBill.getHouseId());
            throw new IllegalArgumentException("当前房屋为固定模式,不能生成收费记录");
        }

        FeeGasChargedFlow saveChargedFlow = feeGasBillToFeeGasCharged(feeGasBill);

        List<FeeGasChargedFlow> existGasChargedFlows = this.dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        if (Optional.ofNullable(existGasChargedFlows).isPresent() && existGasChargedFlows.size() > 0) {
            if (existGasChargedFlows.get(0).getGenerateOrder() == GenerateOrderEnum.YES.getValue()) {
                throw new IllegalArgumentException("该房屋已经生成订单,不可修改");
            }
            saveChargedFlow.setId(existGasChargedFlows.get(0).getId());
        }

        /*查询除当前的上一条记录*/
        String id = null;
        FeeGasReadFlow existRead = feeGasReadFlowService.getCurrentReadByDateAndHouseId(feeGasBill.getGasBillDate(), feeGasBill.getHouseId());
        if (Optional.ofNullable(existRead).isPresent()) {
            id = existRead.getId();
        }
        FeeGasReadFlow lastReadFlow = feeGasReadFlowService.getLastReadFlow(id, feeGasBill.getHouseId());
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            if (feeCommonService.isOpenInitFeeData()) {
                return;
            }
            logger.error("当前房屋[houseId={}]没有初始化燃气表数据", feeGasBill.getHouseId());
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
        FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeUnitEnum.GAS_UNIT, feeGasReadFlow.getHouseId(), null);
        if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            logger.error("当前房屋[houseId={}]为固定模式,不能生成收费记录", feeGasReadFlow.getHouseId());
            return;
        }

        /*查询除当前之外的上一条数据*/
        FeeGasReadFlow lastReadFlow = feeGasReadFlowService.getLastReadFlow(feeGasReadFlow.getId(), feeGasReadFlow.getHouseId());
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            if (feeCommonService.isOpenInitFeeData()) {
                return;
            }
            logger.error("当前房屋[户号={}]没有初始化燃气表数据", feeGasReadFlow.getHouseGasNum());
            throw new IllegalArgumentException("当前房屋没有初始化燃气表数据");
        }

        FeeGasChargedFlow saveFeeGasChargeFlow = feeGasReadToFeeGasCharged(feeGasReadFlow);

        House house = feeCommonService.getHouseById(feeGasReadFlow.getHouseId());
        BigDecimal amount = new BigDecimal(feeGasReadFlow.getGasDegree()).subtract(new BigDecimal(lastReadFlow.getGasDegree())).multiply(new BigDecimal(feeConfig.getConfigValue()));
        /*合租的时候,燃气费为公摊数*/
        if (StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.JOINT_RENT.getValue())) {
            logger.info("当前房屋为合租,费用为公摊数");
            List<Room> rooms = feeCommonService.getRoomByHouseId(house.getId());
            /*正在出租的房间数*/
            Long rentRoomSize = rooms.stream().filter(room -> StringUtils.equals(room.getRoomStatus(), RoomStatusEnum.RENTED.getValue())).count();
            rentRoomSize = rentRoomSize == 0 ? 1 : rentRoomSize;

            amount = amount.divide(new BigDecimal(rentRoomSize));
            saveFeeGasChargeFlow.setGasAmount(amount);
            saveFeeGasChargeFlow.setRentType(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()));
            for (Room room : rooms) {
                FeeGasChargedFlow feeGasChargedFlow = saveFeeGasChargeFlow.clone();
                feeGasChargedFlow.setRoomId(room.getId());

                /*如果存在则获取ID*/
                List<FeeGasChargedFlow> existChargeFlows = dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue());
                if (Optional.ofNullable(existChargeFlows).isPresent() && existChargeFlows.size() > 0) {
                    String id = existChargeFlows.stream().filter(f -> StringUtils.equals(f.getRoomId(), room.getId())).map(FeeGasChargedFlow::getId).findFirst().get();
                    feeGasChargedFlow.setId(id);
                }

                if (StringUtils.equals(room.getRoomStatus(), RoomStatusEnum.RENTED.getValue())) {
                    saveFeeGasChargeFlow.setPayer(PayerEnum.RENT_USER.getValue());
                } else {
                    saveFeeGasChargeFlow.setPayer(PayerEnum.COMPANY.getValue());
                }
                save(feeGasChargedFlow);
            }
        } else {
            List<FeeGasChargedFlow> existChargeFlows = dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue());
            if (Optional.ofNullable(existChargeFlows).isPresent() && existChargeFlows.size() > 0) {
                saveFeeGasChargeFlow.setId(existChargeFlows.get(0).getId());
            }

            saveFeeGasChargeFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
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
        List<FeeGasChargedFlow> existGasChargedFlows = this.dao.getFeeGasChargedFlowByBusinessIdAndFromSource(feeGasBillId, fromSource);
        if (Optional.ofNullable(existGasChargedFlows).isPresent() && existGasChargedFlows.size() > 0) {
            existGasChargedFlows.forEach(gas -> {
                FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
                feeGasChargedFlow.setId(gas.getId());
                feeGasChargedFlow.setDelFlag(Constants.DEL_FLAG_YES);
                save(feeGasChargedFlow);
            });
        }
    }

    public List<FeeGasChargedFlowVo> getFeeGasChargedFee(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeGasChargedFlow(feeCriteriaEntity);
    }

    @Transactional(readOnly = false)
    public void generatorFlow() {
        List<FeeGasChargedFlow> feeGasChargedFlows = Lists.newArrayList();
        List<Room> rooms = feeCommonService.getJoinRentAllRoom();
        rooms.forEach(r -> {
            FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeUnitEnum.GAS_UNIT, r.getHouse().getId(), r.getId());
            if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
                /*创建新增对象*/
                FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
                feeGasChargedFlow.setFromSource(FeeFromSourceEnum.SYSTEM_FIX_SET.getValue());
                feeGasChargedFlow.setGasCalculateDate(new Date());
                feeGasChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
                feeGasChargedFlow.setHouseId(r.getHouse().getId());
                feeGasChargedFlow.setRoomId(r.getId());
                feeGasChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()));
                feeGasChargedFlow.setHouseGasNum(r.getHouse().getGasAccountNum());
                feeGasChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
                feeGasChargedFlow.setPropertyId(r.getHouse().getPropertyProject().getId());
                /*如果配置为零，则不收取费用*/
                if(Double.valueOf(feeConfig.getConfigValue()) == 0){
                    feeGasChargedFlow.setGasAmount(new BigDecimal(0));
                    feeGasChargedFlow.preInsert();
                    feeGasChargedFlows.add(feeGasChargedFlow);
                }else {
                    /*计算金额*/
                    double days;
                    FeeGasChargedFlow lastCharged = dao.getLastRecord(r.getHouse().getId(), r.getId());
                    if (Optional.ofNullable(lastCharged).isPresent()) {
                        days = DateUtils.getDistanceOfTwoDate(lastCharged.getGasCalculateDate(), new Date());
                    } else {
                        days = Double.valueOf(DateUtils.getDay());
                    }
                    if (days > 0) {
                    /*金额 = 固定金额/30*上月生成日至今的天数*/
                        BigDecimal amount = new BigDecimal(feeConfig.getConfigValue()).divide(new BigDecimal(FeeCommonService.FULL_MOUTH_DAYS),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(days));
                        feeGasChargedFlow.setGasAmount(amount);
                        feeGasChargedFlow.preInsert();
                        feeGasChargedFlows.add(feeGasChargedFlow);
                    }
                }
            }
        });
        if (feeGasChargedFlows.size() > 0) {
            dao.batchInsert(feeGasChargedFlows);
            logger.info("总共生成{}条收费流水记录", feeGasChargedFlows.size());
        }
    }

    @Transactional(readOnly = false)
    public void generatorOrder() {
        FeeGasChargedFlow feeGasChargedFlow = new FeeGasChargedFlow();
        feeGasChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        List<FeeGasChargedFlow> feeGasChargedFlows = this.findList(feeGasChargedFlow);

        /*按房屋分组*/
        Map<String, List<FeeGasChargedFlow>> feeGasChargedMap = feeGasChargedFlows.stream()
                .collect(Collectors.groupingBy(FeeGasChargedFlow::getHouseId));

        List<FeeGasChargedFlow> updGasCharges = Lists.newArrayList();
        List<FeeOrder> feeOrders = Lists.newArrayList();

        feeGasChargedMap.forEach((String k, List<FeeGasChargedFlow> v) -> {
            FeeGasChargedFlow judgeCharge = v.get(0);
            if (StringUtils.equals("" + judgeCharge.getRentType(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
                String orderNo = new IdGenerator().nextId();
                FeeOrder feeOrder = feeGasChargedFlowToFeeOrder(judgeCharge);
                feeOrder.setOrderNo(orderNo);
                v.stream().forEach(f -> {
                    f.setGenerateOrder(GenerateOrderEnum.YES.getValue());
                    f.setOrderNo(orderNo);

                    /*添加更新的收费流水*/
                    FeeGasChargedFlow updCharged = f.clone();
                    updCharged.preUpdate();
                    updGasCharges.add(updCharged);

                    /*计算金额*/
                    feeOrder.setAmount(feeOrder.getAmount().add(f.getGasAmount()));
                });
                feeOrders.add(feeOrder);
            } else {
                 /*按房间分组*/
                Map<String, List<FeeGasChargedFlow>> chargedFlowMap = v.stream()
                        .collect(Collectors.groupingBy(FeeGasChargedFlow::getRoomId));

                /*计算每个房间的收费情况*/
                chargedFlowMap.forEach((String key, List<FeeGasChargedFlow> value) -> {
                    String orderNo = new IdGenerator().nextId();
                    FeeOrder feeOrder = feeGasChargedFlowToFeeOrder(value.get(0));
                    feeOrder.setOrderNo(orderNo);
                    value.stream().forEach(f -> {
                        f.setOrderNo(orderNo);
                        f.setGenerateOrder(GenerateOrderEnum.YES.getValue());

                        /*添加更新的收费流水*/
                        FeeGasChargedFlow updCharged = f.clone();
                        updCharged.preUpdate();
                        updGasCharges.add(updCharged);

                        /*计算金额*/
                        feeOrder.setAmount(feeOrder.getAmount().add(f.getGasAmount()));
                    });
                    feeOrders.add(feeOrder);
                });
            }
        });
        /*更新收费流水表*/
        if (updGasCharges.size() > 0) {
            dao.batchUpdate(updGasCharges);
        }
        if (feeOrders.size() > 0) {
            logger.info("生成订单记录");
            feeOrderService.batchInsert(feeOrders);
        }
    }

    private FeeOrder feeGasChargedFlowToFeeOrder(FeeGasChargedFlow feeGasChargedFlow) {
        FeeOrder feeOrder = new FeeOrder();
        feeOrder.setPayer(feeGasChargedFlow.getPayer());
        feeOrder.setHouseId(feeGasChargedFlow.getHouseId());
        feeOrder.setOrderDate(new Date());
        feeOrder.setOrderStatus(OrderStatusEnum.COMMIT.getValue());
        feeOrder.setPropertyId(feeGasChargedFlow.getPropertyId());
        feeOrder.setOrderType(OrderTypeEnum.GAS.getValue());
        feeOrder.setRoomId(feeGasChargedFlow.getRoomId());
        feeOrder.setAmount(new BigDecimal(0));
        feeOrder.preInsert();
        return feeOrder;
    }
}