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
import com.thinkgem.jeesite.modules.device.entity.RoomDevices;
import com.thinkgem.jeesite.modules.device.service.RoomDevicesService;

/**
 * 房屋设备关联信息Controller
 * @author huangsc
 * @version 2015-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/device/roomDevices")
public class RoomDevicesController extends BaseController {

	@Autowired
	private RoomDevicesService roomDevicesService;
	
	@ModelAttribute
	public RoomDevices get(@RequestParam(required=false) String id) {
		RoomDevices entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = roomDevicesService.get(id);
		}
		if (entity == null){
			entity = new RoomDevices();
		}
		return entity;
	}
	
	@RequiresPermissions("device:roomDevices:view")
	@RequestMapping(value = {"list", ""})
	public String list(RoomDevices roomDevices, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RoomDevices> page = roomDevicesService.findPage(new Page<RoomDevices>(request, response), roomDevices); 
		model.addAttribute("page", page);
		return "modules/device/roomDevicesList";
	}

	@RequiresPermissions("device:roomDevices:view")
	@RequestMapping(value = "form")
	public String form(RoomDevices roomDevices, Model model) {
		model.addAttribute("roomDevices", roomDevices);
		return "modules/device/roomDevicesForm";
	}

	@RequiresPermissions("device:roomDevices:edit")
	@RequestMapping(value = "save")
	public String save(RoomDevices roomDevices, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, roomDevices)){
			return form(roomDevices, model);
		}
		roomDevicesService.save(roomDevices);
		addMessage(redirectAttributes, "保存房屋设备关联信息成功");
		return "redirect:"+Global.getAdminPath()+"/device/roomDevices/?repage";
	}
	
	@RequiresPermissions("device:roomDevices:edit")
	@RequestMapping(value = "delete")
	public String delete(RoomDevices roomDevices, RedirectAttributes redirectAttributes) {
		roomDevicesService.delete(roomDevices);
		addMessage(redirectAttributes, "删除房屋设备关联信息成功");
		return "redirect:"+Global.getAdminPath()+"/device/roomDevices/?repage";
	}

}