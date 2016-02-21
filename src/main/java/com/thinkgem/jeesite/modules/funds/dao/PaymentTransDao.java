/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;

/**
 * 款项交易DAO接口
 * @author huangsc
 * @version 2015-06-11
 */
@MyBatisDao
public interface PaymentTransDao extends CrudDao<PaymentTrans> {
	public List<PaymentTrans> findRemindList(PaymentTrans paymentTrans);
}