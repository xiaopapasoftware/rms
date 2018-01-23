package com.thinkgem.jeesite.modules.contract.web;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.List;

public abstract class CommonBusinessController extends BaseController {

    @Autowired
    protected PropertyProjectService propertyProjectService;
    @Autowired
    protected BuildingService buildingService;
    @Autowired
    protected HouseService houseService;
    @Autowired
    protected RoomService roomService;

    protected void commonInit(Model model, PropertyProject paraPP, Building papaBuild, House ppHouse) {
        model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
        setBuildingToModel(model, paraPP);
        if (null != papaBuild && StringUtils.isNotEmpty(papaBuild.getId())) {
            House house = new House();
            Building building = new Building();
            building.setId(papaBuild.getId());
            house.setBuilding(building);
            List<House> houseList = houseService.findList(house);
            model.addAttribute("houseList", houseList);
        }
        if (null != ppHouse && StringUtils.isNotEmpty(ppHouse.getId())) {
            Room room = new Room();
            House house = new House();
            house.setId(ppHouse.getId());
            room.setHouse(house);
            List<Room> roomList = roomService.findList(room);
            model.addAttribute("roomList", roomList);
        }
    }

    protected void commonInit2(Model model, PropertyProject paraPP, Building papaBuild, House ppHouse, Room ppRoom) {
        model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
        setBuildingToModel(model, paraPP);
        if (null != papaBuild) {
            House house = new House();
            Building building = new Building();
            building.setId(papaBuild.getId());
            house.setBuilding(building);
            house.setChoose("1");
            List<House> houseList = houseService.findList(house);
            if (null != ppHouse) houseList.add(houseService.get(ppHouse));
            model.addAttribute("houseList", houseList);
        }
        if (null != ppHouse) {
            Room room = new Room();
            House house = new House();
            house.setId(ppHouse.getId());
            room.setHouse(house);
            room.setChoose("1");
            List<Room> roomList = roomService.findList(room);
            if (null != ppRoom) {
                Room rm = roomService.get(ppRoom);
                if (null != rm) roomList.add(rm);
            }
            model.addAttribute("roomList", CollectionUtils.isNotEmpty(roomList) ? roomList : Lists.newArrayList());
        }
    }

    private void setBuildingToModel(Model model, PropertyProject paraPP) {
        if (null != paraPP && StringUtils.isNotEmpty(paraPP.getId())) {
            Building building = new Building();
            PropertyProject propertyProject = new PropertyProject();
            propertyProject.setId(paraPP.getId());
            building.setPropertyProject(propertyProject);
            List<Building> buildingList = buildingService.findList(building);
            model.addAttribute("buildingList", buildingList);
        }
    }
}
