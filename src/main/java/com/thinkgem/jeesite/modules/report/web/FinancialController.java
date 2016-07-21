package com.thinkgem.jeesite.modules.report.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.web.ViewMessageTypeEnum;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.report.entity.IncomeReceivableReport;
import com.thinkgem.jeesite.modules.report.entity.IncomeRefundReport;
import com.thinkgem.jeesite.modules.report.entity.IncomeRentReport;
import com.thinkgem.jeesite.modules.report.entity.IncomeReport;
import com.thinkgem.jeesite.modules.report.entity.LandlordReport;
import com.thinkgem.jeesite.modules.report.entity.PayRentReport;
import com.thinkgem.jeesite.modules.report.entity.PaymentReport;
import com.thinkgem.jeesite.modules.report.service.ReportService;

@Controller
@RequestMapping(value = "${adminPath}/report/financial")
public class FinancialController extends BaseController {
	@Autowired
	private PropertyProjectService propertyProjectService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private ReportService reportService;
	
	@RequestMapping(value = {"landlord"})
	public String landlord(LandlordReport landlordReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LandlordReport> page = reportService.landlordReport(new Page<LandlordReport>(request, response),landlordReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != landlordReport.getPropertyProject()
				&& StringUtils.isNotEmpty(landlordReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(landlordReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != landlordReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(landlordReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/financial/landlordList";
	}
	
	@RequestMapping(value = "exportLandlord", method=RequestMethod.POST)
    public String exportLandlord(LandlordReport landlordReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "房东开票信息统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<LandlordReport> page = reportService.landlordReport(new Page<LandlordReport>(request, response, -1),landlordReport);
    		new ExportExcel("房东开票信息统计", LandlordReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出房东开票信息统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.landlord(landlordReport, request, response, model);
    }
	
	@RequestMapping(value = {"payment"})
	public String payment(PaymentReport paymentReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PaymentReport> page = reportService.paymentReport(new Page<PaymentReport>(request, response),paymentReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != paymentReport.getPropertyProject()
				&& StringUtils.isNotEmpty(paymentReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(paymentReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != paymentReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(paymentReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/financial/paymentList";
	}
	
	@RequestMapping(value = "exportPayment", method=RequestMethod.POST)
    public String exportPayment(PaymentReport paymentReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "应收账款统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PaymentReport> page = reportService.paymentReport(new Page<PaymentReport>(request, response, -1),paymentReport);
    		new ExportExcel("应收账款统计", PaymentReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出应收账款统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.payment(paymentReport, request, response, model);
    }
	
	@RequestMapping(value = {"pay"})
	public String pay(PayRentReport payRentReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PayRentReport> page = reportService.payrentReport(new Page<PayRentReport>(request, response),payRentReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != payRentReport.getPropertyProject()
				&& StringUtils.isNotEmpty(payRentReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(payRentReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != payRentReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(payRentReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/financial/payrentList";
	}
	
	@RequestMapping(value = "exportPay", method=RequestMethod.POST)
    public String exportPay(PayRentReport payRentReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "待付房租统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PayRentReport> page = reportService.payrentReport(new Page<PayRentReport>(request, response, -1),payRentReport);
    		new ExportExcel("待付房租统计", PayRentReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出待付房租统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.pay(payRentReport, request, response, model);
    }
	
	@RequestMapping(value = {"income"})
	public String income(IncomeReport incomeReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<IncomeReport> page = reportService.incomeReport(new Page<IncomeReport>(request, response),incomeReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != incomeReport.getPropertyProject()
				&& StringUtils.isNotEmpty(incomeReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(incomeReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != incomeReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(incomeReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/financial/incomeList";
	}
	
	@RequestMapping(value = "exportIncome", method=RequestMethod.POST)
    public String exportIncome(IncomeReport incomeReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "每月收入统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<IncomeReport> page = reportService.incomeReport(new Page<IncomeReport>(request, response, -1),incomeReport);
    		new ExportExcel("每月收入统计", IncomeReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出每月收入统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.income(incomeReport, request, response, model);
    }
	
	@RequestMapping(value = {"rent"})
	public String rent(IncomeReport incomeReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<IncomeReport> page = reportService.incomeFangzuReport(new Page<IncomeReport>(request, response),incomeReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != incomeReport.getPropertyProject()
				&& StringUtils.isNotEmpty(incomeReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(incomeReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != incomeReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(incomeReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/financial/rentList";
	}
	
	@RequestMapping(value = "exportRent", method=RequestMethod.POST)
    public String exportRent(IncomeReport incomeReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "房租收入统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<IncomeReport> page = reportService.incomeFangzuReport(new Page<IncomeReport>(request, response, -1),incomeReport);
    		new ExportExcel("房租收入统计", IncomeRentReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出房租收入统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.income(incomeReport, request, response, model);
    }
	
	@RequestMapping(value = {"receivable"})
	public String receivable(IncomeReport incomeReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<IncomeReport> page = reportService.receivableReport(new Page<IncomeReport>(request, response),incomeReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != incomeReport.getPropertyProject()
				&& StringUtils.isNotEmpty(incomeReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(incomeReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != incomeReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(incomeReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/financial/receivableList";
	}
	
	@RequestMapping(value = "exportReceivable", method=RequestMethod.POST)
    public String exportReceivable(IncomeReport incomeReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "应收款统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<IncomeReport> page = reportService.receivableReport(new Page<IncomeReport>(request, response, -1),incomeReport);
    		new ExportExcel("应收款统计", IncomeReceivableReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出应收款统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.income(incomeReport, request, response, model);
    }
	
	@RequestMapping(value = {"refund"})
	public String refund(IncomeReport incomeReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<IncomeReport> page = reportService.refundReport(new Page<IncomeReport>(request, response),incomeReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != incomeReport.getPropertyProject()
				&& StringUtils.isNotEmpty(incomeReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(incomeReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != incomeReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(incomeReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/financial/refundList";
	}
	
	@RequestMapping(value = "exportRefund", method=RequestMethod.POST)
    public String exportRefund(IncomeReport incomeReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "退款统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<IncomeReport> page = reportService.refundReport(new Page<IncomeReport>(request, response, -1),incomeReport);
    		new ExportExcel("退款统计", IncomeRefundReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出退款失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.refund(incomeReport, request, response, model);
    }
}
