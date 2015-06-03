/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.web;

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
import com.thinkgem.jeesite.modules.inventory.entity.ManagementCompany;
import com.thinkgem.jeesite.modules.inventory.service.ManagementCompanyService;

/**
 * 物业公司Controller
 * @author huangsc
 * @version 2015-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/managementCompany")
public class ManagementCompanyController extends BaseController {

	@Autowired
	private ManagementCompanyService managementCompanyService;
	
	@ModelAttribute
	public ManagementCompany get(@RequestParam(required=false) String id) {
		ManagementCompany entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = managementCompanyService.get(id);
		}
		if (entity == null){
			entity = new ManagementCompany();
		}
		return entity;
	}
	
	@RequiresPermissions("inventory:managementCompany:view")
	@RequestMapping(value = {"list", ""})
	public String list(ManagementCompany managementCompany, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ManagementCompany> page = managementCompanyService.findPage(new Page<ManagementCompany>(request, response), managementCompany); 
		model.addAttribute("page", page);
		return "modules/inventory/managementCompanyList";
	}

	@RequiresPermissions("inventory:managementCompany:view")
	@RequestMapping(value = "form")
	public String form(ManagementCompany managementCompany, Model model) {
		model.addAttribute("managementCompany", managementCompany);
		return "modules/inventory/managementCompanyForm";
	}

	@RequiresPermissions("inventory:managementCompany:edit")
	@RequestMapping(value = "save")
	public String save(ManagementCompany managementCompany, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, managementCompany)){
			return form(managementCompany, model);
		}
		managementCompanyService.save(managementCompany);
		addMessage(redirectAttributes, "保存物业公司成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/managementCompany/?repage";
	}
	
	@RequiresPermissions("inventory:managementCompany:edit")
	@RequestMapping(value = "delete")
	public String delete(ManagementCompany managementCompany, RedirectAttributes redirectAttributes) {
		managementCompanyService.delete(managementCompany);
		addMessage(redirectAttributes, "删除物业公司成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/managementCompany/?repage";
	}

}