package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculateStrategy;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import com.thinkgem.jeesite.modules.report.entity.GrossProfitFormCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
  @RequestMapping(value = "grossProfit")
  public String grossProfit(HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("companyList", getCompanyList());
    return "modules/report/gross/grossProfit";
  }

  @RequestMapping(value = {"listGrossProfit"})
  public String listGrossProfit(GrossProfitFormCondition condition, HttpServletRequest request, HttpServletResponse response, Model model) {
    if (!condition.isEmpty()) {
      GrossProfitCondition grossProfitCondition = buildConditionByFormCondition(condition);
      model.addAttribute("report", calculateStrategy.calculateReportVO(grossProfitCondition));
    }
    model.addAttribute("companyList", getCompanyList());
    return "modules/report/gross/grossProfit";
  }

  @RequestMapping(value = {"getSubOrgList"})
  @ResponseBody
  public List<SelectItem> getSubOrgList(SelectItemCondition condition, HttpServletRequest request, HttpServletResponse response, Model model) {
    return selectItemService.getSelectListByBusinessCode(condition);
  }

  private GrossProfitCondition buildConditionByFormCondition(GrossProfitFormCondition condition) {
    GrossProfitCondition profitCondition = new GrossProfitCondition();
    profitCondition.setStartDate(new Date(2015 - 1900, 5, 25));
    profitCondition.setEndDate(new Date(2015 - 1900, 8, 24));
    if (!StringUtils.isEmpty(condition.getHouse())) {
      profitCondition.setTypeEnum(GrossProfitTypeEnum.House);
      profitCondition.setId(condition.getHouse());
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
      profitCondition.setTypeEnum(GrossProfitTypeEnum.Company);
      profitCondition.setId(condition.getCompany());
    }
    return profitCondition;
  }

  private List<SelectItem> getCompanyList() {
    SelectItemCondition condition = new SelectItemCondition();
    condition.setBusiness(SelectItemConstants.org);
    condition.setType(SelectItemConstants.company);
    return selectItemService.getSelectListByBusinessCode(condition);
  }
}
