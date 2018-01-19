/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.app.entity.CustBindInfo;
import org.apache.ibatis.annotations.Param;

/**
 * DAO接口
 * @author mabindong
 * @version 2015-11-24
 */
@MyBatisDao
public interface CustBindInfoDao extends CrudDao<CustBindInfo> {

    CustBindInfo getByCustomerIdAndType(@Param("customerId") String customerId, @Param("accountType") String accountType);

}