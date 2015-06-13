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
import com.thinkgem.jeesite.modules.person.entity.CompanyLinkman;
import com.thinkgem.jeesite.modules.person.service.CompanyLinkmanService;

/**
 * 企业联系人Controller
 * @author huangsc
 * @version 2015-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/person/companyLinkman")
public class CompanyLinkmanController extends BaseController {

	@Autowired
	private CompanyLinkmanService companyLinkmanService;
	
	@ModelAttribute
	public CompanyLinkman get(@RequestParam(required=false) String id) {
		CompanyLinkman entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = companyLinkmanService.get(id);
		}
		if (entity == null){
			entity = new CompanyLinkman();
		}
		return entity;
	}
	
	@RequiresPermissions("person:companyLinkman:view")
	@RequestMapping(value = {"list", ""})
	public String list(CompanyLinkman companyLinkman, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CompanyLinkman> page = companyLinkmanService.findPage(new Page<CompanyLinkman>(request, response), companyLinkman); 
		model.addAttribute("page", page);
		return "modules/person/companyLinkmanList";
	}

	@RequiresPermissions("person:companyLinkman:view")
	@RequestMapping(value = "form")
	public String form(CompanyLinkman companyLinkman, Model model) {
		model.addAttribute("companyLinkman", companyLinkman);
		return "modules/person/companyLinkmanForm";
	}

	@RequiresPermissions("person:companyLinkman:edit")
	@RequestMapping(value = "save")
	public String save(CompanyLinkman companyLinkman, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, companyLinkman)){
			return form(companyLinkman, model);
		}
		companyLinkmanService.save(companyLinkman);
		addMessage(redirectAttributes, "保存企业联系人成功");
		return "redirect:"+Global.getAdminPath()+"/person/companyLinkman/?repage";
	}
	
	@RequiresPermissions("person:companyLinkman:edit")
	@RequestMapping(value = "delete")
	public String delete(CompanyLinkman companyLinkman, RedirectAttributes redirectAttributes) {
		companyLinkmanService.delete(companyLinkman);
		addMessage(redirectAttributes, "删除企业联系人成功");
		return "redirect:"+Global.getAdminPath()+"/person/companyLinkman/?repage";
	}

}