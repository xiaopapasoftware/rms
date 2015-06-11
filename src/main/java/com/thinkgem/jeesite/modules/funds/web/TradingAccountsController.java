/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;

/**
 * 账务交易Controller
 * @author huangsc
 * @version 2015-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/funds/tradingAccounts")
public class TradingAccountsController extends BaseController {

	@Autowired
	private TradingAccountsService tradingAccountsService;
	
	@ModelAttribute
	public TradingAccounts get(@RequestParam(required=false) String id) {
		TradingAccounts entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tradingAccountsService.get(id);
		}
		if (entity == null){
			entity = new TradingAccounts();
		}
		return entity;
	}
	
	@RequiresPermissions("funds:tradingAccounts:view")
	@RequestMapping(value = {"list", ""})
	public String list(TradingAccounts tradingAccounts, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TradingAccounts> page = tradingAccountsService.findPage(new Page<TradingAccounts>(request, response), tradingAccounts); 
		model.addAttribute("page", page);
		return "modules/funds/tradingAccountsList";
	}

	@RequiresPermissions("funds:tradingAccounts:view")
	@RequestMapping(value = "form")
	public String form(TradingAccounts tradingAccounts, Model model) {
		model.addAttribute("tradingAccounts", tradingAccounts);
		return "modules/funds/tradingAccountsForm";
	}

	@RequiresPermissions("funds:tradingAccounts:edit")
	@RequestMapping(value = "save")
	public String save(TradingAccounts tradingAccounts, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tradingAccounts)){
			return form(tradingAccounts, model);
		}
		tradingAccountsService.save(tradingAccounts);
		addMessage(redirectAttributes, "保存账务交易成功");
		return "redirect:"+Global.getAdminPath()+"/funds/tradingAccounts/?repage";
	}
	
	@RequiresPermissions("funds:tradingAccounts:edit")
	@RequestMapping(value = "delete")
	public String delete(TradingAccounts tradingAccounts, RedirectAttributes redirectAttributes) {
		tradingAccountsService.delete(tradingAccounts);
		addMessage(redirectAttributes, "删除账务交易成功");
		return "redirect:"+Global.getAdminPath()+"/funds/tradingAccounts/?repage";
	}

}