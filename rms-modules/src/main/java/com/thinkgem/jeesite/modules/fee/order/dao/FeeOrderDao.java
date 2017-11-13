/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.order.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import com.thinkgem.jeesite.modules.fee.common.entity.FeeCriteriaEntity;
import com.thinkgem.jeesite.modules.fee.order.entity.FeeOrder;
import com.thinkgem.jeesite.modules.fee.order.entity.vo.FeeOrderVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>订单实现类service</p>
 * <p>Table: fee_order - 订单</p>
 * @since 2017-11-05 10:05:53
 * @author generator code
*/
@MyBatisDao
public interface FeeOrderDao extends CrudDao<FeeOrder>{

    void batchInsert(List<FeeOrder> feeOrderList);

    List<FeeOrderVo> getFeeOrderList(FeeCriteriaEntity feeCriteriaEntity);

    List<FeeOrder> getFeeOrderByIds(@Param("ids") String ids);
}