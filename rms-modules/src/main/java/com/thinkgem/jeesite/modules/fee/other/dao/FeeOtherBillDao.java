/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.other.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.other.entity.FeeOtherBill;
import com.thinkgem.jeesite.modules.fee.other.entity.vo.FeeOtherBillVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>宽带、电视费、其他账单表实现类service</p>
 * <p>Table: fee_other_bill - 宽带、电视费、其他账单表</p>
 * @since 2017-11-28 03:02:34
 * @author generator code
*/
@MyBatisDao
public interface FeeOtherBillDao extends CrudDao<FeeOtherBill>{

    List<FeeOtherBillVo> getAllHouseFeeWithAreaAndBuildAndProperty(FeeCriteriaEntity feeCriteriaEntity);

    FeeOtherBill getLastRecord(@Param("id") String id, @Param("houseId") String houseId);

    List<FeeOtherBill> getOtherBillByIds(String ...ids);

    int batchUpdate(List<FeeOtherBill> feeOtherBills);

    Double getTotalAmount(FeeCriteriaEntity feeCriteriaEntity);
}