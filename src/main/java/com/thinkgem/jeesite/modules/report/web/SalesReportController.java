package com.thinkgem.jeesite.modules.report.web;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.report.entity.EntireRentRateReport;
import com.thinkgem.jeesite.modules.report.entity.ExpireReport;
import com.thinkgem.jeesite.modules.report.entity.HouseReport;
import com.thinkgem.jeesite.modules.report.entity.HouseRoomReport;
import com.thinkgem.jeesite.modules.report.entity.JointRentRateReport;
import com.thinkgem.jeesite.modules.report.entity.RecommendReport;
import com.thinkgem.jeesite.modules.report.entity.ReletRateReport;
import com.thinkgem.jeesite.modules.report.entity.ReletReport;
import com.thinkgem.jeesite.modules.report.entity.RentAveragePriceReport;
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
  @Autowired
  private RentContractService rentContractService;
  @Autowired
  private RoomService roomService;

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
   * 房间数量统计报表-查询
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
      List<HouseRoomReport> totalHouseRoomReportList = new ArrayList<HouseRoomReport>();
      totalHouseRoomReportList.add(totalHouseRoomReport);
      Collections.sort(totalPage.getList(), Collections.reverseOrder());// 按照放量大小排序
      totalPage.getList().addAll(totalHouseRoomReportList);
    } else {
      totalPage = reportService.roomsCount(new Page<HouseRoomReport>(request, response), houseRoomReport);
    }
    return totalPage;
  }

  /**
   * 房屋数量统计报表-导出
   */
  @RequestMapping(value = {"exportHousesCount"})
  public String exportHousesCount(HouseReport houseReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "房屋数量统计报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<HouseReport> page = getHousesCountList(houseReport, request, response);
      new ExportExcel("房屋数量统计报表", HouseReport.class).setDataList(page.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出房间数量统计报表失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return housesCount(houseReport, request, response, model);
  }

  /**
   * 房屋数量统计报表-查询
   */
  @RequestMapping(value = {"housesCount"})
  public String housesCount(HouseReport houseReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    if (houseReport.getPropertyProject() != null) {
      model.addAttribute("page", getHousesCountList(houseReport, request, response));
    }
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);
    return "modules/report/sales/housesCount";
  }

  /**
   * 获得房屋数量统计报表的数据
   */
  private Page<HouseReport> getHousesCountList(HouseReport houseReport, HttpServletRequest request, HttpServletResponse response) {
    Page<HouseReport> totalPage = new Page<HouseReport>(request, response, -1);
    if ("ALL".equals(houseReport.getPropertyProject().getId())) {
      totalPage.initialize();
      List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
      int allTotalNum = 0;// 所有小区的总数量
      int allRenovationNum = 0;// 所有小区的总的待装修总数量
      int allToBeReservedNum = 0;// 所有小区的可预订总数量
      int allReservedNum = 0; // 所有小区的已预定总数量
      int allPartLeasedNum = 0;// 所有小区的部分出租的总数量
      int allWholeLeasedNum = 0;// 所有小区的完全出租的总数量
      for (PropertyProject pp : projectList) {
        if (StringUtils.isNotEmpty(pp.getId())) {
          HouseReport hr = new HouseReport();
          hr.setPropertyProject(pp);
          Page<HouseReport> page = reportService.housesCount(new Page<HouseReport>(request, response), hr);
          if (page != null && CollectionUtils.isNotEmpty(page.getList())) {
            totalPage.getList().addAll(page.getList());
            totalPage.setCount(totalPage.getCount() + 1);
            HouseReport tempHR = page.getList().get(0);
            allTotalNum += Integer.valueOf(tempHR.getTotalNum());
            allRenovationNum += Integer.valueOf(tempHR.getRenovationNum());
            allToBeReservedNum += Integer.valueOf(tempHR.getToBeReservedNum());
            allReservedNum += Integer.valueOf(tempHR.getReservedNum());
            allPartLeasedNum += Integer.valueOf(tempHR.getPartRentNum());
            allWholeLeasedNum += Integer.valueOf(tempHR.getWholeRentNum());
          }
        }
      }
      HouseReport totalHouseReport = new HouseReport();// 单独生成合计的数据
      totalHouseReport.setProjectName("合计");
      totalHouseReport.setTotalNum(allTotalNum + "");// 总数量
      totalHouseReport.setRenovationNum(allRenovationNum + "");// 待装修数量
      totalHouseReport.setToBeReservedNum(allToBeReservedNum + "");// 可预订数量
      totalHouseReport.setReservedNum(allReservedNum + "");// 已预定数量
      totalHouseReport.setPartRentNum(allPartLeasedNum + "");// 部分出租数量
      totalHouseReport.setWholeRentNum(allWholeLeasedNum + "");// 完全出租的数量
      List<HouseReport> totalHouseReportList = new ArrayList<HouseReport>();
      totalHouseReportList.add(totalHouseReport);
      Collections.sort(totalPage.getList(), Collections.reverseOrder());// 按照放量大小排序
      totalPage.getList().addAll(totalHouseReportList);
    } else {
      totalPage = reportService.housesCount(new Page<HouseReport>(request, response), houseReport);
    }
    return totalPage;
  }

  /**
   * 合租出租率统计报表-导出
   */
  @RequestMapping(value = {"exportJointRentRateReport"})
  public String exportJointRentRateReport(JointRentRateReport jointRentRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "合租出租率统计报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<JointRentRateReport> totalPage = getJointRentRateReport(jointRentRateReport, request, response);
      new ExportExcel("合租出租率统计报表", JointRentRateReport.class).setDataList(totalPage.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出合租出租率统计报表失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return jointRentRateReport(jointRentRateReport, request, response, model);
  }

  /**
   * 合租出租率统计报表-查询
   */
  @RequestMapping(value = {"jointRentRate"})
  public String jointRentRateReport(JointRentRateReport jointRentRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<JointRentRateReport> totalPage = getJointRentRateReport(jointRentRateReport, request, response);
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);
    model.addAttribute("jointRentRateReport", jointRentRateReport);
    model.addAttribute("page", totalPage);
    return "modules/report/sales/jointRentRateReport";
  }

  private Page<JointRentRateReport> getJointRentRateReport(JointRentRateReport jointRentRateReport, HttpServletRequest request, HttpServletResponse response) {
    Page<JointRentRateReport> totalPage = new Page<JointRentRateReport>(request, response, -1);
    if (jointRentRateReport.getPropertyProject() != null) {
      if ("ALL".equals(jointRentRateReport.getPropertyProject().getId())) {
        totalPage.initialize();
        List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
        for (PropertyProject pp : projectList) {
          totalPage = getJointRentRateReport(totalPage, pp.getId(), jointRentRateReport.getStartDate(), jointRentRateReport.getEndDate());
        }
        Collections.sort(totalPage.getList(), Collections.reverseOrder());
      } else {
        totalPage = getJointRentRateReport(totalPage, jointRentRateReport.getPropertyProject().getId(), jointRentRateReport.getStartDate(), jointRentRateReport.getEndDate());
      }
    }
    return totalPage;
  }

  private Page<JointRentRateReport> getJointRentRateReport(Page<JointRentRateReport> totalPage, String ppId, Date startDate, Date endDate) {
    PropertyProject pp = propertyProjectService.get(ppId);
    int allRoomsCount = roomService.queryRoomsCountByProjectPropertyId(ppId);
    int rentedRoomsCount = rentContractService.queryValidSingleRoomCount(startDate, endDate, ppId);
    JointRentRateReport jrrr = new JointRentRateReport();
    jrrr.setProjectName(pp.getProjectName());
    jrrr.setTotalNum(allRoomsCount + "");
    jrrr.setRentedNum(rentedRoomsCount + "");
    if (allRoomsCount != 0 && rentedRoomsCount != 0) {
      double doubleValue = new BigDecimal(rentedRoomsCount).divide(new BigDecimal(allRoomsCount), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
      NumberFormat num = NumberFormat.getPercentInstance();
      num.setMaximumIntegerDigits(3);
      num.setMaximumFractionDigits(2);
      jrrr.setRentRate(num.format(doubleValue));
    } else {
      jrrr.setRentRate("0");
    }
    totalPage.getList().add(jrrr);
    totalPage.setCount(totalPage.getCount() + 1);
    return totalPage;
  }

  /**
   * 整租出租率统计报表-导出
   */
  @RequestMapping(value = {"exportEntireRentRateReport"})
  public String exportEntireRentRateReport(EntireRentRateReport entireRentRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "整租出租率统计报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<EntireRentRateReport> totalPage = getEntireRentRateReport(entireRentRateReport, request, response);
      new ExportExcel("整租出租率统计报表", EntireRentRateReport.class).setDataList(totalPage.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出整租出租率统计报表失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return entireRentRateReport(entireRentRateReport, request, response, model);
  }

  /**
   * 整租出租率统计报表-查询
   */
  @RequestMapping(value = {"entireRentRate"})
  public String entireRentRateReport(EntireRentRateReport entireRentRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<EntireRentRateReport> totalPage = getEntireRentRateReport(entireRentRateReport, request, response);
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);
    model.addAttribute("jointRentRateReport", entireRentRateReport);
    model.addAttribute("page", totalPage);
    return "modules/report/sales/entireRentRateReport";
  }

  private Page<EntireRentRateReport> getEntireRentRateReport(EntireRentRateReport entireRentRateReport, HttpServletRequest request, HttpServletResponse response) {
    Page<EntireRentRateReport> totalPage = new Page<EntireRentRateReport>(request, response, -1);
    if (entireRentRateReport.getPropertyProject() != null) {
      if ("ALL".equals(entireRentRateReport.getPropertyProject().getId())) {
        totalPage.initialize();
        List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
        for (PropertyProject pp : projectList) {
          totalPage = getEntireRentRateReport(totalPage, pp.getId(), entireRentRateReport.getStartDate(), entireRentRateReport.getEndDate());
        }
        Collections.sort(totalPage.getList(), Collections.reverseOrder());
      } else {
        totalPage = getEntireRentRateReport(totalPage, entireRentRateReport.getPropertyProject().getId(), entireRentRateReport.getStartDate(), entireRentRateReport.getEndDate());
      }
    }
    return totalPage;
  }

  private Page<EntireRentRateReport> getEntireRentRateReport(Page<EntireRentRateReport> totalPage, String ppId, Date startDate, Date endDate) {
    PropertyProject pp = propertyProjectService.get(ppId);
    int allHousesCount = houseService.queryHousesCountByProjectPropertyId(ppId);
    int rentedHousesCount = rentContractService.queryValidEntireHouseCount(startDate, endDate, ppId);
    EntireRentRateReport jrrr = new EntireRentRateReport();
    jrrr.setProjectName(pp.getProjectName());
    jrrr.setTotalNum(allHousesCount + "");
    jrrr.setRentedNum(rentedHousesCount + "");
    if (allHousesCount != 0 && rentedHousesCount != 0) {
      double doubleValue = new BigDecimal(rentedHousesCount).divide(new BigDecimal(allHousesCount), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
      NumberFormat num = NumberFormat.getPercentInstance();
      num.setMaximumIntegerDigits(3);
      num.setMaximumFractionDigits(2);
      jrrr.setRentRate(num.format(doubleValue));
    } else {
      jrrr.setRentRate("0");
    }
    totalPage.getList().add(jrrr);
    totalPage.setCount(totalPage.getCount() + 1);
    return totalPage;
  }

  /**
   * 房租平均价格统计报表-导出
   */
  @RequestMapping(value = {"exportRentAveragePriceReport"})
  public String exportRentAveragePriceReport(RentAveragePriceReport rentAveragePriceReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "房租平均价格统计报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<RentAveragePriceReport> totalPage = getRentAveragePriceReport(rentAveragePriceReport, request, response);
      new ExportExcel("房租平均价格统计报表", RentAveragePriceReport.class).setDataList(totalPage.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      model.addAttribute("message", "导出房租平均价格统计报表失败！失败信息：" + e.getMessage());
      model.addAttribute("messageType", ViewMessageTypeEnum.ERROR.getValue());
    }
    return rentAveragePriceReport(rentAveragePriceReport, request, response, model);
  }

  /**
   * 房租平均价格统计报表-查询
   */
  @RequestMapping(value = {"rentAveragePriceReport"})
  public String rentAveragePriceReport(RentAveragePriceReport rentAveragePriceReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<RentAveragePriceReport> totalPage = getRentAveragePriceReport(rentAveragePriceReport, request, response);
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    model.addAttribute("projectList", projectList);
    model.addAttribute("rentAveragePriceReport", rentAveragePriceReport);
    model.addAttribute("page", totalPage);
    return "modules/report/sales/rentAveragePriceReport";
  }

  private Page<RentAveragePriceReport> getRentAveragePriceReport(RentAveragePriceReport rentAveragePriceReport, HttpServletRequest request, HttpServletResponse response) {
    Page<RentAveragePriceReport> totalPage = new Page<RentAveragePriceReport>(request, response, -1);
    if (rentAveragePriceReport.getPropertyProject() != null) {
      if ("ALL".equals(rentAveragePriceReport.getPropertyProject().getId())) {
        totalPage.initialize();
        List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
        for (PropertyProject pp : projectList) {
          totalPage = getRentAveragePriceReport(totalPage, pp.getId(), rentAveragePriceReport.getStartDate(), rentAveragePriceReport.getEndDate());
        }
        Collections.sort(totalPage.getList(), Collections.reverseOrder());
      } else {
        totalPage = getRentAveragePriceReport(totalPage, rentAveragePriceReport.getPropertyProject().getId(), rentAveragePriceReport.getStartDate(), rentAveragePriceReport.getEndDate());
      }
    }
    return totalPage;
  }

  private Page<RentAveragePriceReport> getRentAveragePriceReport(Page<RentAveragePriceReport> totalPage, String ppId, Date startDate, Date endDate) {
    RentAveragePriceReport rapr = new RentAveragePriceReport();
    double jointAvgPrice = 0d;
    double jointTotalPrice = 0d;
    double entireAvgPrice = 0d;
    double entireTotalPrice = 0d;
    PropertyProject pp = propertyProjectService.get(ppId);
    List<RentContract> singleContracts = rentContractService.queryValidSingleRooms(startDate, endDate, ppId);
    List<RentContract> entireContracts = rentContractService.queryValidEntireHouses(startDate, endDate, ppId);
    if (CollectionUtils.isNotEmpty(singleContracts)) {
      for (RentContract singleContract : singleContracts) {
        jointTotalPrice += singleContract.getRental();
      }
      jointAvgPrice = new BigDecimal(jointTotalPrice).divide(new BigDecimal(singleContracts.size()), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    if (CollectionUtils.isNotEmpty(entireContracts)) {
      for (RentContract entireContract : entireContracts) {
        entireTotalPrice += entireContract.getRental();
      }
      entireAvgPrice = new BigDecimal(entireTotalPrice).divide(new BigDecimal(entireContracts.size()), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    rapr.setProjectName(pp.getProjectName());
    rapr.setJointRentAvgPrice(jointAvgPrice + "");
    rapr.setEntireRentAvgPrice(entireAvgPrice + "");
    totalPage.getList().add(rapr);
    totalPage.setCount(totalPage.getCount() + 1);
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
