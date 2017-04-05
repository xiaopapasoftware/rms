/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.fee.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.fee.entity.ElectricFee;

/**
 * 电费结算DAO接口
 */
@MyBatisDao
public interface ElectricFeeDao extends CrudDao<ElectricFee> {

  List<ElectricFee> getElectricFeeByPaymentTransId(String paymentTransId);
}
