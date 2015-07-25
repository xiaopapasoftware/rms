/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.LeaseContract;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.LeaseContractService;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans;
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
 * 出租合同Controller
 * 
 * @author huangsc
 * @version 2015-06-11
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
	private LeaseContractService leaseContractService;

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

	@RequiresPermissions("contract:rentContract:view")
	@RequestMapping(value = {"list", ""})
	public String list(RentContract rentContract, HttpServletRequest request, HttpServletResponse response, Model model) {
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

		return "modules/contract/rentContractList";
	}

	@RequestMapping(value = {"rentContractDialogList"})
	public String rentContractDialogList(RentContract rentContract, HttpServletRequest request,
			HttpServletResponse response, Model model) {
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
	public String rentContractDialog(RentContract rentContract, HttpServletRequest request,
			HttpServletResponse response, Model model) {
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

	@RequiresPermissions("contract:rentContract:view")
	@RequestMapping(value = "form")
	public String form(RentContract rentContract, Model model) {
		rentContract.setSignType("0");// 新签
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
			if ("0".equals(rentContract.getRentMode()) && null != rentContract.getHouse())
				houseList.add(houseService.get(rentContract.getHouse()));
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
				if (null != rm)
					roomList.add(rm);
			}
			model.addAttribute("roomList", roomList);
		}

		List<Tenant> tenantList = tenantService.findList(new Tenant());
		model.addAttribute("tenantList", tenantList);

		return "modules/contract/rentContractForm";
	}

	@RequestMapping(value = "renewContract")
	public String renewContract(RentContract rentContract, Model model) {
		String contractId = rentContract.getId();
		rentContract = rentContractService.get(contractId);
		rentContract.setContractId(contractId);
		rentContract.setSignType("1");// 正常续签
		rentContract.setContractName(rentContract.getContractName().concat("(续)"));
		rentContract.setDepositElectricAmount(null);
		rentContract.setDepositAmount(null);
		rentContract.setRental(null);
		rentContract.setRenMonths(null);
		rentContract.setDepositMonths(null);
		rentContract.setStartDate(null);
		rentContract.setExpiredDate(null);
		rentContract.setSignDate(null);

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
			if ("0".equals(rentContract.getRentMode()) && null != rentContract.getHouse())
				houseList.add(houseService.get(rentContract.getHouse()));
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
				if (null != rm)
					roomList.add(rm);
			}
			model.addAttribute("roomList", roomList);
		}

		List<Tenant> tenantList = tenantService.findList(new Tenant());
		model.addAttribute("tenantList", tenantList);

		model.addAttribute("renew", "1");

		model.addAttribute("partnerList", partnerService.findList(new Partner()));
		rentContract.setId(null);
		model.addAttribute("rentContract", rentContract);
		return "modules/contract/rentContractForm";
	}

	@RequestMapping(value = "autoRenewContract")
	public String autoRenewContract(RentContract rentContract, Model model) {
		String contractId = rentContract.getId();
		rentContract = rentContractService.get(contractId);
		rentContract.setContractId(contractId);
		rentContract.setSignType("2");// 逾租续签
		rentContract.setContractName(rentContract.getContractName().concat("(续签)"));
		rentContract.setStartDate(null);
		rentContract.setExpiredDate(null);
		rentContract.setSignDate(null);
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
			if ("0".equals(rentContract.getRentMode()) && null != rentContract.getHouse())
				houseList.add(houseService.get(rentContract.getHouse()));
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
				if (null != rm)
					roomList.add(rm);
			}
			model.addAttribute("roomList", roomList);
		}

		List<Tenant> tenantList = tenantService.findList(new Tenant());
		model.addAttribute("tenantList", tenantList);

		model.addAttribute("partnerList", partnerService.findList(new Partner()));

		rentContract.setId(null);
		model.addAttribute("rentContract", rentContract);
		return "modules/contract/rentContractForm";
	}

	@RequiresPermissions("contract:rentContract:edit")
	@RequestMapping(value = "save")
	public String save(RentContract rentContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rentContract) && "1".equals(rentContract.getValidatorFlag())) {
			return form(rentContract, model);
		}

		/* 出租合同的结束时间不能超过承租合同的结束时间 */
		boolean check = true;
		LeaseContract leaseContract = new LeaseContract();
		leaseContract.setHouse(rentContract.getHouse());
		List<LeaseContract> list = leaseContractService.findList(leaseContract);
		if (null != list && list.size() > 0) {
			leaseContract = list.get(0);
			if (leaseContract.getExpiredDate().before(rentContract.getExpiredDate())) {
				model.addAttribute("message", "出租合同结束日期不能晚于承租合同截止日期.");
				model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
			}
		}
		if (check) {
			rentContractService.save(rentContract);
			addMessage(redirectAttributes, "保存出租合同成功");
		}
		if ("1".equals(rentContract.getSaveSource()))
			return "redirect:" + Global.getAdminPath() + "/contract/depositAgreement/?repage";
		else
			return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
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

	@RequestMapping(value = "returnContract")
	public String returnContract(RentContract rentContract, RedirectAttributes redirectAttributes) {
		/* 检查款项是否都入账 */
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setTransId(rentContract.getId());
		paymentTrans.setTransStatus("0");// 未到账登记
		paymentTrans.setDelFlag("0");
		List<PaymentTrans> list = paymentTransService.findList(paymentTrans);
		if (null != list && list.size() > 0) {
			addMessage(redirectAttributes, "有款项未到账,不能正常退租.");
			return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
		}
		rentContractService.returnContract(rentContract);
		addMessage(redirectAttributes, "正常退租成功");
		return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
	}

	@RequestMapping(value = "earlyReturnContract")
	public String earlyReturnContract(RentContract rentContract, RedirectAttributes redirectAttributes) {
		rentContractService.earylReturnContract(rentContract);
		addMessage(redirectAttributes, "提前退租成功");
		return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
	}

	@RequestMapping(value = "lateReturnContract")
	public String lateReturnContract(RentContract rentContract, RedirectAttributes redirectAttributes) {
		/* 检查款项是否都入账 */
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setTransId(rentContract.getId());
		paymentTrans.setTransStatus("0");// 未到账登记
		paymentTrans.setDelFlag("0");
		List<PaymentTrans> list = paymentTransService.findList(paymentTrans);
		if (null != list && list.size() > 0) {
			addMessage(redirectAttributes, "有款项未到账,不能逾期退租.");
			return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
		}

		rentContractService.lateReturnContract(rentContract);
		addMessage(redirectAttributes, "逾期退租成功");
		return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
	}

	@RequestMapping(value = "specialReturnContract")
	public String specialReturnContract(RentContract rentContract, Model model, RedirectAttributes redirectAttributes) {
		// rentContractService.lateReturnContract(rentContract);
		// addMessage(redirectAttributes, "特殊退租成功");
		rentContract.setIsSpecial("1");
		return toSpecialReturnCheck(rentContract, model);
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

	@RequestMapping(value = "toReturnCheck")
	public String toReturnCheck(RentContract rentContract, Model model) {
		rentContract = rentContractService.get(rentContract.getId());

		List<Accounting> outAccountList = new ArrayList<Accounting>();
		Accounting accounting = new Accounting();
		accounting.setFeeType("2");// 水电费押金
		accounting.setFeeAmount(rentContract.getDepositElectricAmount());
		outAccountList.add(accounting);
		accounting = new Accounting();
		accounting.setFeeType("4");// 房租押金
		accounting.setFeeAmount(rentContract.getDepositAmount());
		outAccountList.add(accounting);

		model.addAttribute("outAccountList", outAccountList);
		model.addAttribute("outAccountSize", outAccountList.size());
		model.addAttribute("accountSize", 0);
		rentContract.setTradeType("7");// 正常退租
		model.addAttribute("rentContract", rentContract);
		return "modules/contract/rentContractCheck";
	}

	@RequestMapping(value = "toEarlyReturnCheck")
	public String toEarlyReturnCheck(RentContract rentContract, Model model) {
		rentContract = rentContractService.get(rentContract.getId());

		// 应出核算项列表
		List<Accounting> outAccountList = genOutAccountList4PreBack(rentContract);

		// 应收核算项列表
		List<Accounting> inAccountList = genInAccountList4PreBack(rentContract);

		// double dates =
		// DateUtils.getDistanceOfTwoDate(rentContract.getStartDate(), new
		// Date());// 入住天数
		// double dailyRental = rentContract.getRental() * 12 / 365;// 每天房租租金
		// double tental = dates * dailyRental;
		// BigDecimal bigDecimal = new BigDecimal(tental);
		// tental = bigDecimal.setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();

		/* 已缴房租 */
		// double totalFee = 0;
		// PaymentTrans paymentTrans = new PaymentTrans();
		// paymentTrans.setTransId(rentContract.getId());
		// paymentTrans.setPaymentType("6");// 房租金额
		// paymentTrans.setTransStatus("2");// 完全到账登记
		// paymentTrans.setDelFlag("0");
		// List<PaymentTrans> list = paymentTransService.findList(paymentTrans);
		// for (PaymentTrans tmpPaymentTrans : list) {
		// totalFee += tmpPaymentTrans.getTransAmount();
		// }

		// double surplus = totalFee - tental;// 剩余房租
		// bigDecimal = new BigDecimal(surplus);
		// surplus = bigDecimal.setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
		// accounting = new Accounting();
		// accounting.setFeeType("7");// 提前应退房租
		// accounting.setFeeAmount(surplus);
		// outAccountList.add(accounting);

		// if ("0".equals(rentContract.getChargeType())) {// 预付

		// if (null != rentContract.getTvFee()) {
		// double dailyTvFee = rentContract.getTvFee() * 12 / 365;// 每天电视费
		// double tvfee = dates * dailyTvFee;
		// bigDecimal = new BigDecimal(tvfee);
		// tvfee = bigDecimal.setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
		//
		// /* 已缴电视费 */
		// totalFee = 0;
		// paymentTrans = new PaymentTrans();
		// paymentTrans.setTransId(rentContract.getId());
		// paymentTrans.setPaymentType("18");// 有线电视费
		// paymentTrans.setTransStatus("2");// 完全到账登记
		// paymentTrans.setDelFlag("0");
		// list = paymentTransService.findList(paymentTrans);
		// for (PaymentTrans tmpPaymentTrans : list) {
		// totalFee += tmpPaymentTrans.getTransAmount();
		// }
		//
		// double surplusTvFee = totalFee - tvfee;
		// bigDecimal = new BigDecimal(surplusTvFee);
		// surplusTvFee = bigDecimal.setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
		// accounting = new Accounting();
		// accounting.setFeeType("19");// 有线电视费剩余金额
		// accounting.setFeeAmount(surplusTvFee);
		// outAccountList.add(accounting);
		// }

		// if (null != rentContract.getNetFee()) {
		// double dailyNetFee = rentContract.getNetFee() * 12 / 365;// 每天宽带费
		// double netfee = dates * dailyNetFee;
		// bigDecimal = new BigDecimal(netfee);
		// netfee = bigDecimal.setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
		//
		// /* 已缴宽带费 */
		// totalFee = 0;
		// paymentTrans = new PaymentTrans();
		// paymentTrans.setTransId(rentContract.getId());
		// paymentTrans.setPaymentType("20");// 宽带费
		// paymentTrans.setTransStatus("2");// 完全到账登记
		// paymentTrans.setDelFlag("0");
		// list = paymentTransService.findList(paymentTrans);
		// for (PaymentTrans tmpPaymentTrans : list) {
		// totalFee += tmpPaymentTrans.getTransAmount();
		// }
		//
		// double surplusNetFee = totalFee - netfee;
		// bigDecimal = new BigDecimal(surplusNetFee);
		// surplusNetFee = bigDecimal.setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
		// accounting = new Accounting();
		// accounting.setFeeType("21");// 宽带费剩余金额
		// accounting.setFeeAmount(surplusNetFee);
		// outAccountList.add(accounting);
		// }

		// if (null != rentContract.getServiceFee()) {
		// double dailyServiceFee = rentContract.getServiceFee() * 12 /
		// 365;// 每天服务费
		// double serviceFee = dates * dailyServiceFee;
		// bigDecimal = new BigDecimal(serviceFee);
		// serviceFee = bigDecimal.setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
		// /* 已缴服务费 */
		// totalFee = 0;
		// paymentTrans = new PaymentTrans();
		// paymentTrans.setTransId(rentContract.getId());
		// paymentTrans.setPaymentType("22");// 服务费
		// paymentTrans.setTransStatus("2");// 完全到账登记
		// paymentTrans.setDelFlag("0");
		// list = paymentTransService.findList(paymentTrans);
		// for (PaymentTrans tmpPaymentTrans : list) {
		// totalFee += tmpPaymentTrans.getTransAmount();
		// }
		// double surplusServiceFee = totalFee - serviceFee;
		// bigDecimal = new BigDecimal(surplusServiceFee);
		// surplusServiceFee = bigDecimal.setScale(2,
		// BigDecimal.ROUND_HALF_UP).doubleValue();
		// accounting = new Accounting();
		// accounting.setFeeType("23");// 服务费剩余金额
		// accounting.setFeeAmount(surplusServiceFee);
		// outAccountList.add(accounting);
		// }
		// }

		model.addAttribute("returnRental", "1");// 显示计算公式的标识
		model.addAttribute("totalFee", 100);
		model.addAttribute("rental", rentContract.getRental());
		model.addAttribute("dates", 1);

		model.addAttribute("accountList", inAccountList);
		model.addAttribute("accountSize", inAccountList.size());

		model.addAttribute("outAccountList", outAccountList);
		model.addAttribute("outAccountSize", outAccountList.size());

		rentContract.setTradeType("6");// 提前退租

		model.addAttribute("rentContract", rentContract);

		return "modules/contract/rentContractCheck";
	}

	@RequestMapping(value = "toLateReturnCheck")
	public String toLateReturnCheck(RentContract rentContract, Model model) {
		rentContract = rentContractService.get(rentContract.getId());

		List<Accounting> accountList = new ArrayList<Accounting>();

		List<Accounting> outAccountList = new ArrayList<Accounting>();
		Accounting accounting = new Accounting();
		accounting.setFeeType("2");// 水电费押金
		accounting.setFeeAmount(rentContract.getDepositElectricAmount());
		outAccountList.add(accounting);

		double dates = DateUtils.getDistanceOfTwoDate(rentContract.getExpiredDate(), new Date());// 逾期天数

		double dailyRental = rentContract.getRental() * 12 / 365;// 每天房租租金
		double tental = (dates < 0 ? 0 : dates) * dailyRental;
		BigDecimal bigDecimal = new BigDecimal(tental);
		tental = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		accounting = new Accounting();
		accounting.setFeeType("8");// 逾赔房租
		accounting.setFeeAmount(tental);
		if (tental > 0)
			accountList.add(accounting);

		model.addAttribute("outAccountList", outAccountList);
		model.addAttribute("outAccountSize", outAccountList.size());
		model.addAttribute("accountList", accountList);
		model.addAttribute("accountSize", accountList.size());
		rentContract.setTradeType("8");// 逾期退租
		model.addAttribute("rentContract", rentContract);
		return "modules/contract/rentContractCheck";
	}

	@RequestMapping(value = "toSpecialReturnCheck")
	public String toSpecialReturnCheck(RentContract rentContract, Model model) {
		String isSpecial = rentContract.getIsSpecial();
		rentContract = rentContractService.get(rentContract.getId());
		rentContract.setIsSpecial(isSpecial);

		List<Accounting> accountList = new ArrayList<Accounting>();

		List<Accounting> outAccountList = new ArrayList<Accounting>();
		Accounting accounting = new Accounting();
		accounting.setFeeType("2");// 水电费押金
		accounting.setFeeAmount(rentContract.getDepositElectricAmount());
		outAccountList.add(accounting);

		accounting = new Accounting();
		accounting.setFeeType("4");// 房租押金
		accounting.setFeeAmount(rentContract.getDepositAmount());
		outAccountList.add(accounting);

		model.addAttribute("outAccountList", outAccountList);
		model.addAttribute("outAccountSize", outAccountList.size());
		model.addAttribute("accountList", accountList);
		model.addAttribute("accountSize", accountList.size());
		rentContract.setTradeType("9");// 特殊退租
		model.addAttribute("rentContract", rentContract);
		return "modules/contract/rentContractCheck";
	}

	@RequestMapping(value = "returnCheck")
	public String returnCheck(RentContract rentContract, RedirectAttributes redirectAttributes) {
		rentContractService.returnCheck(rentContract, rentContract.getTradeType());
		if (!StringUtils.isBlank(rentContract.getIsSpecial()))
			addMessage(redirectAttributes, "特殊退租成功");
		else
			addMessage(redirectAttributes, "退租核算成功");
		return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
	}

	@RequiresPermissions("contract:rentContract:edit")
	@RequestMapping(value = "delete")
	public String delete(RentContract rentContract, RedirectAttributes redirectAttributes) {
		rentContractService.delete(rentContract);
		addMessage(redirectAttributes, "删除出租合同成功");
		return "redirect:" + Global.getAdminPath() + "/contract/rentContract/?repage";
	}

	/**
	 * 提前退租------------应出退租核算项列表
	 * */
	private List<Accounting> genOutAccountList4PreBack(RentContract rentContract) {

		List<Accounting> outAccountings = new ArrayList<Accounting>();

		// 水电费押金
		Accounting eaccounting = new Accounting();
		eaccounting.setRentContract(rentContract);
		eaccounting.setAccountingType("0");// '0':'提前退租核算
		eaccounting.setFeeDirection("0");// 0 : 应出
		eaccounting.setFeeType("2");// 水电费押金
		eaccounting.setFeeAmount(rentContract.getDepositElectricAmount());
		outAccountings.add(eaccounting);

		// 房租押金
		Accounting accounting = new Accounting();
		accounting.setRentContract(rentContract);
		accounting.setAccountingType("0");// '0':'提前退租核算
		accounting.setFeeDirection("0");// 0 : 应出
		accounting.setFeeType("4");// 房租押金
		accounting.setFeeAmount(rentContract.getDepositAmount());
		outAccountings.add(accounting);

		// 提前应退房租金额
		Accounting preBackRentalAcc = new Accounting();
		preBackRentalAcc.setRentContract(rentContract);
		preBackRentalAcc.setAccountingType("0");// '0':'提前退租核算
		preBackRentalAcc.setFeeDirection("0");// 0 : 应出
		preBackRentalAcc.setFeeType("7");// 提前应退房租
		preBackRentalAcc.setFeeAmount(calculatePreBackRental(rentContract));
		outAccountings.add(preBackRentalAcc);

		// 预充---应退 智能电表剩余电费
		Accounting elctrBackAcc = new Accounting();
		elctrBackAcc.setRentContract(rentContract);
		elctrBackAcc.setAccountingType("0");// '0':'提前退租核算
		elctrBackAcc.setFeeDirection("0");// 0 : 应出
		elctrBackAcc.setFeeType("13");// 智能电表剩余电费
		elctrBackAcc.setFeeAmount(0D);// TODO
		outAccountings.add(elctrBackAcc);

		// 预付 ---应退 水费剩余金额
		Accounting waterAcc = new Accounting();
		waterAcc.setRentContract(rentContract);
		waterAcc.setAccountingType("0");// '0':'提前退租核算
		waterAcc.setFeeDirection("0");// 0 : 应出
		waterAcc.setFeeType("15");// 水费剩余金额
		waterAcc.setFeeAmount(0D);// TODO
		outAccountings.add(waterAcc);

		// 预付 ---应退 电视费
		Accounting tvAcc = new Accounting();
		tvAcc.setRentContract(rentContract);
		tvAcc.setAccountingType("0");// '0':'提前退租核算
		tvAcc.setFeeDirection("0");// 0 : 应出
		tvAcc.setFeeType("19");// 有线电视费剩余金额
		tvAcc.setFeeAmount(0D);// TODO
		outAccountings.add(tvAcc);

		// 预付 ---应退 宽带费
		Accounting netAcc = new Accounting();
		netAcc.setRentContract(rentContract);
		netAcc.setAccountingType("0");// '0':'提前退租核算
		netAcc.setFeeDirection("0");// 0 : 应出
		netAcc.setFeeType("21");// 宽带费剩余金额
		netAcc.setFeeAmount(0D);// TODO
		outAccountings.add(netAcc);

		// 预付 ---应退 燃气费
		Accounting hotAirAcc = new Accounting();
		hotAirAcc.setRentContract(rentContract);
		hotAirAcc.setAccountingType("0");// '0':'提前退租核算
		hotAirAcc.setFeeDirection("0");// 0 : 应出
		hotAirAcc.setFeeType("17");// 燃气费剩余金额
		hotAirAcc.setFeeAmount(0D);// TODO
		outAccountings.add(hotAirAcc);

		// 预付 ---应退 服务费
		Accounting servAcc = new Accounting();
		servAcc.setRentContract(rentContract);
		servAcc.setAccountingType("0");// '0':'提前退租核算
		servAcc.setFeeDirection("0");// 0 : 应出
		servAcc.setFeeType("23");// 服务费剩余金额
		servAcc.setFeeAmount(0D);// TODO
		outAccountings.add(servAcc);

		return outAccountings;
	}

	/**
	 * 计算提前应退房租金额
	 * */
	private Double calculatePreBackRental(RentContract rentContract) {
		Double preBackRental = 0d;
		// TODO
		return preBackRental;
	}

	/**
	 * 提前退租------------应收退租核算项列表
	 * */
	private List<Accounting> genInAccountList4PreBack(RentContract rentContract) {
		List<Accounting> inAccountings = new ArrayList<Accounting>();

		// 应收---早退违约金
		Accounting earlyDepositAcc = new Accounting();
		earlyDepositAcc.setRentContract(rentContract);
		earlyDepositAcc.setAccountingType("0");// '0':'提前退租核算
		earlyDepositAcc.setFeeDirection("1");// 1 : 应收
		earlyDepositAcc.setFeeType("9");// 早退违约金
		earlyDepositAcc.setFeeAmount(rentContract.getDepositAmount());
		inAccountings.add(earlyDepositAcc);

		// 应收---损坏赔偿金
		Accounting pay4BrokeAcc = new Accounting();
		pay4BrokeAcc.setRentContract(rentContract);
		pay4BrokeAcc.setAccountingType("0");// '0':'提前退租核算
		pay4BrokeAcc.setFeeDirection("1");// 1 : 应收
		pay4BrokeAcc.setFeeType("10");// 损坏赔偿金
		pay4BrokeAcc.setFeeAmount(0D);
		inAccountings.add(pay4BrokeAcc);

		// 应收---退租补偿税金
		Accounting backSuppAcc = new Accounting();
		backSuppAcc.setRentContract(rentContract);
		backSuppAcc.setAccountingType("0");// '0':'提前退租核算
		backSuppAcc.setFeeDirection("1");// 1 : 应收
		backSuppAcc.setFeeType("24");// 损坏赔偿金
		backSuppAcc.setFeeAmount(0D);
		inAccountings.add(backSuppAcc);

		// 应收---电费自用金额
		Accounting elSelAcc = new Accounting();
		elSelAcc.setRentContract(rentContract);
		elSelAcc.setAccountingType("0");// '0':'提前退租核算
		elSelAcc.setFeeDirection("1");// 1 : 应收
		elSelAcc.setFeeType("11");// 电费自用金额
		elSelAcc.setFeeAmount(0D);// 人工计算
		inAccountings.add(elSelAcc);

		// 应收---电费分摊金额
		Accounting elCommAcc = new Accounting();
		elCommAcc.setRentContract(rentContract);
		elCommAcc.setAccountingType("0");// '0':'提前退租核算
		elCommAcc.setFeeDirection("1");// 1 : 应收
		elCommAcc.setFeeType("12");// 电费分摊金额
		elCommAcc.setFeeAmount(0D);// 人工计算
		inAccountings.add(elCommAcc);

		// 应收---水费金额
		Accounting waterSelAcc = new Accounting();
		waterSelAcc.setRentContract(rentContract);
		waterSelAcc.setAccountingType("0");// '0':'提前退租核算
		waterSelAcc.setFeeDirection("1");// 1 : 应收
		waterSelAcc.setFeeType("14");// 水费金额
		waterSelAcc.setFeeAmount(0D);// 人工计算
		inAccountings.add(waterSelAcc);

		// 应收---燃气金额
		Accounting hotAirAcc = new Accounting();
		hotAirAcc.setRentContract(rentContract);
		hotAirAcc.setAccountingType("0");// '0':'提前退租核算
		hotAirAcc.setFeeDirection("1");// 1 : 应收
		hotAirAcc.setFeeType("16");// 燃气金额
		hotAirAcc.setFeeAmount(0D);// 人工计算
		inAccountings.add(hotAirAcc);

		// 应收---有线电视费
		Accounting tvAcc = new Accounting();
		tvAcc.setRentContract(rentContract);
		tvAcc.setAccountingType("0");// '0':'提前退租核算
		tvAcc.setFeeDirection("1");// 1 : 应收
		tvAcc.setFeeType("18");// 电视费金额
		tvAcc.setFeeAmount(0D);// 人工计算
		inAccountings.add(tvAcc);

		// 应收---宽带费
		Accounting netAcc = new Accounting();
		netAcc.setRentContract(rentContract);
		netAcc.setAccountingType("0");// '0':'提前退租核算
		netAcc.setFeeDirection("1");// 1 : 应收
		netAcc.setFeeType("20");// 宽带费金额
		netAcc.setFeeAmount(0D);// 人工计算
		inAccountings.add(netAcc);

		// 应收---服务费
		Accounting servAcc = new Accounting();
		servAcc.setRentContract(rentContract);
		servAcc.setAccountingType("0");// '0':'提前退租核算
		servAcc.setFeeDirection("1");// 1 : 应收
		servAcc.setFeeType("22");// 服务费金额
		servAcc.setFeeAmount(0D);// 人工计算
		inAccountings.add(servAcc);

		return inAccountings;
	}
}