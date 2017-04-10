/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.utils.UserUtils;
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
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.service.MessageService;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
import com.thinkgem.jeesite.modules.common.enums.ValidatorFlagEnum;
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.AgreementBusiStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.AuditStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.ContractSignTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransStatusEnum;
import com.thinkgem.jeesite.modules.contract.enums.PaymentTransTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
import com.thinkgem.jeesite.modules.contract.enums.TradeTypeEnum;
import com.thinkgem.jeesite.modules.contract.service.ContractTenantService;
import com.thinkgem.jeesite.modules.contract.service.DepositAgreementService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
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
  private ContractTenantService contractTenantService;
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
  @Autowired
  private MessageService messageService;// APP消息推送
  @Autowired
  private PaymentTransService paymentTransService;

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
  @RequestMapping(value = {"list", ""})
  public String list(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<DepositAgreement> page = depositAgreementService.findPage(new Page<DepositAgreement>(request, response), depositAgreement);
    // 单独设置该定金协议下的承租人姓名和手机号列表
    if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
      for (DepositAgreement da : page.getList()) {
        List<Tenant> ts = contractTenantService.getDepositAgreementTenantList(da.getId());
        if (CollectionUtils.isNotEmpty(ts)) {
          String name = "";
          String cellphone = "";
          for (Tenant tt : ts) {
            if (StringUtils.isEmpty(name)) {
              name = name + tt.getTenantName();
              cellphone = cellphone + tt.getCellPhone();
            } else {
              name = name + "," + tt.getTenantName();
              cellphone = cellphone + "," + tt.getCellPhone();
            }
          }
          da.setTenantName(name);
          da.setCellPhone(cellphone);
        }
      }
    }
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
    if (depositAgreement.getIsNewRecord()) {
      depositAgreement.setAgreementCode((depositAgreementService.getTotalValidDACounts() + 1) + "-" + "XY");
    }
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
      // if ("0".equals(depositAgreement.getRentMode()) && null !=
      // depositAgreement.getHouse())
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
        if (null != rm) {
          roomList.add(rm);
        }
      }
      model.addAttribute("roomList", CollectionUtils.isNotEmpty(roomList) ? roomList : Lists.newArrayList());
    }

    List<Tenant> tenantList = tenantService.findList(new Tenant());
    model.addAttribute("tenantList", tenantList);

    return "modules/contract/depositAgreementForm";
  }

  /**
   * 牵涉到后台定金协议的新增、修改；以及APP端合同的修改保存
   */
  // @RequiresPermissions("contract:depositAgreement:edit")
  @RequestMapping(value = "save")
  public String save(DepositAgreement depositAgreement, Model model, RedirectAttributes redirectAttributes) {
    if (!beanValidator(model, depositAgreement) && ValidatorFlagEnum.SAVE.getValue().equals(depositAgreement.getValidatorFlag())) {
      return form(depositAgreement, model);
    }
    if (depositAgreement.getIsNewRecord()) {// 设置定金协议编号
      String[] codeArr = depositAgreement.getAgreementCode().split("-");
      depositAgreement.setAgreementCode(codeArr[0] + "-" + (depositAgreementService.getTotalValidDACounts() + 1) + "-" + "XY");
    }
    if (StringUtils.isNotEmpty(depositAgreement.getDataSource()) && DataSourceEnum.FRONT_APP.getValue().equals(depositAgreement.getDataSource())) {} else {
      depositAgreement.setDataSource(DataSourceEnum.BACK_SYSTEM.getValue());
    }
    int result = depositAgreementService.saveDepositAgreement(depositAgreement);
    if (result == -2) {
      model.addAttribute("message", "出租合同结束日期不能晚于承租合同截止日期.");
      model.addAttribute("messageType", ViewMessageTypeEnum.WARNING.getValue());
      return "modules/contract/depositAgreementForm";
    } else if (result == -1) {
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
      addMessage(model, "房源已出租！");
      return form(depositAgreement, model);
    } else {
      if (StringUtils.isNotEmpty(depositAgreement.getDataSource()) && DataSourceEnum.FRONT_APP.getValue().equals(depositAgreement.getDataSource())) {// APP订单后台保存
        try {
          Message message = new Message();
          message.setContent("您的预订申请已被管家确认,请联系管家!");
          message.setTitle("预订提醒");
          message.setType("预订提醒");
          Tenant tenant = depositAgreement.getTenantList().get(0);
          message.setReceiver(tenantService.get(tenant).getCellPhone());
          messageService.addMessage(message, true);
        } catch (Exception e) {
          logger.error("预订推送异常:", e);
        }
      }
      addMessage(redirectAttributes, "保存定金协议成功");
      return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
    }
  }

  @RequestMapping(value = "audit")
  public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
    depositAgreementService.audit(auditHis);
    return list(new DepositAgreement(), request, response, model);
  }

  @RequestMapping(value = "cancel")
  public String cancel(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model) {
    auditHis.setAuditStatus(AuditStatusEnum.REFUSE.getValue());
    auditHis.setUpdateUser(UserUtils.getUser().getId());
    depositAgreementService.audit(auditHis);
    return list(new DepositAgreement(), request, response, model);
  }

  /**
   * 定金转违约，各分别生成一笔应出定金，一笔定金违约金，都是已经到账的。 如果有退费再生成退费
   */
  @RequestMapping(value = "breakContract")
  public String breakContract(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {

    Double refundAmount = depositAgreement.getRefundAmount();// 转违约，可能要退一点费用给客户，这里就是退费的金额
    String agreementId = depositAgreement.getId();// 定金协议ID
    depositAgreement = depositAgreementService.get(agreementId);
    Date startDate = depositAgreement.getStartDate();
    Date expireDate = depositAgreement.getExpiredDate();
    // 生成定金转违约退费款项
    if (null != refundAmount && refundAmount > 0) {
      paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.DEPOSIT_TO_BREAK.getValue(), PaymentTransTypeEnum.DEPOSIT_REFUND_FEE.getValue(), agreementId, TradeDirectionEnum.OUT.getValue(),
          refundAmount, refundAmount, 0D, PaymentTransStatusEnum.NO_SIGN.getValue(), startDate, expireDate, null);
    }
    // 生成完全到账的应出定金款项
    paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.DEPOSIT_TO_BREAK.getValue(), PaymentTransTypeEnum.OUT_DEPOSIT_AMOUNT.getValue(), agreementId, TradeDirectionEnum.OUT.getValue(),
        refundAmount, 0D, refundAmount, PaymentTransStatusEnum.WHOLE_SIGN.getValue(), startDate, expireDate, null);

    // 生成完全到账的应收定金违约金款项
    paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.DEPOSIT_TO_BREAK.getValue(), PaymentTransTypeEnum.LIQUIDATED_DEPOSIT.getValue(), agreementId, TradeDirectionEnum.IN.getValue(),
        refundAmount, 0D, refundAmount, PaymentTransStatusEnum.WHOLE_SIGN.getValue(), startDate, expireDate, null);
    // 更新定金协议业务状态
    if (null != refundAmount && refundAmount > 0) {
      depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.CONVERTBREAK_TO_SIGN.getValue());
    } else {
      depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.BE_CONVERTED_BREAK.getValue());
    }
    depositAgreementService.update(depositAgreement);
    // 更新房源状态,需校验房源状态为已预定才可以变更
    if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(depositAgreement.getRentMode())) {// 整租,释放房源
      House house = houseService.get(depositAgreement.getHouse().getId());
      houseService.releaseWholeHouse(house);
    } else {// 单间
      Room room = roomServie.get(depositAgreement.getRoom().getId());
      houseService.releaseSingleRoom(room);
    }
    if (refundAmount != null && refundAmount > 0) {
      model.addAttribute("message", "定金转违约成功，请进行到账登记操作！");
    } else {
      model.addAttribute("message", "定金转违约成功！");
    }
    model.addAttribute("messageType", ViewMessageTypeEnum.SUCCESS.getValue());
    model.addAttribute("depositAgreement", new DepositAgreement());
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
    rentContract.setSignType(ContractSignTypeEnum.NEW_SIGN.getValue());
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
    rentContract.setLiveList(depositAgreement.getTenantList());
    rentContract.setRemarks(depositAgreement.getRemarks());
    rentContract.setAgreementId(depositAgreement.getId());
    rentContract.setContractCode((rentContractService.getAllValidRentContractCounts() + 1) + "-" + "CZ");
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
