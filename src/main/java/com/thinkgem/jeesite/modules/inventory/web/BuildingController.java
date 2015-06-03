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
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;

/**
 * 楼宇Controller
 * @author huangsc
 * @version 2015-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/building")
public class BuildingController extends BaseController {

	@Autowired
	private BuildingService buildingService;
	
	@ModelAttribute
	public Building get(@RequestParam(required=false) String id) {
		Building entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = buildingService.get(id);
		}
		if (entity == null){
			entity = new Building();
		}
		return entity;
	}
	
	@RequiresPermissions("inventory:building:view")
	@RequestMapping(value = {"list", ""})
	public String list(Building building, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Building> page = buildingService.findPage(new Page<Building>(request, response), building); 
		model.addAttribute("page", page);
		return "modules/inventory/buildingList";
	}

	@RequiresPermissions("inventory:building:view")
	@RequestMapping(value = "form")
	public String form(Building building, Model model) {
		model.addAttribute("building", building);
		return "modules/inventory/buildingForm";
	}

	@RequiresPermissions("inventory:building:edit")
	@RequestMapping(value = "save")
	public String save(Building building, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, building)){
			return form(building, model);
		}
		buildingService.save(building);
		addMessage(redirectAttributes, "保存楼宇成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/building/?repage";
	}
	
	@RequiresPermissions("inventory:building:edit")
	@RequestMapping(value = "delete")
	public String delete(Building building, RedirectAttributes redirectAttributes) {
		buildingService.delete(building);
		addMessage(redirectAttributes, "删除楼宇成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/building/?repage";
	}

}