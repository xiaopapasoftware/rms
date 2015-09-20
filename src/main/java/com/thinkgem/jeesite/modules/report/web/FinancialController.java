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
import com.thinkgem.jeesite.modules.report.entity.LandlordReport;
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
}
