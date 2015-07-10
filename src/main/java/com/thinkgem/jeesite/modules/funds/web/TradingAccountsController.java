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
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 账务交易Controller
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/funds/tradingAccounts")
public class TradingAccountsController extends BaseController {

	@Autowired
	private TradingAccountsService tradingAccountsService;
	@Autowired
	private PaymentTransService paymentTransService;

	@ModelAttribute
	public TradingAccounts get(@RequestParam(required = false) String id) {
		TradingAccounts entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = tradingAccountsService.get(id);
		}
		if (entity == null) {
			entity = new TradingAccounts();
		}
		return entity;
	}

	@RequiresPermissions("funds:tradingAccounts:view")
	@RequestMapping(value = {"list", ""})
	public String list(TradingAccounts tradingAccounts, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<TradingAccounts> page = tradingAccountsService.findPage(new Page<TradingAccounts>(request, response),
				tradingAccounts);
		model.addAttribute("page", page);
		return "modules/funds/tradingAccountsList";
	}

	@RequiresPermissions("funds:tradingAccounts:view")
	@RequestMapping(value = "form")
	public String form(TradingAccounts tradingAccounts, Model model) {
		String[] tradeId = tradingAccounts.getTransIds().split(",");
		double amount = 0;
		String tradeType = "";
		for (int i = 0; i < tradeId.length; i++) {
			PaymentTrans paymentTrans = paymentTransService.get(tradeId[i]);
			if ("0".equals(paymentTrans.getTradeDirection()))// 应出
				amount -= paymentTrans.getTradeAmount();
			else
				amount += paymentTrans.getTradeAmount();
			tradeType = paymentTrans.getTradeType();
		}
		tradingAccounts.setTradeAmount(amount);
		tradingAccounts.setTradeDirection(amount > 0 ? "1" : "0");
		tradingAccounts.setTradeDirectionDesc(DictUtils.getDictLabel(tradingAccounts.getTradeDirection(),
				"trans_dirction", ""));
		tradingAccounts.setTradeType(tradeType);
		tradingAccounts.setTradeTypeDesc(DictUtils.getDictLabel(tradingAccounts.getTradeType(), "trans_type", ""));
		model.addAttribute("tradingAccounts", tradingAccounts);
		return "modules/funds/tradingAccountsForm";
	}

	@RequestMapping(value = "findOne")
	public String findOne(TradingAccounts tradingAccounts, Model model) {
		tradingAccounts = tradingAccountsService.get(tradingAccounts);
		model.addAttribute("tradingAccounts", tradingAccounts);
		return "modules/funds/tradingAccountsEdit";
	}

	@RequestMapping(value = "audit")
	public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
		tradingAccountsService.audit(auditHis);
		return list(new TradingAccounts(), request, response, model);
	}

	@RequiresPermissions("funds:tradingAccounts:edit")
	@RequestMapping(value = "save")
	public String save(TradingAccounts tradingAccounts, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tradingAccounts)) {
			return form(tradingAccounts, model);
		}
		tradingAccounts.setTradeStatus("0");// 待审核
		tradingAccountsService.save(tradingAccounts);
		addMessage(redirectAttributes, "保存账务交易成功");
		return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
	}

	@RequiresPermissions("funds:tradingAccounts:edit")
	@RequestMapping(value = "delete")
	public String delete(TradingAccounts tradingAccounts, RedirectAttributes redirectAttributes) {
		tradingAccountsService.delete(tradingAccounts);
		addMessage(redirectAttributes, "删除账务交易成功");
		return "redirect:" + Global.getAdminPath() + "/funds/tradingAccounts/?repage";
	}

}