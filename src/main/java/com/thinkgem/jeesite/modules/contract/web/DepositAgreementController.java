/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
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
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.PartnerService;
import com.thinkgem.jeesite.modules.person.service.TenantService;

/**
 * 定金协议Controller
 * 
 * @author huangsc
 * @version 2015-06-09
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/depositAgreement")
public class DepositAgreementController extends BaseController {
    @Autowired
    private RentContractService rentContractService;
    @Autowired
    private DepositAgreementService depositAgreementService;
    @Autowired
    private PropertyProjectService propertyProjectService;
    @Autowired
    private BuildingService buildingService;
    @Autowired
    private HouseService houseService;
    @Autowired
    private RoomService roomServie;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private PartnerService partnerService;

    @ModelAttribute
    public DepositAgreement get(@RequestParam(required = false) String id) {
	DepositAgreement entity = null;
	if (StringUtils.isNotBlank(id)) {
	    entity = depositAgreementService.get(id);
	}
	if (entity == null) {
	    entity = new DepositAgreement();
	}
	return entity;
    }

    // @RequiresPermissions("contract:depositAgreement:view")
    @RequestMapping(value = { "list", "" })
    public String list(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {
	Page<DepositAgreement> page = depositAgreementService.findPage(new Page<DepositAgreement>(request, response), depositAgreement);
	model.addAttribute("page", page);

	List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
	model.addAttribute("projectList", projectList);

	if (null != depositAgreement.getPropertyProject()) {
	    PropertyProject propertyProject = new PropertyProject();
	    propertyProject.setId(depositAgreement.getPropertyProject().getId());
	    Building building = new Building();
	    building.setPropertyProject(propertyProject);
	    List<Building> buildingList = buildingService.findList(building);
	    model.addAttribute("buildingList", buildingList);
	}

	if (null != depositAgreement.getBuilding()) {
	    House house = new House();
	    Building building = new Building();
	    building.setId(depositAgreement.getBuilding().getId());
	    house.setBuilding(building);
	    List<House> houseList = houseService.findList(house);
	    model.addAttribute("houseList", houseList);
	}

	if (null != depositAgreement.getHouse()) {
	    Room room = new Room();
	    House house = new House();
	    house.setId(depositAgreement.getHouse().getId());
	    room.setHouse(house);
	    List<Room> roomList = roomServie.findList(room);
	    model.addAttribute("roomList", roomList);
	}

	return "modules/contract/depositAgreementList";
    }

    // @RequiresPermissions("contract:depositAgreement:view")
    @RequestMapping(value = "form")
    public String form(DepositAgreement depositAgreement, Model model) {
	model.addAttribute("depositAgreement", depositAgreement);

	int currAgreeNum = 1;
	List<DepositAgreement> allAgreements = depositAgreementService.findAllValidAgreements();
	if (CollectionUtils.isNotEmpty(allAgreements)) {
	    currAgreeNum = currAgreeNum + allAgreements.size();
	}
	depositAgreement.setAgreementCode(currAgreeNum + "-" + "XY");

	if (null != depositAgreement && !StringUtils.isBlank(depositAgreement.getId())) {
	    depositAgreement.setTenantList(depositAgreementService.findTenant(depositAgreement));
	}

	List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
	model.addAttribute("projectList", projectList);

	if (null != depositAgreement.getPropertyProject()) {
	    Building building = new Building();
	    PropertyProject propertyProject = new PropertyProject();
	    propertyProject.setId(depositAgreement.getPropertyProject().getId());
	    building.setPropertyProject(propertyProject);
	    List<Building> buildingList = buildingService.findList(building);
	    model.addAttribute("buildingList", buildingList);
	}

	if (null != depositAgreement.getBuilding()) {
	    House house = new House();
	    Building building = new Building();
	    building.setId(depositAgreement.getBuilding().getId());
	    house.setBuilding(building);
	    house.setChoose("1");
	    List<House> houseList = houseService.findList(house);
	    if ("0".equals(depositAgreement.getRentMode()) && null != depositAgreement.getHouse())
		houseList.add(houseService.get(depositAgreement.getHouse()));
	    model.addAttribute("houseList", houseList);
	}

	if (null != depositAgreement.getHouse()) {
	    Room room = new Room();
	    House house = new House();
	    house.setId(depositAgreement.getHouse().getId());
	    room.setHouse(house);
	    room.setChoose("1");
	    List<Room> roomList = Lists.newArrayList();
	    roomList = roomServie.findList(room);
	    if (null != depositAgreement.getRoom()) {
		Room rm = roomServie.get(depositAgreement.getRoom());
		if (null != rm)
		    roomList.add(rm);
	    }
	    model.addAttribute("roomList", CollectionUtils.isNotEmpty(roomList) ? roomList : Lists.newArrayList());
	}

	List<Tenant> tenantList = tenantService.findList(new Tenant());
	model.addAttribute("tenantList", tenantList);

	return "modules/contract/depositAgreementForm";
    }

    // @RequiresPermissions("contract:depositAgreement:edit")
    @RequestMapping(value = "save")
    public String save(DepositAgreement depositAgreement, Model model, RedirectAttributes redirectAttributes) {
	if (!beanValidator(model, depositAgreement) && "1".equals(depositAgreement.getValidatorFlag())) {
	    return form(depositAgreement, model);
	}
	depositAgreementService.save(depositAgreement);
	addMessage(redirectAttributes, "保存定金协议成功");
	return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
    }

    @RequestMapping(value = "audit")
    public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
	depositAgreementService.audit(auditHis);
	return list(new DepositAgreement(), request, response, model);
    }

    /**
     * 转违约
     */
    @RequestMapping(value = "breakContract")
    public String breakContract(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {
	depositAgreementService.breakContract(depositAgreement);
	return list(new DepositAgreement(), request, response, model);
    }

    /**
     * 转合同
     */
    @RequestMapping(value = "intoContract")
    public String intoContract(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {
	depositAgreement = depositAgreementService.get(depositAgreement.getId());

	if (null != depositAgreement && !StringUtils.isBlank(depositAgreement.getId())) {
	    depositAgreement.setTenantList(depositAgreementService.findTenant(depositAgreement));
	}
	RentContract rentContract = new RentContract();
	rentContract.setSignType("0");// 新签
	rentContract.setContractName(null);
	rentContract.setRentMode(depositAgreement.getRentMode());
	rentContract.setPropertyProject(depositAgreement.getPropertyProject());
	rentContract.setBuilding(depositAgreement.getBuilding());
	rentContract.setHouse(depositAgreement.getHouse());
	rentContract.setRoom(depositAgreement.getRoom());
	rentContract.setRental(depositAgreement.getHousingRent());
	rentContract.setRenMonths(depositAgreement.getRenMonths());
	rentContract.setDepositMonths(depositAgreement.getDepositMonths());
	rentContract.setStartDate(depositAgreement.getStartDate());
	rentContract.setExpiredDate(depositAgreement.getExpiredDate());
	rentContract.setSignDate(depositAgreement.getAgreementDate());
	rentContract.setUser(depositAgreement.getUser());
	rentContract.setTenantList(depositAgreement.getTenantList());
	rentContract.setRemarks(depositAgreement.getRemarks());
	rentContract.setAgreementId(depositAgreement.getId());
	int currContractNum = 1;
	List<RentContract> allContracts = rentContractService.findAllValidRentContracts();
	if (CollectionUtils.isNotEmpty(allContracts)) {
	    currContractNum = currContractNum + allContracts.size();
	}
	rentContract.setContractCode(currContractNum + "-" + "CZ");
	model.addAttribute("rentContract", rentContract);

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
	    List<House> houseList = houseService.findList(house);
	    model.addAttribute("houseList", houseList);
	}

	if (null != rentContract.getRoom()) {
	    Room room = new Room();
	    House house = new House();
	    house.setId(rentContract.getHouse().getId());
	    room.setHouse(house);
	    List<Room> roomList = roomServie.findList(room);
	    model.addAttribute("roomList", roomList);
	}

	model.addAttribute("partnerList", partnerService.findList(new Partner()));

	model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
	model.addAttribute("tenantList", tenantService.findList(new Tenant()));
	model.addAttribute("depositAmount", depositAgreement.getDepositAmount());
	return "modules/contract/rentContractAdd";
    }

    // @RequiresPermissions("contract:depositAgreement:edit")
    @RequestMapping(value = "delete")
    public String delete(DepositAgreement depositAgreement, RedirectAttributes redirectAttributes) {
	depositAgreementService.delete(depositAgreement);
	addMessage(redirectAttributes, "删除定金协议成功");
	return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
    }

}