/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;

/**
 * 定金协议Controller
 * @author huangsc
 * @version 2015-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/depositAgreement")
public class DepositAgreementController extends BaseController {

	@Autowired
	private DepositAgreementService depositAgreementService;
	@Autowired
	private PropertyProjectService propertyProjectService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private RoomService roomService;
	
	@ModelAttribute
	public DepositAgreement get(@RequestParam(required=false) String id) {
		DepositAgreement entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = depositAgreementService.get(id);
		}
		if (entity == null){
			entity = new DepositAgreement();
		}
		return entity;
	}
	
	@RequiresPermissions("contract:depositAgreement:view")
	@RequestMapping(value = {"list", ""})
	public String list(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DepositAgreement> page = depositAgreementService.findPage(new Page<DepositAgreement>(request, response), depositAgreement); 
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);
		
		List<Building> buildingList = buildingService.findList(new Building());
		model.addAttribute("buildingList", buildingList);
		
		List<House> houseList = houseService.findList(new House());
		model.addAttribute("houseList", houseList);
		
		List<Room> roomList = roomService.findList(new Room());
		model.addAttribute("roomList", roomList);
		
		return "modules/contract/depositAgreementList";
	}

	@RequiresPermissions("contract:depositAgreement:view")
	@RequestMapping(value = "form")
	public String form(DepositAgreement depositAgreement, Model model) {
		model.addAttribute("depositAgreement", depositAgreement);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);
		
		List<Building> buildingList = buildingService.findList(new Building());
		model.addAttribute("buildingList", buildingList);
		
		List<House> houseList = houseService.findList(new House());
		model.addAttribute("houseList", houseList);
		
		List<Room> roomList = roomService.findList(new Room());
		model.addAttribute("roomList", roomList);
		
		return "modules/contract/depositAgreementForm";
	}

	@RequiresPermissions("contract:depositAgreement:edit")
	@RequestMapping(value = "save")
	public String save(DepositAgreement depositAgreement, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, depositAgreement)){
			return form(depositAgreement, model);
		}
		depositAgreementService.save(depositAgreement);
		addMessage(redirectAttributes, "保存定金协议成功");
		return "redirect:"+Global.getAdminPath()+"/contract/depositAgreement/?repage";
	}
	
	@RequiresPermissions("contract:depositAgreement:edit")
	@RequestMapping(value = "delete")
	public String delete(DepositAgreement depositAgreement, RedirectAttributes redirectAttributes) {
		depositAgreementService.delete(depositAgreement);
		addMessage(redirectAttributes, "删除定金协议成功");
		return "redirect:"+Global.getAdminPath()+"/contract/depositAgreement/?repage";
	}

}