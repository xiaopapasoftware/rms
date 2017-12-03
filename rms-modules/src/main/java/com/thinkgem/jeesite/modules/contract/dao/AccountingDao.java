/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author wangshujin
 */
@MyBatisDao
public interface AccountingDao extends CrudDao<Accounting> {
  void updatePaymentTransId(Accounting accounting);

  Date getFeeDateByContractId(@Param("contractId") String contractId);
}
