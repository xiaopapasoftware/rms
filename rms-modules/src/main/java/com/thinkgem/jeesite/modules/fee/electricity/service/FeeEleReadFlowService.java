/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeEleReadFlowDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleReadFlowVo;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>抄电表流水实现类 service</p>
 * <p>Table: fee_ele_read_flow - 抄电表流水</p>
 *
 * @author generator code
 * @since 2017-09-18 08:24:39
 */
@Service
@Transactional(readOnly = true)
public class FeeEleReadFlowService extends CrudService<FeeEleReadFlowDao, FeeEleReadFlow> {

    private Logger logger = LoggerFactory.getLogger(FeeElectricityBillService.class);

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeEleChargedFlowService feeEleChargedFlowService;

    @Transactional(readOnly = false)
    public void feeEleReadFlowSave(FeeEleReadFlow feeEleReadFlow, String[] roomId, String[] eleDegree) {
        House house = feeCommonService.getHouseById(feeEleReadFlow.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[id={}]不存在,请确认", feeEleReadFlow.getHouseEleNum());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        if (StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
            FeeEleReadFlow query = new FeeEleReadFlow();
            query.setEleReadDate(feeEleReadFlow.getEleReadDate());
            query.setHouseId(feeEleReadFlow.getHouseId());
            List<FeeEleReadFlow> feeEleReadFlows = this.findList(query);
            if (Optional.ofNullable(feeEleReadFlows).isPresent()
                    && feeEleReadFlows.size() > 0) {
                FeeEleReadFlow existEleRead = feeEleReadFlows.get(0);
                feeEleReadFlow.setId(existEleRead.getId());
                if (existEleRead.getFromSource() == FeeFromSourceEnum.ACCOUNT_BILL.getValue()) {
                    logger.error("当前抄表数是账单生成,不可修改");
                    throw new IllegalArgumentException("当前抄表数是账单生成不可修改");
                }
            }

            feeEleReadFlow.setHouseEleNum(house.getEleAccountNum());
            feeEleReadFlow.setPropertyId(house.getPropertyProject().getId());
            feeEleReadFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());

            judgeLastRead(feeEleReadFlow);

            save(feeEleReadFlow);

            logger.info("生成收款流水");
            //save fee charge save
            feeEleChargedFlowService.saveFeeEleChargedFlowByFeeEleReadFlow(feeEleReadFlow);
        } else {
            if (StringUtils.isBlank(feeEleReadFlow.getRoomId())) {
                for (int i = 0; i < roomId.length; i++) {
                    FeeEleReadFlow saveFeeEleReadFlow = new FeeEleReadFlow();

                    FeeEleReadFlow query = new FeeEleReadFlow();
                    query.setEleReadDate(feeEleReadFlow.getEleReadDate());
                    query.setRoomId(roomId[i]);
                    List<FeeEleReadFlow> feeEleReadFlows = this.findList(query);
                    if (Optional.ofNullable(feeEleReadFlows).isPresent()
                            && feeEleReadFlows.size() > 0) {
                        saveFeeEleReadFlow.setId(feeEleReadFlows.get(0).getId());
                    }

                    saveFeeEleReadFlow.setEleValleyDegree(feeEleReadFlow.getEleValleyDegree());
                    saveFeeEleReadFlow.setElePeakDegree(feeEleReadFlow.getElePeakDegree());
                    saveFeeEleReadFlow.setEleReadDate(feeEleReadFlow.getEleReadDate());
                    saveFeeEleReadFlow.setHouseId(feeEleReadFlow.getHouseId());
                    saveFeeEleReadFlow.setHouseEleNum(house.getEleAccountNum());
                    saveFeeEleReadFlow.setRoomId(roomId[i]);
                    saveFeeEleReadFlow.setPropertyId(house.getPropertyProject().getId());
                    saveFeeEleReadFlow.setEleDegree(Float.valueOf(eleDegree[i]));
                    saveFeeEleReadFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());

                    judgeLastRead(saveFeeEleReadFlow);

                    save(saveFeeEleReadFlow);

                    logger.info("生成收款流水");
                    //save fee charge
                    feeEleChargedFlowService.saveFeeEleChargedFlowByFeeEleReadFlow(saveFeeEleReadFlow);
                }
            } else {
                FeeEleReadFlow query = new FeeEleReadFlow();
                query.setEleReadDate(feeEleReadFlow.getEleReadDate());
                query.setRoomId(feeEleReadFlow.getRoomId());
                List<FeeEleReadFlow> feeEleReadFlows = this.findList(query);
                if (Optional.ofNullable(feeEleReadFlows).isPresent()
                        && feeEleReadFlows.size() > 0) {
                    feeEleReadFlow.setId(feeEleReadFlows.get(0).getId());
                }
                feeEleReadFlow.setEleDegree(feeEleReadFlow.getEleDegree());
                feeEleReadFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());

                judgeLastRead(feeEleReadFlow);

                save(feeEleReadFlow);

