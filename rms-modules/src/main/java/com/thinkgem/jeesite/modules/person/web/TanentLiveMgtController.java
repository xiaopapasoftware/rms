package com.thinkgem.jeesite.modules.person.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.contract.web.CommonBusinessController;
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
public class TanentLiveMgtController extends CommonBusinessController {

    @Autowired
    private RentContractService rentContractService;
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
        if (rentContract != null) {
            commonInit(model, rentContract.getPropertyProject(), rentContract.getBuilding(), rentContract.getHouse());
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
        commonInit2(model, rentContract.getPropertyProject(), rentContract.getBuilding(), rentContract.getHouse(), rentContract.getRoom());
        model.addAttribute("partnerList", partnerService.findList(new Partner()));
        return "modules/person/tanentLiveRentPersonsMgtForm";
    }

}
