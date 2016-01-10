/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;

/**
 * 预约看房信息DAO接口
 * @author huangsc
 */
@MyBatisDao
public interface ContractBookDao extends CrudDao<ContractBook> {
	public List<ContractBook> findBookedContract(ContractBook contractBook);
	public List<ContractBook> findRentContract(ContractBook contractBook);
	public void updateStatusByHouseId(ContractBook contractBook);
	public List<ContractBook> findByUser(ContractBook contractBook);
}