                logger.info("生成收款流水");
                //save fee charge
                feeEleChargedFlowService.saveFeeEleChargedFlowByFeeEleReadFlow(feeEleReadFlow);
            }
        }
    }

    private void judgeLastRead(FeeEleReadFlow feeEleReadFlow){
        FeeEleReadFlow lastRead = dao.getLastReadFlow(feeEleReadFlow);
        if(Optional.ofNullable(lastRead).isPresent()){
            if(feeEleReadFlow.getElePeakDegree() != null && lastRead.getElePeakDegree() > feeEleReadFlow.getElePeakDegree()){
                logger.error("当前峰值数不能小于上次峰值数");
                throw new IllegalArgumentException("当前峰值数不能小于上次峰值数");
            }

            if(feeEleReadFlow.getEleValleyDegree() != null && lastRead.getEleValleyDegree() > feeEleReadFlow.getEleValleyDegree()){
                logger.error("当前谷值数不能小于上次谷值数");
                throw new IllegalArgumentException("当前谷值数不能小于上次谷值数");
            }

            if(feeEleReadFlow.getEleDegree() != null && lastRead.getEleDegree() > feeEleReadFlow.getEleDegree()){
                logger.error("当前电表度数不能小于上次电表度数");
                throw new IllegalArgumentException("当前电表度数不能小于上次电表度数");
            }

            if(lastRead.getEleReadDate().compareTo(feeEleReadFlow.getEleReadDate()) > 0){
                logger.error("下次抄表数已经生成不能修改");
                throw new IllegalArgumentException("下次抄表数已经生成不能修改");
            }
        }
    }

    @Transactional(readOnly = false)
    public void saveFeeEleReadFlowByFeeEleBill(FeeElectricityBill feeElectricityBill){
        FeeEleReadFlow saveReadFlow = new FeeEleReadFlow();
        saveReadFlow.setElePeakDegree(feeElectricityBill.getElePeakDegree());
        saveReadFlow.setEleValleyDegree(feeElectricityBill.getEleValleyDegree());
        saveReadFlow.setEleReadDate(feeElectricityBill.getEleBillDate());
        saveReadFlow.setPropertyId(feeElectricityBill.getPropertyId());
        saveReadFlow.setHouseId(feeElectricityBill.getHouseId());
        saveReadFlow.setHouseEleNum(feeElectricityBill.getHouseEleNum());
        saveReadFlow.setBusinessId(feeElectricityBill.getId());
        saveReadFlow.setFromSource(FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        FeeEleReadFlow existReadFlow = dao.getFeeEleReadFlowByFeeBillId(feeElectricityBill.getId());
        if (Optional.ofNullable(existReadFlow).isPresent()) {
            saveReadFlow.setId(existReadFlow.getId());
        }
        judgeLastRead(saveReadFlow);

        save(saveReadFlow);

        logger.info("生成收款流水");
        //save fee charge
        feeEleChargedFlowService.saveFeeEleChargedFlowByFeeEleReadFlow(saveReadFlow);
    }

    @Transactional(readOnly = false)
    public void deleteFeeEleReadFlowByFeeEleBill(String feeEleBillId){
        FeeEleReadFlow existReadFlow = dao.getFeeEleReadFlowByFeeBillId(feeEleBillId);
        FeeEleReadFlow feeEleReadFlow = new FeeEleReadFlow();
        feeEleReadFlow.setId(existReadFlow.getId());
        feeEleReadFlow.setDelFlag(Constants.DEL_FLAG_YES);
        this.save(feeEleReadFlow);
    }

    @Transactional(readOnly = false)
    public void deleteFeeEleReadFlow(String id){
        FeeEleReadFlow existEleReadFlow = get(id);
        if (Optional.ofNullable(existEleReadFlow).isPresent()) {
            if(existEleReadFlow.getFromSource() == FeeFromSourceEnum.ACCOUNT_BILL.getValue()){
                logger.error("电抄表[id={}]为账单录入,不能删除", id);
                throw new IllegalArgumentException("当前信息为账单生成,不能删除");
            }
            FeeEleReadFlow feeEleReadFlow = new FeeEleReadFlow();
            feeEleReadFlow.setId(id);
            feeEleReadFlow.setDelFlag(Constants.DEL_FLAG_YES);
            this.save(feeEleReadFlow);
            //删除相应的收费流水记录
            feeEleChargedFlowService.deleteFeeEleChargedFlowByBusinessIdAndFromSource(id,FeeFromSourceEnum.READ_METER.getValue());
        } else {
            logger.error("电抄表[id={}]不存在,不能删除", id);
            throw new IllegalArgumentException("当前信息不存在,不能删除");
        }
    }

    public List<FeeEleReadFlowVo> getFeeEleReadFlowWithAllInfo(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeEleReadFlowWithAllInfo(feeCriteriaEntity);
    }

    public FeeEleReadFlow getLastReadFlow(FeeEleReadFlow feeEleReadFlow){
        return dao.getLastReadFlow(feeEleReadFlow);
    }
}