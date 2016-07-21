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
import com.thinkgem.jeesite.modules.report.entity.ContractReport;
import com.thinkgem.jeesite.modules.report.entity.LeaseContractReport;
import com.thinkgem.jeesite.modules.report.entity.TenantReport;
import com.thinkgem.jeesite.modules.report.service.ContractReportService;
import com.thinkgem.jeesite.modules.report.service.CustomerReportService;
import com.thinkgem.jeesite.modules.report.service.TenantReportService;

@Controller
@RequestMapping(value = "${adminPath}/report/customer")
public class CustomerReportController extends BaseController {
	@Autowired
	private CustomerReportService customerReportService;
	@Autowired
	private PropertyProjectService propertyProjectService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private TenantReportService tenantReportService;
	@Autowired
	private HouseService houseService;
	@Autowired
	private ContractReportService contractReportService;

	//@RequiresPermissions("customer:leaseContract:view")
	@RequestMapping(value = {"leaseContract"})
	public String leaseContract(LeaseContractReport leaseContractReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<LeaseContractReport> page = customerReportService.findLeaseContractList(new Page<LeaseContractReport>(request, response),leaseContractReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != leaseContractReport.getPropertyProject()
				&& StringUtils.isNotEmpty(leaseContractReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(leaseContractReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		return "modules/report/customer/leaseContractList";
	}
	
	//@RequiresPermissions("customer:leaseContract:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(LeaseContractReport leaseContractReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "房源统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<LeaseContractReport> page = customerReportService.findLeaseContractList(new Page<LeaseContractReport>(request, response, -1),leaseContractReport);
    		new ExportExcel("房源统计", LeaseContractReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出房源统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.leaseContract(leaseContractReport, request, response, model);
    }
	
	//@RequiresPermissions("customer:tenant:view")
	@RequestMapping(value = {"tenant"})
	public String tenant(TenantReport tenantReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TenantReport> page = tenantReportService.findTenantList(new Page<TenantReport>(request, response),tenantReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != tenantReport.getPropertyProject()
				&& StringUtils.isNotEmpty(tenantReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(tenantReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != tenantReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(tenantReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/customer/rentContractList";
	}
	
	//@RequiresPermissions("customer:tenant:export")
    @RequestMapping(value = "exportTenant", method=RequestMethod.POST)
    public String exportTenant(TenantReport tenantReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "租客统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TenantReport> page = tenantReportService.findTenantList(new Page<TenantReport>(request, response, -1),tenantReport);
    		new ExportExcel("租客统计", TenantReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出租客统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.tenant(tenantReport, request, response, model);
    }
    
    @RequestMapping(value = {"contract"})
	public String contract(ContractReport contractReport,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ContractReport> page = contractReportService.report(new Page<ContractReport>(request, response),contractReport);
		model.addAttribute("page", page);
		
		List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
		model.addAttribute("projectList", projectList);

		if (null != contractReport.getPropertyProject()
				&& StringUtils.isNotEmpty(contractReport.getPropertyProject().getId())) {
			Building building = new Building();
			PropertyProject propertyProject = new PropertyProject();
			propertyProject.setId(contractReport.getPropertyProject().getId());
			building.setPropertyProject(propertyProject);
			List<Building> buildingList = buildingService.findList(building);
			model.addAttribute("buildingList", buildingList);
		}
		
		if (null != contractReport.getBuilding()) {
			House house = new House();
			Building building = new Building();
			building.setId(contractReport.getBuilding().getId());
			house.setBuilding(building);
			List<House> houseList = houseService.findList(house);
			model.addAttribute("houseList", houseList);
		}
		
		return "modules/report/customer/contractReport";
	}
    
    @RequestMapping(value = "exportContract", method=RequestMethod.POST)
    public String exportContract(ContractReport contractReport, HttpServletRequest request, HttpServletResponse response, Model model) {
		try {
            String fileName = "催款统计"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ContractReport> page = contractReportService.report(new Page<ContractReport>(request, response, -1),contractReport);
    		new ExportExcel("催款统计", ContractReport.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			model.addAttribute("message", "导出催款统计失败！失败信息："+e.getMessage());
			model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
		}
		return this.contract(contractReport, request, response, model);
    }
}
