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
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;

/**
 * 物业项目Controller
 * @author huangsc
 * @version 2015-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/propertyProject")
public class PropertyProjectController extends BaseController {

	@Autowired
	private PropertyProjectService propertyProjectService;
	
	@ModelAttribute
	public PropertyProject get(@RequestParam(required=false) String id) {
		PropertyProject entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = propertyProjectService.get(id);
		}
		if (entity == null){
			entity = new PropertyProject();
		}
		return entity;
	}
	
	@RequiresPermissions("inventory:propertyProject:view")
	@RequestMapping(value = {"list", ""})
	public String list(PropertyProject propertyProject, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PropertyProject> page = propertyProjectService.findPage(new Page<PropertyProject>(request, response), propertyProject); 
		model.addAttribute("page", page);
		return "modules/inventory/propertyProjectList";
	}

	@RequiresPermissions("inventory:propertyProject:view")
	@RequestMapping(value = "form")
	public String form(PropertyProject propertyProject, Model model) {
		model.addAttribute("propertyProject", propertyProject);
		return "modules/inventory/propertyProjectForm";
	}

	@RequiresPermissions("inventory:propertyProject:edit")
	@RequestMapping(value = "save")
	public String save(PropertyProject propertyProject, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, propertyProject)){
			return form(propertyProject, model);
		}
		propertyProjectService.save(propertyProject);
		addMessage(redirectAttributes, "保存物业项目成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/propertyProject/?repage";
	}
	
	@RequiresPermissions("inventory:propertyProject:edit")
	@RequestMapping(value = "delete")
	public String delete(PropertyProject propertyProject, RedirectAttributes redirectAttributes) {
		propertyProjectService.delete(propertyProject);
		addMessage(redirectAttributes, "删除物业项目成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/propertyProject/?repage";
	}

}