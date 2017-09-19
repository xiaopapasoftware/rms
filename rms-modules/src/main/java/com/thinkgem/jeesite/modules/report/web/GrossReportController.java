package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculateStrategy;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReportVO;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import com.thinkgem.jeesite.modules.report.entity.GrossProfitFormCondition;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiao
 */
@Controller
@RequestMapping(value = "${adminPath}/report/gross")
public class GrossReportController extends BaseController {

  @Autowired
  private GrossProfitCalculateStrategy calculateStrategy;

  @Autowired
  private SelectItemService selectItemService;

  /**
   * 毛利率统计报表-查询
   */
  @RequiresPermissions("report:gross:view")
  @RequestMapping(value = "grossProfit")
  public String grossProfit(HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("countyList", getCountyList());
    return "modules/report/gross/grossProfit";
  }

  @RequiresPermissions("report:gross:view")
  @RequestMapping(value = {"listGrossProfit"})
  @ResponseBody
  public List<GrossProfitReport> listGrossProfit(GrossProfitFormCondition condition, HttpServletRequest request, HttpServletResponse response, Model model) {
    ArrayList<GrossProfitReport> reportList = new ArrayList<>();
    if (!condition.isEmpty()) {
      GrossProfitCondition grossProfitCondition = buildConditionByFormCondition(condition);
      GrossProfitReportVO reportVO = calculateStrategy.calculateReportVO(grossProfitCondition);
      reportList.add(reportVO.getParent());
      if (!CollectionUtils.isEmpty(reportVO.getChildReportList())) {
        reportList.addAll(reportVO.getChildReportList());
      }
      return reportList;
    } else {
      return null;
    }
  }

  @RequestMapping(value = {"getSubOrgList"})
  @ResponseBody
  public List<SelectItem> getSubOrgList(SelectItemCondition condition, HttpServletRequest request, HttpServletResponse response, Model model) {
    return selectItemService.getSelectListByBusinessCode(condition);
  }

  private GrossProfitCondition buildConditionByFormCondition(GrossProfitFormCondition condition) {
    GrossProfitCondition profitCondition = new GrossProfitCondition();
    profitCondition.setStartDate(condition.getStart());
    profitCondition.setEndDate(condition.getEnd());
    if (!StringUtils.isEmpty(condition.getHouse())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.House);
      profitCondition.setId(condition.getHouse());
    } else if (!StringUtils.isEmpty(condition.getBuilding())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.Building);
      profitCondition.setId(condition.getBuilding());
    } else if (!StringUtils.isEmpty(condition.getProject())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.Project);
      profitCondition.setId(condition.getProject());
    } else if (!StringUtils.isEmpty(condition.getArea())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.Area);
      profitCondition.setId(condition.getArea());
    } else if (!StringUtils.isEmpty(condition.getCenter())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.Center);
      profitCondition.setId(condition.getCenter());
    } else {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.County);
      profitCondition.setId(condition.getCounty());
    }
    return profitCondition;
  }

  private List<SelectItem> getCountyList() {
    SelectItemCondition condition = new SelectItemCondition();
    condition.setBusiness(SelectItemConstants.org);
    condition.setType(SelectItemConstants.county);
    return selectItemService.getSelectListByBusinessCode(condition);
  }
}
