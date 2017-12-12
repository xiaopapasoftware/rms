/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.gas.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
import com.thinkgem.jeesite.modules.fee.gas.dao.FeeGasReadFlowDao;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasBill;
import com.thinkgem.jeesite.modules.fee.gas.entity.FeeGasReadFlow;
import com.thinkgem.jeesite.modules.fee.gas.entity.vo.FeeGasReadFlowVo;
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
 * <p>抄燃气表流水实现类 service</p>
 * <p>Table: fee_gas_read_flow - 抄燃气表流水</p>
 *
 * @author generator code
 * @since 2017-10-20 06:26:38
 */
@Service
@Transactional(readOnly = true)
public class FeeGasReadFlowService extends CrudService<FeeGasReadFlowDao, FeeGasReadFlow> {

    private Logger logger = LoggerFactory.getLogger(FeeGasReadFlowService.class);

    @Autowired
    private FeeCommonService feeCommonService;

    @Autowired
    private FeeGasChargedFlowService feeGasChargedFlowService;

    @Transactional(readOnly = false)
    public void saveFeeGasReadFlow(FeeGasReadFlow feeGasReadFlow) {
        House house = feeCommonService.getHouseById(feeGasReadFlow.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[id={}]不存在,请确认", feeGasReadFlow.getHouseGasNum());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        FeeGasReadFlow existGasRead = dao.getCurrentReadByDateAndHouseId(feeGasReadFlow.getGasReadDate(), feeGasReadFlow.getHouseId());
        if (Optional.ofNullable(existGasRead).isPresent()) {
            feeGasReadFlow.setId(existGasRead.getId());
            if (existGasRead.getFromSource() == FeeFromSourceEnum.ACCOUNT_BILL.getValue()) {
                logger.error("当前抄表数是账单生成,不可修改");
                throw new IllegalArgumentException("当前抄表数是账单生成不可修改");
            }
        }

        feeGasReadFlow.setHouseGasNum(house.getGasAccountNum());
        feeGasReadFlow.setPropertyId(house.getPropertyProject().getId());
        feeGasReadFlow.setFromSource(FeeFromSourceEnum.READ_METER.getValue());

        judgeLastRead(feeGasReadFlow);

        save(feeGasReadFlow);

        logger.info("生成收款流水");
        //save fee charge save
        feeGasChargedFlowService.saveFeeGasChargedFlowByFeeGasReadFlow(feeGasReadFlow);
    }

    private void judgeLastRead(FeeGasReadFlow feeGasReadFlow) {
        FeeGasReadFlow lastRead = dao.getLastRecord(feeGasReadFlow.getId(), feeGasReadFlow.getHouseId());
        if (Optional.ofNullable(lastRead).isPresent()) {

            if (feeGasReadFlow.getGasDegree() != null && lastRead.getGasDegree() > feeGasReadFlow.getGasDegree()) {
                logger.error("当前电表度数不能小于上次电表度数");
                throw new IllegalArgumentException("当前电表度数不能小于上次电表度数");
            }

            if (lastRead.getGasReadDate().compareTo(feeGasReadFlow.getGasReadDate()) > 0) {
                logger.error("下次抄表数已经生成不能修改");
                throw new IllegalArgumentException("下次抄表数已经生成不能修改");
            }
        }
    }

    @Transactional(readOnly = false)
    public void saveFeeGasReadFlowByFeeGasBill(FeeGasBill feeGasBill) {
        FeeGasReadFlow saveReadFlow = new FeeGasReadFlow();
        saveReadFlow.setGasDegree(feeGasBill.getGasDegree());
        saveReadFlow.setGasReadDate(feeGasBill.getGasBillDate());
        saveReadFlow.setPropertyId(feeGasBill.getPropertyId());
        saveReadFlow.setHouseId(feeGasBill.getHouseId());
        saveReadFlow.setHouseGasNum(feeGasBill.getHouseGasNum());
        saveReadFlow.setBusinessId(feeGasBill.getId());
        saveReadFlow.setFromSource(FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        FeeGasReadFlow existReadFlow = dao.getFeeGasReadFlowByFeeBillId(feeGasBill.getId());
        if (Optional.ofNullable(existReadFlow).isPresent()) {
            saveReadFlow.setId(existReadFlow.getId());
        }

        judgeLastRead(saveReadFlow);

        save(saveReadFlow);
    }

    @Transactional(readOnly = false)
    public void deleteFeeGasReadFlowByFeeGasBill(String feeGasBillId) {
        FeeGasReadFlow existReadFlow = dao.getFeeGasReadFlowByFeeBillId(feeGasBillId);
        if (Optional.ofNullable(existReadFlow).isPresent()) {
            FeeGasReadFlow feeGasReadFlow = new FeeGasReadFlow();
            feeGasReadFlow.setId(existReadFlow.getId());
            feeGasReadFlow.setDelFlag(Constants.DEL_FLAG_YES);
            this.save(feeGasReadFlow);
        }
    }

    @Transactional(readOnly = false)
    public void deleteFeeGasReadFlow(String id) {
        FeeGasReadFlow existGasReadFlow = get(id);
        if (Optional.ofNullable(existGasReadFlow).isPresent()) {
            if (existGasReadFlow.getFromSource() == FeeFromSourceEnum.ACCOUNT_BILL.getValue()) {
                logger.error("该抄表[id={}]为账单录入,不能删除", id);
                throw new IllegalArgumentException("当前信息为账单生成,不能删除");
            }
            FeeGasReadFlow feeGasReadFlow = new FeeGasReadFlow();
            feeGasReadFlow.setId(id);
            feeGasReadFlow.setDelFlag(Constants.DEL_FLAG_YES);
            this.save(feeGasReadFlow);
            /*删除相应的收费流水记录*/
            feeGasChargedFlowService.deleteFeeGasChargedFlowByBusinessIdAndFromSource(id, FeeFromSourceEnum.READ_METER.getValue());
        } else {
            logger.error("该抄表[id={}]不存在,不能删除", id);
            throw new IllegalArgumentException("当前信息不存在,不能删除");
        }
    }

    public List<FeeGasReadFlowVo> getFeeGasReadFlowWithAllInfo(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeGasReadFlowWithAllInfo(feeCriteriaEntity);
    }

    public FeeGasReadFlow getLastReadFlow(String id, String houseId) {
        return dao.getLastRecord(id, houseId);
    }

    public FeeGasReadFlow getCurrentReadByDateAndHouseId(Date gasReadDate, String houseId) {
        return dao.getCurrentReadByDateAndHouseId(gasReadDate, houseId);
    }
}