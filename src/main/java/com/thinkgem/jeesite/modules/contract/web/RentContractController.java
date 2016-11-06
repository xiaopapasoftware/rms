/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.service.MessageService;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
import com.thinkgem.jeesite.modules.common.enums.ValidatorFlagEnum;
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.AuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractBusiStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractSignTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.fee.service.ElectricFeeService;
import com.thinkgem.jeesite.modules.funds.dao.PaymentTradeDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.entity.TradingAccounts;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
import com.thinkgem.jeesite.modules.funds.service.TradingAccountsService;
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
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * @author huangsc
 * @author wangshujin
 */
@Controller
@RequestMapping(value = "${adminPath}/contract/rentContract")
public class RentContractController extends BaseController {

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
  private TenantService tenantService;
  @Autowired
  private PaymentTransService paymentTransService;
  @Autowired
  private PartnerService partnerService;
  @Autowired
  private TradingAccountsService tradingAccountsService;
  @Autowired
  private PaymentTradeDao paymentTradeDao;
  @Autowired
  private RoomService roomService;
  @Autowired
  private ElectricFeeService electricFeeService;
  @Autowired
  private DepositAgreementService depositAgreementService;

  @Autowired
  private MessageService messageService;// APP消息推送

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

  // @RequiresPermissions("contract:rentContract:view")
  @RequestMapping(value = {"list", ""})
  public String list(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<RentContract> page = rentContractService.findPage(new Page<RentContract>(request, response), rentContract);
    model.addAttribute("page", page);

    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

    if (null != rentContract.getPropertyProject()) {
      PropertyProject propertyProject = new PropertyProject();
      propertyProject.setId(rentContract.getPropertyProject().getId());
      Building building = new Building();
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

    if (null != rentContract.getHouse()) {
      Room room = new Room();
      House house = new House();
      house.setId(rentContract.getHouse().getId());
      room.setHouse(house);
      List<Room> roomList = roomServie.findList(room);
      model.addAttribute("roomList", roomList);
    }

    return "modules/contract/rentContractList";
  }

  @RequestMapping(value = {"rentContractDialogList"})
  public String rentContractDialogList(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<RentContract> page = rentContractService.findPage(new Page<RentContract>(request, response), rentContract);
    model.addAttribute("page", page);

    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

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
      house.setId(rentContract.getRoom().getId());
      room.setHouse(house);
      List<Room> roomList = roomServie.findList(room);
      model.addAttribute("roomList", roomList);
    }

    return "modules/contract/rentContractDialog";
  }

  @RequestMapping(value = {"rentContractDialog"})
  public String rentContractDialog(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
    rentContract.setContractStatus("6");// 到账收据审核通过
    model.addAttribute("rentContract", rentContract);

    Page<RentContract> page = rentContractService.findPage(new Page<RentContract>(request, response), rentContract);
    model.addAttribute("page", page);

    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

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
      house.setId(rentContract.getRoom().getId());
      room.setHouse(house);
      List<Room> roomList = roomServie.findList(room);
      model.addAttribute("roomList", roomList);
    }

