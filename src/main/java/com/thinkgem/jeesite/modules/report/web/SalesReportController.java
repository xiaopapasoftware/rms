package com.thinkgem.jeesite.modules.report.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import com.thinkgem.jeesite.modules.funds.entity.PaymentTrans4Export;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.report.entity.ExpireReport;
import com.thinkgem.jeesite.modules.report.entity.HouseRoomReport;
import com.thinkgem.jeesite.modules.report.entity.RecommendReport;
import com.thinkgem.jeesite.modules.report.entity.ReletRateReport;
import com.thinkgem.jeesite.modules.report.entity.ReletReport;
import com.thinkgem.jeesite.modules.report.entity.RentDataReport;
import com.thinkgem.jeesite.modules.report.entity.RentReport;
import com.thinkgem.jeesite.modules.report.entity.ResultsReport;
import com.thinkgem.jeesite.modules.report.service.ContractReportService;
import com.thinkgem.jeesite.modules.report.service.ReportService;

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
  @Autowired
  private ReportService reportService;

  @RequestMapping(value = {"expire"})
  public String expire(ExpireReport expireReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<ExpireReport> page = contractReportService.expireReport(new Page<ExpireReport>(request, response), expireReport);
    model.addAttribute("page", page);

    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

    if (null != expireReport.getPropertyProject() && StringUtils.isNotEmpty(expireReport.getPropertyProject().getId())) {
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

  @RequestMapping(value = "exportExpire", method = RequestMethod.POST)
  public String exportExpire(ExpireReport expireReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "合同到期提醒统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<ExpireReport> page = contractReportService.expireReport(new Page<ExpireReport>(request, response, -1), expireReport);
      new ExportExcel("合同到期提醒统计", ExpireReport.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出合同到期提醒统计失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return this.expire(expireReport, request, response, model);
  }

  /**
   * 房间数量统计报表-导出
   */
  @RequestMapping(value = {"exportRoomsCount"})
  public String exportRoomsCount(HouseRoomReport houseRoomReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "房间数量统计报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<HouseRoomReport> page = getRoomsCountList(houseRoomReport, request, response);
      new ExportExcel("房间数量统计报表", HouseRoomReport.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出房间数量统计报表失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return roomsCount(houseRoomReport, request, response, model);
  }

  /**
   * 房间数量统计报表
   */
  @RequestMapping(value = {"roomsCount"})
  public String roomsCount(HouseRoomReport houseRoomReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    if (houseRoomReport.getPropertyProject() != null) {
      model.addAttribute("page", getRoomsCountList(houseRoomReport, request, response));
    }
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);
    return "modules/report/sales/roomsCount";
  }

  /**
   * 获得房间数量统计报表的数据
   */
  private Page<HouseRoomReport> getRoomsCountList(HouseRoomReport houseRoomReport, HttpServletRequest request, HttpServletResponse response) {
    Page<HouseRoomReport> totalPage = new Page<HouseRoomReport>(request, response, -1);
    if ("ALL".equals(houseRoomReport.getPropertyProject().getId())) {
      totalPage.initialize();
      List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
      int allTotalNum = 0;// 所有小区的总数量
      int allRenovationNum = 0;// 所有小区的待装修总数量
      int allToBeReservedNum = 0;// 所有小区的可预订总数量
      int allReservedNum = 0; // 所有小区的已预定总数量
      int allLeasedNum = 0;// 所有小区的已出租总数量
      int allReturned4ReservedNum = 0;// 所有小区的已退租可预订总数量
      int allDamagedNum = 0;// 所有小区的已损坏总数量
      for (PropertyProject pp : projectList) {
        if (StringUtils.isNotEmpty(pp.getId())) {
          HouseRoomReport hrr = new HouseRoomReport();
          hrr.setPropertyProject(pp);
          Page<HouseRoomReport> page = reportService.roomsCount(new Page<HouseRoomReport>(request, response), hrr);
          if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
            totalPage.getList().addAll(page.getList());
            totalPage.setCount(totalPage.getCount() + 1);
            HouseRoomReport tempHRR = page.getList().get(0);
            allTotalNum += Integer.valueOf(tempHRR.getTotalNum());
            allRenovationNum += Integer.valueOf(tempHRR.getRenovationNum());
            allToBeReservedNum += Integer.valueOf(tempHRR.getToBeReservedNum());
            allReservedNum += Integer.valueOf(tempHRR.getReservedNum());
            allLeasedNum += Integer.valueOf(tempHRR.getLeasedNum());
            allReturned4ReservedNum += Integer.valueOf(tempHRR.getReturned4ReservedNum());
            allDamagedNum += Integer.valueOf(tempHRR.getDamagedNum());
          }
        }
      }
      HouseRoomReport totalHouseRoomReport = new HouseRoomReport();// 单独生成合计的数据
      totalHouseRoomReport.setProjectName("合计");
      totalHouseRoomReport.setTotalNum(allTotalNum + "");// 总数量
      totalHouseRoomReport.setRenovationNum(allRenovationNum + "");// 待装修数量
      totalHouseRoomReport.setToBeReservedNum(allToBeReservedNum + "");// 可预订数量
      totalHouseRoomReport.setReservedNum(allReservedNum + "");// 已预定数量
      totalHouseRoomReport.setLeasedNum(allLeasedNum + "");// 已出租数量
      totalHouseRoomReport.setReturned4ReservedNum(allReturned4ReservedNum + "");// 已退租可预定数量
      totalHouseRoomReport.setDamagedNum(allDamagedNum + "");// 已损坏数量
      List<HouseRoomReport> totalHouseRoomReportList = new ArrayList<HouseRoomReport>();
      totalHouseRoomReportList.add(totalHouseRoomReport);
      Collections.sort(totalPage.getList(), Collections.reverseOrder());// 按照放量大小排序
      totalPage.getList().addAll(totalHouseRoomReportList);
    } else {
      totalPage = reportService.roomsCount(new Page<HouseRoomReport>(request, response), houseRoomReport);
    }
    return totalPage;
  }

  @RequestMapping(value = {"relet"})
  public String relet(ReletReport reletReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<ReletReport> page = contractReportService.reletReport(new Page<ReletReport>(request, response), reletReport);
    model.addAttribute("page", page);

    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

    if (null != reletReport.getPropertyProject() && StringUtils.isNotEmpty(reletReport.getPropertyProject().getId())) {
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

  @RequestMapping(value = "exportRelet", method = RequestMethod.POST)
  public String exportRelet(ReletReport reletReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "续租合同统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<ReletReport> page = contractReportService.reletReport(new Page<ReletReport>(request, response, -1), reletReport);
      new ExportExcel("续租合同统计", ReletReport.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出续租合同统计失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return this.relet(reletReport, request, response, model);
  }

  @RequestMapping(value = {"recommend"})
  public String recommend(RecommendReport recommendReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<RecommendReport> page = contractReportService.recommendReport(new Page<RecommendReport>(request, response), recommendReport);
    model.addAttribute("page", page);

    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

    if (null != recommendReport.getPropertyProject() && StringUtils.isNotEmpty(recommendReport.getPropertyProject().getId())) {
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

  @RequestMapping(value = "exportRecommend", method = RequestMethod.POST)
  public String exportRecommend(RecommendReport recommendReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "第三方推介统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<RecommendReport> page = contractReportService.recommendReport(new Page<RecommendReport>(request, response, -1), recommendReport);
      new ExportExcel("第三方推介统计", RecommendReport.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出第三方推介统计失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return this.recommend(recommendReport, request, response, model);
  }

  @RequestMapping(value = {"rent"})
  public String rent(RentReport rentReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<RentReport> page = contractReportService.rentReport(new Page<RentReport>(request, response), rentReport);
    model.addAttribute("page", page);

    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

    if (null != rentReport.getPropertyProject() && StringUtils.isNotEmpty(rentReport.getPropertyProject().getId())) {
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

  @RequestMapping(value = "exportRent", method = RequestMethod.POST)
  public String exportRent(RentReport rentReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "退租统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<RentReport> page = contractReportService.rentReport(new Page<RentReport>(request, response, -1), rentReport);
      new ExportExcel("退租统计", RentReport.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出退租统计失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return this.rent(rentReport, request, response, model);
  }

  @RequestMapping(value = {"reletRate"})
  public String reletRate(ReletRateReport reletRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<ReletRateReport> page = reportService.reletRateReport(new Page<ReletRateReport>(request, response), reletRateReport);
    model.addAttribute("page", page);

    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

    return "modules/report/sales/reletRateList";
  }

  @RequestMapping(value = "exportReletRate", method = RequestMethod.POST)
  public String exportReletRate(ReletRateReport reletRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "合同到期续租率统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<ReletRateReport> page = reportService.reletRateReport(new Page<ReletRateReport>(request, response, -1), reletRateReport);
      new ExportExcel("合同到期续租率统计", ReletRateReport.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出合同到期续租率统计失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return this.reletRate(reletRateReport, request, response, model);
  }

  @RequestMapping(value = {"results"})
  public String results(ResultsReport resultsReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<ResultsReport> page = reportService.resultsReport(new Page<ResultsReport>(request, response), resultsReport);
    model.addAttribute("page", page);

    return "modules/report/sales/resultsList";
  }

  @RequestMapping(value = "exportResults", method = RequestMethod.POST)
  public String exportResults(ResultsReport resultsReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "销售业绩统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<ResultsReport> page = reportService.resultsReport(new Page<ResultsReport>(request, response, -1), resultsReport);
      new ExportExcel("销售业绩统计", ResultsReport.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出销售业绩统计失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return this.results(resultsReport, request, response, model);
  }

  @RequestMapping(value = {"rentData"})
  public String rentData(RentDataReport rentDataReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<RentDataReport> page = reportService.rentDataReport(new Page<RentDataReport>(request, response), rentDataReport);
    model.addAttribute("page", page);

    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);

    return "modules/report/sales/rentDataList";
  }

  @RequestMapping(value = "exportRentData", method = RequestMethod.POST)
  public String exportRentData(RentDataReport rentDataReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "出租数据统计" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<RentDataReport> page = reportService.rentDataReport(new Page<RentDataReport>(request, response, -1), rentDataReport);
      new ExportExcel("出租数据统计", RentDataReport.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出出租数据统计失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return this.rentData(rentDataReport, request, response, model);
  }
}
