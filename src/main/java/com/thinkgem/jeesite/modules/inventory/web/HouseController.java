/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
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
import com.thinkgem.jeesite.modules.inventory.entity.HouseOwner;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseOwnerService;
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

    @Autowired
    private HouseOwnerService houseOwnerService;

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

    // @RequiresPermissions("inventory:house:view")
    @RequestMapping(value = { "list", "" })
    public String list(House house, HttpServletRequest request, HttpServletResponse response, Model model) {
	Page<House> page = houseService.findPage(new Page<House>(request, response), house);
	model.addAttribute("page", page);

	// 查询房屋下所有的业主信息
	if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
	    for (House h : page.getList()) {
		String ownerNamesOfHouse = "";
		HouseOwner ho = new HouseOwner();
		ho.setHouseId(h.getId());
		List<HouseOwner> hos = houseOwnerService.findList(ho);
		if (CollectionUtils.isNotEmpty(hos)) {
		    for (HouseOwner tempHO : hos) {
			Owner owner = new Owner();
			owner.setId(tempHO.getOwnerId());
			Owner tempO = ownerService.get(owner);
			if (tempO != null && StringUtils.isNotEmpty(tempO.getName())) {
			    if (StringUtils.isEmpty(ownerNamesOfHouse)) {
				ownerNamesOfHouse = tempO.getName();
			    } else {
				ownerNamesOfHouse = ownerNamesOfHouse + "，" + tempO.getName();
			    }
			}
		    }
		}
		h.setOwnerNamesOfHouse(ownerNamesOfHouse);
	    }
	}
	// 组装物业项目搜索条件值
	model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));
	// 组装业主搜索条件值
	model.addAttribute("listOwner", ownerService.findList(new Owner()));
	// 组装楼宇搜索条件值
	if (house.getPropertyProject() != null && StringUtils.isNotEmpty(house.getPropertyProject().getId())) {
	    PropertyProject pp = new PropertyProject();
	    pp.setId(house.getPropertyProject().getId());
	    Building bd = new Building();
	    bd.setPropertyProject(pp);
	    model.addAttribute("listBuilding", buildingService.findList(bd));
	}
	return "modules/inventory/houseList";
    }

    @RequestMapping(value = { "findList" })
    @ResponseBody
    public List<House> findList(Building building) {
	House house = new House();
	house.setBuilding(building);
	house.setChoose(building.getChoose());// 过滤不可用
	List<House> list = houseService.findList(house);
	return list;
    }

    // @RequiresPermissions("inventory:house:view")
    @RequestMapping(value = "form")
    public String form(House house, Model model) {
	if (house.getIsNewRecord()) {
	    Integer currentValidHouseNum = houseService.getCurrentValidHouseNum();
	    currentValidHouseNum = currentValidHouseNum + 1;
	    house.setHouseCode(currentValidHouseNum.toString());
	}
	if (house.getPropertyProject() != null && StringUtils.isNotEmpty(house.getPropertyProject().getId())) {
	    PropertyProject pp = new PropertyProject();
	    pp.setId(house.getPropertyProject().getId());
	    Building bd = new Building();
	    bd.setPropertyProject(pp);
	    model.addAttribute("listBuilding", buildingService.findList(bd));
	}
	model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));
	model.addAttribute("listOwner", ownerService.findList(new Owner()));

	List<Owner> ownerList = null;
	if (!house.getIsNewRecord()) {
	    if (null == house.getOwner()) {
		ownerList = ownerService.findByHouse(house);
	    } else {
		ownerList = new ArrayList<Owner>();
		ownerList.add(ownerService.get(house.getOwner().getId()));
	    }
	    house.setOwnerList(ownerList);
	}
	model.addAttribute("ownerList", ownerService.findList(new Owner()));
	model.addAttribute("house", house);
	return "modules/inventory/houseForm";
    }

    @RequestMapping(value = "add")
    public String add(House house, Model model) {
	Integer currentValidHouseNum = houseService.getCurrentValidHouseNum();
	currentValidHouseNum = currentValidHouseNum + 1;
	house.setHouseCode(currentValidHouseNum.toString());
	if (house.getPropertyProject() != null && StringUtils.isNotEmpty(house.getPropertyProject().getId())) {
	    List<Building> list = new ArrayList<Building>();
	    list.add(buildingService.get(house.getBuilding()));
	    model.addAttribute("listBuilding", list);
	}
	List<PropertyProject> list = new ArrayList<PropertyProject>();
	list.add(propertyProjectService.get(house.getPropertyProject()));
	model.addAttribute("listPropertyProject", list);
	model.addAttribute("listOwner", ownerService.findList(new Owner()));

	List<Owner> ownerList = null;
	if (!house.getIsNewRecord()) {
	    if (null == house.getOwner()) {
		ownerList = ownerService.findByHouse(house);
	    } else {
		ownerList = new ArrayList<Owner>();
		ownerList.add(ownerService.get(house.getOwner().getId()));
	    }
	    house.setOwnerList(ownerList);
	}
	model.addAttribute("ownerList", ownerService.findList(new Owner()));
	model.addAttribute("house", house);
	return "modules/inventory/houseAdd";
    }

    // @RequiresPermissions("inventory:house:edit")
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

    // @RequiresPermissions("inventory:house:edit")
    @RequestMapping(value = "save")
    public String save(House house, Model model, RedirectAttributes redirectAttributes) {
	if (!beanValidator(model, house)) {
	    return form(house, model);
	}
	List<House> houses = houseService.findHourseByProPrjAndBuildingAndHouseNo(house);
	if (!house.getIsNewRecord()) {// 更新
	    if (CollectionUtils.isNotEmpty(houses)) {
		house.setId(houses.get(0).getId());
	    }
	    houseService.save(house);
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
		String houseCode = house.getHouseCode();
		if (houseCode.contains("-")) {
		    house.setHouseCode(houseCode.split("-")[0] + "-" + (houseService.getCurrentValidHouseNum() + 1));
		} else {
		    house.setHouseCode((houseService.getCurrentValidHouseNum() + 1) + "");
		}
		houseService.save(house);
		addMessage(redirectAttributes, "保存房屋信息成功");
		return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
	    }
	}

    }

    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(House house, Model model, RedirectAttributes redirectAttributes) {
	JSONObject jsonObject = new JSONObject();
	List<House> houses = houseService.findHourseByProPrjAndBuildingAndHouseNo(house);
	if (CollectionUtils.isNotEmpty(houses)) {
	    if (house.getPropertyProject() != null && StringUtils.isNotEmpty(house.getPropertyProject().getId())) {
		List<Building> list = new ArrayList<Building>();
		list.add(buildingService.get(house.getBuilding()));
		model.addAttribute("listBuilding", list);
	    }
	    List<PropertyProject> list = new ArrayList<PropertyProject>();
	    list.add(propertyProjectService.get(house.getPropertyProject()));
	    model.addAttribute("listPropertyProject", list);

	    model.addAttribute("listOwner", ownerService.findList(new Owner()));
	    jsonObject.put("message", "该物业项目及该楼宇下的房屋号已被使用，不能重复添加");
	} else {
	    if (StringUtils.isBlank(house.getHouseStatus()))
		house.setHouseStatus(DictUtils.getDictValue("待装修", "house_status", "0"));
	    String houseCode = house.getHouseCode();
	    if (houseCode.contains("-")) {
		house.setHouseCode(houseCode.split("-")[0] + "-" + (houseService.getCurrentValidHouseNum() + 1));
	    } else {
		house.setHouseCode((houseService.getCurrentValidHouseNum() + 1) + "");
	    }
	    houseService.save(house);
	    jsonObject.put("id", house.getId());
	    jsonObject.put("name", house.getHouseNo());
	}

	return jsonObject.toString();
    }

    // @RequiresPermissions("inventory:house:edit")
    @RequestMapping(value = "delete")
    public String delete(House house, RedirectAttributes redirectAttributes) {
	House queryhouse = houseService.get(house);
	String houseStatus = queryhouse.getHouseStatus();
	// 2已预定 3部分出租 4完全出租
	if ("2".equals(houseStatus) || "3".equals(houseStatus) || "4".equals(houseStatus)) {
	    addMessage(redirectAttributes, "房屋已预定或已出租，不能删除！");
	} else {
	    houseService.delete(house);
	    addMessage(redirectAttributes, "删除房屋、房间及其图片信息成功");
	}
	return "redirect:" + Global.getAdminPath() + "/inventory/house/?repage";
    }

}