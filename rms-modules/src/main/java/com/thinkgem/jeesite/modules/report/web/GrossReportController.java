package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculateStrategy;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * @author wangshujin
 */
@Controller
@RequestMapping(value = "${adminPath}/report/gross")
public class GrossReportController extends BaseController {

  @Autowired
  private PropertyProjectService propertyProjectService;

  @Autowired
  private GrossProfitCalculateStrategy calculateStrategy;

  /**
   * 毛利率统计报表-查询
   */
  @RequestMapping(value = "grossProfit")
  public String grossProfit(GrossProfitReport grossProfitReport, HttpServletRequest request, HttpServletResponse response, Model model) {
    GrossProfitCondition condition = new GrossProfitCondition();
    condition.setTypeEnum(GrossProfitTypeEnum.House);
    condition.setId("2a78c4d25dc545e78e2bf063cacd6769");
    condition.setStartDate(new Date(2016 - 1900, 4, 25));
    condition.setEndDate(new Date(2017 - 1900, 6, 24));
    calculateStrategy.calculateGrossProfit(condition);
    model.addAttribute("page", getGrossProfitList(grossProfitReport, request, response));
    return "modules/report/gross/grossProfit";
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
