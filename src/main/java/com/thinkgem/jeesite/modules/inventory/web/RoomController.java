/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;

/**
 * 房间信息Controller
 * @author huangsc
 * @version 2015-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/room")
public class RoomController extends BaseController {

	@Autowired
	private RoomService roomService;
	
	@ModelAttribute
	public Room get(@RequestParam(required=false) String id) {
		Room entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = roomService.get(id);
		}
		if (entity == null){
			entity = new Room();
		}
		return entity;
	}
	
	@RequiresPermissions("inventory:room:view")
	@RequestMapping(value = {"list", ""})
	public String list(Room room, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Room> page = roomService.findPage(new Page<Room>(request, response), room); 
		model.addAttribute("page", page);
		return "modules/inventory/roomList";
	}
	
	@RequestMapping(value = {"findList", ""})
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
		return "modules/inventory/roomForm";
	}

	@RequiresPermissions("inventory:room:edit")
	@RequestMapping(value = "save")
	public String save(Room room, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, room)){
			return form(room, model);
		}
		roomService.save(room);
		addMessage(redirectAttributes, "保存房间信息成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/room/?repage";
	}
	
	@RequiresPermissions("inventory:room:edit")
	@RequestMapping(value = "delete")
	public String delete(Room room, RedirectAttributes redirectAttributes) {
		roomService.delete(room);
		addMessage(redirectAttributes, "删除房间信息成功");
		return "redirect:"+Global.getAdminPath()+"/inventory/room/?repage";
	}

}