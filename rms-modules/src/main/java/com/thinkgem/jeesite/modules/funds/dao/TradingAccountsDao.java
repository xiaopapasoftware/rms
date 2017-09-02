/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;

import java.util.List;

/**
 * 账务交易DAO接口
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@MyBatisDao
public interface TradingAccountsDao extends CrudDao<TradingAccounts> {

    /**
     * 根据合同id查询新签合同的进账项
     */
    List<TradingAccounts> queryIncomeTradeAccountsByTradeId(String tradeId);

    /**
     * 根据合同id查询新签合同的出账项
     */
    List<TradingAccounts> queryCostTradeAccountsByTradeId(String tradeId);
}