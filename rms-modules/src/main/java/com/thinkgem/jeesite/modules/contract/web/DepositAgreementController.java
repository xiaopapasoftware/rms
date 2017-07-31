package com.thinkgem.jeesite.modules.contract.web;

import java.util.ArrayList;
import java.util.Date;
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
import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.entity.Message;
import com.thinkgem.jeesite.modules.app.service.MessageService;
import com.thinkgem.jeesite.modules.common.enums.DataSourceEnum;
import com.thinkgem.jeesite.modules.common.enums.ValidatorFlagEnum;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.DepositAgreement;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.enums.AgreementAuditStatusEnum;
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
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrade;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
import com.thinkgem.jeesite.modules.funds.service.PaymentTradeService;
import com.thinkgem.jeesite.modules.funds.service.PaymentTransService;
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
import com.thinkgem.jeesite.modules.person.entity.Partner;
import com.thinkgem.jeesite.modules.person.entity.Tenant;
import com.thinkgem.jeesite.modules.person.service.PartnerService;
import com.thinkgem.jeesite.modules.person.service.TenantService;
import com.thinkgem.jeesite.modules.utils.UserUtils;

/**
 * @author wangshujin
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
  private RoomService roomService;
  @Autowired
  private TenantService tenantService;
  @Autowired
  private PartnerService partnerService;
  @Autowired
  private MessageService messageService;// APP消息推送
  @Autowired
  private PaymentTransService paymentTransService;
  @Autowired
  private PaymentTradeService paymentTradeService;

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
  @RequestMapping(value = {""})
  public String listNoQuery(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {
    initQueryCondition(model, depositAgreement);
    return "modules/contract/depositAgreementList";
  }

  // @RequiresPermissions("contract:depositAgreement:view")
  @RequestMapping(value = {"list"})
  public String listQuery(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model) {
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
    initQueryCondition(model, depositAgreement);
    return "modules/contract/depositAgreementList";
  }

  private void initQueryCondition(Model model, DepositAgreement depositAgreement) {
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
      List<Room> roomList = roomService.findList(room);
      model.addAttribute("roomList", roomList);
    }
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
      roomList = roomService.findList(room);
      if (null != depositAgreement.getRoom()) {
        Room rm = roomService.get(depositAgreement.getRoom());
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
    if (result == -3) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "系统繁忙，请稍后再试！");
    } else if (result == -2) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "出租合同结束日期不能晚于承租合同截止日期，请重试！");
    } else if (result == -1) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "房源已出租，保存失败，请重试！");
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
      addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "保存定金协议成功！");
    }
    return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
  }

  @RequestMapping(value = "audit")
  public String audit(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
    depositAgreementService.audit(auditHis);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "操作成功！");
    return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
  }

  @RequestMapping(value = "cancel")
  public String cancel(AuditHis auditHis, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
    auditHis.setAuditStatus(AuditStatusEnum.REFUSE.getValue());
    auditHis.setUpdateUser(UserUtils.getUser().getId());
    depositAgreementService.audit(auditHis);
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "操作成功！");
    return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
  }

  /**
   * 定金转违约，各分别生成一笔应出定金，一笔定金违约金，都是已经到账的。 如果有退费再生成退费款项。
   */
  @RequestMapping(value = "breakContract")
  public String breakContract(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
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
    double depositAmount = depositAgreement.getDepositAmount();
    // 生成完全到账的应出定金款项
    paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.DEPOSIT_TO_BREAK.getValue(), PaymentTransTypeEnum.OUT_DEPOSIT_AMOUNT.getValue(), agreementId, TradeDirectionEnum.OUT.getValue(),
        depositAmount, 0D, depositAmount, PaymentTransStatusEnum.WHOLE_SIGN.getValue(), startDate, expireDate, null);
    // 生成完全到账的应收定金违约金款项,默认为定金金额
    paymentTransService.generateAndSavePaymentTrans(TradeTypeEnum.DEPOSIT_TO_BREAK.getValue(), PaymentTransTypeEnum.LIQUIDATED_DEPOSIT.getValue(), agreementId, TradeDirectionEnum.IN.getValue(),
        depositAmount, 0D, depositAmount, PaymentTransStatusEnum.WHOLE_SIGN.getValue(), startDate, expireDate, null);
    // 更新定金协议业务状态
    if (null != refundAmount && refundAmount > 0) {
      depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.CONVERTBREAK_TO_SIGN.getValue());
    } else {
      depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.BE_CONVERTED_BREAK.getValue());
      if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(depositAgreement.getRentMode())) {
        House house = houseService.get(depositAgreement.getHouse().getId());
        houseService.releaseWholeHouse(house);
      } else {
        Room room = roomService.get(depositAgreement.getRoom().getId());
        houseService.releaseSingleRoom(room);
      }
    }
    depositAgreementService.update(depositAgreement);
    if (refundAmount != null && refundAmount > 0) {
      addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "定金转违约成功，请进行到账登记操作！");
    } else {
      addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "定金转违约成功！");
    }
    model.addAttribute("depositAgreement", new DepositAgreement());
    return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
  }

  /**
   * 撤销定金转违约 可撤销的定金协议状态为：已转违约、 定金转违约到账待登记、定金转违约到账待审核、定金转违约到账审核拒绝 4种
   */
  @RequestMapping(value = "backDoorRevokeBreak")
  public String backDoorRevokeBreak(DepositAgreement depositAgreement, HttpServletRequest request, HttpServletResponse response, Model model, RedirectAttributes redirectAttributes) {
    String agreementId = depositAgreement.getId();
    String agreementBusiStatus = depositAgreement.getAgreementBusiStatus();
    if (AgreementAuditStatusEnum.INVOICE_AUDITED_PASS.getValue().equals(depositAgreement.getAgreementStatus())) {
      if (AgreementBusiStatusEnum.BE_CONVERTED_BREAK.getValue().equals(agreementBusiStatus)) {
        if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(depositAgreement.getRentMode())) {
          House house = houseService.get(depositAgreement.getHouse().getId());
          if (!HouseStatusEnum.RENT_FOR_RESERVE.getValue().equals(house.getHouseStatus())) { // 房源已被租，不能被撤销
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "该房源已被出租，不能撤回！");
            return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
          }
        } else {
          Room r = roomService.get(depositAgreement.getRoom().getId());
          if (!RoomStatusEnum.RENT_FOR_RESERVE.getValue().equals(r.getRoomStatus())) { // 房源已被租，不能被撤销
            addMessage(redirectAttributes, ViewMessageTypeEnum.ERROR, "该房源已被出租，不能撤回！");
            return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
          }
        }
        doDeleteRefundFeeTrans(agreementId);
        if (RentModelTypeEnum.WHOLE_RENT.getValue().equals(depositAgreement.getRentMode())) {// 整租
          String houseId = depositAgreement.getHouse().getId();
          boolean isLock = houseService.isLockWholeHouse4Deposit(houseId);
          if (isLock) {
            roomService.depositAllRooms(houseId);
          }
        } else {// 合租
          String roomId = depositAgreement.getRoom().getId();
          boolean isLock = roomService.isLockSingleRoom4Deposit(roomId);
          if (isLock) {
            houseService.calculateHouseStatus(roomId);
          }
        }
      } else {
        if (AgreementBusiStatusEnum.CONVERTBREAK_TO_SIGN.getValue().equals(agreementBusiStatus)) {
          PaymentTrans pt = new PaymentTrans();
          pt.setTransId(agreementId);
          pt.setPaymentType(PaymentTransTypeEnum.DEPOSIT_REFUND_FEE.getValue());
          paymentTransService.delete(pt);
        }
        if (AgreementBusiStatusEnum.CONVERTBREAK_TO_AUDIT.getValue().equals(agreementBusiStatus)) {
          doDeleteRefundFeeTrans(agreementId);
        }
        if (AgreementBusiStatusEnum.CONVERTBREAK_AUDIT_REFUSE.getValue().equals(agreementBusiStatus)) {
          PaymentTrans pt = new PaymentTrans();
          pt.setTransId(agreementId);
          pt.setPaymentType(PaymentTransTypeEnum.DEPOSIT_REFUND_FEE.getValue());
          List<PaymentTrans> targetPTs = paymentTransService.findList(pt);
          if (CollectionUtils.isNotEmpty(targetPTs)) {
            PaymentTrade ptrade = new PaymentTrade();
            ptrade.setTransId(targetPTs.get(0).getId());
            paymentTradeService.delete(ptrade);
            paymentTransService.delete(pt);
          }
        }
      }
      // 删除生成的完全到账的应出定金款项、完全到账的应收定金违约金款项、定金转违约退费款项（如果有的话）
      List<String> tradeTypeList = new ArrayList<String>();
      tradeTypeList.add(TradeTypeEnum.DEPOSIT_TO_BREAK.getValue());
      List<String> paymentTypeList = new ArrayList<String>();
      paymentTypeList.add(PaymentTransTypeEnum.OUT_DEPOSIT_AMOUNT.getValue());
      paymentTypeList.add(PaymentTransTypeEnum.LIQUIDATED_DEPOSIT.getValue());
      paymentTransService.deleteTransList(agreementId, tradeTypeList, paymentTypeList);
      depositAgreement.setAgreementBusiStatus(AgreementBusiStatusEnum.TOBE_CONVERTED.getValue());
      depositAgreementService.update(depositAgreement);
      addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "该定金协议已成功恢复为待转合同状态！");
    } else {
      addMessage(redirectAttributes, ViewMessageTypeEnum.WARNING, "该定金协议的审核状态非法！");
    }
    return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
  }

  /**
   * 删除定金转违约退费款项及其账务记录，账务款项关联记录等
   */
  private void doDeleteRefundFeeTrans(String agreementId) {
    PaymentTrans pt = new PaymentTrans();
    pt.setTransId(agreementId);
    pt.setPaymentType(PaymentTransTypeEnum.DEPOSIT_REFUND_FEE.getValue());
    List<PaymentTrans> targetPTs = paymentTransService.findList(pt);
    if (CollectionUtils.isNotEmpty(targetPTs)) {
      PaymentTrade ptrade = new PaymentTrade();
      ptrade.setTransId(targetPTs.get(0).getId());
      List<String> tradeIdList = new ArrayList<String>();
      tradeIdList.add(paymentTradeService.findList(ptrade).get(0).getTradeId());
      paymentTransService.deleteAttachReceiptTradingAccounts(tradeIdList);
      paymentTransService.delete(pt);
    }
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
      List<Room> roomList = roomService.findList(room);
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
    addMessage(redirectAttributes, ViewMessageTypeEnum.SUCCESS, "删除定金协议成功！");
    return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
  }

}
