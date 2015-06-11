/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;

/**
 * 账务交易Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class TradingAccountsService extends CrudService<TradingAccountsDao, TradingAccounts> {

	public TradingAccounts get(String id) {
		return super.get(id);
	}
	
	public List<TradingAccounts> findList(TradingAccounts tradingAccounts) {
		return super.findList(tradingAccounts);
	}
	
	public Page<TradingAccounts> findPage(Page<TradingAccounts> page, TradingAccounts tradingAccounts) {
		return super.findPage(page, tradingAccounts);
	}
	
	@Transactional(readOnly = false)
	public void save(TradingAccounts tradingAccounts) {
		super.save(tradingAccounts);
	}
	
	@Transactional(readOnly = false)
	public void delete(TradingAccounts tradingAccounts) {
		super.delete(tradingAccounts);
	}
	
}