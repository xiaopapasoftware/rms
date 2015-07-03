/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.funds.dao.InvoiceDao;
import com.thinkgem.jeesite.modules.funds.dao.TradingAccountsDao;
import com.thinkgem.jeesite.modules.funds.entity.Invoice;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;

/**
 * 发票信息Service
 * @author huangsc
 * @version 2015-06-11
 */
@Service
@Transactional(readOnly = true)
public class InvoiceService extends CrudService<InvoiceDao, Invoice> {
	@Autowired
	private TradingAccountsDao tradingAccountsDao;

	public Invoice get(String id) {
		return super.get(id);
	}
	
	public List<Invoice> findList(Invoice invoice) {
		return super.findList(invoice);
	}
	
	public Page<Invoice> findPage(Page<Invoice> page, Invoice invoice) {
		return super.findPage(page, invoice);
	}
	
	@Transactional(readOnly = false)
	public void save(Invoice invoice) {
		TradingAccounts tradingAccounts = tradingAccountsDao.get(invoice.getTradingAccountsId());
		invoice.setTradingAccounts(tradingAccounts);
		super.save(invoice);
		
		if(null != tradingAccounts) {
			tradingAccounts.setTradeStatus("3");//发票已开
			tradingAccountsDao.update(tradingAccounts);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Invoice invoice) {
		super.delete(invoice);
	}
	
}