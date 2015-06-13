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
import com.thinkgem.jeesite.modules.funds.entity.Receipt;
import com.thinkgem.jeesite.modules.funds.service.ReceiptService;

/**
 * 账务收据Controller
 * @author huangsc
 * @version 2015-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/funds/receipt")
public class ReceiptController extends BaseController {

	@Autowired
	private ReceiptService receiptService;
	
	@ModelAttribute
	public Receipt get(@RequestParam(required=false) String id) {
		Receipt entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = receiptService.get(id);
		}
		if (entity == null){
			entity = new Receipt();
		}
		return entity;
	}
	
	@RequiresPermissions("funds:receipt:view")
	@RequestMapping(value = {"list", ""})
	public String list(Receipt receipt, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Receipt> page = receiptService.findPage(new Page<Receipt>(request, response), receipt); 
		model.addAttribute("page", page);
		return "modules/funds/receiptList";
	}

	@RequiresPermissions("funds:receipt:view")
	@RequestMapping(value = "form")
	public String form(Receipt receipt, Model model) {
		model.addAttribute("receipt", receipt);
		return "modules/funds/receiptForm";
	}

	@RequiresPermissions("funds:receipt:edit")
	@RequestMapping(value = "save")
	public String save(Receipt receipt, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, receipt)){
			return form(receipt, model);
		}
		receiptService.save(receipt);
		addMessage(redirectAttributes, "保存账务收据成功");
		return "redirect:"+Global.getAdminPath()+"/funds/receipt/?repage";
	}
	
	@RequiresPermissions("funds:receipt:edit")
	@RequestMapping(value = "delete")
	public String delete(Receipt receipt, RedirectAttributes redirectAttributes) {
		receiptService.delete(receipt);
		addMessage(redirectAttributes, "删除账务收据成功");
		return "redirect:"+Global.getAdminPath()+"/funds/receipt/?repage";
	}

}