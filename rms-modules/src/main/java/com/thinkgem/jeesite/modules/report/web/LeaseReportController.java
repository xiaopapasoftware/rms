package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.lease.LeaseCalculateStrategy;
import com.thinkgem.jeesite.modules.lease.condition.LeaseStatisticsCondition;
import com.thinkgem.jeesite.modules.lease.entity.LeaseStatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author xiao
 */
@Controller
@RequestMapping(value = "${adminPath}/report/lease")
public class LeaseReportController extends BaseController {

  @Autowired
  private SelectItemService selectItemService;

  @Autowired
  private LeaseCalculateStrategy calculateStrategy;

  /**
   * 出租率统计报表-查询
   */
  @RequestMapping(value = "leaseStatistics")
  public String leaseStatistics(Model model) {
    model.addAttribute("countyList", getCountyList());
    return "modules/report/lease/leaseStatistics";
  }

  private List<SelectItem> getCountyList() {
    SelectItemCondition condition = new SelectItemCondition();
    condition.setBusiness(SelectItemConstants.ORG);
    condition.setType(SelectItemConstants.COUNTY);
    return selectItemService.getSelectListByBusinessCode(condition);
  }

  @RequestMapping(value = "listLeaseStatistics")
  @ResponseBody
  public LeaseStatisticsVO listLeaseStatistics(LeaseStatisticsCondition condition) {
    return calculateStrategy.calculateLease(fillCondition(condition));
  }

  private LeaseStatisticsCondition fillCondition(LeaseStatisticsCondition condition) {
    if (!StringUtils.isEmpty(condition.getProject())) {
      condition.setType(SelectItemConstants.PROJECT);
      condition.setId(condition.getProject());
    } else if (!StringUtils.isEmpty(condition.getArea())) {
      condition.setType(SelectItemConstants.AREA);
      condition.setId(condition.getArea());
    } else if (!StringUtils.isEmpty(condition.getCenter())) {
      condition.setType(SelectItemConstants.CENTER);
      condition.setId(condition.getCenter());
    } else {
      condition.setType(SelectItemConstants.COUNTY);
      condition.setId(condition.getCounty());
    }
    return condition;
  }

}
