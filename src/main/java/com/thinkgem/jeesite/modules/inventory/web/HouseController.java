/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;

/**
 * 房屋信息Controller
 * @author huangsc
 * @version 2015-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/house")
public class HouseController extends BaseController {

	@Autowired
	private HouseService houseService;
	
	@ModelAttribute
	public House get(@RequestParam(required=false) String id) {
		House entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = houseService.get(id);
		}
		if (entity == null){
			entity = new House();
		}
		return entity;
	}
	
	@RequiresPermissions("inventory:house:view")
	@RequestMapping(value = {"list", ""})
	public String list(House house, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<House> page = houseService.findPage(new Page<House>(request, response), house); 
		model.addAttribute("page", page);
		return "modules/inventory/houseList";
	}
	
	@RequestMapping(value = {"findList", ""})
	@ResponseBody
	public List<House> findList(String id) {
		House house = new House();
		Building building = new Building();
		building.setId(id);
		house.setBuilding(building);
		List<House> list = houseService.findList(house);
		return list;
	}

	@RequiresPermissions("inventory:house:view")
	@RequestMapping(value = "form")
	public String form(House house, Model model) {
		model.addAttribute("house", house);
		return "modules/inventory/houseForm";
	}

	@RequiresPermissions("inventory:house:edit")
	@RequestMapping(value = "save")
	public String save(House house, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, house)){
			return form(house, model);
		}
		houseService.save(house);
		addMessage(redirectAttributes, "保存房屋信息成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/house/?repage";
	}
	
	@RequiresPermissions("inventory:house:edit")
	@RequestMapping(value = "delete")
	public String delete(House house, RedirectAttributes redirectAttributes) {
		houseService.delete(house);
		addMessage(redirectAttributes, "删除房屋信息成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/house/?repage";
	}

}