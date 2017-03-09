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
import com.thinkgem.jeesite.modules.inventory.entity.Neighborhood;
import com.thinkgem.jeesite.modules.inventory.service.NeighborhoodService;

/**
 * 居委会Controller
 * 
 * @author huangsc
 * @version 2015-06-03
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/neighborhood")
public class NeighborhoodController extends BaseController {

    @Autowired
    private NeighborhoodService neighborhoodService;

    @ModelAttribute
    public Neighborhood get(@RequestParam(required = false) String id) {
	Neighborhood entity = null;
	if (StringUtils.isNotBlank(id)) {
	    entity = neighborhoodService.get(id);
	}
	if (entity == null) {
	    entity = new Neighborhood();
	}
	return entity;
    }

    // @RequiresPermissions("inventory:neighborhood:view")
    @RequestMapping(value = { "list", "" })
    public String list(Neighborhood neighborhood, HttpServletRequest request, HttpServletResponse response, Model model) {
	Page<Neighborhood> page = neighborhoodService.findPage(new Page<Neighborhood>(request, response), neighborhood);
	model.addAttribute("page", page);
	return "modules/inventory/neighborhoodList";
    }

    // @RequiresPermissions("inventory:neighborhood:view")
    @RequestMapping(value = "form")
    public String form(Neighborhood neighborhood, Model model) {
	model.addAttribute("neighborhood", neighborhood);
	return "modules/inventory/neighborhoodForm";
    }

    @RequestMapping(value = "add")
    public String add(Neighborhood neighborhood, Model model) {
	model.addAttribute("neighborhood", neighborhood);
	return "modules/inventory/neighborhoodAdd";
    }

    // @RequiresPermissions("inventory:neighborhood:edit")
    @RequestMapping(value = "save")
    public String save(Neighborhood neighborhood, Model model, RedirectAttributes redirectAttributes) {
	if (!beanValidator(model, neighborhood)) {
	    return form(neighborhood, model);
	}
	List<Neighborhood> neighborhoods = neighborhoodService.findNeighborhoodByNameAndAddress(neighborhood);
	if (!neighborhood.getIsNewRecord()) {// 是更新
	    if (CollectionUtils.isNotEmpty(neighborhoods)) {// 只更新备注
		neighborhood.setId(neighborhoods.get(0).getId());
	    }
	    neighborhoodService.save(neighborhood);
	    addMessage(redirectAttributes, "修改居委会成功");
	    return "redirect:" + Global.getAdminPath() + "/inventory/neighborhood/?repage";
	} else {// 新增
	    if (CollectionUtils.isNotEmpty(neighborhoods)) {
		model.addAttribute("message", "居委会名称及地址已被使用，不能重复添加");
		model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());
		return "modules/inventory/neighborhoodForm";
	    } else {
		neighborhoodService.save(neighborhood);
		addMessage(redirectAttributes, "新增居委会成功");
		return "redirect:" + Global.getAdminPath() + "/inventory/neighborhood/?repage";
	    }
	}
    }

    @RequestMapping(value = "ajaxSave")
    @ResponseBody
    public String ajaxSave(Neighborhood neighborhood, Model model, RedirectAttributes redirectAttributes) {
	JSONObject jsonObject = new JSONObject();
	List<Neighborhood> neighborhoods = neighborhoodService.findNeighborhoodByNameAndAddress(neighborhood);
	if (CollectionUtils.isNotEmpty(neighborhoods)) {
	    jsonObject.put("message", "居委会名称及地址已被使用，不能重复添加");
	} else {
	    neighborhoodService.save(neighborhood);
	    jsonObject.put("id", neighborhood.getId());
	    jsonObject.put("name", neighborhood.getNeighborhoodName());
	}

	return jsonObject.toString();
    }

    // @RequiresPermissions("inventory:neighborhood:edit")
    @RequestMapping(value = "delete")
    public String delete(Neighborhood neighborhood, RedirectAttributes redirectAttributes) {
	neighborhoodService.delete(neighborhood);
	addMessage(redirectAttributes, "删除居委会及所有的居委会联系人成功");
	return "redirect:" + Global.getAdminPath() + "/inventory/neighborhood/?repage";
    }

}