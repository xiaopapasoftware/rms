package com.thinkgem.jeesite.modules.contract.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.BaseEntity;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContractOwner;
import com.thinkgem.jeesite.modules.contract.service.AuditHisService;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractOwnerService;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractService;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import com.thinkgem.jeesite.modules.person.entity.Remittancer;
import com.thinkgem.jeesite.modules.person.service.OwnerService;
import com.thinkgem.jeesite.modules.person.service.RemittancerService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 承租合同
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/leaseContract")
public class LeaseContractController extends BaseController {

  @Autowired
  private LeaseContractService leaseContractService;
  @Autowired
  private PropertyProjectService propertyProjectService;
  @Autowired
  private BuildingService buildingService;
  @Autowired
  private RemittancerService remittancerService;
  @Autowired
  private AuditHisService auditHisService;
  @Autowired
  private HouseService houseService;
  @Autowired
  private OwnerService ownerService;
  @Autowired
  private LeaseContractOwnerService leaseContractOwnerService;

  @ModelAttribute
  public LeaseContract get(@RequestParam(required = false) String id) {
    LeaseContract entity = null;
    if (StringUtils.isNotBlank(id)) {
      entity = leaseContractService.get(id);
    }
    if (entity == null) {
      entity = new LeaseContract();
    }
    return entity;
  }

  @RequiresPermissions("contract:leaseContract:view")
  @RequestMapping(value = {"list"})
  public String listQuery(LeaseContract leaseContract, HttpServletRequest request, HttpServletResponse response, Model model) {
    buildIdListByOwner(leaseContract);
    Page<LeaseContract> page = leaseContractService.findPage(new Page<LeaseContract>(request, response), leaseContract);
    page.getList().forEach(contract -> {
      List<LeaseContractOwner> contractOwnerList = leaseContractOwnerService.getListByContractId(contract.getId());
      if (CollectionUtils.isNotEmpty(contractOwnerList)) {
        contract.setOwnerList(contractOwnerList.stream().map(LeaseContractOwner::getOwnerId).map(ownerService::get).collect(Collectors.toList()));
      }
    });
    model.addAttribute("page", page);
    initQueryCondition(model, leaseContract);
    return "modules/contract/leaseContractList";
  }

  private void buildIdListByOwner(LeaseContract leaseContract) {
    Owner owner = leaseContract.getOwner();
    if (StringUtils.isNotBlank(owner.getName()) || StringUtils.isNotBlank(owner.getSocialNumber())
            ||StringUtils.isNotBlank(owner.getCellPhone()) || StringUtils.isNotBlank(owner.getSecondCellPhone())) {
      List<Owner> ownerList = ownerService.findList(owner);
      if (CollectionUtils.isNotEmpty(ownerList)) {
        List<String> idList = leaseContractOwnerService.getContractIdListByOwnerIdList
                (ownerList.stream().map(Owner::getId).collect(Collectors.toList()));
        if (CollectionUtils.isNotEmpty(idList)) {
          leaseContract.setIdList(idList);
        }
      } else {
        leaseContract.setIdList(Collections.singletonList("test"));
      }
    }
  }

  @RequiresPermissions("contract:leaseContract:view")
  @RequestMapping(value = {""})
  public String listNoQuery(LeaseContract leaseContract, HttpServletRequest request, HttpServletResponse response, Model model) {
    initQueryCondition(model, leaseContract);
    return "modules/contract/leaseContractList";
  }

