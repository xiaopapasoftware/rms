package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.app.util.JsonUtil;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculateStrategy;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReport;
import com.thinkgem.jeesite.modules.profit.entity.GrossProfitReportVO;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import com.thinkgem.jeesite.modules.report.entity.GrossProfitCompareCondition;
import com.thinkgem.jeesite.modules.report.entity.GrossProfitFormCondition;
import com.thinkgem.jeesite.modules.report.entity.ReportCompareCondition;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

  private List<SelectItem> getCountyList() {
    SelectItemCondition condition = new SelectItemCondition();
    condition.setBusiness(SelectItemConstants.ORG);
    condition.setType(SelectItemConstants.COUNTY);
    return selectItemService.getSelectListByBusinessCode(condition);
  }

  @RequestMapping(value = {"getSubOrgList"})
  @ResponseBody
  public List<SelectItem> getSubOrgList(SelectItemCondition condition, HttpServletRequest request, HttpServletResponse response, Model model) {
    return selectItemService.getSelectListByBusinessCode(condition);
  }

  @RequiresPermissions("report:gross:view")
  @RequestMapping(value = {"listGrossProfit"})
  @ResponseBody
  public List<GrossProfitReport> listGrossProfit(GrossProfitFormCondition condition, HttpServletRequest request, HttpServletResponse response, Model model) {
    return condition.isEmpty() ? null : buildReportListByReportVO(calculateStrategy.calculateReportVO(buildConditionByFormCondition(condition)));
  }

  private GrossProfitCondition buildConditionByFormCondition(GrossProfitFormCondition condition) {
    GrossProfitCondition profitCondition = new GrossProfitCondition();
    profitCondition.setStartDate(condition.getStart());
    profitCondition.setEndDate(condition.getEnd());
    if (!StringUtils.isEmpty(condition.getHouse())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.HOUSE);
      profitCondition.setId(condition.getHouse());
    } else if (!StringUtils.isEmpty(condition.getBuilding())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.BUILDING);
      profitCondition.setId(condition.getBuilding());
    } else if (!StringUtils.isEmpty(condition.getProject())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.PROJECT);
      profitCondition.setId(condition.getProject());
    } else if (!StringUtils.isEmpty(condition.getArea())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.AREA);
      profitCondition.setId(condition.getArea());
    } else if (!StringUtils.isEmpty(condition.getCenter())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.CENTER);
      profitCondition.setId(condition.getCenter());
    } else {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.COUNTY);
      profitCondition.setId(condition.getCounty());
    }
    return profitCondition;
  }

  /**
   * 毛利率统计报表对比查询
   */
  @RequiresPermissions("report:gross:view")
  @RequestMapping(value = "grossProfitCompare")
  public String grossProfitCompare(HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("categoryList", SelectItemService.getCommonReportCompareItem());
    return "modules/report/gross/grossProfitCompare";
  }

  @RequestMapping(value = {"listGrossProfitCompare"})
  @ResponseBody
  public List<GrossProfitReport> listGrossProfitCompare(GrossProfitCompareCondition condition, HttpServletRequest request, HttpServletResponse response, Model model) {
    try {
      List<ReportCompareCondition> conditionList = JsonUtil.jsonToCollection(request.getParameter("compareData"), List.class, ReportCompareCondition.class);
      condition.setConditionList(conditionList);
    } catch (Exception e) {
      logger.error("listGrossProfitCompare error ", e);
    }
    return condition.isEmpty() ? null : buildReportListByReportVO(calculateStrategy.calculateReportCompareVO(buildConditionByCompareCondition(condition)));
  }

  private List<GrossProfitCondition> buildConditionByCompareCondition(GrossProfitCompareCondition condition) {
    return condition.getConditionList().stream().filter(i -> !(StringUtils.isEmpty(i.getId())||StringUtils.isEmpty(i.getType()))).map(compareCondition -> {
      GrossProfitCondition profitCondition = new GrossProfitCondition();
      profitCondition.setId(compareCondition.getId());
      profitCondition.setTypeEnum(GrossProfitTypeEnum.valueOf(compareCondition.getType()));
      profitCondition.setStartDate(condition.getStart());
      profitCondition.setEndDate(condition.getEnd());
      return profitCondition;
    }).collect(Collectors.toList());
  }

  private List<GrossProfitReport> buildReportListByReportVO(GrossProfitReportVO reportVO) {
    List<GrossProfitReport> reportList = new ArrayList<>();
    reportList.add(reportVO.getParent());
    if (!CollectionUtils.isEmpty(reportVO.getChildReportList())) {
      reportVO.getChildReportList().sort(Comparator.comparing(GrossProfitReport::getName));
      reportList.addAll(reportVO.getChildReportList());
    }
    return reportList;
  }

}