    return "modules/contract/rentContractDialog";
  }

  @RequestMapping(value = "audit")
  public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
    rentContractService.audit(auditHis);
    return list(new RentContract(), request, response, model);
  }

  @RequestMapping(value = "cancel")
  public String cancel(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
    auditHis.setAuditStatus(AuditStatusEnum.REFUSE.getValue());
    auditHis.setUpdateUser(UserUtils.getUser().getId());
    rentContractService.audit(auditHis);
    return list(new RentContract(), request, response, model);
  }

  // @RequiresPermissions("contract:rentContract:view")
  @RequestMapping(value = "form")
  public String form(RentContract rentContract, Model model) {
    if (rentContract.getIsNewRecord()) {
      rentContract.setSignType("0");// 新签
      rentContract.setContractCode((rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");
    }
    model.addAttribute("rentContract", rentContract);
    model.addAttribute("partnerList", partnerService.findList(new Partner()));
    model.addAttribute("projectList", propertyProjectService.findList(new PropertyProject()));
    if (null != rentContract && !StringUtils.isBlank(rentContract.getId())) {
      rentContract.setLiveList(rentContractService.findLiveTenant(rentContract));
      rentContract.setTenantList(rentContractService.findTenant(rentContract));
    }
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
    List<Tenant> tenantList = tenantService.findList(new Tenant());
    model.addAttribute("tenantList", tenantList);
    return "modules/contract/rentContractForm";
  }

  /**
   * 人工续签
   */
  @RequestMapping(value = "renewContract")
  public String renewContract(RentContract rentContract, Model model) {
    String contractId = rentContract.getId();
    rentContract = rentContractService.get(contractId);
    rentContract.setOriEndDate(DateUtils.formatDate(rentContract.getExpiredDate()));// 为了实现续签合同的开始日期默认为原合同的结束日期，则把原合同的结束日期带到页面
    rentContract.setContractId(contractId);
    rentContract.setSignType(ContractSignTypeEnum.RENEW_SIGN.getValue());
    rentContract.setContractName(rentContract.getContractName().concat("(XU)"));
    rentContract.setDepositElectricAmount(null);
    rentContract.setDepositAmount(null);
    rentContract.setRental(null);
    rentContract.setRenMonths(null);
    rentContract.setDepositMonths(null);
    rentContract.setStartDate(null);
    rentContract.setExpiredDate(null);
    rentContract.setSignDate(null);
    rentContract.setRemindTime(null);
    int currContractNum = 1;
    List<RentContract> allContracts = rentContractService.findAllValidRentContracts();
    if (CollectionUtils.isNotEmpty(allContracts)) {
      currContractNum = currContractNum + allContracts.size();
    }
    rentContract.setContractCode(rentContract.getContractCode().split("-")[0] + "-" + currContractNum + "-" + rentContract.getContractCode().split("-")[2]);
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);
    rentContract.setLiveList(rentContractService.findLiveTenant(rentContract));
    rentContract.setTenantList(rentContractService.findTenant(rentContract));
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
    model.addAttribute("tenantList", tenantService.findList(new Tenant()));
    model.addAttribute("renew", "1");
    model.addAttribute("partnerList", partnerService.findList(new Partner()));
    rentContract.setId(null);
    rentContract.setAgreementId(null);// 防止对某些定金转的合同，再进行续签，导致最终续签合同还持有agreementId
    model.addAttribute("rentContract", rentContract);
    return "modules/contract/rentContractForm";
  }

  /**
   * 点击“逾期自动续签”
   */
  @RequestMapping(value = "autoRenewContract")
  public String autoRenewContract(RentContract rentContract, Model model) {
    String contractId = rentContract.getId();
    rentContract = rentContractService.get(contractId);
    rentContract.setContractId(contractId);
    rentContract.setSignType("2");// 逾租续签
    rentContract.setContractName(rentContract.getContractName().concat("(XU)"));
    rentContract.setStartDate(null);
    rentContract.setExpiredDate(null);
    rentContract.setSignDate(null);
    int currContractNum = 1;
    List<RentContract> allContracts = rentContractService.findAllValidRentContracts();
    if (CollectionUtils.isNotEmpty(allContracts)) {
      currContractNum = currContractNum + allContracts.size();
    }
    rentContract.setContractCode(rentContract.getContractCode().split("-")[0] + "-" + currContractNum + "-" + rentContract.getContractCode().split("-")[2]);
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);
    rentContract.setLiveList(rentContractService.findLiveTenant(rentContract));
    rentContract.setTenantList(rentContractService.findTenant(rentContract));
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
    List<Tenant> tenantList = tenantService.findList(new Tenant());
    model.addAttribute("tenantList", tenantList);
    model.addAttribute("partnerList", partnerService.findList(new Partner()));
    rentContract.setId(null);
    rentContract.setAgreementId(null);// 防止对某些定金转的合同，再进行续签，导致最终续签合同还持有agreementId
    model.addAttribute("rentContract", rentContract);
    return "modules/contract/rentContractForm";
  }

  /**
   * 牵涉到后台合同的新增、修改；以及APP端合同的修改保存
   */
  // @RequiresPermissions("contract:rentContract:edit")
  @RequestMapping(value = "save")
  public String save(RentContract rentContract, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, rentContract) && ValidatorFlagEnum.SAVE.getValue().equals(rentContract.getValidatorFlag())) {
      return form(rentContract, model);
    }
    if (rentContract.getIsNewRecord()) {// 设置出租合同编号
      String[] codeArr = rentContract.getContractCode().split("-");
      rentContract.setContractCode(codeArr[0] + "-" + (rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");
    }
    if (StringUtils.isNotEmpty(rentContract.getDataSource()) && DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource())) {} else {
      rentContract.setDataSource(DataSourceEnum.BACK_SYSTEM.getValue());
    }
    int result = rentContractService.saveContract(rentContract);
    if (result == -2) {
      model.addAttribute("message", "出租合同结束日期不能晚于承租合同截止日期.");
      model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());
      initExceptionedModel(model, rentContract);
      return "modules/contract/rentContractForm";
    } else if (result == -1) {
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
      addMessage(model, "房源已出租！");
      return form(rentContract, model);
    } else {
      if (StringUtils.isNotEmpty(rentContract.getDataSource()) && DataSourceEnum.FRONT_APP.getValue().equals(rentContract.getDataSource())) {// APP订单后台保存
        try {
          Message message = new Message();
          message.setContent("您的签约申请已被管家确认,请联系管家!");
          message.setTitle("签约提醒");
          message.setType("签约提醒");
          Tenant tenant = rentContract.getTenantList().get(0);
          message.setReceiver(tenantService.get(tenant).getCellPhone());
          messageService.addMessage(message, true);
        } catch (Exception e) {
          logger.error("签约推送异常:", e);
        }
      }
      addMessage(redirectAttributes, "保存出租合同成功");
      if ("1".equals(rentContract.getSaveSource())) {
        return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
      } else {
        return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
      }
    }
  }

  private void initExceptionedModel(Model model, RentContract rentContract) {
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
    model.addAttribute("tenantList", tenantService.findList(new Tenant()));
  }

  @RequestMapping(value = "saveAdditional")
  public String saveAdditional(AgreementChange agreementChange, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, agreementChange)) {
      RentContract rentContract = new RentContract();
      rentContract.setId(agreementChange.getContractId());
      return changeContract(rentContract, model);
    }
    rentContractService.saveAdditional(agreementChange);
    addMessage(redirectAttributes, "保存变更协议成功");
    return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
  }

  /**
   * 点击“正常退租”按钮触发的业务
   */
  @RequestMapping(value = "returnContract")
  public String returnContract(RentContract rentContract, RedirectAttributes redirectAttributes) {
    if (paymentTransService.checkNotSignedPaymentTrans(rentContract.getId())) {
      addMessage(redirectAttributes, "有款项未到账,不能正常退租。");
      return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }
    rentContract = rentContractService.get(rentContract.getId());
    rentContract.setContractBusiStatus(ContractBusiStatusEnum.NORMAL_RETURN_ACCOUNT.getValue());
    rentContractService.save(rentContract);
    addMessage(redirectAttributes, "正常退租成功，接下来请进行正常退租核算！");
    return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
  }

  /**
   * 点击“提前退租”按钮触发的业务
   */
  @RequestMapping(value = "earlyReturnContract")
  public String earlyReturnContract(RentContract rentContract, RedirectAttributes redirectAttributes) {
    String rentContractId = rentContract.getId();
    rentContract = rentContractService.get(rentContractId);
    rentContract.setContractBusiStatus(ContractBusiStatusEnum.EARLY_RETURN_ACCOUNT.getValue());
    rentContractService.save(rentContract);
    paymentTransService.deleteNotSignPaymentTrans(rentContractId);
    addMessage(redirectAttributes, "提前退租成功，接下来请进行提前退租核算！");
    return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
  }

  /**
   * 点击“逾期退租”按钮触发的业务
   */
  @RequestMapping(value = "lateReturnContract")
  public String lateReturnContract(RentContract rentContract, RedirectAttributes redirectAttributes) {
    if (paymentTransService.checkNotSignedPaymentTrans(rentContract.getId())) {
      addMessage(redirectAttributes, "有款项未到账,不能逾期退租。");
      return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
    }
    rentContract = rentContractService.get(rentContract.getId());
    rentContract.setContractBusiStatus(ContractBusiStatusEnum.LATE_RETURN_ACCOUNT.getValue());
    rentContractService.save(rentContract);
    addMessage(redirectAttributes, "逾期退租成功，接下来请进行逾期退租核算！");
    return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
  }

  /**
   * 点击“特殊退租”按钮触发的业务
   */
  @RequestMapping(value = "specialReturnContract")
  public String specialReturnContract(RentContract rentContract, Model model, RedirectAttributes redirectAttributes) {
    String returnDate = rentContract.getReturnDate();
    String contractId = rentContract.getId();
    rentContract = rentContractService.get(contractId);
    rentContract.setIsSpecial("1");
    rentContract.setReturnDate(returnDate);
    rentContract.setTradeType(TradeTypeEnum.SPECIAL_RETURN_RENT.getValue());// 特殊退租
    List<Accounting> outAccountList = genOutAccountListBack(rentContract, "3", false);// 应出核算项列表
    List<Accounting> inAccountList = genInAccountListBack(rentContract, "3", false, false);// 应收核算项列表
    model.addAttribute("outAccountList", outAccountList);
    model.addAttribute("outAccountSize", outAccountList.size());
    model.addAttribute("accountList", inAccountList);
    model.addAttribute("accountSize", inAccountList.size());
    model.addAttribute("rentContract", rentContract);
    model.addAttribute("totalRefundAmount", calculateTatalRefundAmt(outAccountList, inAccountList));
    return "modules/contract/rentContractCheck";

  }

  @RequestMapping(value = "changeContract")
  public String changeContract(RentContract rentContract, Model model) {
    AgreementChange agreementChange = new AgreementChange();
    agreementChange.setContractId(rentContract.getId());
    model.addAttribute("agreementChange", agreementChange);
    List<Tenant> tenantList = tenantService.findList(new Tenant());
    model.addAttribute("tenantList", tenantList);
    return "modules/contract/additionalContract";
  }

  /**
   * 点击“正常退租核算”按钮触发的业务
   */
  @RequestMapping(value = "toReturnCheck")
  public String toReturnCheck(RentContract rentContract, Model model) {
    rentContract = rentContractService.get(rentContract.getId());
    List<Accounting> outAccountList = genOutAccountListBack(rentContract, "1", false);// 应出核算项列表
    List<Accounting> inAccountList = genInAccountListBack(rentContract, "1", false, false);// 应收核算项列表
    model.addAttribute("accountList", inAccountList);
    model.addAttribute("accountSize", inAccountList.size());
    model.addAttribute("outAccountList", outAccountList);
    model.addAttribute("outAccountSize", outAccountList.size());
    rentContract.setTradeType("7");// 正常退租
    model.addAttribute("rentContract", rentContract);
    model.addAttribute("totalRefundAmount", calculateTatalRefundAmt(outAccountList, inAccountList));
    return "modules/contract/rentContractCheck";
  }

  /**
   * 点击“提前退租核算”按钮触发的业务
   */
  @RequestMapping(value = "toEarlyReturnCheck")
  public String toEarlyReturnCheck(RentContract rentContract, Model model) {
    rentContract = rentContractService.get(rentContract.getId());
    List<Accounting> outAccountList = genOutAccountListBack(rentContract, "0", true);// 应出核算项列表
    List<Accounting> inAccountList = genInAccountListBack(rentContract, "0", true, false);// 应收核算项列表
    model.addAttribute("returnRental", "1");// 显示计算公式的标识
    model.addAttribute("totalFee", commonCalculateTotalAmount(rentContract, "6"));// 已缴纳房租总金额
    model.addAttribute("rental", rentContract.getRental());
    model.addAttribute("dates", DateUtils.getDistanceOfTwoDate(rentContract.getStartDate(), new Date()));// 已住天数
    model.addAttribute("accountList", inAccountList);
    model.addAttribute("accountSize", inAccountList.size());
    model.addAttribute("outAccountList", outAccountList);
    model.addAttribute("outAccountSize", outAccountList.size());
    rentContract.setTradeType("6");// 提前退租
    model.addAttribute("rentContract", rentContract);
    model.addAttribute("totalRefundAmount", calculateTatalRefundAmt(outAccountList, inAccountList));
    return "modules/contract/rentContractCheck";
  }

  /**
   * 点击“逾期退租核算”按钮触发的业务
   */
  @RequestMapping(value = "toLateReturnCheck")
  public String toLateReturnCheck(RentContract rentContract, Model model) {
    rentContract = rentContractService.get(rentContract.getId());
    List<Accounting> outAccountList = genOutAccountListBack(rentContract, "2", false);// 应出核算项列表
    List<Accounting> inAccountList = genInAccountListBack(rentContract, "2", false, true);// 应收核算项列表
    model.addAttribute("outAccountList", outAccountList);
    model.addAttribute("outAccountSize", outAccountList.size());
    model.addAttribute("accountList", inAccountList);
    model.addAttribute("accountSize", inAccountList.size());
    rentContract.setTradeType("8");// 逾期退租
    model.addAttribute("rentContract", rentContract);
    model.addAttribute("totalRefundAmount", calculateTatalRefundAmt(outAccountList, inAccountList));
    return "modules/contract/rentContractCheck";
  }

  /**
   * 计算各种退款的应退或应收的总金额
   */
  private String calculateTatalRefundAmt(List<Accounting> outAccountList, List<Accounting> inAccountList) {
    double totalOutAmt = 0;// 应出总金额
    if (CollectionUtils.isNotEmpty(outAccountList)) {
      for (Accounting outAcct : outAccountList) {
        if (TradeDirectionEnum.OUT.getValue().equals(outAcct.getFeeDirection())) {
          totalOutAmt += outAcct.getFeeAmount();
        }
      }
    }
    double totalInAmt = 0;// 应收总金额
    if (CollectionUtils.isNotEmpty(inAccountList)) {
      for (Accounting intAccount : inAccountList) {
        if (TradeDirectionEnum.IN.getValue().equals(intAccount.getFeeDirection())) {
          totalInAmt += intAccount.getFeeAmount();
        }
      }
    }

    double refundAmount = totalInAmt - totalOutAmt;
    return new BigDecimal(refundAmount).setScale(1, BigDecimal.ROUND_HALF_UP).toString();
  }

  /**
   * 在退租核算页面，点保存按钮
   */
  @RequestMapping(value = "returnCheck")
  public String returnCheck(RentContract rentContract, RedirectAttributes redirectAttributes) {
    rentContractService.returnCheck(rentContract, rentContract.getTradeType());
    if (!StringUtils.isBlank(rentContract.getIsSpecial())) {
      addMessage(redirectAttributes, "特殊退租成功！");
    } else {
      addMessage(redirectAttributes, "退租核算成功！");
    }
    return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
  }

  // @RequiresPermissions("contract:rentContract:edit")
  @RequestMapping(value = "delete")
  public String delete(RentContract rentContract, RedirectAttributes redirectAttributes) {
    rentContract.preUpdate();
    rentContractService.delete(rentContract);
    addMessage(redirectAttributes, "删除出租合同成功");
    return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
  }

  /**
   * 应出退租核算项列表
   * 
   * @param isPre 是否提前
   */
  private List<Accounting> genOutAccountListBack(RentContract rentContract, String accountingType, boolean isPre) {

    List<Accounting> outAccountings = new ArrayList<Accounting>();

    // 水电费押金
    Accounting eaccounting = new Accounting();
    eaccounting.setRentContract(rentContract);
    eaccounting.setAccountingType(accountingType);
    eaccounting.setFeeDirection("0");// 0 : 应出
    eaccounting.setFeeType("2");// 水电费押金
    if ("1".equals(rentContract.getSignType())) {// 如果是正常续签的合同，需要退被续签合同的水电费押金+水电费押金差额,需做递归处理
      eaccounting.setFeeAmount(calculateContinueContractAmount(rentContract, "2"));
    } else {// 如果是新签合同、逾期续签合同则直接退水电费押金
      eaccounting.setFeeAmount(rentContract.getDepositElectricAmount());
    }
    outAccountings.add(eaccounting);

    // 房租押金
    Accounting accounting = new Accounting();
    accounting.setRentContract(rentContract);
    accounting.setAccountingType(accountingType);
    accounting.setFeeDirection("0");// 0 : 应出
    if ("1".equals(rentContract.getSignType())) {// 如果是正常续签的合同，需要退被续签合同的房租押金+房租押金差额,需做递归处理
      accounting.setFeeAmount(calculateContinueContractAmount(rentContract, "4"));
    } else {// 如果是新签合同、逾期续签合同则直接退房租押金
      accounting.setFeeAmount(rentContract.getDepositAmount());
    }
    accounting.setFeeType("4");// 房租押金
    outAccountings.add(accounting);

    if (isPre || "1".equals(rentContract.getIsSpecial())) {// 提前退租或许特殊退租，需计算应退房租金额
      Accounting preBackRentalAcc = new Accounting();
      preBackRentalAcc.setRentContract(rentContract);
      preBackRentalAcc.setAccountingType(accountingType);
      preBackRentalAcc.setFeeDirection("0");// 0 : 应出
      preBackRentalAcc.setFeeType("7");// 应退房租
      preBackRentalAcc.setFeeAmount(commonCalculateBackAmount(rentContract, "6", rentContract.getRental()));
      if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
        preBackRentalAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
        preBackRentalAcc.setFeeDateStr(rentContract.getReturnDate());
      } else {
        preBackRentalAcc.setFeeDate(new Date());
      }
      outAccountings.add(preBackRentalAcc);
    }

    // 预充---应退 智能电表剩余电费
    // 整租不装智能电表，只有合租会装智能电表
    if ("1".equals(rentContract.getRentMode())) {
      Room room = roomService.get(rentContract.getRoom());
      if (room != null && StringUtils.isNotEmpty(room.getMeterNo())) {// 合租且电表号不为空
        Accounting elctrBackAcc = new Accounting();
        elctrBackAcc.setRentContract(rentContract);
        elctrBackAcc.setAccountingType(accountingType);
        elctrBackAcc.setFeeDirection("0");// 0 : 应出
        elctrBackAcc.setFeeType("13");// 智能电表剩余电费
        Map<Integer, String> resultMap = electricFeeService.getMeterFee(rentContract.getId(), DateUtils.firstDateOfCurrentMonth(), DateUtils.lastDateOfCurrentMonth());
        Double feeAmount = 0D;
        if (MapUtils.isNotEmpty(resultMap)) {
          String remainedTotalEle = resultMap.get(3);// 剩余总电量
          String personElePrice = resultMap.get(4);// 个人电量单价
          if (StringUtils.isNotEmpty(remainedTotalEle) && StringUtils.isNotEmpty(personElePrice)) {
            Double price = Double.valueOf(remainedTotalEle) * Double.valueOf(personElePrice);
            if (price > 0) {
              feeAmount = new BigDecimal(price).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue(); // 保留两位小数
            }
          }
        }
        elctrBackAcc.setFeeAmount(feeAmount);
        if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
          elctrBackAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
          elctrBackAcc.setFeeDateStr(rentContract.getReturnDate());
        } else {
          elctrBackAcc.setFeeDate(new Date());
        }
        outAccountings.add(elctrBackAcc);
      }
    }

    // 预付费
    if ("0".equals(rentContract.getChargeType())) {

      // 预付 ---应退 水费剩余金额
      if (rentContract.getWaterFee() != null && rentContract.getWaterFee() > 0) {
        Accounting waterAcc = new Accounting();
        waterAcc.setRentContract(rentContract);
        waterAcc.setAccountingType(accountingType);
        waterAcc.setFeeDirection("0");// 0 : 应出
        waterAcc.setFeeType("15");// 水费剩余金额
        waterAcc.setFeeAmount(commonCalculateBackAmount(rentContract, "14", rentContract.getWaterFee()));
        if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
          waterAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
          waterAcc.setFeeDateStr(rentContract.getReturnDate());
        } else {
          waterAcc.setFeeDate(new Date());
        }
        outAccountings.add(waterAcc);
      }

      // 预付 ---应退 电视费
      if ("1".equals(rentContract.getHasTv()) && null != rentContract.getTvFee() && rentContract.getTvFee() > 0) {
        Accounting tvAcc = new Accounting();
        tvAcc.setRentContract(rentContract);
        tvAcc.setAccountingType(accountingType);
        tvAcc.setFeeDirection("0");// 0 : 应出
        tvAcc.setFeeType("19");// 有线电视费剩余金额
        tvAcc.setFeeAmount(commonCalculateBackAmount(rentContract, "18", rentContract.getTvFee()));
        if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
          tvAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
          tvAcc.setFeeDateStr(rentContract.getReturnDate());
        } else {
          tvAcc.setFeeDate(new Date());
        }
        outAccountings.add(tvAcc);
      }

      // 预付 ---应退 宽带费
      if ("1".equals(rentContract.getHasNet()) && null != rentContract.getNetFee() && rentContract.getNetFee() > 0) {
        Accounting netAcc = new Accounting();
        netAcc.setRentContract(rentContract);
        netAcc.setAccountingType(accountingType);
        netAcc.setFeeDirection("0");// 0 : 应出
        netAcc.setFeeType("21");// 宽带费剩余金额
        netAcc.setFeeAmount(commonCalculateBackAmount(rentContract, "20", rentContract.getNetFee()));
        if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
          netAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
          netAcc.setFeeDateStr(rentContract.getReturnDate());
        } else {
          netAcc.setFeeDate(new Date());
        }
        outAccountings.add(netAcc);
      }

      // 预付 ---应退 服务费
      if (null != rentContract.getServiceFee() && rentContract.getServiceFee() > 0) {
        Accounting servAcc = new Accounting();
        servAcc.setRentContract(rentContract);
        servAcc.setAccountingType(accountingType);
        servAcc.setFeeDirection("0");// 0 : 应出
        servAcc.setFeeType("23");// 服务费剩余金额
        if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
          servAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
          servAcc.setFeeDateStr(rentContract.getReturnDate());
        } else {
          servAcc.setFeeDate(new Date());
        }
        servAcc.setFeeAmount(commonCalculateBackAmount(rentContract, "22", rentContract.getServiceFee()));
        outAccountings.add(servAcc);
      }
    }
    return outAccountings;
  }

  /**
   * 如果当前合同是续签合同，则计算出当前合同前所有的被续签合同的押金总额
   * 
   * @param depositType 为4=则计算所有房租押金金额；为2=则计算所有水电押金金额
   */
  private double calculateContinueContractAmount(RentContract currentContract, String depositType) {
    Double totalAmount = 0d;
    if ("4".equals(depositType)) {// 房租押金金额
      totalAmount = currentContract.getDepositAmount();
    }
    if ("2".equals(depositType)) {// 水电押金金额
      totalAmount = currentContract.getDepositElectricAmount();
    }
    RentContract tempContract = currentContract;// 防止修改原合同数据
    while (StringUtils.isNotEmpty(tempContract.getContractId())) {
      RentContract tempOriRentContract = rentContractService.get(tempContract.getContractId());
      if (tempOriRentContract != null) {
        if ("4".equals(depositType)) {// 房租押金金额
          totalAmount = totalAmount + tempOriRentContract.getDepositAmount();
        }
        if ("2".equals(depositType)) {// 水电押金金额
          totalAmount = totalAmount + tempOriRentContract.getDepositElectricAmount();
        }
        tempContract = tempOriRentContract;
      } else {
        logger.warn("can not find oriRentContract by contractID:[" + tempContract.getContractId() + "]");
        break;
      }
    }
    return new BigDecimal(totalAmount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  /**
   * 获取合同下账务交易类型为“新签合同”、“正常人工续签”、“逾期自动续签”的，审核通过的账务交易关联的所有款项列表
   */
  private List<PaymentTrans> genPaymentTrades(RentContract rentContract) {
    List<PaymentTrans> allPTs = Lists.newArrayList();
    // 获取到所有的新签、人工续签、逾期续签的合同审批成功的账务交易记录
    TradingAccounts ta = new TradingAccounts();
    ta.setTradeId(rentContract.getId());
    ta.setTradeStatus("1");// 账务交易审核通过
    if ("0".equals(rentContract.getSignType())) {// 新签
      ta.setTradeType("3");// 账务交易类型为“新签合同”
    }
    if ("1".equals(rentContract.getSignType())) {// 正常续签
      ta.setTradeType("4");// 账务交易类型为“正常人工续签”
    }
    if ("2".equals(rentContract.getSignType())) {// 逾期续签
      ta.setTradeType("5");// 逾期自动续签
    }
    List<TradingAccounts> tradingAccounts = tradingAccountsService.findList(ta);
    if (CollectionUtils.isNotEmpty(tradingAccounts)) {
      for (TradingAccounts taccount : tradingAccounts) {
        PaymentTrade paymentTrade = new PaymentTrade();
        paymentTrade.setTradeId(taccount.getId());
        List<PaymentTrade> pts = paymentTradeDao.findList(paymentTrade);// 账务-款项关联表
        if (CollectionUtils.isNotEmpty(pts)) {
          for (PaymentTrade pt : pts) {
            PaymentTrans paymentTran = paymentTransService.get(pt.getTransId());
            if (paymentTran != null) {
              allPTs.add(paymentTran);
            }
          }
        }
      }
    }
    return allPTs;
  }

  /**
   * 通用的计算应退房租、水费、电视费、燃气费、宽带费、服务费已成功缴纳且到账的总金额
   * 
   * @param paymentType 款项类型
   */
  private Double commonCalculateTotalAmount(RentContract rentContract, String paymentType) {
    Double totalAmount = 0d;// 租客总共已经成功缴纳的费用金额
    List<PaymentTrans> rentalPaymentTrans = genPaymentTrades(rentContract);
    if (CollectionUtils.isNotEmpty(rentalPaymentTrans)) {
      for (PaymentTrans pt : rentalPaymentTrans) {
        if (pt.getTradeAmount() != null && paymentType.equals(pt.getPaymentType())) {// 款项类型为“房租金额”
          totalAmount = totalAmount + pt.getTradeAmount();
        }
      }
    }
    // 特殊的，当合同为定金转过来的时候，用户已经支付的定金金额需要额外补充计算
    if ("6".equals(paymentType)) {// 应退房租
      if (!StringUtils.isBlank(rentContract.getAgreementId())) {
        DepositAgreement depositAgreement = depositAgreementService.get(rentContract.getAgreementId());
        if (depositAgreement.getDepositAmount() > 0d) {
          totalAmount = totalAmount + depositAgreement.getDepositAmount();
        }
      }
    }
    return totalAmount;
  }

  /**
   * 通用的计算应退房租、水费、电视费、燃气费、宽带费、服务费金额的方法
   * 
   * @param paymentType 款项类型
   * @param monthFeeAmount 每月费用金额
   */
  private Double commonCalculateBackAmount(RentContract rentContract, String paymentType, double monthFeeAmount) {

    Double totalAmount = commonCalculateTotalAmount(rentContract, paymentType);

    Date endDate = new Date();
    if ("1".equals(rentContract.getIsSpecial())) {// 特殊退租时
      endDate = DateUtils.parseDate(rentContract.getReturnDate());
    }
    double dates = DateUtils.getDistanceOfTwoDate(rentContract.getStartDate(), endDate);// 实际入住天数
    double dailyFee = monthFeeAmount * 12 / 365;// 平摊到每天的费用金额
    double hasLivedAmount = dates * dailyFee;

    Double refundAmount = totalAmount - hasLivedAmount;// 提前退租应退金额
    if (refundAmount < 0) {
      return 0d;
    } else {
      return new BigDecimal(refundAmount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
  }

  /**
   * 应收退租核算项列表
   * 
   * @param isPre 是否提前退租
   * @param isLate 是否逾期退租
   */
  private List<Accounting> genInAccountListBack(RentContract rentContract, String accountingType, boolean isPre, boolean isLate) {
    List<Accounting> inAccountings = new ArrayList<Accounting>();

    if (isPre) {// 应收---早退违约金
      Accounting earlyDepositAcc = new Accounting();
      earlyDepositAcc.setRentContract(rentContract);
      earlyDepositAcc.setAccountingType(accountingType);
      earlyDepositAcc.setFeeDirection("1");// 1 : 应收
      earlyDepositAcc.setFeeType("9");// 早退违约金
      if ("1".equals(rentContract.getSignType())) {// 如果是正常续签的合同，需要退被续签合同的水电费押金+水电费押金差额,需做递归处理
        earlyDepositAcc.setFeeAmount(calculateContinueContractAmount(rentContract, "4"));
      } else {// 如果是新签合同、逾期续签合同则直接退水电费押金
        earlyDepositAcc.setFeeAmount(rentContract.getDepositAmount());
      }
      inAccountings.add(earlyDepositAcc);
    }

    if (isLate) {// 应收---逾赔房租
      Accounting lateAcc = new Accounting();
      lateAcc.setRentContract(rentContract);
      lateAcc.setAccountingType(accountingType);
      lateAcc.setFeeDirection("1");// 1 : 应收
      lateAcc.setFeeType("8");// 逾赔房租
      Date endDate = new Date();
      if ("1".equals(rentContract.getIsSpecial())) endDate = DateUtils.parseDate(rentContract.getReturnDate());
      double dates = DateUtils.getDistanceOfTwoDate(rentContract.getExpiredDate(), endDate) - 1;// 逾期天数
      double dailyRental = rentContract.getRental() * 12 / 365;// 每天房租租金
      double tental = (dates < 0 ? 0 : dates) * dailyRental;
      lateAcc.setFeeAmount(new BigDecimal(tental).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
      inAccountings.add(lateAcc);
    }

    // 应收---损坏赔偿金
    Accounting pay4BrokeAcc = new Accounting();
    pay4BrokeAcc.setRentContract(rentContract);
    pay4BrokeAcc.setAccountingType(accountingType);
    pay4BrokeAcc.setFeeDirection("1");// 1 : 应收
    pay4BrokeAcc.setFeeType("10");// 损坏赔偿金
    pay4BrokeAcc.setFeeAmount(0D);
    if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
      pay4BrokeAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
      pay4BrokeAcc.setFeeDateStr(rentContract.getReturnDate());
    } else {
      pay4BrokeAcc.setFeeDate(new Date());
    }
    inAccountings.add(pay4BrokeAcc);

    // 应收---退租补偿税金
    Accounting backSuppAcc = new Accounting();
    backSuppAcc.setRentContract(rentContract);
    backSuppAcc.setAccountingType(accountingType);
    backSuppAcc.setFeeDirection("1");// 1 : 应收
    backSuppAcc.setFeeType("24");// 损坏赔偿金
    backSuppAcc.setFeeAmount(0D);
    if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
      backSuppAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
      backSuppAcc.setFeeDateStr(rentContract.getReturnDate());
    } else {
      backSuppAcc.setFeeDate(new Date());
    }
    inAccountings.add(backSuppAcc);

    // 应收---电费自用金额
    Accounting elSelAcc = new Accounting();
    elSelAcc.setRentContract(rentContract);
    elSelAcc.setAccountingType(accountingType);
    elSelAcc.setFeeDirection("1");// 1 : 应收
    elSelAcc.setFeeType("11");// 电费自用金额
    elSelAcc.setFeeAmount(0D);// 人工计算
    if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
      elSelAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
      elSelAcc.setFeeDateStr(rentContract.getReturnDate());
    } else {
      elSelAcc.setFeeDate(new Date());
    }
    inAccountings.add(elSelAcc);

    // 应收---电费分摊金额
    Accounting elCommAcc = new Accounting();
    elCommAcc.setRentContract(rentContract);
    elCommAcc.setAccountingType(accountingType);
    elCommAcc.setFeeDirection("1");// 1 : 应收
    elCommAcc.setFeeType("12");// 电费分摊金额
    elCommAcc.setFeeAmount(0D);// 人工计算
    if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
      elCommAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
      elCommAcc.setFeeDateStr(rentContract.getReturnDate());
    } else {
      elCommAcc.setFeeDate(new Date());
    }
    inAccountings.add(elCommAcc);

    // 应收---水费金额
    Accounting waterSelAcc = new Accounting();
    waterSelAcc.setRentContract(rentContract);
    waterSelAcc.setAccountingType(accountingType);
    waterSelAcc.setFeeDirection("1");// 1 : 应收
    waterSelAcc.setFeeType("14");// 水费金额
    waterSelAcc.setFeeAmount(0D);// 人工计算
    if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
      waterSelAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
      waterSelAcc.setFeeDateStr(rentContract.getReturnDate());
    } else {
      waterSelAcc.setFeeDate(new Date());
    }
    inAccountings.add(waterSelAcc);

    // 应收---有线电视费
    Accounting tvAcc = new Accounting();
    tvAcc.setRentContract(rentContract);
    tvAcc.setAccountingType(accountingType);
    tvAcc.setFeeDirection("1");// 1 : 应收
    tvAcc.setFeeType("18");// 电视费金额
    tvAcc.setFeeAmount(0D);// 人工计算
    if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
      tvAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
      tvAcc.setFeeDateStr(rentContract.getReturnDate());
    } else {
      tvAcc.setFeeDate(new Date());
    }
    inAccountings.add(tvAcc);

    // 应收---宽带费
    Accounting netAcc = new Accounting();
    netAcc.setRentContract(rentContract);
    netAcc.setAccountingType(accountingType);
    netAcc.setFeeDirection("1");// 1 : 应收
    netAcc.setFeeType("20");// 宽带费金额
    netAcc.setFeeAmount(0D);// 人工计算
    if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
      netAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
      netAcc.setFeeDateStr(rentContract.getReturnDate());
    } else {
      netAcc.setFeeDate(new Date());
    }
    inAccountings.add(netAcc);

    // 应收---服务费
    Accounting servAcc = new Accounting();
    servAcc.setRentContract(rentContract);
    servAcc.setAccountingType(accountingType);
    servAcc.setFeeDirection("1");// 1 : 应收
    servAcc.setFeeType("22");// 服务费金额
    servAcc.setFeeAmount(0D);// 人工计算
    if ("1".equals(rentContract.getIsSpecial())) {// 如果是特殊退租，把人为设定的退租日期作为核算记录的核算时间
      servAcc.setFeeDate(DateUtils.parseDate(rentContract.getReturnDate()));
      servAcc.setFeeDateStr(rentContract.getReturnDate());
    } else {
      servAcc.setFeeDate(new Date());
    }
    inAccountings.add(servAcc);
    return inAccountings;
  }

}
