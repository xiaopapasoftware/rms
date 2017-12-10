/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.ContractTenant;
import org.apache.ibatis.annotations.Param;

/**
 * 合同租客关联信息DAO接口
 * @author huangsc
 * @version 2015-06-21
 */
@MyBatisDao
public interface ContractTenantDao extends CrudDao<ContractTenant> {

    ContractTenant getByContractId(@Param("contractId")String contractId);

}