/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
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
import com.thinkgem.jeesite.modules.inventory.enums.HouseStatusEnum;
import com.thinkgem.jeesite.modules.inventory.enums.RoomStatusEnum;
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

  // @RequiresPermissions("inventory:room:view")
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
  public List<Room> findList(House house) {
    Room room = new Room();
    room.setHouse(house);
    room.setChoose(house.getChoose());// 过滤不可用
    List<Room> list = roomService.findList(room);
    return list;
  }

  // @RequiresPermissions("inventory:room:view")
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

  @RequestMapping(value = "add")
  public String add(Room room, Model model) {

    model.addAttribute("room", room);
    List<PropertyProject> list = new ArrayList<PropertyProject>();
    PropertyProject propertyProject = propertyProjectService.get(room.getPropertyProject());
    list.add(propertyProject);
    model.addAttribute("listPropertyProject", list);

    List<Building> listBuilding = new ArrayList<Building>();
    Building building = buildingService.get(room.getBuilding());
    listBuilding.add(building);
    model.addAttribute("listBuilding", listBuilding);

    List<House> listHouse = new ArrayList<House>();
    House house = houseService.get(room.getHouse());
    listHouse.add(house);
    model.addAttribute("listHouse", listHouse);

    model.addAttribute("listOrientation", DictUtils.getDictList("orientation"));
    model.addAttribute("listStructure", DictUtils.getDictList("structure"));

    if (StringUtils.isNotEmpty(room.getOrientation())) {
      room.setOrientationList(convertToDictListFromSelVal(room.getOrientation()));
    }
    if (StringUtils.isNotEmpty(room.getStructure())) {
      room.setStructureList(convertToDictListFromSelVal(room.getStructure()));
    }
    return "modules/inventory/roomAdd";
  }

  // @RequiresPermissions("inventory:room:edit")
  @RequestMapping(value = "finishDirect")
  @ResponseBody
  public String finishDirect(Room room, Model model, RedirectAttributes redirectAttributes) {
    Room r = roomService.get(room);
    houseService.finishSingleRoomDirect4Status(r);
    return "SUCCESS";
  }

  // @RequiresPermissions("inventory:room:edit")
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
        upRoom.setIsFeature(room.getIsFeature());
        upRoom.setRental(room.getRental());
        upRoom.setShortDesc(room.getShortDesc());
        upRoom.setShortLocation(room.getShortLocation());
        upRoom.setPayWay(room.getPayWay());
        roomService.saveRoom(upRoom);
      } else {
        if (CollectionUtils.isNotEmpty(room.getStructureList())) {
          room.setStructure(convertToStrFromList(room.getStructureList()));
        }
        if (CollectionUtils.isNotEmpty(room.getOrientationList())) {
          room.setOrientation(convertToStrFromList(room.getOrientationList()));
        }
        roomService.saveRoom(room);
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
      } else {// 可以新增房间
        House house = houseService.get(room.getHouse());
        if (HouseStatusEnum.TO_RENOVATION.getValue().equals(house.getHouseStatus())) {
          room.setRoomStatus(RoomStatusEnum.TO_RENOVATION.getValue());
        } else if (HouseStatusEnum.BE_RESERVED.getValue().equals(house.getHouseStatus())) {
          room.setRoomStatus(RoomStatusEnum.BE_RESERVED.getValue());
        } else if (HouseStatusEnum.WHOLE_RENT.getValue().equals(house.getHouseStatus())) {
          room.setRoomStatus(RoomStatusEnum.RENTED.getValue());
        } else {
          room.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
        }
        if (CollectionUtils.isNotEmpty(room.getStructureList())) {
          room.setStructure(convertToStrFromList(room.getStructureList()));
        }
        if (CollectionUtils.isNotEmpty(room.getOrientationList())) {
          room.setOrientation(convertToStrFromList(room.getOrientationList()));
        }
        roomService.saveRoom(room);
        addMessage(redirectAttributes, "保存房间信息成功");
        return "redirect:" + Global.getAdminPath() + "/inventory/room/?repage";
      }
    }
  }

  @RequestMapping(value = "ajaxSave")
  @ResponseBody
  public String ajaxSave(Room room, Model model, RedirectAttributes redirectAttributes) {
    JSONObject jsonObject = new JSONObject();
    List<Room> rooms = roomService.findRoomByPrjAndBldAndHouNoAndRomNo(room);
    if (CollectionUtils.isNotEmpty(rooms)) {// 重复
      List<PropertyProject> list = new ArrayList<PropertyProject>();
      PropertyProject propertyProject = propertyProjectService.get(room.getPropertyProject());
      list.add(propertyProject);
      model.addAttribute("listPropertyProject", list);

      List<Building> listBuilding = new ArrayList<Building>();
      Building building = buildingService.get(room.getBuilding());
      listBuilding.add(building);
      model.addAttribute("listBuilding", listBuilding);

      List<House> listHouse = new ArrayList<House>();
      House house = houseService.get(room.getHouse());
      listHouse.add(house);
      model.addAttribute("listHouse", listHouse);

      model.addAttribute("listOrientation", DictUtils.getDictList("orientation"));
      model.addAttribute("listStructure", DictUtils.getDictList("structure"));

      jsonObject.put("message", "该物业项目及该楼宇下及该房屋下的房间号已被使用，不能重复添加");
    } else {// 可以新增
      room.setRoomStatus(RoomStatusEnum.RENT_FOR_RESERVE.getValue());
      if (CollectionUtils.isNotEmpty(room.getStructureList())) {
        room.setStructure(convertToStrFromList(room.getStructureList()));
      }
      if (CollectionUtils.isNotEmpty(room.getOrientationList())) {
        room.setOrientation(convertToStrFromList(room.getOrientationList()));
      }
      roomService.saveRoom(room);
      jsonObject.put("id", room.getId());
      jsonObject.put("name", room.getRoomNo());
    }
    return jsonObject.toString();
  }

  // @RequiresPermissions("inventory:room:edit")
  @RequestMapping(value = "delete")
  public String delete(Room room, RedirectAttributes redirectAttributes) {
    Room queryRoom = roomService.get(room);
    String roomStatus = queryRoom.getRoomStatus();
    if (RoomStatusEnum.BE_RESERVED.getValue().equals(roomStatus) || RoomStatusEnum.RENTED.getValue().equals(roomStatus)) {
      addMessage(redirectAttributes, "房间已预定或已出租，不能删除！");
    } else {
      roomService.delete(room);
      addMessage(redirectAttributes, "删除房间及图片信息成功");
    }
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

  public Page<House> listFeature(int p_n, int p_c) {
    Page<House> page = new Page<House>();
    page.setPageNo(p_n);
    page.setPageSize(p_c);
    return roomService.findFeaturePage(page);
  }
}
