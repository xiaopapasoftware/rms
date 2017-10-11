/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeEleChargedFlowDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeEleChargedFlow;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.enums.FeeFromSourceEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
* <p>电费收取流水实现类 service</p>
* <p>Table: fee_ele_charged_flow - 电费收取流水</p>
* @since 2017-09-18 08:24:32
* @author generator code
*/
@Service
@Transactional(readOnly = true)
public class FeeEleChargedFlowService extends CrudService<FeeEleChargedFlowDao, FeeEleChargedFlow> {

    @Transactional(readOnly = false)
    public void saveFeeEleChargedFlowByFeeEleBill(FeeElectricityBill feeElectricityBill){
        FeeEleChargedFlow saveChargedFlow = new FeeEleChargedFlow();
        saveChargedFlow.setEleAmount(feeElectricityBill.getEleBillAmount());
        saveChargedFlow.setCreateDate(feeElectricityBill.getEleBillDate());
        saveChargedFlow.setPropertyId(feeElectricityBill.getPropertyId());
        saveChargedFlow.setHouseId(feeElectricityBill.getHouseId());
        saveChargedFlow.setHouseEleNum(feeElectricityBill.getHouseEleNum());
        saveChargedFlow.setBusinessId(feeElectricityBill.getId());
        saveChargedFlow.setGenerateOrder(-1);
        saveChargedFlow.setRentType(Integer.valueOf(RentModelTypeEnum.WHOLE_RENT.getValue()));
        saveChargedFlow.setFromSource(FeeFromSourceEnum.ACCOUNT_BILL.getValue());

        FeeEleChargedFlow existEleChargedFlow = this.dao.getFeeEleChargedFlowByBusinessIdAndFromSource(feeElectricityBill.getId(),FeeFromSourceEnum.ACCOUNT_BILL.getValue());
        if (Optional.ofNullable(existEleChargedFlow).isPresent()) {
            saveChargedFlow.setId(existEleChargedFlow.getId());
        }
        save(saveChargedFlow);
    }

    @Transactional(readOnly = false)
    public void deleteFeeEleChargedFlowByFeeEleBill(String feeEleBillId,int fromSource){
        FeeEleChargedFlow existEleChargedFlow = this.dao.getFeeEleChargedFlowByBusinessIdAndFromSource(feeEleBillId,fromSource);
        FeeEleChargedFlow feeEleChargedFlow = new FeeEleChargedFlow();
        feeEleChargedFlow.setId(existEleChargedFlow.getId());
        feeEleChargedFlow.setDelFlag(Constants.DEL_FLAG_YES);
        save(feeEleChargedFlow);
    }


}