/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.fee.common.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.common.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeEleReadFlowDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleReadFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeEleReadFlowVo;
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

    @Transactional(readOnly = false)
    public void feeEleReadFlowSave(FeeEleReadFlow feeEleReadFlow, String[] roomId, String[] eleDegree) {
        House house = feeCommonService.getHouseById(feeEleReadFlow.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[id={}]不存在,请确认", feeEleReadFlow.getHouseEleNum());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        if (StringUtils.equals(house.getIntentMode(), "0")) {
            FeeEleReadFlow query = new FeeEleReadFlow();
            query.setEleReadDate(feeEleReadFlow.getEleReadDate());
            query.setHouseId(feeEleReadFlow.getHouseId());
            List<FeeEleReadFlow> feeEleReadFlows = this.findList(query);
            if (Optional.ofNullable(feeEleReadFlows).isPresent()
                    && feeEleReadFlows.size() > 0) {
                FeeEleReadFlow existEleRead = feeEleReadFlows.get(0);
                feeEleReadFlow.setId(existEleRead.getId());
                if (existEleRead.getFromSource() == 1) {
                    logger.error("当前抄表数是账单生成,不可修改");
                    throw new IllegalArgumentException("当前抄表数是账单生成不可修改");
                }
            }
            feeEleReadFlow.setHouseEleNum(house.getEleAccountNum());
            feeEleReadFlow.setPropertyId(house.getPropertyProject().getId());
            save(feeEleReadFlow);
        } else {
            if (StringUtils.isBlank(feeEleReadFlow.getRoomId())) {
                for (int i = 0; i < roomId.length; i++) {
                    FeeEleReadFlow query = new FeeEleReadFlow();
                    query.setEleReadDate(feeEleReadFlow.getEleReadDate());
                    query.setRoomId(roomId[i]);
                    List<FeeEleReadFlow> feeEleReadFlows = this.findList(query);
                    if (Optional.ofNullable(feeEleReadFlows).isPresent()
                            && feeEleReadFlows.size() > 0) {
                        feeEleReadFlow.setId(feeEleReadFlows.get(0).getId());
                    }
                    feeEleReadFlow.setHouseEleNum(house.getEleAccountNum());
                    feeEleReadFlow.setRoomId(roomId[i]);
                    feeEleReadFlow.setPropertyId(house.getPropertyProject().getId());
                    feeEleReadFlow.setEleDegree(Float.valueOf(eleDegree[i]));
                    save(feeEleReadFlow);
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
                save(feeEleReadFlow);
            }
        }
    }

    public List<FeeEleReadFlowVo> getFeeEleReadFlowWithAllInfo(FeeCriteriaEntity feeCriteriaEntity) {
        return dao.getFeeEleReadFlowWithAllInfo(feeCriteriaEntity);
    }
}