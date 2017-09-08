package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculateStrategy;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


/**
 * @author wangshujin
 */
@Controller
@RequestMapping(value = "${adminPath}/report/gross")
public class GrossReportController extends BaseController {

  @Autowired
  private GrossProfitCalculateStrategy calculateStrategy;

  /**
   * 毛利率统计报表-查询
   */
  @RequestMapping(value = "grossProfit")
  public String grossProfit(HttpServletRequest request, HttpServletResponse response, Model model) {
    GrossProfitCondition condition = new GrossProfitCondition();
    condition.setTypeEnum(GrossProfitTypeEnum.House);
    condition.setId("48b2b667b01b443baed2203c6456e279");
    condition.setStartDate(new Date(2015 - 1900, 5, 25));
    condition.setEndDate(new Date(2015 - 1900, 8, 24));
    model.addAttribute("report", calculateStrategy.calculateReportVO(condition));
    return "modules/report/gross/grossProfit";
  }

  @RequestMapping(value = {"listGrossProfit"})
  public String listGrossProfit(GrossProfitCondition condition, HttpServletRequest request, HttpServletResponse response, Model model) {
    model.addAttribute("report", calculateStrategy.calculateReportVO(condition));
    return "modules/report/gross/grossProfit";
  }


}
