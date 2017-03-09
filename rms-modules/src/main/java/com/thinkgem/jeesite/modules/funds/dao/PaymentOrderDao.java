/**
 * \ * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All
 * rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentOrder;

/**
 * 支付订单信息DAO接口
 * 
 * @author huangsc
 */
@MyBatisDao
public interface PaymentOrderDao extends CrudDao<PaymentOrder> {

  List<PaymentOrder> findByHouseId(PaymentOrder paymentOrder);

  PaymentOrder findByOrderId(PaymentOrder paymentOrder);

  void updateStatusByOrderId(PaymentOrder paymentOrder);

  void deleteByTradeId(PaymentOrder paymentOrder);
}
