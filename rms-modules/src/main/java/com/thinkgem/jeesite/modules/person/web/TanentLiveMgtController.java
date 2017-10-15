package com.thinkgem.jeesite.modules.person.web;

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

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.ContractSignTypeEnum;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.person.entity.Partner;
import com.thinkgem.jeesite.modules.person.service.PartnerService;

@Controller
@RequestMapping(value = "${adminPath}/person/tanentLiveMgt")
public class TanentLiveMgtController extends BaseController {

  @Autowired
  private RentContractService rentContractService;
  @Autowired
  private PropertyProjectService propertyProjectService;
  @Autowired
  private BuildingService buildingService;
  @Autowired
  private HouseService houseService;
  @Autowired
  private RoomService roomServie;
  @Autowired
  private PartnerService partnerService;

  @ModelAttribute
  public RentContract get(@RequestParam(required = false) String id) {
    RentContract entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = rentContractService.get(id);
    }
    if (entity == null) {
      entity = new RentContract();
    }
    return entity;
  }

  @RequiresPermissions("person:tanentLiveMgt:view")
  @RequestMapping(value = {""})
  public String initPage(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
    initContractSearchConditions(rentContract, model, request, response, false);
    return "modules/person/tanentLiveRentPersonsMgtList";
  }

  @RequiresPermissions("person:tanentLiveMgt:view")
  @RequestMapping(value = {"list"})
  public String list(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
    initContractSearchConditions(rentContract, model, request, response, true);
    return "modules/person/tanentLiveRentPersonsMgtList";
  }

  private void initContractSearchConditions(RentContract rentContract, Model model, HttpServletRequest request, HttpServletResponse response, boolean needQuery) {
    if (needQuery) {
      Page<RentContract> page = rentContractService.findPage(new Page<RentContract>(request, response), rentContract);
      model.addAttribute("page", page);
    }
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);
    if (rentContract != null) {
      if (null != rentContract.getPropertyProject() && StringUtils.isNotBlank(rentContract.getPropertyProject().getId())) {
        PropertyProject propertyProject = new PropertyProject();
        propertyProject.setId(rentContract.getPropertyProject().getId());
        Building building = new Building();
        building.setPropertyProject(propertyProject);
        List<Building> buildingList = buildingService.findList(building);
        model.addAttribute("buildingList", buildingList);
      }
      if (null != rentContract.getBuilding() && StringUtils.isNotBlank(rentContract.getBuilding().getId())) {
        House house = new House();
        Building building = new Building();
        building.setId(rentContract.getBuilding().getId());
        house.setBuilding(building);
        List<House> houseList = houseService.findList(house);
        model.addAttribute("houseList", houseList);
      }
      if (null != rentContract.getHouse() && StringUtils.isNotBlank(rentContract.getHouse().getId())) {
        Room room = new Room();
        House house = new House();
        house.setId(rentContract.getHouse().getId());
        room.setHouse(house);
        List<Room> roomList = roomServie.findList(room);
        model.addAttribute("roomList", roomList);
      }
    }
  }

  @RequiresPermissions("person:tanentLiveMgt:view")
  @RequestMapping(value = "form")
  public String form(RentContract rentContract, Model model, HttpServletRequest request) {
    if (rentContract.getIsNewRecord()) {
      rentContract.setSignType(ContractSignTypeEnum.NEW_SIGN.getValue());
      rentContract.setContractCode((rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");
    }
    model.addAttribute("rentContract", rentContract);
    model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
    if (null != rentContract.getPropertyProject()) {
      Building building = new Building();
      PropertyProject propertyProject = new PropertyProject();
      propertyProject.setId(rentContract.getPropertyProject().getId());
      building.setPropertyProject(propertyProject);
      List<Building> buildingList = buildingService.findList(building);
      model.addAttribute("buildingList", buildingList);
    }
    if (null != rentContract.getBuilding()) {
      House house = new House();
      Building building = new Building();
      building.setId(rentContract.getBuilding().getId());
      house.setBuilding(building);
      house.setChoose("1");
      List<House> houseList = houseService.findList(house);
      if (null != rentContract.getHouse()) houseList.add(houseService.get(rentContract.getHouse()));
      model.addAttribute("houseList", houseList);
    }
    if (null != rentContract.getRoom()) {
      Room room = new Room();
      House house = new House();
      house.setId(rentContract.getHouse().getId());
      room.setHouse(house);
      room.setChoose("1");
      List<Room> roomList = roomServie.findList(room);
      if (null != rentContract.getRoom()) {
        Room rm = roomServie.get(rentContract.getRoom());
        if (null != rm) roomList.add(rm);
      }
      model.addAttribute("roomList", roomList);
    }
    model.addAttribute("partnerList", partnerService.findList(new Partner()));
    return "modules/person/tanentLiveRentPersonsMgtForm";
  }

}
