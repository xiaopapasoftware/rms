/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.funds.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.ReceiptService;
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
	@Autowired
	private ReceiptService receiptService;

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
		/*收据*/
		List<Receipt> receiptList = new ArrayList<Receipt>();
		
		String[] tradeId = tradingAccounts.getTransIds().split(",");
		double amount = 0;
		String tradeType = "";
		Map<String,Receipt> paymentTypeMap = new HashMap<String,Receipt>();
		
		for (int i = 0; i < tradeId.length; i++) {
			Receipt receipt = new Receipt();
			PaymentTrans paymentTrans = paymentTransService.get(tradeId[i]);
			if ("0".equals(paymentTrans.getTradeDirection())) {// 应出
				amount -= paymentTrans.getLastAmount();
			} else {// 应收
				amount += paymentTrans.getLastAmount();
			}
			tradeType = paymentTrans.getTradeType();
			
			String paymentType = paymentTrans.getPaymentType();
			
			if (!"0".equals(paymentTrans.getTradeDirection())) {
				if(!paymentTypeMap.containsKey(paymentType)) {
					receipt.setReceiptAmount((null==receipt.getReceiptAmount()?0d:receipt.getReceiptAmount())+paymentTrans.getLastAmount());
					receipt.setPaymentType(paymentType);
				} else {
					receipt = paymentTypeMap.get(paymentType);
					receipt.setReceiptAmount((null==receipt.getReceiptAmount()?0d:receipt.getReceiptAmount())+paymentTrans.getLastAmount());
					receipt.setPaymentType(paymentType);
				}
				paymentTypeMap.put(paymentType, receipt);
			}
		}

		for (String key : paymentTypeMap.keySet()) {
			Receipt receipt = paymentTypeMap.get(key);
			receiptList.add(receipt);
		}
		
		tradingAccounts.setTradeDirection(amount > 0 ? "1" : "0");
		tradingAccounts.setTradeAmount(Math.abs(amount));
		tradingAccounts.setTradeDirectionDesc(DictUtils.getDictLabel(tradingAccounts.getTradeDirection(),
				"trans_dirction", ""));
		tradingAccounts.setTradeType(tradeType);
		tradingAccounts.setTradeTypeDesc(DictUtils.getDictLabel(tradingAccounts.getTradeType(), "trans_type", ""));
		model.addAttribute("tradingAccounts", tradingAccounts);
		
		tradingAccounts.setReceiptList(receiptList);
		
		return "modules/funds/tradingAccountsForm";
	}

	@RequestMapping(value = "edit")
	public String edit(TradingAccounts tradingAccounts, Model model) {
		tradingAccounts = tradingAccountsService.get(tradingAccounts.getId());
		List<Receipt> receiptList = new ArrayList<Receipt>();
		receiptList = tradingAccountsService.findReceiptList(tradingAccounts);
		tradingAccounts.setReceiptList(receiptList);
		model.addAttribute("tradingAccounts", tradingAccounts);
		return "modules/funds/tradingAccountsEdit";
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
		String id = tradingAccounts.getId();

		if (!beanValidator(model, tradingAccounts)) {
			return form(tradingAccounts, model);
		}

		/* 校验收据编号重复 */
		boolean check = true;
		String receiptNo = "";
		List<String> receiptNoList = new ArrayList<String>();
		if (null != tradingAccounts.getReceiptList()) {
			for (Receipt receipt : tradingAccounts.getReceiptList()) {
				Receipt tmpReceipt = new Receipt();
				tmpReceipt.setReceiptNo(receipt.getReceiptNo());
				tmpReceipt.setDelFlag("0");
				List<Receipt> list = receiptService.findList(tmpReceipt);
				if ((null != list && list.size() > 0) || receiptNoList.contains(receipt.getReceiptNo())) {
					for (Receipt tReceipt : list) {
						if (receipt.getReceiptNo().equals(tReceipt.getReceiptNo())
								&& !tReceipt.getTradingAccounts().getId().equals(tradingAccounts.getId())) {
							receiptNo = receipt.getReceiptNo();
							check = false;
							break;
						}
					}
				}
				if (!check)
					break;
				receiptNoList.add(receipt.getReceiptNo());
			}
		}

		if (!check) {
			model.addAttribute("message", "收据编号:" + receiptNo + "已存在.");
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
			if (StringUtils.isEmpty(id)) {
				return form(tradingAccounts, model);
			} else {
				return "modules/funds/tradingAccountsForm";
			}
		}

		tradingAccounts.setTradeStatus("0");// 待审核
		tradingAccountsService.save(tradingAccounts);
		addMessage(redirectAttributes, "保存账务交易成功");
		if (StringUtils.isEmpty(id)) {
			return "redirect:" + Global.getAdminPath() + "/funds/paymentTrans/?repage";
		} else {
			return "redirect:" + Global.getAdminPath() + "/funds/tradingAccounts/?repage";
		}
	}

	@RequiresPermissions("funds:tradingAccounts:edit")
	@RequestMapping(value = "delete")
	public String delete(TradingAccounts tradingAccounts, RedirectAttributes redirectAttributes) {
		tradingAccountsService.delete(tradingAccounts);
		addMessage(redirectAttributes, "删除账务交易成功");
		return "redirect:" + Global.getAdminPath() + "/funds/tradingAccounts/?repage";
	}

}