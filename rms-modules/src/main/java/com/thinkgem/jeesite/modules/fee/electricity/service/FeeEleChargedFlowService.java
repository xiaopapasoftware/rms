/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

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
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeEleChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleChargedFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleChargedFlowVo;
import com.thinkgem.jeesite.modules.fee.enums.*;
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
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeEleReadFlowService feeEleReadFlowService;

    @Autowired
    private FeeOrderService feeOrderService;

    private FeeEleChargedFlow feeEleBillToFeeEleCharged(FeeElectricityBill feeEleBill) {
        FeeEleChargedFlow saveChargedFlow = new FeeEleChargedFlow();
        saveChargedFlow.setCreateDate(feeEleBill.getEleBillDate());
        saveChargedFlow.setPropertyId(feeEleBill.getPropertyId());
        saveChargedFlow.setHouseId(feeEleBill.getHouseId());
        saveChargedFlow.setRoomId("0");
        saveChargedFlow.setHouseEleNum(feeEleBill.getHouseEleNum());
        saveChargedFlow.setBusinessId(feeEleBill.getId());
        saveChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        saveChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        saveChargedFlow.setFromSource(FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        saveChargedFlow.setEleCalculateDate(new Date());
        return saveChargedFlow;
    }

    private FeeEleChargedFlow feeEleReadToFeeEleCharged(FeeEleReadFlow feeEleRead) {
        FeeEleChargedFlow saveChargedFlow = new FeeEleChargedFlow();
        saveChargedFlow.setBusinessId(feeEleRead.getId());
        saveChargedFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());
        saveChargedFlow.setEleCalculateDate(new Date());
        saveChargedFlow.setHouseEleNum(feeEleRead.getHouseEleNum());
        saveChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        saveChargedFlow.setPropertyId(feeEleRead.getPropertyId());
        saveChargedFlow.setHouseId(feeEleRead.getHouseId());
        saveChargedFlow.setRoomId(feeEleRead.getRoomId());
        return saveChargedFlow;
    }

    @Transactional(readOnly = false)
    public void saveFeeEleChargedFlowByFeeEleBill(FeeElectricityBill feeEleBill) {
        House house = feeCommonService.getHouseById(feeEleBill.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[id={}]不存在,请确认", house.getHouseId());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        FeeEleChargedFlow saveChargedFlow = feeEleBillToFeeEleCharged(feeEleBill);
        saveChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));

        FeeEleChargedFlow existEleChargedFlow = this.dao.getFeeEleChargedFlowByBusinessIdAndFromSource(feeEleBill.getId(), FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        if (Optional.ofNullable(existEleChargedFlow).isPresent()) {
            if (existEleChargedFlow.getGenerateOrder() == GenerateOrderEnum.YES.getValue()) {
                throw new IllegalArgumentException("该房屋已经生成订单,不可修改");
            }
            saveChargedFlow.setId(existEleChargedFlow.getId());
        }

        /*获取上一条抄表记录*/
        String id = null;
        FeeEleReadFlow existEleRead = feeEleReadFlowService.getCurrentReadByDateAndHouseIdAndRoomId(feeEleBill.getEleBillDate(),feeEleBill.getHouseId(),null);
        if(Optional.ofNullable(existEleRead).isPresent()){
            id = existEleRead.getId();
        }
        FeeEleReadFlow lastReadFlow = feeEleReadFlowService.getLastReadFlow(id,feeEleBill.getHouseId(),null);
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            logger.error("当前房屋[houseId={}]没有初始化电表数据", feeEleBill.getHouseId());
            throw new IllegalArgumentException("当前房屋没有初始化电表数据");
        }

        /*计算金额*/
        FeeConfig peakFeeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.ELE_VALLEY_UNIT, feeEleBill.getHouseId(), null);
        FeeConfig valleyFeeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.ELE_VALLEY_UNIT, feeEleBill.getHouseId(), null);

        if (peakFeeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue() || valleyFeeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
            logger.error("当前房屋[houseId={}]为固定模式,不能生成收费记录", feeEleBill.getHouseId());
            throw new IllegalArgumentException("当前房屋为固定模式,不能生成收费记录");
        }
        double peakAmount = (feeEleBill.getElePeakDegree() - lastReadFlow.getElePeakDegree()) * Float.valueOf(peakFeeConfig.getConfigValue());
        double valleyAmount = (feeEleBill.getEleValleyDegree() - lastReadFlow.getEleValleyDegree()) * Float.valueOf(valleyFeeConfig.getConfigValue());
        saveChargedFlow.setElePeakAmount(new BigDecimal(peakAmount));
        saveChargedFlow.setEleValleyAmount(new BigDecimal(valleyAmount));
        saveChargedFlow.setEleAmount(new BigDecimal(peakAmount).add(new BigDecimal(valleyAmount)));

        if (StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.PART_RENT.getValue())
                || StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.WHOLE_RENT.getValue())) {
            saveChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
        } else {
            saveChargedFlow.setPayer(PayerEnum.COMPANY.getValue());
        }
        save(saveChargedFlow);
    }

    @Transactional(readOnly = false)
    public void saveFeeEleChargedFlowByFeeEleReadFlow(FeeEleReadFlow feeEleReadFlow) {
        FeeEleChargedFlow feeEleChargedFlow = feeEleReadToFeeEleCharged(feeEleReadFlow);

        FeeEleChargedFlow existChargeFlow = dao.getFeeEleChargedFlowByBusinessIdAndFromSource(feeEleReadFlow.getId(), FeeFromSourceEnum.READ_METER.getValue());
        if (Optional.ofNullable(existChargeFlow).isPresent()) {
            feeEleChargedFlow.setId(existChargeFlow.getId());
        }

        feeEleChargedFlow = calculateCharge(feeEleReadFlow, feeEleChargedFlow);

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

    public FeeEleChargedFlow calculateCharge(FeeEleReadFlow feeEleReadFlow, FeeEleChargedFlow feeEleChargedFlow) {
        House house = feeCommonService.getHouseById(feeEleChargedFlow.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[houseId={}]不存在,请确认", house.getId());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        /*获取上一条抄表记录*/
        FeeEleReadFlow lastReadFlow = feeEleReadFlowService.getLastReadFlow(feeEleReadFlow.getId(),feeEleReadFlow.getHouseId(),feeEleReadFlow.getRoomId());
        if (!Optional.ofNullable(lastReadFlow).isPresent()) {
            logger.error("当前房屋[houseId={}]没有初始化电表数据", house.getId());
            throw new IllegalArgumentException("当前房屋没有初始化电表数据");
        }

        if (StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
            feeEleChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
            FeeConfig peakFeeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.ELE_VALLEY_UNIT, feeEleChargedFlow.getHouseId(), feeEleChargedFlow.getRoomId());
            FeeConfig valleyFeeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.ELE_VALLEY_UNIT, feeEleChargedFlow.getHouseId(), feeEleChargedFlow.getRoomId());

            if (peakFeeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue() || valleyFeeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
                logger.error("当前房屋[houseId={}]为固定模式,不能生成收费记录", house.getId());
                throw new IllegalArgumentException("当前房屋为固定模式,不能生成收费记录");
            }

            double peakAmount = (feeEleReadFlow.getElePeakDegree() - lastReadFlow.getElePeakDegree()) * Float.valueOf(peakFeeConfig.getConfigValue());
            double valleyAmount = (feeEleReadFlow.getEleValleyDegree() - lastReadFlow.getEleValleyDegree()) * Float.valueOf(valleyFeeConfig.getConfigValue());
            feeEleChargedFlow.setElePeakAmount(new BigDecimal(peakAmount));
            feeEleChargedFlow.setEleValleyAmount(new BigDecimal(valleyAmount));
            feeEleChargedFlow.setEleAmount(new BigDecimal(peakAmount).add(new BigDecimal(valleyAmount)));

            if (StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.PART_RENT.getValue())
                    || StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.WHOLE_RENT.getValue())) {
                feeEleChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
            } else {
                feeEleChargedFlow.setPayer(PayerEnum.COMPANY.getValue());
            }
        } else {
            feeEleChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.JOINT_RENT.getValue()));
            FeeConfig feeConfig = feeCommonService.getFeeConfig(FeeTypeEnum.ELECTRICITY_UNIT, feeEleChargedFlow.getHouseId(), feeEleChargedFlow.getRoomId());

            if (feeConfig.getChargeMethod() == ChargeMethodEnum.FIX_MODEL.getValue()) {
                logger.error("当前房屋[houseId={}]为固定模式,不能生成收费记录", house.getId());
                throw new IllegalArgumentException("当前房屋为固定模式,不能生成收费记录");
            }

            double amount = (feeEleReadFlow.getEleDegree() - lastReadFlow.getEleDegree()) * Float.valueOf(feeConfig.getConfigValue());
            /*公共区域*/
            if (StringUtils.isNotBlank(feeEleChargedFlow.getRoomId()) && StringUtils.equals(feeEleChargedFlow.getRoomId(), "0")) {
                List<Room> rooms = feeCommonService.getRoomByHouseId(house.getId());

                /*正在出租的房间数*/
                Long rentRoomSize = rooms.stream().filter(room -> StringUtils.equals(room.getRoomStatus(), RoomStatusEnum.RENTED.getValue())).count();

                if (StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.PART_RENT.getValue())
                        || StringUtils.equals(house.getHouseStatus(), HouseStatusEnum.WHOLE_RENT.getValue())) {
                    feeEleChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
                } else {
                    feeEleChargedFlow.setPayer(PayerEnum.COMPANY.getValue());
                }

                feeEleChargedFlow.setEleAmount(new BigDecimal(amount).divide(new BigDecimal(rentRoomSize)));
            } else {
                Room room = feeCommonService.getRoomById(feeEleChargedFlow.getRoomId());
                if (StringUtils.equals(room.getRoomStatus(), RoomStatusEnum.RENTED.getValue())) {
                    feeEleChargedFlow.setPayer(PayerEnum.RENT_USER.getValue());
                } else {
                    feeEleChargedFlow.setPayer(PayerEnum.COMPANY.getValue());
                }
                feeEleChargedFlow.setEleAmount(new BigDecimal(amount));
            }
        }
        return feeEleChargedFlow;
    }

    public void generatorOrder() {
        /*获取所有为生成订单的数据*/
        FeeEleChargedFlow feeEleChargedFlow = new FeeEleChargedFlow();
        feeEleChargedFlow.setGenerateOrder(GenerateOrderEnum.NO.getValue());
        List<FeeEleChargedFlow> feeEleChargedFlows = this.findList(feeEleChargedFlow);
        /*按房屋分组*/
        Map<String, List<FeeEleChargedFlow>> feeEleChargedMap = feeEleChargedFlows.stream()
                .collect(Collectors.groupingBy(FeeEleChargedFlow::getHouseId));

        List<FeeEleChargedFlow> updEleCharges = Lists.newArrayList();
        List<FeeOrder> feeOrders = Lists.newArrayList();

        feeEleChargedMap.forEach((String k, List<FeeEleChargedFlow> v) -> {
            FeeEleChargedFlow judgeCharged = v.get(0);
            if (StringUtils.equals("" + judgeCharged.getRentType(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
                String orderNo = new IdGenerator().nextId();
                FeeOrder feeOrder = eleChargedFlowToFeeOrder(judgeCharged);
                feeOrder.setOrderNo(orderNo);
                v.stream().forEach(f -> {
                    f.setGenerateOrder(GenerateOrderEnum.YES.getValue());
                    f.setOrderNo(orderNo);
                    updEleCharges.add(f);
                    feeOrder.setAmount(feeOrder.getAmount().add(f.getEleValleyAmount()).add(f.getElePeakAmount()));
                });
                feeOrders.add(feeOrder);
            } else {
                /*按房间分组*/
                Map<String, List<FeeEleChargedFlow>> chargedFlowMap = v.stream()
                        .collect(Collectors.groupingBy(FeeEleChargedFlow::getRoomId));
                /*公摊记录*/
                List<FeeEleChargedFlow> commonAreaEleCharge = chargedFlowMap.get("0");

                chargedFlowMap.forEach((String key, List<FeeEleChargedFlow> value) -> {
                    if (StringUtils.equals(key, "0")) {
                        value.stream().forEach(f -> {
                            /*设置公摊的orderNo为000000*/
                            f.setOrderNo("000000");
                            updEleCharges.add(f);
                        });
                    } else {
                        String orderNo = new IdGenerator().nextId();
                        FeeOrder feeOrder = eleChargedFlowToFeeOrder(value.get(0));
                        feeOrder.setOrderNo(orderNo);
                        value.stream().forEach(f -> {
                            f.setOrderNo(orderNo);
                            f.setGenerateOrder(GenerateOrderEnum.YES.getValue());
                            updEleCharges.add(f);

                            feeOrder.setAmount(feeOrder.getAmount().add(f.getEleAmount()));

                            /*如果是租客支付,支付金额添加公摊数*/
                            if (f.getPayer() == PayerEnum.RENT_USER.getValue()) {
                                commonAreaEleCharge.forEach(c -> {
                                    if (StringUtils.equals(DateUtils.formatDate(f.getEleCalculateDate()), DateUtils.formatDate(c.getEleCalculateDate()))) {
                                        feeOrder.setAmount(feeOrder.getAmount().add(c.getEleAmount()));
                                    }
                                });
                            }
                        });
                        feeOrders.add(feeOrder);
                    }
                });
            }
        });

        this.batchUpdate(updEleCharges);
        logger.info("开始生成账单");
        feeOrderService.batchInsert(feeOrders);
    }

    @Transactional(readOnly = false)
    public void batchUpdate(List<FeeEleChargedFlow> feeEleChargedFlows){
        dao.batchUpdate(feeEleChargedFlows);
    }

    private FeeOrder eleChargedFlowToFeeOrder(FeeEleChargedFlow feeEleChargedFlow) {
        FeeOrder feeOrder = new FeeOrder();
        feeOrder.setPayer(feeEleChargedFlow.getPayer());
        feeOrder.setHouseId(feeEleChargedFlow.getHouseId());
        feeOrder.setOrderDate(new Date());
        feeOrder.setOrderStatus(OrderStatusEnum.COMMIT.getValue());
        feeOrder.setPropertyId(feeEleChargedFlow.getPropertyId());
        feeOrder.setOrderType(OrderTypeEnum.ELECTRICITY.getValue());
        feeOrder.setRoomId(feeEleChargedFlow.getRoomId());
        return feeOrder;
    }
}