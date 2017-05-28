/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.HouseAd;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseAdService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;

/**
 * 广告管理Controller
 * 
 * @author huangsc
 */
@Controller
@RequestMapping(value = "${adminPath}/inventory/ad")
public class HouseAdController extends BaseController {

  @Autowired
  private HouseAdService houseAdService;
  @Autowired
  private PropertyProjectService propertyProjectService;
  @Autowired
  private BuildingService buildingService;
  @Autowired
  private HouseService houseService;
  @Autowired
  private RoomService roomServie;

  @ModelAttribute
  public HouseAd get(@RequestParam(required = false) String id) {
    HouseAd entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = houseAdService.get(id);
    }
    if (entity == null) {
      entity = new HouseAd();
    }
    return entity;
  }

  @RequestMapping(value = {"list", ""})
  public String list(HouseAd houseAd, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<HouseAd> page = houseAdService.findPage(new Page<HouseAd>(request, response), houseAd);
    model.addAttribute("page", page);
    return "modules/inventory/houseAdList";
  }

  @RequestMapping(value = "form")
  public String form(HouseAd houseAd, Model model) {
    model.addAttribute("houseAd", houseAd);

    model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
    if (null != houseAd.getPropertyProject()) {
      Building building = new Building();
      PropertyProject propertyProject = new PropertyProject();
      if (!StringUtils.isBlank(houseAd.getPropertyProject().getId())) {
        propertyProject.setId(houseAd.getPropertyProject().getId());
        building.setPropertyProject(propertyProject);
        List<Building> buildingList = buildingService.findList(building);
        model.addAttribute("buildingList", buildingList);
      }
    }
    if (null != houseAd.getBuilding()) {
      House house = new House();
      Building building = new Building();
      if (!StringUtils.isBlank(houseAd.getBuilding().getId())) {
        building.setId(houseAd.getBuilding().getId());
        house.setBuilding(building);
        house.setChoose("1");
        List<House> houseList = houseService.findList(house);
        if (null != houseAd.getHouse()) houseList.add(houseService.get(houseAd.getHouse()));
        model.addAttribute("houseList", houseList);
      }
    }
    if (null != houseAd.getRoom()) {
      Room room = new Room();
      House house = new House();
      if (!StringUtils.isBlank(houseAd.getHouse().getId())) {
        house.setId(houseAd.getHouse().getId());
        room.setHouse(house);
        room.setChoose("1");
        List<Room> roomList = roomServie.findList(room);
        if (null != houseAd.getRoom()) {
          Room rm = roomServie.get(houseAd.getRoom());
          if (null != rm) roomList.add(rm);
        }
        model.addAttribute("roomList", roomList);
      }
    }
    return "modules/inventory/houseAdForm";
  }

  @RequestMapping(value = "save")
  public String save(HouseAd houseAd, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, houseAd)) {
      return form(houseAd, model);
    }
    houseAdService.save(houseAd);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存广告管理成功");
    return "redirect:" + Global.getAdminPath() + "/inventory/ad/?repage";
  }

  @RequestMapping(value = "delete")
  public String delete(HouseAd houseAd, RedirectAttributes redirectAttributes) {
    houseAdService.delete(houseAd);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除广告管理成功");
    return "redirect:" + Global.getAdminPath() + "/inventory/ad/?repage";
  }

}
