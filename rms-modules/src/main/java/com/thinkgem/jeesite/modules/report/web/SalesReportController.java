package com.thinkgem.jeesite.modules.report.web;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.report.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.report.service.ReportService;

/**
 * @author wangshujin
 */
@Controller
@RequestMapping(value = "${adminPath}/report/sales")
public class SalesReportController extends BaseController {
  @Autowired
  private PropertyProjectService propertyProjectService;
  @Autowired
  private HouseService houseService;
  @Autowired
  private ReportService reportService;
  @Autowired
  private RentContractService rentContractService;
  @Autowired
  private RoomService roomService;

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
      addMessage(model, ViewMessageTypeEnum.ERROR, "导出房间数量统计报表失败！失败信息：" + e.getMessage());
    }
    return roomsCount(houseRoomReport, request, response, model);
  }

  /**
   * 房间数量统计报表-查询
   */
  @RequestMapping(value = {"roomsCount"})
  public String roomsCount(HouseRoomReport houseRoomReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("page", getRoomsCountList(houseRoomReport, request, response));
    return "modules/report/sales/roomsCount";
  }

  /**
   * 获得房间数量统计报表的数据
   */
  private Page<HouseRoomReport> getRoomsCountList(HouseRoomReport houseRoomReport, HttpServletRequest request, HttpServletResponse response) {
    Page<HouseRoomReport> totalPage = new Page<HouseRoomReport>(request, response, -1);
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
      addMessage(model, ViewMessageTypeEnum.ERROR, "导出房间数量统计报表失败！失败信息：" + e.getMessage());
    }
    return housesCount(houseReport, request, response, model);
  }

  /**
   * 房屋数量统计报表-查询
   */
  @RequestMapping(value = {"housesCount"})
  public String housesCount(HouseReport houseReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("page", getHousesCountList(houseReport, request, response));
    return "modules/report/sales/housesCount";
  }

  /**
   * 获得房屋数量统计报表的数据
   */
  private Page<HouseReport> getHousesCountList(HouseReport houseReport, HttpServletRequest request, HttpServletResponse response) {
    Page<HouseReport> totalPage = new Page<HouseReport>(request, response, -1);
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
    return totalPage;
  }

  /**
   * 单间出租率-导出
   */
  @RequestMapping(value = {"exportJointRentRateReport"})
  public String exportJointRentRateReport(JointRentRateReport jointRentRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "单间出租率统计报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<JointRentRateReport> totalPage = getJointRentRateReport(jointRentRateReport, request, response);
      new ExportExcel("单间出租率统计报表", JointRentRateReport.class).setDataList(totalPage.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      addMessage(model, ViewMessageTypeEnum.ERROR, "导出单间出租率统计报表失败！失败信息：" + e.getMessage());
    }
    return jointRentRateReport(jointRentRateReport, request, response, model);
  }

  /**
   * 单间出租率-查询
   */
  @RequestMapping(value = {"jointRentRate"})
  public String jointRentRateReport(JointRentRateReport jointRentRateReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<JointRentRateReport> totalPage = getJointRentRateReport(jointRentRateReport, request, response);
    model.addAttribute("jointRentRateReport", jointRentRateReport);
    model.addAttribute("page", totalPage);
    return "modules/report/sales/jointRentRateReport";
  }

  private Page<JointRentRateReport> getJointRentRateReport(JointRentRateReport jointRentRateReport, HttpServletRequest request, HttpServletResponse response) {
    Page<JointRentRateReport> totalPage = new Page<JointRentRateReport>(request, response, -1);
    totalPage.initialize();
    int allRoomCount = 0;
    int allRentedRoom = 0;
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    for (PropertyProject pp : projectList) {
      int allRoomsCount = roomService.queryRoomsCountByProjectPropertyId(jointRentRateReport.getStartDate(), pp.getId()); // 获取到在某个时间点之前小区的所有房间的数量
      int rentedRoomsCount = rentContractService.queryValidSingleRoomCount(jointRentRateReport.getStartDate(), pp.getId());// 获取某个日期的所有已经出租掉的房间数，包括合租的和整租的
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
      allRoomCount += allRoomsCount;
      allRentedRoom += rentedRoomsCount;
      totalPage.getList().add(jrrr);
      totalPage.setCount(totalPage.getCount() + 1);
      Collections.sort(totalPage.getList(), Collections.reverseOrder());
    }
    JointRentRateReport jrrr = new JointRentRateReport();
    jrrr.setProjectName("总平均出租率");
    jrrr.setTotalNum(allRoomCount + "");
    jrrr.setRentedNum(allRentedRoom + "");
    if (allRoomCount != 0 && allRentedRoom != 0) {
      double doubleValue = new BigDecimal(allRentedRoom).divide(new BigDecimal(allRoomCount), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
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
      addMessage(model, ViewMessageTypeEnum.ERROR, "导出整租出租率统计报表失败！失败信息：" + e.getMessage());
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
    totalPage.initialize();
    int allRoomCount = 0;
    int allRentedRoom = 0;
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    for (PropertyProject pp : projectList) {
      int allHousesCount = houseService.queryHousesCountByProjectPropertyId(pp.getId(), entireRentRateReport.getStartDate());// 指定日期的房屋总数
      int rentedHousesCount = rentContractService.queryValidEntireHouseCount(pp.getId(), entireRentRateReport.getStartDate());// 指定日期所有部分出租+完全出租的房屋套数
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
      allRoomCount += allHousesCount;
      allRentedRoom += rentedHousesCount;
      totalPage.getList().add(jrrr);
      totalPage.setCount(totalPage.getCount() + 1);
      Collections.sort(totalPage.getList(), Collections.reverseOrder());
    }
    EntireRentRateReport errr = new EntireRentRateReport();
    errr.setProjectName("总平均出租率");
    errr.setTotalNum(allRoomCount + "");
    errr.setRentedNum(allRentedRoom + "");
    if (allRoomCount != 0 && allRentedRoom != 0) {
      double doubleValue = new BigDecimal(allRentedRoom).divide(new BigDecimal(allRoomCount), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
      NumberFormat num = NumberFormat.getPercentInstance();
      num.setMaximumIntegerDigits(3);
      num.setMaximumFractionDigits(2);
      errr.setRentRate(num.format(doubleValue));
    } else {
      errr.setRentRate("0");
    }
    totalPage.getList().add(errr);
    totalPage.setCount(totalPage.getCount() + 1);
    return totalPage;
  }

  /**
   * 单间平均房租价格统计报表-导出
   */
  @RequestMapping(value = {"exportRentAveragePriceReport"})
  public String exportRentAveragePriceReport(RentAveragePriceReport rentAveragePriceReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "单间平均房租价格统计报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<RentAveragePriceReport> totalPage = getRentAveragePriceReport(rentAveragePriceReport, request, response);
      new ExportExcel("单间平均房租价格统计报表", RentAveragePriceReport.class).setDataList(totalPage.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      addMessage(model, ViewMessageTypeEnum.ERROR, "导出单间平均房租价格统计报表失败！失败信息：" + e.getMessage());
    }
    return rentAveragePriceReport(rentAveragePriceReport, request, response, model);
  }

  /**
   * 单间平均房租价格统计报表-查询
   */
  @RequestMapping(value = {"rentAveragePriceReport"})
  public String rentAveragePriceReport(RentAveragePriceReport rentAveragePriceReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<RentAveragePriceReport> totalPage = getRentAveragePriceReport(rentAveragePriceReport, request, response);
    model.addAttribute("rentAveragePriceReport", rentAveragePriceReport);
    model.addAttribute("page", totalPage);
    return "modules/report/sales/rentAveragePriceReport";
  }

  private Page<RentAveragePriceReport> getRentAveragePriceReport(RentAveragePriceReport rentAveragePriceReport, HttpServletRequest request, HttpServletResponse response) {
    Page<RentAveragePriceReport> totalPage = new Page<RentAveragePriceReport>(request, response, -1);
    totalPage.initialize();
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    Map<String, Double> map = new HashMap<String, Double>();
    map.put("totalRental", 0d);// 所有小区的单间总租金
    map.put("totalRoomCount", 0d);// 所有小区的单间数
    for (PropertyProject pp : projectList) {
      totalPage = getRentAveragePriceReport(map, totalPage, pp.getId(), rentAveragePriceReport.getStartDate(), rentAveragePriceReport.getEndDate());
    }
    Collections.sort(totalPage.getList(), Collections.reverseOrder());
    // 所有小区的单间的平均房租
    double wholeJointAvgPrice = 0d;
    if (map.get("totalRental") > 0d && map.get("totalRoomCount") > 0d) {
      wholeJointAvgPrice = new BigDecimal(map.get("totalRental")).divide(new BigDecimal(map.get("totalRoomCount")), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    RentAveragePriceReport rapr = new RentAveragePriceReport();
    rapr.setProjectName("合计");
    rapr.setJointRentAvgPrice(wholeJointAvgPrice + "");
    totalPage.getList().add(rapr);
    totalPage.setCount(totalPage.getCount() + 1);
    return totalPage;
  }

  private Page<RentAveragePriceReport> getRentAveragePriceReport(Map<String, Double> paMap, Page<RentAveragePriceReport> totalPage, String ppId, Date startDate, Date endDate) {
    RentAveragePriceReport rapr = new RentAveragePriceReport();
    double jointAvgPrice = 0d;// 单个小区的单间的平均房租
    double jointTotalPrice = 0d;// 单个小区的单间房租总和
    PropertyProject pp = propertyProjectService.get(ppId);
    List<RentContract> singleContracts = rentContractService.queryValidSingleRooms(startDate, endDate, ppId);// 所有有效的合租合同
    if (CollectionUtils.isNotEmpty(singleContracts)) {
      for (RentContract singleContract : singleContracts) {
        jointTotalPrice += singleContract.getRental();
      }
      jointAvgPrice = new BigDecimal(jointTotalPrice).divide(new BigDecimal(singleContracts.size()), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
      paMap.put("totalRental", paMap.get("totalRental") + jointTotalPrice);
      paMap.put("totalRoomCount", paMap.get("totalRoomCount") + singleContracts.size());
    }
    rapr.setProjectName(pp.getProjectName());
    rapr.setJointRentAvgPrice(jointAvgPrice + "");
    totalPage.getList().add(rapr);
    totalPage.setCount(totalPage.getCount() + 1);
    return totalPage;
  }

  /**
   * 整租平均房租价格统计报表-导出
   */
  @RequestMapping(value = {"exportWholeRentAveragePriceReport"})
  public String exportWholeRentAveragePriceReport(WholeAvgPriceReport wholeAvgPriceReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      String fileName = "整租平均房租价格统计报表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
      Page<WholeAvgPriceReport> totalPage = getWholeRentAveragePriceReport(wholeAvgPriceReport, request, response);
      new ExportExcel("整租平均房租价格统计报表", WholeAvgPriceReport.class).setDataList(totalPage.getList()).write(response, fileName).dispose();
      return null;
    } catch (Exception e) {
      addMessage(model, ViewMessageTypeEnum.ERROR, "导出整租平均房租价格统计报表失败！失败信息：" + e.getMessage());
    }
    return wholeRentAveragePriceReport(wholeAvgPriceReport, request, response, model);
  }


  /**
   * 整租平均房租价格统计报表-查询
   */
  @RequestMapping(value = {"wholeRentAveragePriceReport"})
  public String wholeRentAveragePriceReport(WholeAvgPriceReport wholeAvgPriceReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    Page<WholeAvgPriceReport> totalPage = getWholeRentAveragePriceReport(wholeAvgPriceReport, request, response);
    model.addAttribute("wholeAvgPriceReport", wholeAvgPriceReport);
    model.addAttribute("page", totalPage);
    return "modules/report/sales/wholeRentAveragePriceReport";
  }

  private Page<WholeAvgPriceReport> getWholeRentAveragePriceReport(WholeAvgPriceReport wholeAvgPriceReport, HttpServletRequest request, HttpServletResponse response) {
    Page<WholeAvgPriceReport> totalPage = new Page<WholeAvgPriceReport>(request, response, -1);
    totalPage.initialize();
    // 按顺序：1室1厅、1室2厅、2室1厅、2室2厅、3室1厅、3室2厅、4室1厅、4室2厅
    for (int i = 1; i < 5; i++) {
      for (int j = 1; j < 3; j++) {
        String wholeAvgHouseType = i + "室" + j + "厅";
        double allHouseTypesTotalRental = 0d;// 此类房型的所有房屋的总租金,求平均值用
        double allHouseTypesTotalCount = 0d;// 此类房型的所有房屋的数量，求平均值用
        double allHouseTypesAvgRental = 0d;// 此类房型的所有房屋的平均房价
        List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
        for (PropertyProject pp : projectList) {
          double ppHouseTypesTotalRental = 0d; // 此类房型某个小区的所有房屋的总租金
          double ppAvgEntirePrice = 0d;// 此房型的某小区的平均房租
          String propertyName = pp.getProjectName();
          List<RentContract> rentContracts = rentContractService.queryValidConditionalEntireHouses(wholeAvgPriceReport.getStartDate(), wholeAvgPriceReport.getEndDate(), pp.getId(), i, j);
          if (CollectionUtils.isNotEmpty(rentContracts)) {
            for (RentContract rc : rentContracts) {
              ppHouseTypesTotalRental += rc.getRental();
            }
            allHouseTypesTotalRental += ppHouseTypesTotalRental;
            allHouseTypesTotalCount += rentContracts.size();
            ppAvgEntirePrice = new BigDecimal(ppHouseTypesTotalRental).divide(new BigDecimal(rentContracts.size()), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
          }
          WholeAvgPriceReport tempR = new WholeAvgPriceReport();
          tempR.setWholeAvgHouseType(wholeAvgHouseType);
          tempR.setProjectName(propertyName);
          tempR.setWholeAvgPrice(ppAvgEntirePrice + "");
          totalPage.getList().add(tempR);
          totalPage.setCount(totalPage.getCount() + 1);
        }
        // 新增某种房型下的平均房租
        WholeAvgPriceReport houseTypeAvg = new WholeAvgPriceReport();
        houseTypeAvg.setWholeAvgHouseType(wholeAvgHouseType);
        houseTypeAvg.setProjectName("该房型总平均");
        if (allHouseTypesTotalCount > 0d) {
          allHouseTypesAvgRental = new BigDecimal(allHouseTypesTotalRental).divide(new BigDecimal(allHouseTypesTotalCount), 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        houseTypeAvg.setWholeAvgPrice(allHouseTypesAvgRental + "");
        totalPage.getList().add(houseTypeAvg);
        totalPage.setCount(totalPage.getCount() + 1);
      }
    }
    return totalPage;
  }

  /**
   * 毛利率统计报表-查询
   */
  @RequestMapping(value = {"grossProfit"})
  public String grossProfit(GrossProfitReport grossProfitReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("page", getGrossProfitList(grossProfitReport, request, response));
    return "modules/report/sales/grossProfit";
  }

  /**
   * 获得房间数量统计报表的数据
   */
  private Page<GrossProfitReport> getGrossProfitList(GrossProfitReport grossProfitReport, HttpServletRequest request, HttpServletResponse response) {
    Page<GrossProfitReport> totalPage = new Page<GrossProfitReport>(request, response, -1);
    totalPage.initialize();
    List<PropertyProject> projectList = propertyProjectService.findList(new PropertyProject());
    for (PropertyProject project : projectList) {
      if (StringUtils.isNotEmpty(project.getId())) {
        List<House> houseList = houseService.findHouseListByProjectId(project.getId());
        List<String> houseIdList = houseList.stream().map(House::getId).collect(Collectors.toList());
      }
    }
//    Collections.sort(totalPage.getList(), Collections.reverseOrder());// 按照放量大小排序
//    totalPage.getList().addAll(totalHouseRoomReportList);
    GrossProfitReport report = new GrossProfitReport();
    report.setName("test");
    report.setTotalProfit(1000);
    report.setProfitPercent("12.21");
    totalPage.getList().add(report);
    totalPage.setCount(1);
    return totalPage;
  }
}
