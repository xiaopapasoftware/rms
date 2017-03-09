/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;

/**
 * 账务交易DAO接口
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@MyBatisDao
public interface TradingAccountsDao extends CrudDao<TradingAccounts> {
}