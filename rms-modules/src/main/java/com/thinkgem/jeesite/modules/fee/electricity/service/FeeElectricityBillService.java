/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.electricity.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.fee.common.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.common.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.electricity.dao.FeeElectricityBillDao;
import com.thinkgem.jeesite.modules.fee.electricity.entity.FeeElectricityBill;
import com.thinkgem.jeesite.modules.fee.electricity.entity.vo.FeeElectricityBillVo;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.enums.HouseRentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* <p>电费账单表实现类 service</p>
* <p>Table: fee_electricity_bill - 电费账单表</p>
* @since 2017-09-18 08:24:24
* @author generator code
*/
@Service
@Transactional(readOnly = true)
public class FeeElectricityBillService extends CrudService<FeeElectricityBillDao, FeeElectricityBill> {

    @Autowired
    private FeeCommonService feeCommonService;

    public List<FeeElectricityBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity){
        areaScopeFilter(feeCriteriaEntity, "dsf", "tpp.area_id=sua.area_id");
        return this.dao.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
    }

    public FeeElectricityBillVo getWithProperty(String id,String houseId){
        FeeElectricityBillVo feeElectricityBillVo = this.dao.getWithProperty(id);
        if(Optional.ofNullable(feeElectricityBillVo).isPresent()){
            House queryHouse = new House();
            queryHouse.setId(houseId);
            House house = feeCommonService.getHouseById(queryHouse);
            if(Optional.ofNullable(house).isPresent()) {
                feeElectricityBillVo.setHouseId(house.getHouseId());
                feeElectricityBillVo.setHouseEleNum(house.getEleNum());
                feeElectricityBillVo.setProjectAddress(house.getProjectAddr());
            }
        }
        return feeElectricityBillVo;
    }

    @Transactional(readOnly = false)
    public void saveFeeElectricityBill(FeeElectricityBill feeElectricityBill){
        House queryHouse = new House();
        queryHouse.setId(feeElectricityBill.getHouseId());
        House house = feeCommonService.getHouseById(queryHouse);
        if(Optional.ofNullable(house).isPresent()) {
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }
        FeeElectricityBill query = new FeeElectricityBill();
        query.setEleBillDate(feeElectricityBill.getEleBillDate());
        query.setHouseEleNum(feeElectricityBill.getHouseEleNum());
        List<FeeElectricityBill> feeElectricityBills = this.findList(query);
        if(!Optional.ofNullable(feeElectricityBills).isPresent() && feeElectricityBills.size() > 0){
            feeElectricityBill.setId(feeElectricityBills.get(0).getId());
        }
        this.save(feeElectricityBill);

        //TODO 判断房屋是否整组，如果整组生成抄表流水记录
        if(StringUtils.equals(house.getRentMethod(), HouseRentMethod.FULL_RENT.value())){

        }
    }
}