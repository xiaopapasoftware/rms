/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContractDtl;

import java.util.List;

/**
 * 承租合同明细DAO接口
 * @author huangsc
 * @version 2015-06-14
 */
@MyBatisDao
public interface LeaseContractDtlDao extends CrudDao<LeaseContractDtl> {
    List<LeaseContractDtl> getLeaseContractDtlListByContractId(String contractId);

    void deleteContractDtlListByContractId(String contractId);
}