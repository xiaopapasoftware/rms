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

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;

/**
 * 房间信息Controller
 * 
 * @author huangsc
 * @version 2015-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/room")
public class RoomController extends BaseController {

	@Autowired
	private PropertyProjectService propertyProjectService;

	@Autowired
	private BuildingService buildingService;

	@Autowired
	private HouseService houseService;

	@Autowired
	private RoomService roomService;

	@ModelAttribute
	public Room get(@RequestParam(required = false) String id) {
		Room entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = roomService.get(id);
		}
		if (entity == null) {
			entity = new Room();
		}
		return entity;
	}

	@RequiresPermissions("inventory:room:view")
	@RequestMapping(value = {"list", ""})
	public String list(Room room, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Room> page = roomService.findPage(new Page<Room>(request, response), room);
		model.addAttribute("page", page);

		model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));

		if (room.getPropertyProject() != null && StringUtils.isNotEmpty(room.getPropertyProject().getId())) {
			PropertyProject pp = new PropertyProject();
			pp.setId(room.getPropertyProject().getId());
			Building bd = new Building();
			bd.setPropertyProject(pp);
			model.addAttribute("listBuilding", buildingService.findList(bd));
		}

		if (room.getBuilding() != null && StringUtils.isNotEmpty(room.getBuilding().getId())) {
			Building bd = new Building();
			bd.setId(room.getBuilding().getId());
			House h = new House();
			h.setBuilding(bd);
			model.addAttribute("listHouse", houseService.findList(h));
		}

		return "modules/inventory/roomList";
	}

	@RequestMapping(value = {"findList"})
	@ResponseBody
	public List<Room> findList(String id) {
		Room room = new Room();
		House house = new House();
		house.setHouseNo(id);
		room.setHouse(house);
		List<Room> list = roomService.findList(room);
		return list;
	}

	@RequiresPermissions("inventory:room:view")
	@RequestMapping(value = "form")
	public String form(Room room, Model model) {

		model.addAttribute("room", room);
		model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));

		if (room.getPropertyProject() != null && StringUtils.isNotEmpty(room.getPropertyProject().getId())) {
			PropertyProject pp = new PropertyProject();
			pp.setId(room.getPropertyProject().getId());
			Building bd = new Building();
			bd.setPropertyProject(pp);
			model.addAttribute("listBuilding", buildingService.findList(bd));
		}

		if (room.getBuilding() != null && StringUtils.isNotEmpty(room.getBuilding().getId())) {
			Building bd = new Building();
			bd.setId(room.getBuilding().getId());
			House h = new House();
			h.setBuilding(bd);
			model.addAttribute("listHouse", houseService.findList(h));
		}

		model.addAttribute("listOrientation", DictUtils.getDictList("orientation"));
		model.addAttribute("listStructure", DictUtils.getDictList("structure"));

		if (StringUtils.isNotEmpty(room.getOrientation())) {
			room.setOrientationList(convertToDictListFromSelVal(room.getOrientation()));
		}
		if (StringUtils.isNotEmpty(room.getStructure())) {
			room.setStructureList(convertToDictListFromSelVal(room.getStructure()));
		}
		return "modules/inventory/roomForm";
	}

	@RequiresPermissions("inventory:room:edit")
	@RequestMapping(value = "save")
	public String save(Room room, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, room)) {
			return form(room, model);
		}
		List<Room> rooms = roomService.findRoomByPrjAndBldAndHouNoAndRomNo(room);
		if (!room.getIsNewRecord()) {// 更新
			if (CollectionUtils.isNotEmpty(rooms)) {
				Room upRoom = new Room();
				upRoom.setId(rooms.get(0).getId());
				upRoom.setRoomStatus(rooms.get(0).getRoomStatus());
				upRoom.setPropertyProject(room.getPropertyProject());
				upRoom.setBuilding(room.getBuilding());
				upRoom.setHouse(room.getHouse());
				upRoom.setRoomNo(room.getRoomNo());
				upRoom.setMeterNo(room.getMeterNo());
				upRoom.setRoomSpace(room.getRoomSpace());
				if (CollectionUtils.isNotEmpty(room.getStructureList())) {
					upRoom.setStructure(convertToStrFromList(room.getStructureList()));
				}
				if (CollectionUtils.isNotEmpty(room.getOrientationList())) {
					upRoom.setOrientation(convertToStrFromList(room.getOrientationList()));
				}
				upRoom.setAttachmentPath(room.getAttachmentPath());
				upRoom.setRemarks(room.getRemarks());
				roomService.save(upRoom);
			} else {
				if (CollectionUtils.isNotEmpty(room.getStructureList())) {
					room.setStructure(convertToStrFromList(room.getStructureList()));
				}
				if (CollectionUtils.isNotEmpty(room.getOrientationList())) {
					room.setOrientation(convertToStrFromList(room.getOrientationList()));
				}
				roomService.save(room);
			}
			addMessage(redirectAttributes, "修改房间信息成功");
			return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
		} else {// 新增
			if (CollectionUtils.isNotEmpty(rooms)) {// 重复
				model.addAttribute("message", "该物业项目及该楼宇下及该房屋下的房间号已被使用，不能重复添加");
				model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());

				model.addAttribute("listPropertyProject", propertyProjectService.findList(new PropertyProject()));

				PropertyProject pp = new PropertyProject();
				pp.setId(room.getPropertyProject().getId());
				Building bd = new Building();
				bd.setPropertyProject(pp);
				model.addAttribute("listBuilding", buildingService.findList(bd));

				Building bd2 = new Building();
				bd2.setId(room.getBuilding().getId());
				House h = new House();
				h.setBuilding(bd2);
				model.addAttribute("listHouse", houseService.findList(h));

				model.addAttribute("listOrientation", DictUtils.getDictList("orientation"));
				model.addAttribute("listStructure", DictUtils.getDictList("structure"));
				return "modules/inventory/roomForm";
			} else {// 可以新增
				room.setRoomStatus(DictUtils.getDictValue("待装修", "room_status", "0"));
				if (CollectionUtils.isNotEmpty(room.getStructureList())) {
					room.setStructure(convertToStrFromList(room.getStructureList()));
				}
				if (CollectionUtils.isNotEmpty(room.getOrientationList())) {
					room.setOrientation(convertToStrFromList(room.getOrientationList()));
				}
				roomService.save(room);
				addMessage(redirectAttributes, "保存房间信息成功");
				return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
			}
		}

	}
	@RequiresPermissions("inventory:room:edit")
	@RequestMapping(value = "delete")
	public String delete(Room room, RedirectAttributes redirectAttributes) {
		roomService.delete(room);
		addMessage(redirectAttributes, "删除房间及图片信息成功");
		return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
	}

	private String convertToStrFromList(List<Dict> dicts) {
		String resultStr = StringUtils.EMPTY;
		for (Dict d : dicts) {
			if (StringUtils.isNotEmpty(d.getId())) {
				if (StringUtils.isEmpty(resultStr)) {
					resultStr = resultStr + d.getId();
				} else {
					resultStr = resultStr + "," + d.getId();
				}
			}
		}
		return resultStr;
	}

	private List<Dict> convertToDictListFromSelVal(String selVal) {
		if (StringUtils.isNotEmpty(selVal)) {
			List<Dict> orientationList = Lists.newArrayList();
			for (String val : StringUtils.split(selVal, ",")) {
				Dict d = new Dict();
				d.setId(val);
				orientationList.add(d);
			}
			return orientationList;
		} else {
			return Lists.newArrayList();
		}
	}
}