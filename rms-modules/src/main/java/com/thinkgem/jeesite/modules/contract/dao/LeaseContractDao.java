/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContractCondition;

import java.util.List;

/**
 * 承租合同DAO接口
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@MyBatisDao
public interface LeaseContractDao extends CrudDao<LeaseContract> {

  Integer getTotalValidLeaseContractCounts(LeaseContract leaseContract);

  List<LeaseContract> findLeaseContractListByCondition(LeaseContractCondition condition);
}
