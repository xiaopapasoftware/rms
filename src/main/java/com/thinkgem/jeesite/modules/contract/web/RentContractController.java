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
import com.thinkgem.jeesite.modules.contract.entity.Accounting;
import com.thinkgem.jeesite.modules.contract.entity.AgreementChange;
import com.thinkgem.jeesite.modules.contract.entity.AuditHis;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
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
				if(null != rm) roomList.add(rm);
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
		rentContract.setId(null);
		rentContract.setContractId(contractId);
		rentContract.setSignType("1");// 正常续签
		rentContract.setContractName(rentContract.getContractName().concat("(续签)"));
		rentContract.setDepositElectricAmount(null);
		rentContract.setDepositAmount(null);
		rentContract.setRental(null);
		rentContract.setRenMonths(null);
		rentContract.setDepositMonths(null);
		rentContract.setStartDate(null);
		rentContract.setExpiredDate(null);
		rentContract.setSignDate(null);
		model.addAttribute("rentContract", rentContract);

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
				if(null!=rm)roomList.add(rm);
			}
			model.addAttribute("roomList", roomList);
		}

		List<Tenant> tenantList = tenantService.findList(new Tenant());
		model.addAttribute("tenantList", tenantList);
		
		model.addAttribute("renew", "1");

		return "modules/contract/rentContractForm";
	}

	@RequestMapping(value = "autoRenewContract")
	public String autoRenewContract(RentContract rentContract, Model model) {
		String contractId = rentContract.getId();
		rentContract = rentContractService.get(contractId);
		rentContract.setId(null);
		rentContract.setContractId(contractId);
		rentContract.setSignType("2");// 逾租续签
		rentContract.setContractName(rentContract.getContractName().concat("(续签)"));
		rentContract.setStartDate(null);
		rentContract.setExpiredDate(null);
		rentContract.setSignDate(null);
		model.addAttribute("rentContract", rentContract);

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
				if(null!=rm)roomList.add(rm);
			}
			model.addAttribute("roomList", roomList);
		}

		List<Tenant> tenantList = tenantService.findList(new Tenant());
		model.addAttribute("tenantList", tenantList);

		return "modules/contract/rentContractForm";
	}

	@RequiresPermissions("contract:rentContract:edit")
	@RequestMapping(value = "save")
	public String save(RentContract rentContract, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, rentContract) && "1".equals(rentContract.getValidatorFlag())) {
			return form(rentContract, model);
		}
		rentContractService.save(rentContract);
		addMessage(redirectAttributes, "保存出租合同成功");
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

		List<Accounting> outAccountList = new ArrayList<Accounting>();
		Accounting accounting = new Accounting();
		accounting.setFeeType("2");// 水电费押金
		accounting.setFeeAmount(rentContract.getDepositElectricAmount());
		outAccountList.add(accounting);

		double dates = DateUtils.getDistanceOfTwoDate(rentContract.getStartDate(), new Date());// 入住天数

		double dailyRental = rentContract.getRental() * 12 / 365;// 每天房租租金
		double tental = dates * dailyRental;
		BigDecimal bigDecimal = new BigDecimal(tental);
		tental = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		/*已缴房租*/
		double totalFee = 0;
		PaymentTrans paymentTrans = new PaymentTrans();
		paymentTrans.setTransId(rentContract.getId());
		paymentTrans.setPaymentType("6");//房租金额
		paymentTrans.setTransStatus("2");//完全到账登记
		paymentTrans.setDelFlag("0");
		List<PaymentTrans> list = paymentTransService.findList(paymentTrans);
		for(PaymentTrans tmpPaymentTrans : list) {
			totalFee += tmpPaymentTrans.getTransAmount();
		}
		
		double surplus = totalFee - tental;// 剩余房租
		bigDecimal = new BigDecimal(surplus);
		surplus = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		accounting = new Accounting();
		accounting.setFeeType("7");// 提前应退房租
		accounting.setFeeAmount(surplus);
		outAccountList.add(accounting);
		
		model.addAttribute("returnRental", "1");
		model.addAttribute("totalFee", totalFee);
		model.addAttribute("rental", rentContract.getRental());
		model.addAttribute("dates", dates);

		if ("0".equals(rentContract.getChargeType())) {// 预付

			if (null != rentContract.getTvFee()) {
				double dailyTvFee = rentContract.getTvFee() * 12 / 365;// 每天电视费
				double tvfee = dates * dailyTvFee;
				bigDecimal = new BigDecimal(tvfee);
				tvfee = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				/*已缴电视费*/
				totalFee = 0;
				paymentTrans = new PaymentTrans();
				paymentTrans.setTransId(rentContract.getId());
				paymentTrans.setPaymentType("18");//有线电视费
				paymentTrans.setTransStatus("2");//完全到账登记
				paymentTrans.setDelFlag("0");
				list = paymentTransService.findList(paymentTrans);
				for(PaymentTrans tmpPaymentTrans : list) {
					totalFee += tmpPaymentTrans.getTransAmount();
				}
				
				double surplusTvFee = totalFee - tvfee;
				bigDecimal = new BigDecimal(surplusTvFee);
				surplusTvFee = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				accounting = new Accounting();
				accounting.setFeeType("19");// 有线电视费剩余金额
				accounting.setFeeAmount(surplusTvFee);
				outAccountList.add(accounting);
			}

			if (null != rentContract.getNetFee()) {
				double dailyNetFee = rentContract.getNetFee() * 12 / 365;// 每天宽带费
				double netfee = dates * dailyNetFee;
				bigDecimal = new BigDecimal(netfee);
				netfee = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				
				/*已缴宽带费*/
				totalFee = 0;
				paymentTrans = new PaymentTrans();
				paymentTrans.setTransId(rentContract.getId());
				paymentTrans.setPaymentType("20");//宽带费
				paymentTrans.setTransStatus("2");//完全到账登记
				paymentTrans.setDelFlag("0");
				list = paymentTransService.findList(paymentTrans);
				for(PaymentTrans tmpPaymentTrans : list) {
					totalFee += tmpPaymentTrans.getTransAmount();
				}
				
				double surplusNetFee = totalFee - netfee;
				bigDecimal = new BigDecimal(surplusNetFee);
				surplusNetFee = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				accounting = new Accounting();
				accounting.setFeeType("21");// 宽带费剩余金额
				accounting.setFeeAmount(surplusNetFee);
				outAccountList.add(accounting);
			}

			if (null != rentContract.getServiceFee()) {
				double dailyServiceFee = rentContract.getServiceFee() * 12 / 365;// 每天服务费
				double serviceFee = dates * dailyServiceFee;
				bigDecimal = new BigDecimal(serviceFee);
				serviceFee = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				/*已缴服务费*/
				totalFee = 0;
				paymentTrans = new PaymentTrans();
				paymentTrans.setTransId(rentContract.getId());
				paymentTrans.setPaymentType("22");//服务费
				paymentTrans.setTransStatus("2");//完全到账登记
				paymentTrans.setDelFlag("0");
				list = paymentTransService.findList(paymentTrans);
				for(PaymentTrans tmpPaymentTrans : list) {
					totalFee += tmpPaymentTrans.getTransAmount();
				}
				double surplusServiceFee = totalFee - serviceFee;
				bigDecimal = new BigDecimal(surplusServiceFee);
				surplusServiceFee = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				accounting = new Accounting();
				accounting.setFeeType("23");// 服务费剩余金额
				accounting.setFeeAmount(surplusServiceFee);
				outAccountList.add(accounting);
			}
		}

		List<Accounting> accountList = new ArrayList<Accounting>();
		accounting = new Accounting();
		accounting.setFeeType("9");// 早退违约金
		accounting.setFeeAmount(rentContract.getDepositAmount());
		accountList.add(accounting);

		model.addAttribute("accountList", accountList);
		model.addAttribute("accountSize", accountList.size());

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

}