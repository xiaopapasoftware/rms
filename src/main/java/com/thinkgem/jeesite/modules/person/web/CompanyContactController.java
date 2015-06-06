/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.web;

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
import com.thinkgem.jeesite.modules.person.entity.CompanyContact;
import com.thinkgem.jeesite.modules.person.service.CompanyContactService;

/**
 * 物业公司联系人Controller
 * @author huangsc
 * @version 2015-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/person/companyContact")
public class CompanyContactController extends BaseController {

	@Autowired
	private CompanyContactService companyContactService;
	
	@ModelAttribute
	public CompanyContact get(@RequestParam(required=false) String id) {
		CompanyContact entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = companyContactService.get(id);
		}
		if (entity == null){
			entity = new CompanyContact();
		}
		return entity;
	}
	
	@RequiresPermissions("person:companyContact:view")
	@RequestMapping(value = {"list", ""})
	public String list(CompanyContact companyContact, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CompanyContact> page = companyContactService.findPage(new Page<CompanyContact>(request, response), companyContact); 
		model.addAttribute("page", page);
		return "modules/person/companyContactList";
	}

	@RequiresPermissions("person:companyContact:view")
	@RequestMapping(value = "form")
	public String form(CompanyContact companyContact, Model model) {
		model.addAttribute("companyContact", companyContact);
		return "modules/person/companyContactForm";
	}

	@RequiresPermissions("person:companyContact:edit")
	@RequestMapping(value = "save")
	public String save(CompanyContact companyContact, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, companyContact)){
			return form(companyContact, model);
		}
		companyContactService.save(companyContact);
		addMessage(redirectAttributes, "保存物业公司联系人成功");
		return "redirect:"+Global.getAdminPath()+"/person/companyContact/?repage";
	}
	
	@RequiresPermissions("person:companyContact:edit")
	@RequestMapping(value = "delete")
	public String delete(CompanyContact companyContact, RedirectAttributes redirectAttributes) {
		companyContactService.delete(companyContact);
		addMessage(redirectAttributes, "删除物业公司联系人成功");
		return "redirect:"+Global.getAdminPath()+"/person/companyContact/?repage";
	}

}