/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.web;

import java.util.Date;
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
import com.thinkgem.jeesite.modules.device.entity.Devices;
import com.thinkgem.jeesite.modules.device.service.DevicesService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 设备信息Controller
 * 
 * @author huangsc
 * @version 2015-06-13
 */
@Controller
@RequestMapping(value = "${adminPath}/device/devices")
public class DevicesController extends BaseController {

	@Autowired
	private DevicesService devicesService;

	@ModelAttribute
	public Devices get(@RequestParam(required = false) String id) {
		Devices entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = devicesService.get(id);
		}
		if (entity == null) {
			entity = new Devices();
		}
		return entity;
	}

	@RequiresPermissions("device:devices:view")
	@RequestMapping(value = {"list", ""})
	public String list(Devices devices, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Devices> page = devicesService.findPage(new Page<Devices>(request, response), devices);
		model.addAttribute("page", page);
		return "modules/device/devicesList";
	}

	@RequestMapping(value = {"deviceDialog"})
	public String deviceDialog(Devices devices, HttpServletRequest request, HttpServletResponse response, Model model) {
		devices.setDeviceStatus("0");
		Page<Devices> page = devicesService.findPage(new Page<Devices>(request, response), devices);
		model.addAttribute("page", page);
		return "modules/device/deviceDialog";
	}

	@RequiresPermissions("device:devices:view")
	@RequestMapping(value = "form")
	public String form(Devices devices, Model model) {
		model.addAttribute("devices", devices);
		return "modules/device/devicesForm";
	}

	@RequiresPermissions("device:devices:edit")
	@RequestMapping(value = "save")
	public String save(Devices devices, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, devices)) {
			return form(devices, model);
		}
		List<Devices> exDevices = devicesService.findExistedDevices(devices);
		if (!devices.getIsNewRecord()) {// 是更新
			if (CollectionUtils.isNotEmpty(exDevices)) {
				devices.setId(exDevices.get(0).getId());
			}
			devicesService.save(devices);
			addMessage(redirectAttributes, "修改设备信息成功");
			return "redirect:" + Global.getAdminPath() + "/device/devices/?repage";
		} else {// 是新增
			if (CollectionUtils.isNotEmpty(exDevices)) {
				model.addAttribute("message", "要添加设备的设备编号、设备名称、设备型号、设备类型、设备品牌已经存在，不能重复添加！");
				model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());
				return "modules/device/devicesForm";
			} else {
				devices.setDevicesChooseFlag("0");// 设备初始设置状态为“未设置”
				devices.setDeviceStatus("0");// 设备初始状态为“入库未分配”
				devicesService.save(devices);
				addMessage(redirectAttributes, "保存设备信息成功");
				return "redirect:" + Global.getAdminPath() + "/device/devices/?repage";
			}
		}
	}

	@RequiresPermissions("device:devices:edit")
	@RequestMapping(value = "delete")
	public String delete(Devices devices, RedirectAttributes redirectAttributes) {
		devicesService.delete(devices);
		addMessage(redirectAttributes, "删除设备信息成功");
		return "redirect:" + Global.getAdminPath() + "/device/devices/?repage";
	}

	@RequestMapping(value = "updateDevicesStatus")
	@ResponseBody
	public void updateDevicesStatus(String id, String serNum, String status) {
		Devices upDevices = new Devices();
		upDevices.setId(id);
		upDevices.setDeviceStatus(status);// 更改为出库已分配
		upDevices.setDistrSerlNum(serNum);
		upDevices.setUpdateBy(UserUtils.getUser());
		upDevices.setUpdateDate(new Date());
		devicesService.updateDevicesStatus(upDevices);
	}

	@RequestMapping(value = "updateDevicesChooseFlag")
	@ResponseBody
	public void updateDevicesChooseFlag(String id, String status) {
		Devices upDevices = new Devices();
		upDevices.setId(id);
		upDevices.setDevicesChooseFlag(status);// 更改为已设置状态
		upDevices.setUpdateBy(UserUtils.getUser());
		upDevices.setUpdateDate(new Date());
		devicesService.updateDevicesChooseFlag(upDevices);
	}

}