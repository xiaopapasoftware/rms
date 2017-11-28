package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.service.RentContractService;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.report.entity.EntireRentRateReport;
import com.thinkgem.jeesite.modules.report.entity.JointRentRateReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

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
  private RentContractService rentContractService;
  @Autowired
  private RoomService roomService;

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

}
