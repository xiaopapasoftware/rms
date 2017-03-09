/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.web;

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
import com.thinkgem.jeesite.modules.inventory.entity.ManagementCompany;
import com.thinkgem.jeesite.modules.inventory.entity.Neighborhood;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.ManagementCompanyService;
import com.thinkgem.jeesite.modules.inventory.service.NeighborhoodService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;

/**
 * 物业项目Controller
 * 
 * @author huangsc
 * @version 2015-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/propertyProject")
public class PropertyProjectController extends BaseController {

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Autowired
    private NeighborhoodService neighborhoodService;

    @Autowired
    private ManagementCompanyService managementCompanyService;

    @ModelAttribute
    public PropertyProject get(@RequestParam(required = false) String id) {
	PropertyProject entity = null;
	if (StringUtils.isNotBlank(id)) {
	    entity = propertyProjectService.get(id);
	}
	if (entity == null) {
	    entity = new PropertyProject();
	}
	return entity;
    }

    // @RequiresPermissions("inventory:propertyProject:view")
    @RequestMapping(value = { "list", "" })
    public String list(PropertyProject propertyProject, HttpServletRequest request, HttpServletResponse response, Model model) {
	List<Neighborhood> listNeighborhood = neighborhoodService.findList(new Neighborhood());
	model.addAttribute("listNeighborhood", listNeighborhood);

	List<ManagementCompany> listManagementCompany = managementCompanyService.findList(new ManagementCompany());
	model.addAttribute("listManagementCompany", listManagementCompany);

	Page<PropertyProject> page = propertyProjectService.findPage(new Page<PropertyProject>(request, response), propertyProject);

	model.addAttribute("page", page);
	return "modules/inventory/propertyProjectList";
    }

    // @RequiresPermissions("inventory:propertyProject:view")
    @RequestMapping(value = "form")
    public String form(PropertyProject propertyProject, Model model) {

	model.addAttribute("propertyProject", propertyProject);
	model.addAttribute("listNeighborhood", neighborhoodService.findList(new Neighborhood()));
	model.addAttribute("listManagementCompany", managementCompanyService.findList(new ManagementCompany()));

	return "modules/inventory/propertyProjectForm";
    }

    @RequestMapping(value = "add")
    public String add(PropertyProject propertyProject, Model model) {

	model.addAttribute("propertyProject", propertyProject);
	model.addAttribute("listNeighborhood", neighborhoodService.findList(new Neighborhood()));
	model.addAttribute("listManagementCompany", managementCompanyService.findList(new ManagementCompany()));

	return "modules/inventory/propertyProjectAdd";
    }

    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(PropertyProject propertyProject, Model model, RedirectAttributes redirectAttributes) {
	JSONObject jsonObject = new JSONObject();
	List<PropertyProject> pps = propertyProjectService.findPropertyProjectByNameAndAddress(propertyProject);
	if (CollectionUtils.isNotEmpty(pps)) {
	    jsonObject.put("message", "物业项目名称及地址已被使用，不能重复添加");
	} else {
	    propertyProjectService.save(propertyProject);
	    jsonObject.put("id", propertyProject.getId());
	    jsonObject.put("name", propertyProject.getProjectName());
	}

	return jsonObject.toString();
    }

    // @RequiresPermissions("inventory:propertyProject:edit")
    @RequestMapping(value = "save")
    public String save(PropertyProject propertyProject, Model model, RedirectAttributes redirectAttributes) {
	if (!beanValidator(model, propertyProject)) {
	    return form(propertyProject, model);
	}
	List<PropertyProject> pps = propertyProjectService.findPropertyProjectByNameAndAddress(propertyProject);
	if (!propertyProject.getIsNewRecord()) {// 更新
	    if (CollectionUtils.isNotEmpty(pps)) {
		propertyProject.setId(pps.get(0).getId());
	    }
	    propertyProjectService.save(propertyProject);
	    addMessage(redirectAttributes, "修改物业项目成功");
	    return "redirect:" + Global.getAdminPath() + "/inventory/propertyProject/?repage";
	} else {// 新增
	    if (CollectionUtils.isNotEmpty(pps)) {
		model.addAttribute("message", "物业项目名称及地址已被使用，不能重复添加");
		model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());
		model.addAttribute("neighborhood", propertyProject.getNeighborhood());
		model.addAttribute("listNeighborhood", neighborhoodService.findList(new Neighborhood()));
		model.addAttribute("managementCompany", propertyProject.getManagementCompany());
		model.addAttribute("listManagementCompany", managementCompanyService.findList(new ManagementCompany()));
		return "modules/inventory/propertyProjectForm";
	    } else {
		propertyProjectService.save(propertyProject);
		addMessage(redirectAttributes, "保存物业项目成功");
		return "redirect:" + Global.getAdminPath() + "/inventory/propertyProject/?repage";
	    }
	}
    }

    // @RequiresPermissions("inventory:propertyProject:edit")
    @RequestMapping(value = "delete")
    public String delete(PropertyProject propertyProject, RedirectAttributes redirectAttributes) {
	propertyProjectService.delete(propertyProject);
	addMessage(redirectAttributes, "删除物业项目和图片及其楼宇和图片、房屋和图片、房间和图片成功");
	return "redirect:" + Global.getAdminPath() + "/inventory/propertyProject/?repage";
    }

}