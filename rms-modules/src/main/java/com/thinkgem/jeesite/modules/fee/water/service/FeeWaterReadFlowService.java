/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.water.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
import com.thinkgem.jeesite.modules.fee.water.dao.FeeWaterReadFlowDao;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterBill;
import com.thinkgem.jeesite.modules.fee.water.entity.FeeWaterReadFlow;
import com.thinkgem.jeesite.modules.fee.water.entity.vo.FeeWaterReadFlowVo;
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
 * <p>抄水表流水实现类 service</p>
 * <p>Table: fee_water_read_flow - 抄水表流水</p>
 *
 * @author generator code
 * @since 2017-10-20 06:26:14
 */
@Service
@Transactional(readOnly = true)
public class FeeWaterReadFlowService extends CrudService<FeeWaterReadFlowDao, FeeWaterReadFlow> {

    private Logger logger = LoggerFactory.getLogger(FeeWaterReadFlowService.class);

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeWaterChargedFlowService feeWaterChargedFlowService;

    @Transactional(readOnly = false)
    public void saveFeeWaterReadFlow(FeeWaterReadFlow feeWaterReadFlow) {
        House house = feeCommonService.getHouseById(feeWaterReadFlow.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[houseId={}]不存在,请确认", feeWaterReadFlow.getHouseId());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        FeeWaterReadFlow existWaterRead = dao.getCurrentReadByDateAndHouseId(feeWaterReadFlow.getWaterReadDate(), feeWaterReadFlow.getHouseId());
        if (Optional.ofNullable(existWaterRead).isPresent()) {
            feeWaterReadFlow.setId(existWaterRead.getId());
            if (existWaterRead.getFromSource() == FeeFromSourceEnum.ACCOUNT_BILL.getValue()) {
                logger.error("当前抄表数是账单生成,不可修改");
                throw new IllegalArgumentException("当前抄表数是账单生成不可修改");
            }
        }

        feeWaterReadFlow.setHouseWaterNum(house.getWaterAccountNum());
        feeWaterReadFlow.setPropertyId(house.getPropertyProject().getId());
        feeWaterReadFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());

        judgeLastRead(feeWaterReadFlow);

        save(feeWaterReadFlow);

        logger.info("生成收款流水");
        //save fee charge save
        feeWaterChargedFlowService.saveFeeWaterChargedFlowByFeeWaterReadFlow(feeWaterReadFlow);
    }

    private void judgeLastRead(FeeWaterReadFlow feeWaterReadFlow) {
        FeeWaterReadFlow lastRead = dao.getLastRecord(feeWaterReadFlow.getId(), feeWaterReadFlow.getHouseId());
        if (Optional.ofNullable(lastRead).isPresent()) {

            if (feeWaterReadFlow.getWaterDegree() != null && lastRead.getWaterDegree() > feeWaterReadFlow.getWaterDegree()) {
                logger.error("当前电表度数不能小于上次电表度数");
                throw new IllegalArgumentException("当前抄表度数不能小于上次抄表度数");
            }

            if (lastRead.getWaterReadDate().compareTo(feeWaterReadFlow.getWaterReadDate()) > 0) {
                logger.error("下次抄表数已经生成不能修改");
                throw new IllegalArgumentException("下次抄表数已经生成不能修改");
            }
        }
    }

    @Transactional(readOnly = false)
    public void saveFeeWaterReadFlowByFeeWaterBill(FeeWaterBill feeWaterBill) {
        FeeWaterReadFlow saveReadFlow = new FeeWaterReadFlow();
        saveReadFlow.setWaterDegree(feeWaterBill.getWaterDegree());
        saveReadFlow.setWaterReadDate(feeWaterBill.getWaterBillDate());
        saveReadFlow.setPropertyId(feeWaterBill.getPropertyId());
        saveReadFlow.setHouseId(feeWaterBill.getHouseId());
        saveReadFlow.setHouseWaterNum(feeWaterBill.getHouseWaterNum());
        saveReadFlow.setBusinessId(feeWaterBill.getId());
        saveReadFlow.setFromSource(FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        FeeWaterReadFlow existReadFlow = dao.getFeeWaterReadFlowByFeeBillId(feeWaterBill.getId());
        if (Optional.ofNullable(existReadFlow).isPresent()) {
            saveReadFlow.setId(existReadFlow.getId());
        }

        judgeLastRead(saveReadFlow);

        save(saveReadFlow);
    }

    @Transactional(readOnly = false)
    public void deleteFeeWaterReadFlowByFeeWaterBill(String feeWaterBillId) {
        FeeWaterReadFlow existReadFlow = dao.getFeeWaterReadFlowByFeeBillId(feeWaterBillId);
        if (Optional.ofNullable(existReadFlow).isPresent()) {
            FeeWaterReadFlow feeWaterReadFlow = new FeeWaterReadFlow();
            feeWaterReadFlow.setId(existReadFlow.getId());
            feeWaterReadFlow.setDelFlag(Constants.DEL_FLAG_YES);
            this.save(feeWaterReadFlow);
        }
    }

    @Transactional(readOnly = false)
    public void deleteFeeWaterReadFlow(String id) {
        FeeWaterReadFlow existWaterReadFlow = get(id);
        if (Optional.ofNullable(existWaterReadFlow).isPresent()) {
            if (existWaterReadFlow.getFromSource() == FeeFromSourceEnum.ACCOUNT_BILL.getValue()) {
                logger.error("该抄表[id={}]为账单录入,不能删除", id);
                throw new IllegalArgumentException("当前信息为账单生成,不能删除");
            }
            FeeWaterReadFlow feeWaterReadFlow = new FeeWaterReadFlow();
            feeWaterReadFlow.setId(id);
            feeWaterReadFlow.setDelFlag(Constants.DEL_FLAG_YES);
            this.save(feeWaterReadFlow);
            //删除相应的收费流水记录
            feeWaterChargedFlowService.deleteFeeWaterChargedFlowByBusinessIdAndFromSource(id, FeeFromSourceEnum.READ_METER.getValue());
        } else {
            logger.error("该抄表[id={}]不存在,不能删除", id);
            throw new IllegalArgumentException("当前信息不存在,不能删除");
        }
    }

    public List<FeeWaterReadFlowVo> getFeeWaterReadFlowWithAllInfo(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeWaterReadFlowWithAllInfo(feeCriteriaEntity);
    }

    public FeeWaterReadFlow getLastReadFlow(String id, String houseId) {
        return dao.getLastRecord(id, houseId);
    }

    public FeeWaterReadFlow getCurrentReadByDateAndHouseId(Date gasReadDate, String houseId) {
        return dao.getCurrentReadByDateAndHouseId(gasReadDate, houseId);
    }
}