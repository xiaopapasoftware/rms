/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.web;

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
import com.thinkgem.jeesite.modules.device.entity.Devices;
import com.thinkgem.jeesite.modules.device.service.DevicesService;

/**
 * 设备信息Controller
 * @author huangsc
 * @version 2015-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/devices")
public class DevicesController extends BaseController {

	@Autowired
	private DevicesService devicesService;
	
	@ModelAttribute
	public Devices get(@RequestParam(required=false) String id) {
		Devices entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = devicesService.get(id);
		}
		if (entity == null){
			entity = new Devices();
		}
		return entity;
	}
	
	@RequiresPermissions("inventory:devices:view")
	@RequestMapping(value = {"list", ""})
	public String list(Devices devices, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Devices> page = devicesService.findPage(new Page<Devices>(request, response), devices); 
		model.addAttribute("page", page);
		return "modules/inventory/devicesList";
	}

	@RequiresPermissions("inventory:devices:view")
	@RequestMapping(value = "form")
	public String form(Devices devices, Model model) {
		model.addAttribute("devices", devices);
		return "modules/inventory/devicesForm";
	}

	@RequiresPermissions("inventory:devices:edit")
	@RequestMapping(value = "save")
	public String save(Devices devices, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, devices)){
			return form(devices, model);
		}
		devicesService.save(devices);
		addMessage(redirectAttributes, "保存设备信息成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/devices/?repage";
	}
	
	@RequiresPermissions("inventory:devices:edit")
	@RequestMapping(value = "delete")
	public String delete(Devices devices, RedirectAttributes redirectAttributes) {
		devicesService.delete(devices);
		addMessage(redirectAttributes, "删除设备信息成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/devices/?repage";
	}

}