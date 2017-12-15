/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
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

import java.util.Date;
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
            logger.error("当前房屋[id={}]不存在,请确认", feeEleReadFlow.getHouseId());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        feeEleReadFlow.setHouseEleNum(house.getEleAccountNum());
        feeEleReadFlow.setPropertyId(house.getPropertyProject().getId());
        feeEleReadFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());
        feeEleReadFlow.setRoomId("0");

        if (StringUtils.equals(house.getIntentMode(), RentModelTypeEnum.WHOLE_RENT.getValue())) {
            List<FeeEleReadFlow> existEleReads = dao.getCurrentReadByDateAndHouseIdAndRoomId(feeEleReadFlow.getEleReadDate(), feeEleReadFlow.getHouseId(), "0");
            if (Optional.ofNullable(existEleReads).isPresent()) {
                FeeEleReadFlow existEleRead = existEleReads.get(0);
                feeEleReadFlow.setId(existEleRead.getId());
                if (existEleRead.getFromSource() == FeeFromSourceEnum.ACCOUNT_BILL.getValue()) {
                    logger.error("当前抄表数是账单生成,不可修改");
                    throw new IllegalArgumentException("当前抄表数是账单生成不可修改");
                }
            }

            judgeLastRead(feeEleReadFlow);

            save(feeEleReadFlow);

            logger.info("生成收款流水");
            //save fee charge save
            feeEleChargedFlowService.saveFeeEleChargedFlowByFeeEleReadFlow(feeEleReadFlow);
        } else {
            /*新增*/
            if (StringUtils.isBlank(feeEleReadFlow.getRoomId())) {
                double allDegree = 0f, houseDegree = 0f;
                for (int i = 0; i < roomId.length; i++) {
                    if (StringUtils.equals(roomId[i], "0")) {
                        allDegree = Double.valueOf(eleDegree[i]);
                    } else {
                        houseDegree += Double.valueOf(eleDegree[i]);
                    }
                }
                if (allDegree < houseDegree) {
                    logger.error("总表的度数不能小于各房间度数之和");
                    throw new IllegalArgumentException("总表的度数不能小于各房间度数之和");
                }
                for (int i = 0; i < roomId.length; i++) {

                    FeeEleReadFlow saveFeeEleReadFlow = feeEleReadFlow.clone();
                    saveFeeEleReadFlow.setRoomId(roomId[i]);
                    /*总表存公摊数*/
                    if (StringUtils.equals(roomId[i], "0")) {
                        saveFeeEleReadFlow.setEleDegree(allDegree - houseDegree);
                    } else {
                        saveFeeEleReadFlow.setEleDegree(Double.valueOf(eleDegree[i]));
                    }

                    /*查询今天是否已经存在抄表，一天只有一条抄表记录*/
                    List<FeeEleReadFlow> existEleReads = dao.getCurrentReadByDateAndHouseIdAndRoomId(feeEleReadFlow.getEleReadDate(), feeEleReadFlow.getHouseId(), roomId[i]);
                    if (Optional.ofNullable(existEleReads).isPresent()) {
                        FeeEleReadFlow existEleRead = existEleReads.get(0);
                        saveFeeEleReadFlow.setId(existEleRead.getId());
                    }

                    judgeLastRead(saveFeeEleReadFlow);

                    save(saveFeeEleReadFlow);

                    logger.info("生成收款流水");
                    //save fee charge
                    feeEleChargedFlowService.saveFeeEleChargedFlowByFeeEleReadFlow(saveFeeEleReadFlow);
                }
            } else {
                /*修改*/
                List<FeeEleReadFlow> existEleReads = dao.getCurrentReadByDateAndHouseIdAndRoomId(feeEleReadFlow.getEleReadDate(), feeEleReadFlow.getHouseId(), "");

                FeeEleReadFlow updateRecord = existEleReads.stream().filter(f -> StringUtils.equals(f.getRoomId(), feeEleReadFlow.getRoomId())).findFirst().orElseGet(null);
                if (Optional.ofNullable(updateRecord).isPresent()) {
                    feeEleReadFlow.setId(updateRecord.getId());
                }else {
                    logger.error("当前修改记录不存在请确认在修改");
                    throw new IllegalArgumentException("当前修改记录不存在请确认在修改");
                }

                /*修改总表记录*/
                if (StringUtils.equals(feeEleReadFlow.getRoomId(), "0")) {
                    double otherRecord = existEleReads.stream().filter(f -> !StringUtils.equals(f.getRoomId(), feeEleReadFlow.getRoomId()))
                            .mapToDouble(FeeEleReadFlow::getEleDegree).sum();
                    if (feeEleReadFlow.getEleDegree() < otherRecord) {
                        logger.error("总表的度数不能小于各房间度数之和");
                        throw new IllegalArgumentException("总表的度数不能小于各房间度数之和");
                    }
                } else {
                    /*修改房间记录*/
                    double allRecord = existEleReads.stream().filter(f -> StringUtils.equals(f.getRoomId(), "0")).mapToDouble(FeeEleReadFlow::getEleDegree).sum();
                    double otherRecord = existEleReads.stream()
                            .filter(f -> !StringUtils.equals(f.getRoomId(), feeEleReadFlow.getRoomId()) && !StringUtils.equals(f.getRoomId(), "0"))
                            .mapToDouble(FeeEleReadFlow::getEleDegree).sum();

                        otherRecord += feeEleReadFlow.getEleDegree();

                    if (allRecord < otherRecord) {
                        logger.error("总表的度数不能小于各房间度数之和");
                        throw new IllegalArgumentException("总表的度数不能小于各房间度数之和");
                    }
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

    private void judgeLastRead(FeeEleReadFlow feeEleReadFlow) {
        FeeEleReadFlow lastRead = dao.getLastRecord(feeEleReadFlow.getId(), feeEleReadFlow.getHouseId(), feeEleReadFlow.getRoomId());
        if (Optional.ofNullable(lastRead).isPresent()) {
            if (feeEleReadFlow.getElePeakDegree() != null && lastRead.getElePeakDegree() > feeEleReadFlow.getElePeakDegree()) {
                logger.error("当前峰值数不能小于上次峰值数");
                throw new IllegalArgumentException("当前峰值数不能小于上次峰值数");
            }

            if (feeEleReadFlow.getEleValleyDegree() != null && lastRead.getEleValleyDegree() > feeEleReadFlow.getEleValleyDegree()) {
                logger.error("当前谷值数不能小于上次谷值数");
                throw new IllegalArgumentException("当前谷值数不能小于上次谷值数");
            }

            if (feeEleReadFlow.getEleDegree() != null && lastRead.getEleDegree() > feeEleReadFlow.getEleDegree()) {
                logger.error("当前电表度数不能小于上次电表度数");
                throw new IllegalArgumentException("当前电表度数不能小于上次电表度数");
            }

            if (lastRead.getEleReadDate().compareTo(feeEleReadFlow.getEleReadDate()) > 0) {
                logger.error("下次抄表数已经生成不能修改");
                throw new IllegalArgumentException("下次抄表数已经生成不能修改");
            }
        }
    }

    @Transactional(readOnly = false)
    public void saveFeeEleReadFlowByFeeEleBill(FeeElectricityBill feeElectricityBill) {
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
    }

    @Transactional(readOnly = false)
    public void deleteFeeEleReadFlowByFeeEleBill(String feeEleBillId) {
        FeeEleReadFlow existReadFlow = dao.getFeeEleReadFlowByFeeBillId(feeEleBillId);
        if (Optional.ofNullable(existReadFlow).isPresent()) {
            FeeEleReadFlow feeEleReadFlow = new FeeEleReadFlow();
            feeEleReadFlow.setId(existReadFlow.getId());
            feeEleReadFlow.setDelFlag(Constants.DEL_FLAG_YES);
            this.save(feeEleReadFlow);
        }
    }

    @Transactional(readOnly = false)
    public void deleteFeeEleReadFlow(String id) {
        FeeEleReadFlow existEleReadFlow = get(id);
        if (Optional.ofNullable(existEleReadFlow).isPresent()) {
            if (existEleReadFlow.getFromSource() == FeeFromSourceEnum.ACCOUNT_BILL.getValue()) {
                logger.error("该抄表[id={}]为账单录入,不能删除", id);
                throw new IllegalArgumentException("当前信息为账单生成,不能删除");
            }
            FeeEleReadFlow feeEleReadFlow = new FeeEleReadFlow();
            feeEleReadFlow.setId(id);
            feeEleReadFlow.setDelFlag(Constants.DEL_FLAG_YES);
            this.save(feeEleReadFlow);
            /*删除相应的收费流水记录*/
            feeEleChargedFlowService.deleteFeeEleChargedFlowByBusinessIdAndFromSource(id, FeeFromSourceEnum.READ_METER.getValue());
        } else {
            logger.error("该抄表[id={}]不存在,不能删除", id);
            throw new IllegalArgumentException("当前信息不存在,不能删除");
        }
    }

    public List<FeeEleReadFlowVo> getFeeEleReadFlowWithAllInfo(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeEleReadFlowWithAllInfo(feeCriteriaEntity);
    }

    public FeeEleReadFlow getLastReadFlow(String id, String houseId, String roomId) {
        return dao.getLastRecord(id, houseId, roomId);
    }

    public FeeEleReadFlow getCurrentReadByDateAndHouseIdAndRoomId(Date eleReadDate, String houseId, String roomId) {
        List<FeeEleReadFlow> feeEleReadFlows = dao.getCurrentReadByDateAndHouseIdAndRoomId(eleReadDate, houseId, roomId);
        if (Optional.ofNullable(feeEleReadFlows).isPresent()) {
            return feeEleReadFlows.get(0);
        }
        return null;
    }
}