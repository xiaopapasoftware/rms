package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.enums.ViewMessageTypeEnum;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.common.service.SelectItemService;
import com.thinkgem.jeesite.modules.lease.LeaseCalculateStrategy;
import com.thinkgem.jeesite.modules.lease.condition.LeaseStatisticsCondition;
import com.thinkgem.jeesite.modules.lease.entity.LeaseStatistics;
import com.thinkgem.jeesite.modules.lease.entity.LeaseStatisticsVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
  @RequiresPermissions("report:lease:view")
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

  @RequestMapping(value = "exportLeaseStatistics")
  public void exportLeaseStatistics(LeaseStatisticsCondition condition, HttpServletResponse response, Model model) {
    String fileName = "出租率统计报表" + DateUtils.formatDate(condition.getStartDate()) + "至" + DateUtils.formatDate(condition.getEndDate());
      List<LeaseStatistics> statisticsList = buildListByVO(calculateStrategy.calculateLeaseVO(fillCondition(condition)));
    try {
      new ExportExcel(fileName, LeaseStatistics.class).setDataList(statisticsList).write(response, fileName  + ".xlsx").dispose();
    } catch (Exception e) {
      addMessage(model, ViewMessageTypeEnum.ERROR, "导出出租率统计报表失败！失败信息：" + e.getMessage());
    }
  }

  @RequiresPermissions("report:lease:view")
  @RequestMapping(value = "listLeaseStatistics")
  @ResponseBody
  public List<LeaseStatistics> listLeaseStatistics(LeaseStatisticsCondition condition) {
    return buildListByVO(calculateStrategy.calculateLeaseVO(fillCondition(condition)));
  }

  private List<LeaseStatistics> buildListByVO(LeaseStatisticsVO leaseStatisticsVO) {
    List<LeaseStatistics> statisticsList = new ArrayList<>();
    statisticsList.add(leaseStatisticsVO.getParent());
    if (!CollectionUtils.isEmpty(leaseStatisticsVO.getChildList())) {
      statisticsList.addAll(leaseStatisticsVO.getChildList());
    }
    return statisticsList;
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
