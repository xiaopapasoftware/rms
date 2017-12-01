/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.other.service;

import com.google.common.collect.Lists;
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
import com.thinkgem.jeesite.modules.fee.other.dao.FeeOtherChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.other.entity.FeeOtherChargedFlow;
import com.thinkgem.jeesite.modules.fee.other.entity.vo.FeeOtherChargedFlowVo;
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
import java.util.stream.Collectors;

/**
 * <p>宽带、电视费、其他账单收取流水实现类 service</p>
 * <p>Table: fee_other_charged_flow - 宽带、电视费、其他账单收取流水</p>
 *
 * @author generator code
 * @since 2017-11-28 03:02:42
 */
@Service
@Transactional(readOnly = true)
public class FeeOtherChargedFlowService extends CrudService<FeeOtherChargedFlowDao, FeeOtherChargedFlow> {

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeOrderService feeOrderService;

    public List<FeeOtherChargedFlowVo> getFeeOtherChargedFlow(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeOtherChargedFlow(feeCriteriaEntity);
    }

    @Transactional(readOnly = false)
    public void generatorFlow() {
        List<FeeOtherChargedFlow> feeChargedFlows = Lists.newArrayList();

        List<House> houses = feeCommonService.getWholeRentAllHouse();
        houses.forEach(h ->{
            /*创建新增对象*/
            FeeOtherChargedFlow feeOtherChargedFlow = new FeeOtherChargedFlow();
            feeOtherChargedFlow.setCalculateDate(new Date());
            feeOtherChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
            feeOtherChargedFlow.setHouseId(h.getId());
            feeOtherChargedFlow.setRoomId("0");
            feeOtherChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
            feeOtherChargedFlow.setPropertyId(h.getPropertyProject().getId());

            FeeConfig netFeeConfig = feeCommonService.getFeeConfig(FeeUnitEnum.NET_UNIT, h.getId(),"0");
            FeeConfig tvFeeConfig = feeCommonService.getFeeConfig(FeeUnitEnum.TV_UNIT, h.getId(), "0");

             /*计算金额*/
            double days;
            FeeOtherChargedFlow lastCharged = dao.getLastRecord(h.getId(), "0");
            if (Optional.ofNullable(lastCharged).isPresent()) {
                days = DateUtils.getDistanceOfTwoDate(lastCharged.getCalculateDate(), new Date());
            } else {
                days = Double.valueOf(DateUtils.getDay());
            }

            if (netFeeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
                FeeOtherChargedFlow netChargedFlow = getOtherChargedFlow(feeOtherChargedFlow, netFeeConfig, days);
                netChargedFlow.setType(OrderTypeEnum.NET.getValue());
                feeChargedFlows.add(netChargedFlow);
            }
            if (tvFeeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
                FeeOtherChargedFlow tvChargedFlow = getOtherChargedFlow(feeOtherChargedFlow, tvFeeConfig, days);
                tvChargedFlow.setType(OrderTypeEnum.TV.getValue());
                feeChargedFlows.add(tvChargedFlow);
            }
        });

        List<Room> rooms = feeCommonService.getJoinRentAllRoom();
        rooms.forEach(r -> {
            /*创建新增对象*/
            FeeOtherChargedFlow feeOtherChargedFlow = new FeeOtherChargedFlow();
            feeOtherChargedFlow.setCalculateDate(new Date());
            feeOtherChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
            feeOtherChargedFlow.setHouseId(r.getHouse().getId());
            feeOtherChargedFlow.setRoomId(r.getId());
            feeOtherChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()));
            feeOtherChargedFlow.setPropertyId(r.getHouse().getPropertyProject().getId());

            FeeConfig netFeeConfig = feeCommonService.getFeeConfig(FeeUnitEnum.NET_UNIT, r.getHouse().getId(), r.getId());
            FeeConfig tvFeeConfig = feeCommonService.getFeeConfig(FeeUnitEnum.TV_UNIT, r.getHouse().getId(), r.getId());

            /*计算金额*/
            double days;
            FeeOtherChargedFlow lastCharged = dao.getLastRecord(r.getHouse().getId(), r.getId());
            if (Optional.ofNullable(lastCharged).isPresent()) {
                days = DateUtils.getDistanceOfTwoDate(lastCharged.getCalculateDate(), new Date());
            } else {
                days = Double.valueOf(DateUtils.getDay());
            }

            if (netFeeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
                FeeOtherChargedFlow netChargedFlow = getOtherChargedFlow(feeOtherChargedFlow, netFeeConfig, days);
                netChargedFlow.setType(OrderTypeEnum.NET.getValue());
                feeChargedFlows.add(netChargedFlow);
            }
            if (tvFeeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
                FeeOtherChargedFlow tvChargedFlow = getOtherChargedFlow(feeOtherChargedFlow, tvFeeConfig, days);
                tvChargedFlow.setType(OrderTypeEnum.TV.getValue());
                feeChargedFlows.add(tvChargedFlow);
            }
        });

        if (feeChargedFlows.size() > 0) {
            dao.batchInsert(feeChargedFlows);
            logger.info("总共生成{}条收费流水记录", feeChargedFlows.size());
        }
    }

    private FeeOtherChargedFlow getOtherChargedFlow(FeeOtherChargedFlow feeOtherChargedFlow, FeeConfig feeConfig, double days) {
        FeeOtherChargedFlow otherChargedFlow = feeOtherChargedFlow.clone();
        if (Double.valueOf(feeConfig.getConfigValue()) == 0) {
            otherChargedFlow.setAmount(new BigDecimal(0));
        } else {
            /*金额 = 固定金额/30*上月生成日至今的天数*/
            BigDecimal amount = new BigDecimal(feeConfig.getConfigValue()).divide(new BigDecimal(FeeCommonService.FULL_MOUTH_DAYS),2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(days));
            otherChargedFlow.setAmount(amount);
        }
        otherChargedFlow.preInsert();
        return otherChargedFlow;
    }

    @Transactional(readOnly = false)
    public void generatorOrder() {
        FeeOtherChargedFlow feeChargedFlow = new FeeOtherChargedFlow();
        feeChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        List<FeeOtherChargedFlow> feeChargedFlows = this.findList(feeChargedFlow);

        /*按房屋 费用类型 分组*/
        Map<String, List<FeeOtherChargedFlow>> feeChargedMap = feeChargedFlows.stream()
                .collect(Collectors.groupingBy(f -> f.getHouseId() + f.getType()));

        List<FeeOtherChargedFlow> updCharges = Lists.newArrayList();
        List<FeeOrder> feeOrders = Lists.newArrayList();

        feeChargedMap.forEach((String k, List<FeeOtherChargedFlow> v) -> {
            FeeOtherChargedFlow judgeCharge = v.get(0);
            if (StringUtils.equals("" + judgeCharge.getRentType(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
                String orderNo = new IdGenerator().nextId();
                FeeOrder feeOrder = feeOtherChargedFlowToFeeOrder(judgeCharge);
                feeOrder.setOrderNo(orderNo);
                v.stream().forEach(f -> {
                    f.setGenerateOrder(GenerateOrderEnum.YES.getValue());
                    f.setOrderNo(orderNo);

                    /*添加更新的收费流水*/
                    FeeOtherChargedFlow updCharged = f.clone();
                    updCharged.preUpdate();
                    updCharges.add(updCharged);

                    /*计算金额*/
                    feeOrder.setAmount(feeOrder.getAmount().add(f.getAmount()));
                });
                feeOrders.add(feeOrder);
            } else {
                 /*按房间分组*/
                Map<String, List<FeeOtherChargedFlow>> chargedFlowMap = v.stream()
                        .collect(Collectors.groupingBy(FeeOtherChargedFlow::getRoomId));

                /*计算每个房间的收费情况*/
                chargedFlowMap.forEach((String key, List<FeeOtherChargedFlow> value) -> {
                    String orderNo = new IdGenerator().nextId();
                    FeeOrder feeOrder = feeOtherChargedFlowToFeeOrder(value.get(0));
                    feeOrder.setOrderNo(orderNo);
                    value.stream().forEach(f -> {
                        f.setOrderNo(orderNo);
                        f.setGenerateOrder(GenerateOrderEnum.YES.getValue());

                        /*添加更新的收费流水*/
                        FeeOtherChargedFlow updCharged = f.clone();
                        updCharged.preUpdate();
                        updCharges.add(updCharged);

                        /*计算金额*/
                        feeOrder.setAmount(feeOrder.getAmount().add(f.getAmount()));
                    });
                    feeOrders.add(feeOrder);
                });
            }
        });
        /*更新收费流水表*/
        if (updCharges.size() > 0) {
            dao.batchUpdate(updCharges);
        }
        if (feeOrders.size() > 0) {
            logger.info("生成订单记录");
            feeOrderService.batchInsert(feeOrders);
        }
    }

    private FeeOrder feeOtherChargedFlowToFeeOrder(FeeOtherChargedFlow feeChargedFlow) {
        FeeOrder feeOrder = new FeeOrder();
        feeOrder.setPayer(PayerEnum.RENT_USER.getValue());
        feeOrder.setHouseId(feeChargedFlow.getHouseId());
        feeOrder.setOrderDate(new Date());
        feeOrder.setOrderStatus(OrderStatusEnum.COMMIT.getValue());
        feeOrder.setPropertyId(feeChargedFlow.getPropertyId());
        feeOrder.setOrderType(feeChargedFlow.getType());
        feeOrder.setRoomId(feeChargedFlow.getRoomId());
        feeOrder.setAmount(new BigDecimal(0));
        feeOrder.preInsert();
        return feeOrder;
    }
}