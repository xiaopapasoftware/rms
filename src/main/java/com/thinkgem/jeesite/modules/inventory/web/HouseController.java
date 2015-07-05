/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import com.thinkgem.jeesite.modules.person.service.OwnerService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 房屋信息Controller
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/house")
public class HouseController extends BaseController {

	@Autowired
	private PropertyProjectService propertyProjectService;

	@Autowired
	private BuildingService buildingService;

	@Autowired
	private HouseService houseService;

	@Autowired
	private OwnerService ownerService;

	@ModelAttribute
	public House get(@RequestParam(required = false) String id) {
		House entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = houseService.get(id);
		}
		if (entity == null) {
			entity = new House();
		}
		return entity;
	}

	@RequiresPermissions("inventory:house:view")
	@RequestMapping(value = {"list", ""})
	public String list(House house, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<House> page = houseService.findPage(new Page<House>(request, response), house);
		model.addAttribute("page", page);

		model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));
		model.addAttribute("listOwner", ownerService.findList(new Owner()));

		if (house.getPropertyProject() != null && StringUtils.isNotEmpty(house.getPropertyProject().getId())) {
			PropertyProject pp = new PropertyProject();
			pp.setId(house.getPropertyProject().getId());
			Building bd = new Building();
			bd.setPropertyProject(pp);
			model.addAttribute("listBuilding", buildingService.findList(bd));
		}
		return "modules/inventory/houseList";
	}

	@RequestMapping(value = {"findList"})
	@ResponseBody
	public List<House> findList(String id) {
		House house = new House();
		Building building = new Building();
		building.setId(id);
		house.setBuilding(building);
		house.setChoose("1");//过滤不可用
		List<House> list = houseService.findList(house);
		return list;
	}

	@RequiresPermissions("inventory:house:view")
	@RequestMapping(value = "form")
	public String form(House house, Model model) {

		model.addAttribute("house", house);
		if (house.getPropertyProject() != null && StringUtils.isNotEmpty(house.getPropertyProject().getId())) {
			PropertyProject pp = new PropertyProject();
			pp.setId(house.getPropertyProject().getId());
			Building bd = new Building();
			bd.setPropertyProject(pp);
			model.addAttribute("listBuilding", buildingService.findList(bd));
		}
		model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));
		model.addAttribute("listOwner", ownerService.findList(new Owner()));
		return "modules/inventory/houseForm";
	}

	@RequiresPermissions("inventory:house:edit")
	@RequestMapping(value = "finishDirect")
	@ResponseBody
	public String finishDirect(House house, Model model, RedirectAttributes redirectAttributes) {
		int i = houseService.updateHouseStatus(house);
		if (i > 0) {
			return "SUCCESS";
		} else {
			return "FAIL";
		}
	}

	@RequiresPermissions("inventory:house:edit")
	@RequestMapping(value = "save")
	public String save(House house, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, house)) {
			return form(house, model);
		}
		List<House> houses = houseService.findHourseByProPrjAndBuildingAndHouseNo(house);
		if (!house.getIsNewRecord()) {// 更新
			if (CollectionUtils.isNotEmpty(houses)) {
				House upHouse = new House();
				upHouse.setId(houses.get(0).getId());
				upHouse.setHouseStatus(houses.get(0).getHouseStatus());
				upHouse.setPropertyProject(house.getPropertyProject());
				upHouse.setBuilding(house.getBuilding());
				upHouse.setOwner(house.getOwner());
				upHouse.setHouseNo(house.getHouseNo());
				upHouse.setAttachmentPath(house.getAttachmentPath());
				upHouse.setDecorationSpance(house.getDecorationSpance());
				upHouse.setDecorationStructure(house.getDecorationStructure());
				upHouse.setHouseFloor(house.getHouseFloor());
				upHouse.setHouseSpace(house.getHouseSpace());
				upHouse.setHouseStructure(house.getHouseStructure());
				upHouse.setRemarks(house.getRemarks());
				houseService.save(upHouse);
			} else {
				houseService.save(house);
			}
			addMessage(redirectAttributes, "修改房屋信息成功");
			return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
		} else {// 新增
			if (CollectionUtils.isNotEmpty(houses)) {
				model.addAttribute("message", "该物业项目及该楼宇下的房屋号已被使用，不能重复添加");
				model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());

				model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));

				PropertyProject pp = new PropertyProject();
				pp.setId(house.getPropertyProject().getId());
				Building bd = new Building();
				bd.setPropertyProject(pp);
				model.addAttribute("listBuilding", buildingService.findList(bd));

				model.addAttribute("listOwner", ownerService.findList(new Owner()));
				return "modules/inventory/houseForm";
			} else {
				house.setHouseStatus(DictUtils.getDictValue("待装修", "house_status", "0"));
				houseService.save(house);
				addMessage(redirectAttributes, "保存房屋信息成功");
				return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
			}
		}

	}

	@RequiresPermissions("inventory:house:edit")
	@RequestMapping(value = "delete")
	public String delete(House house, RedirectAttributes redirectAttributes) {
		houseService.delete(house);
		addMessage(redirectAttributes, "删除房屋、房间及其图片信息成功");
		return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
	}

}