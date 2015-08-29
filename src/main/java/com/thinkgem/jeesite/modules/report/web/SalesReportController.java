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
import com.thinkgem.jeesite.modules.report.entity.ExpireReport;
import com.thinkgem.jeesite.modules.report.entity.RecommendReport;
import com.thinkgem.jeesite.modules.report.entity.ReletReport;
import com.thinkgem.jeesite.modules.report.entity.RentReport;
import com.thinkgem.jeesite.modules.report.service.ContractReportService;

@Controller
@RequestMapping(value = "${adminPath}/report/sales")
public class SalesReportController extends BaseController {
	@Autowired
	private PropertyProjectService propertyProjectService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private ContractReportService contractReportService;
	
	@RequestMapping(value = {"expire"})
	public String expire(ExpireReport expireReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ExpireReport> page = contractReportService.expireReport(new Page<ExpireReport>(request, response),expireReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != expireReport.getPropertyProject()
				&& StringUtils.isNotEmpty(expireReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(expireReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != expireReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(expireReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/sales/expireList";
	}
	
	@RequestMapping(value = "exportExpire", method=RequestMethod.POST)
    public String exportExpire(ExpireReport expireReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "合同到期提醒统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ExpireReport> page = contractReportService.expireReport(new Page<ExpireReport>(request, response, -1),expireReport);
    		new ExportExcel("合同到期提醒统计", ExpireReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出合同到期提醒统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.expire(expireReport, request, response, model);
    }
	
	@RequestMapping(value = {"relet"})
	public String relet(ReletReport reletReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ReletReport> page = contractReportService.reletReport(new Page<ReletReport>(request, response),reletReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != reletReport.getPropertyProject()
				&& StringUtils.isNotEmpty(reletReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(reletReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != reletReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(reletReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/sales/reletList";
	}
	
	@RequestMapping(value = "exportRelet", method=RequestMethod.POST)
    public String exportRelet(ReletReport reletReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "续租合同统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ReletReport> page = contractReportService.reletReport(new Page<ReletReport>(request, response, -1),reletReport);
    		new ExportExcel("续租合同统计", ReletReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出续租合同失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.relet(reletReport, request, response, model);
    }
	
	@RequestMapping(value = {"recommend"})
	public String recommend(RecommendReport recommendReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RecommendReport> page = contractReportService.recommendReport(new Page<RecommendReport>(request, response),recommendReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != recommendReport.getPropertyProject()
				&& StringUtils.isNotEmpty(recommendReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(recommendReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != recommendReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(recommendReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/sales/recommendList";
	}
	
	@RequestMapping(value = "exportRecommend", method=RequestMethod.POST)
    public String exportRecommend(RecommendReport recommendReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "第三方推介统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RecommendReport> page = contractReportService.recommendReport(new Page<RecommendReport>(request, response, -1),recommendReport);
    		new ExportExcel("第三方推介统计", RecommendReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出第三方推介失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.recommend(recommendReport, request, response, model);
    }
	
	@RequestMapping(value = {"rent"})
	public String rent(RentReport rentReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<RentReport> page = contractReportService.rentReport(new Page<RentReport>(request, response),rentReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != rentReport.getPropertyProject()
				&& StringUtils.isNotEmpty(rentReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(rentReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != rentReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(rentReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/sales/rentList";
	}
	
	@RequestMapping(value = "exportRent", method=RequestMethod.POST)
    public String exportRent(RentReport rentReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "退租统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<RentReport> page = contractReportService.rentReport(new Page<RentReport>(request, response, -1),rentReport);
    		new ExportExcel("退租统计", RentReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出退租失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.rent(rentReport, request, response, model);
    }
}
