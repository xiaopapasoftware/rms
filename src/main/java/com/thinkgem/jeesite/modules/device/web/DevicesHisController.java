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
import com.thinkgem.jeesite.modules.device.entity.DevicesHis;
import com.thinkgem.jeesite.modules.device.service.DevicesHisService;

/**
 * 设备变更信息Controller
 * @author huangsc
 * @version 2015-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/device/devicesHis")
public class DevicesHisController extends BaseController {

	@Autowired
	private DevicesHisService devicesHisService;
	
	@ModelAttribute
	public DevicesHis get(@RequestParam(required=false) String id) {
		DevicesHis entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = devicesHisService.get(id);
		}
		if (entity == null){
			entity = new DevicesHis();
		}
		return entity;
	}
	
	@RequiresPermissions("device:devicesHis:view")
	@RequestMapping(value = {"list", ""})
	public String list(DevicesHis devicesHis, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DevicesHis> page = devicesHisService.findPage(new Page<DevicesHis>(request, response), devicesHis); 
		model.addAttribute("page", page);
		return "modules/device/devicesHisList";
	}

	@RequiresPermissions("device:devicesHis:view")
	@RequestMapping(value = "form")
	public String form(DevicesHis devicesHis, Model model) {
		model.addAttribute("devicesHis", devicesHis);
		return "modules/device/devicesHisForm";
	}

	@RequiresPermissions("device:devicesHis:edit")
	@RequestMapping(value = "save")
	public String save(DevicesHis devicesHis, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, devicesHis)){
			return form(devicesHis, model);
		}
		devicesHisService.save(devicesHis);
		addMessage(redirectAttributes, "保存设备变更信息成功");
		return "redirect:"+Global.getAdminPath()+"/device/devicesHis/?repage";
	}
	
	@RequiresPermissions("device:devicesHis:edit")
	@RequestMapping(value = "delete")
	public String delete(DevicesHis devicesHis, RedirectAttributes redirectAttributes) {
		devicesHisService.delete(devicesHis);
		addMessage(redirectAttributes, "删除设备变更信息成功");
		return "redirect:"+Global.getAdminPath()+"/device/devicesHis/?repage";
	}

}