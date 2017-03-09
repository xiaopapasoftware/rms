/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;

/**
 * 退租核算DAO接口
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@MyBatisDao
public interface AccountingDao extends CrudDao<Accounting> {
    public int delByRent(Accounting accounting);
}