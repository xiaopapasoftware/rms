/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.web;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.device.entity.Devices;
import com.thinkgem.jeesite.modules.device.entity.RoomDevices;
import com.thinkgem.jeesite.modules.device.service.DevicesService;
import com.thinkgem.jeesite.modules.device.service.RoomDevicesService;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 房屋设备关联信息Controller
 * 
 * @author huangsc
 * @version 2015-06-28
 */
@Controller
@RequestMapping(value = "${adminPath}/device/roomDevices")
public class RoomDevicesController extends BaseController {

	@Autowired
	private RoomDevicesService roomDevicesService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private PropertyProjectService propertyProjectService;

	@Autowired
	private DevicesService devicesService;

	@Autowired
	private HouseService houseService;

	@ModelAttribute
	public RoomDevices get(@RequestParam(required = false) String id) {
		RoomDevices entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = roomDevicesService.get(id);
		}
		if (entity == null) {
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
		if (!beanValidator(model, roomDevices)) {
			return form(roomDevices, model);
		}
		List<Devices> addeDevices = Lists.newArrayList();// 所有后来添加的设备
		List<Devices> deletedDevices = Lists.newArrayList();// 所有本次要删除的设备
		List<Devices> allDevices = roomDevices.getRoomDevicesDtlList();
		if (CollectionUtils.isNotEmpty(allDevices)) {
			for (Devices d : allDevices) {
				if ("1".equals(d.getManualSetFlag()) && StringUtils.isNotEmpty(d.getId())) {// 全部为手动添加的数据
					addeDevices.add(d);
				}
				if ("0".equals(d.getManualSetFlag()) && StringUtils.isNotEmpty(d.getId()) && "1".equals(d.getDelFlag())) {// 全部要删除的数据
					deletedDevices.add(d);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(addeDevices)) {
			roomDevicesService.addRoomDevices(roomDevices, addeDevices);
		}
		if (CollectionUtils.isNotEmpty(deletedDevices)) {
			roomDevicesService.deleteRoomDevices(roomDevices, deletedDevices);
		}
		addMessage(redirectAttributes, "房间设备配备成功");
		return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
	}
	@RequiresPermissions("device:roomDevices:edit")
	@RequestMapping(value = "delete")
	public String delete(RoomDevices roomDevices, RedirectAttributes redirectAttributes) {
		roomDevicesService.delete(roomDevices);
		addMessage(redirectAttributes, "删除房屋设备关联信息成功");
		return "redirect:" + Global.getAdminPath() + "/device/roomDevices/?repage";
	}

	/**
	 * 房间设备维护
	 * */
	@RequiresPermissions("device:roomDevices:view")
	@RequestMapping(value = "maintainDevices")
	public String maintainDevices(RoomDevices roomDevices, RedirectAttributes redirectAttributes, Model model) {
		RoomDevices rd = new RoomDevices();

		String roomId = roomDevices.getRoomId();// 房间ID
		Room rm = roomService.get(roomId);
		rd.setRoomId(roomId);
		rd.setRoomNo(rm.getRoomNo());
		rd.setRoom(rm);

		rd.setHouseId(rm.getHouse().getId());
		rd.setHouseNo(rm.getHouse().getHouseNo());

		rd.setBuildingId(rm.getBuilding().getId());
		rd.setBuildingName(rm.getBuilding().getBuildingName());

		rd.setPropertyProjectId(rm.getPropertyProject().getId());
		rd.setProjectName(rm.getPropertyProject().getProjectName());

		List<Devices> roomedDevices = Lists.newArrayList();
		RoomDevices rds = new RoomDevices();
		rds.setRoomId(roomId);
		List<RoomDevices> rdList = roomDevicesService.findList(rds);
		if (CollectionUtils.isNotEmpty(rdList)) {
			for (RoomDevices tempRD : rdList) {
				if (StringUtils.isNotEmpty(tempRD.getDeviceId())) {
					Devices d = devicesService.get(tempRD.getDeviceId());
					d.setDeviceTypeDesc(DictUtils.getDictLabel(d.getDeviceType(), "device_type", d.getDeviceType()));
					roomedDevices.add(d);
				}
			}
		}
		rd.setRoomDevicesDtlList(roomedDevices);

		model.addAttribute("room", rm);
		model.addAttribute("roomDevices", rd);

		return "modules/inventory/roomDevicesMaintain";
	}

	/**
	 * 房屋设备查看
	 * */
	@RequiresPermissions("device:roomDevices:view")
	@RequestMapping(value = "viewHouseDevices")
	public String viewHouseDevices(RoomDevices roomDevices, Model model) {
		String houseId = roomDevices.getHouseId();
		House house = houseService.get(houseId);
		RoomDevices rd = new RoomDevices();

		rd.setHouseId(house.getId());
		rd.setHouseNo(house.getHouseNo());

		rd.setBuildingId(house.getBuilding().getId());
		rd.setBuildingName(house.getBuilding().getBuildingName());

		rd.setPropertyProjectId(house.getPropertyProject().getId());
		rd.setProjectName(house.getPropertyProject().getProjectName());

		List<Devices> roomedDevices = Lists.newArrayList();
		RoomDevices rds = new RoomDevices();
		rds.setHouseId(houseId);
		List<RoomDevices> rdList = roomDevicesService.findList(rds);
		if (CollectionUtils.isNotEmpty(rdList)) {
			for (RoomDevices tempRD : rdList) {
				if (StringUtils.isNotEmpty(tempRD.getDeviceId())) {
					Devices d = devicesService.get(tempRD.getDeviceId());
					d.setDeviceTypeDesc(DictUtils.getDictLabel(d.getDeviceType(), "device_type", d.getDeviceType()));
					roomedDevices.add(d);
				}
			}
		}
		rd.setRoomDevicesDtlList(roomedDevices);
		model.addAttribute("roomDevices", rd);
		model.addAttribute("house", house);
		
		return "modules/inventory/houseDevicesView";
	}
}