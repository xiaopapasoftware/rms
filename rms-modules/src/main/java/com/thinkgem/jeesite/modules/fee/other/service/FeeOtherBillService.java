/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.other.service;

import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.common.service.FeeCommonService;
import com.thinkgem.jeesite.modules.fee.enums.FeeBillStatusEnum;
import com.thinkgem.jeesite.modules.fee.other.dao.FeeOtherBillDao;
import com.thinkgem.jeesite.modules.fee.other.entity.FeeOtherBill;
import com.thinkgem.jeesite.modules.fee.other.entity.vo.FeeOtherBillVo;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* <p>宽带、电视费、其他账单表实现类 service</p>
* <p>Table: fee_other_bill - 宽带、电视费、其他账单表</p>
* @since 2017-11-28 03:02:33
* @author generator code
*/
@Service
@Transactional(readOnly = true)
public class FeeOtherBillService extends CrudService<FeeOtherBillDao, FeeOtherBill> {
    private Logger logger = LoggerFactory.getLogger(FeeOtherBillService.class);

    @Autowired
    private FeeCommonService feeCommonService;

    public List<FeeOtherBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity) {
        areaScopeFilter(feeCriteriaEntity, "dsf", "tpp.area_id=sua.area_id");
        return this.dao.getAllHouseFeeWithAreaAndBuildAndProperty(feeCriteriaEntity);
    }

    public Double getTotalAmount(FeeCriteriaEntity feeCriteriaEntity) {
        return this.dao.getTotalAmount(feeCriteriaEntity);
    }

    @Transactional(readOnly = false)
    public void saveFeeOtherBill(FeeOtherBill feeOtherBill) {
        House house = feeCommonService.getHouseById(feeOtherBill.getHouseId());
        if (!Optional.ofNullable(house).isPresent()) {
            logger.error("当前房屋[id={}]不存在,请确认", feeOtherBill.getHouseId());
            throw new IllegalArgumentException("当前房屋不存在,请确认");
        }

        if(StringUtils.isNotBlank(feeOtherBill.getId())){
            FeeOtherBill existBill = dao.get(feeOtherBill.getId());
            if (Optional.ofNullable(existBill).isPresent()) {
                if (existBill.getBillStatus() != null && existBill.getBillStatus() != FeeBillStatusEnum.APP.getValue()
                        && existBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                    logger.error("当前账单已经提交不能修改");
                    throw new IllegalArgumentException("当前账单已经提交不能修改");
                }
            }
        }

        this.save(feeOtherBill);
    }

    @Transactional(readOnly = false)
    public void deleteFeeOtherBill(String id) {
        FeeOtherBill feeOtherBill = dao.get(id);
        if (feeOtherBill.getBillStatus() != FeeBillStatusEnum.APP.getValue()
                && feeOtherBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
            logger.error("该账单[id={}]已提交,不能删除", id);
            throw new IllegalArgumentException("该账单已提交,不能删除");
        }

        FeeOtherBill updBill = new FeeOtherBill();
        updBill.setId(id);
        updBill.setDelFlag(Constants.DEL_FLAG_YES);
        this.save(updBill);
    }

    @Transactional(readOnly = false)
    public void feeOtherBillAudit(String status, String... ids) {
        String batchNo = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHMMssSSS");
        for (String id : ids) {
            FeeOtherBill feeOtherBill = dao.get(id);
            if (Optional.ofNullable(feeOtherBill).isPresent()) {
                switch (status) {
                    case "1":
                        if (feeOtherBill.getBillStatus() != FeeBillStatusEnum.APP.getValue() && feeOtherBill.getBillStatus() != FeeBillStatusEnum.REJECT.getValue()) {
                            logger.error("账单当前状态为{},不能提交",FeeBillStatusEnum.fromValue(feeOtherBill.getBillStatus()).getName());
                            throw new IllegalArgumentException("账单当前状态为[" + FeeBillStatusEnum.fromValue(feeOtherBill.getBillStatus()).getName() + "],不能提交");
                        }
                        break;
                    case "2":
                        if (feeOtherBill.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                            logger.error("账单当前状态为{},不能同意", FeeBillStatusEnum.fromValue(feeOtherBill.getBillStatus()).getName());
                            throw new IllegalArgumentException("账单当前状态为[" + FeeBillStatusEnum.fromValue(feeOtherBill.getBillStatus()).getName() + "],不可同意");
                        }
                        break;
                    case "3":
                        if (feeOtherBill.getBillStatus() == FeeBillStatusEnum.APP.getValue()) {
                            logger.error("账单当前状态为{},不能驳回", FeeBillStatusEnum.fromValue(feeOtherBill.getBillStatus()).getName());
                            throw new IllegalArgumentException("账单当前状态为[" + FeeBillStatusEnum.fromValue(feeOtherBill.getBillStatus()).getName() + "],不可驳回");
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("账单不在处理状态");
                }
                FeeOtherBill updFeeOtherBill = new FeeOtherBill();
                if (StringUtils.isBlank(updFeeOtherBill.getBatchNo())) {
                    updFeeOtherBill.setBatchNo(batchNo);
                }
                updFeeOtherBill.setBillStatus(Integer.valueOf(status));
                updFeeOtherBill.setId(feeOtherBill.getId());
                this.save(updFeeOtherBill);
                //TODO 记录审核日志
            }
        }
    }
}