  private void initQueryCondition(Model model, LeaseContract leaseContract) {
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);
    if (null != leaseContract.getPropertyProject() && StringUtils.isNotEmpty(leaseContract.getPropertyProject().getId())) {
      Building building = new Building();
      PropertyProject propertyProject = new PropertyProject();
      propertyProject.setId(leaseContract.getPropertyProject().getId());
      building.setPropertyProject(propertyProject);
      List<Building> buildingList = buildingService.findList(building);
      model.addAttribute("buildingList", buildingList);
    }
    if (null != leaseContract.getBuilding() && StringUtils.isNotEmpty(leaseContract.getBuilding().getId())) {
      House house = new House();
      Building building = new Building();
      building.setId(leaseContract.getBuilding().getId());
      house.setBuilding(building);
      List<House> houseList = houseService.findList(house);
      model.addAttribute("houseList", houseList);
    }
  }

  @RequestMapping(value = "audit")
  public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
    leaseContractService.audit(auditHis);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "操作成功！");
    return "redirect:" + Global.getAdminPath() + "/contract/leaseContract/?repage";
  }

  @RequestMapping(value = "auditHis")
  public String auditHis(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<AuditHis> page = auditHisService.findPage(new Page<AuditHis>(request, response), auditHis);
    model.addAttribute("page", page);
    return "modules/contract/auditHis";
  }

  @RequiresPermissions("contract:leaseContract:view")
  @RequestMapping(value = "form")
  public String form(LeaseContract leaseContract, Model model) {
    model.addAttribute("leaseContract", leaseContract);
    if (leaseContract.getIsNewRecord()) {
      leaseContract.setContractCode((leaseContractService.getTotalValidLeaseContractCounts() + 1) + "-" + "SF");
    }
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

    if (null != leaseContract.getPropertyProject()) {
      Building building = new Building();
      PropertyProject propertyProject = new PropertyProject();
      propertyProject.setId(leaseContract.getPropertyProject().getId());
      building.setPropertyProject(propertyProject);
      List<Building> buildingList = buildingService.findList(building);
      model.addAttribute("buildingList", buildingList);
    }
    if (null != leaseContract.getBuilding()) {
      House house = new House();
      Building building = new Building();
      building.setId(leaseContract.getBuilding().getId());
      house.setBuilding(building);
      List<House> houseList = houseService.findList(house);
      model.addAttribute("houseList", houseList);
    }
    List<Remittancer> remittancerList = remittancerService.findList(new Remittancer());
    model.addAttribute("remittancerList", remittancerList);
    List<LeaseContractOwner> contractOwnerList = leaseContractOwnerService.getListByContractId(leaseContract.getId());
    if (CollectionUtils.isNotEmpty(contractOwnerList)) {
      leaseContract.setOwnerList(contractOwnerList.stream().map(LeaseContractOwner::getOwnerId).map(ownerService::get)
              .collect(Collectors.toList()));
    }
    return "modules/contract/leaseContractForm";
  }

  @RequiresPermissions("contract:leaseContract:edit")
  @RequestMapping(value = "save")
  public String save(LeaseContract leaseContract, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, leaseContract)) {
      return form(leaseContract, model);
    }
    LeaseContract tmpLeaseContract = new LeaseContract();
    tmpLeaseContract.setPropertyProject(leaseContract.getPropertyProject());
    tmpLeaseContract.setBuilding(leaseContract.getBuilding());
    tmpLeaseContract.setHouse(leaseContract.getHouse());
    tmpLeaseContract.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
    List<LeaseContract> list = leaseContractService.findList(tmpLeaseContract);
    if (null != list && list.size() > 0) {
      tmpLeaseContract = list.get(0);
      if ("0".equals(tmpLeaseContract.getContractStatus()) || "1".equals(tmpLeaseContract.getContractStatus())) {
        if ("add".equals(leaseContract.getType())) {
          addMessage(model, ViewMessageTypeEnum.ERROR, "该房屋已签承租合同！");
          return form(leaseContract, model);
        } else {
          if (!leaseContract.getPropertyProject().getId().equals(tmpLeaseContract.getPropertyProject().getId()) || !leaseContract.getBuilding().getId().equals(tmpLeaseContract.getBuilding().getId())
              || !leaseContract.getHouse().getId().equals(tmpLeaseContract.getHouse().getId())) {
            addMessage(model, ViewMessageTypeEnum.ERROR, "该房屋已签承租合同！");
            return form(leaseContract, model);
          }
        }
      }
    }
    if (leaseContract.getIsNewRecord()) {
      String[] codeArr = leaseContract.getContractCode().split("-");
      leaseContract.setContractCode(codeArr[0] + "-" + (leaseContractService.getTotalValidLeaseContractCounts() + 1) + "-" + "SF");
    }
    leaseContractService.save(leaseContract);
    leaseContractOwnerService.deleteListByContractId(leaseContract.getId());
    leaseContract.getOwnerList().forEach(owner -> {
      LeaseContractOwner contractOwner = new LeaseContractOwner();
      contractOwner.setLeaseContractId(leaseContract.getId());
      contractOwner.setOwnerId(owner.getId());
      leaseContractOwnerService.save(contractOwner);
    });
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存承租合同成功");
    return "redirect:" + Global.getAdminPath() + "/contract/leaseContract/?repage";
  }

  @RequiresPermissions("contract:leaseContract:edit")
  @RequestMapping(value = "delete")
  public String delete(LeaseContract leaseContract, RedirectAttributes redirectAttributes) {
    leaseContractService.delete(leaseContract);
    leaseContractOwnerService.deleteListByContractId(leaseContract.getId());
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除承租合同成功");
    return "redirect:" + Global.getAdminPath() + "/contract/leaseContract/?repage";
  }


  @RequestMapping(value = "ajaxQueryOwners")
  @ResponseBody
  public String ajaxQueryOwners(String id) {
    if (StringUtils.isNotBlank(id)) {
      List<LeaseContractOwner> contractOwnerList = leaseContractOwnerService.getListByContractId(id);
      if (CollectionUtils.isNotEmpty(contractOwnerList)) {
        return getSelectOptionResult(contractOwnerList.stream().map(LeaseContractOwner::getOwnerId).map(ownerService::get).collect(Collectors.toList()));
      }
    }
    return "";
  }

  private String getSelectOptionResult(List<Owner> ownerList) {
    StringBuffer optionResultBuf = new StringBuffer();
    if (CollectionUtils.isNotEmpty(ownerList)) {
      ownerList.forEach(owner -> optionResultBuf.append("<option selected value='").append(owner.getId()).append("'>").append(owner.getName()).append("-").append(owner.getSocialNumber()).append("-").append(owner.getCellPhone())
              .append("/").append(owner.getSecondCellPhone()).append("</option>"));
    }
    return optionResultBuf.toString();
  }

}
