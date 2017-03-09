/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.service.AccountingService;

/**
 * 退租核算Controller
 * 
 * @author huangsc
 * @version 2015-06-11
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/accounting")
public class AccountingController extends BaseController {

    @Autowired
    private AccountingService accountingService;

    @ModelAttribute
    public Accounting get(@RequestParam(required = false) String id) {
	Accounting entity = null;
	if (StringUtils.isNotBlank(id)) {
	    entity = accountingService.get(id);
	}
	if (entity == null) {
	    entity = new Accounting();
	}
	return entity;
    }

    // @RequiresPermissions("contract:accounting:view")
    @RequestMapping(value = { "list", "" })
    public String list(Accounting accounting, HttpServletRequest request, HttpServletResponse response, Model model) {
	Page<Accounting> page = accountingService.findPage(new Page<Accounting>(request, response), accounting);
	model.addAttribute("page", page);
	return "modules/contract/accountingList";
    }

    @RequestMapping(value = { "listByContract" })
    public String listByContract(Accounting accounting, HttpServletRequest request, HttpServletResponse response, Model model) {
	Page<Accounting> page = accountingService.findPage(new Page<Accounting>(request, response), accounting);
	model.addAttribute("page", page);
	return "modules/contract/accountingViewList";
    }

    // @RequiresPermissions("contract:accounting:view")
    @RequestMapping(value = "form")
    public String form(Accounting accounting, Model model) {
	model.addAttribute("accounting", accounting);
	return "modules/contract/accountingForm";
    }

    /**
     * 后台直接修改退租核算记录
     */
    // @RequiresPermissions("contract:accounting:edit")
    @RequestMapping(value = "save")
    public String save(Accounting accounting, Model model, RedirectAttributes redirectAttributes) {
	if (!beanValidator(model, accounting)) {
	    return form(accounting, model);
	}
	accountingService.updateAccountingAndPaymentTrans(accounting);
	addMessage(redirectAttributes, "保存退租核算成功");
	return "redirect:" + Global.getAdminPath() + "/contract/accounting/?repage";
    }

    // @RequiresPermissions("contract:accounting:edit")
    @RequestMapping(value = "delete")
    public String delete(Accounting accounting, RedirectAttributes redirectAttributes) {
	accountingService.delete(accounting);
	addMessage(redirectAttributes, "删除退租核算成功");
	return "redirect:" + Global.getAdminPath() + "/contract/accounting/?repage";
    }

}