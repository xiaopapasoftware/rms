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
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.TenantService;

/**
 * 租客信息Controller
 * @author huangsc
 * @version 2015-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/person/tenant")
public class TenantController extends BaseController {

	@Autowired
	private TenantService tenantService;
	
	@ModelAttribute
	public Tenant get(@RequestParam(required=false) String id) {
		Tenant entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = tenantService.get(id);
		}
		if (entity == null){
			entity = new Tenant();
		}
		return entity;
	}
	
	@RequiresPermissions("person:tenant:view")
	@RequestMapping(value = {"list", ""})
	public String list(Tenant tenant, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Tenant> page = tenantService.findPage(new Page<Tenant>(request, response), tenant); 
		model.addAttribute("page", page);
		return "modules/person/tenantList";
	}

	@RequiresPermissions("person:tenant:view")
	@RequestMapping(value = "form")
	public String form(Tenant tenant, Model model) {
		model.addAttribute("tenant", tenant);
		return "modules/person/tenantForm";
	}

	@RequiresPermissions("person:tenant:edit")
	@RequestMapping(value = "save")
	public String save(Tenant tenant, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, tenant)){
			return form(tenant, model);
		}
		tenantService.save(tenant);
		addMessage(redirectAttributes, "保存租客信息成功");
		return "redirect:"+Global.getAdminPath()+"/person/tenant/?repage";
	}
	
	@RequiresPermissions("person:tenant:edit")
	@RequestMapping(value = "delete")
	public String delete(Tenant tenant, RedirectAttributes redirectAttributes) {
		tenantService.delete(tenant);
		addMessage(redirectAttributes, "删除租客信息成功");
		return "redirect:"+Global.getAdminPath()+"/person/tenant/?repage";
	}

}