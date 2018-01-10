/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContractOwner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用报名DAO接口
 * 
 * @author xiao
 */
@MyBatisDao
public interface LeaseContractOwnerDao extends CrudDao<LeaseContractOwner> {

    List<LeaseContractOwner> getListByContractId(@Param("leaseContractId") String leaseContractId);

    void deleteListByContractId(@Param("leaseContractId") String leaseContractId);

    List<String> getContractIdListByOwnerIdList (@Param("ownerIdList") List<String> ownerIdList);

}
