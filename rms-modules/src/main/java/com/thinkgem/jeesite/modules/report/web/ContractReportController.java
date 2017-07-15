package com.thinkgem.jeesite.modules.report.web;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thinkgem.jeesite.common.filter.search.MatchType;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.PropertyType;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.filter.search.builder.PropertyFilterBuilder;
import com.thinkgem.jeesite.common.filter.search.builder.SortBuilder;
import com.thinkgem.jeesite.common.support.MessageSupport;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excels.utils.ExcelUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.service.ContractReportService;
import com.thinkgem.jeesite.modules.report.service.ReportComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangganggang
 */
@Controller
@RequestMapping(value = "${adminPath}/report/contract/")
public class ContractReportController extends BaseController {

  @Autowired
  private ContractReportService contractReportService;

  @Autowired
  private ReportComponentService reportComponentService;

  @RequestMapping("index")
  public String redirectIndex() {
    return "modules/report/contract/contractReport";
  }

  @RequestMapping("query")
  @ResponseBody
  public Object queryContract(HttpServletRequest request) {
    List<Sort> sorts = SortBuilder.create().addAsc("trc.id").end();
    Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
    List<Map> reportEntities = contractReportService.queryContract(getFilterParams(request), sorts);
    reportEntities = reportComponentService.fillTenantInfo(reportEntities);
    return MessageSupport.successDataTableMsg(page, reportEntities);
  }

  @RequestMapping("export")
  public void exportContract(HttpServletRequest request, HttpServletResponse response) {
    List<Sort> sorts = SortBuilder.create().addAsc("trc.id").end();
    // Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
    List<Map> reportEntities = contractReportService.queryContract(getFilterParams(request), sorts);
    reportEntities = reportComponentService.fillTenantInfo(reportEntities);
    logger.debug("查询到合同数据为:" + reportEntities.toString());
    List<Map> dataList = new ArrayList<>();
    Map dataMap = new HashMap();
    dataMap.put("fieldsList", reportEntities);
    dataMap.put("parametersMap", request.getParameterMap());
    dataList.add(dataMap);
    ExcelUtils excelUtils = new ExcelUtils(dataList);
    excelUtils.setTemplatePath("/templates/report/contract_template.xls");
    excelUtils.setFilename("合同表报表_" + DateUtils.getDate() + ".xls");
    try {
      excelUtils.export(request, response);
    } catch (IOException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * 添加查询判断的条件
   **/
  private List<PropertyFilter> getFilterParams(HttpServletRequest request) {
    /** 模糊条件查询 **/
    PropertyFilterBuilder propertyFilterBuilder =
        PropertyFilterBuilder.create().matchTye(MatchType.LIKE).propertyType(PropertyType.S).add("trc.contract_code", StringUtils.trimToEmpty(request.getParameter("contractCode")));

    /** 楼宇号 **/
    String buildingName = request.getParameter("buildingName");
    if (StringUtils.isNotBlank(buildingName)) {
      propertyFilterBuilder.add("tb.building_name", StringUtils.trimToEmpty(buildingName));
    }

    /** 房号 **/
    String houseNo = request.getParameter("houseNo");
    if (StringUtils.isNotBlank(houseNo)) {
      propertyFilterBuilder.add("th.house_no", StringUtils.trimToEmpty(houseNo));
    }

    /** 室号 **/
    String roomNo = request.getParameter("roomNo");
    if (StringUtils.isNotBlank(roomNo)) {
      propertyFilterBuilder.add("tr.room_no", StringUtils.trimToEmpty(roomNo));
    }

    /** 合同业务状态 **/
    String dictValue = request.getParameter("dictValue");
    if (StringUtils.isNotBlank(dictValue)) {
      propertyFilterBuilder.matchTye(MatchType.EQ).propertyType(PropertyType.S).add("trc.contract_busi_status", StringUtils.trimToEmpty(dictValue));
    }

    /** 物业项目 **/
    String projectValue = request.getParameter("projectValue");
    if (StringUtils.isNotBlank(projectValue)) {
      propertyFilterBuilder.matchTye(MatchType.EQ).propertyType(PropertyType.S).add("tpp.id", StringUtils.trimToEmpty(projectValue));
    }

    /** 合同类型 **/
    String signType = request.getParameter("signType");
    if (StringUtils.isNotBlank(signType)) {
      propertyFilterBuilder.matchTye(MatchType.EQ).propertyType(PropertyType.S).add("trc.sign_type", StringUtils.trimToEmpty(signType));
    }

    /** 签订日期范围 **/
    String signDateStart = request.getParameter("signDateStart");
    if (StringUtils.isNotBlank(signDateStart)) {
      propertyFilterBuilder.matchTye(MatchType.GE).propertyType(PropertyType.D).add("trc.sign_date", StringUtils.trimToEmpty(signDateStart));
    }
    String signDateEnd = request.getParameter("signDateEnd");
    if (StringUtils.isNotBlank(signDateEnd)) {
      propertyFilterBuilder.matchTye(MatchType.LE).propertyType(PropertyType.D).add("trc.sign_date", StringUtils.trimToEmpty(signDateEnd));
    }

    /** 合同开始日期范围 **/
    String startDateBegin = request.getParameter("startDateBegin");
    if (StringUtils.isNotBlank(startDateBegin)) {
      propertyFilterBuilder.matchTye(MatchType.GE).propertyType(PropertyType.D).add("trc.start_date", StringUtils.trimToEmpty(startDateBegin));
    }
    String startDateEnd = request.getParameter("startDateEnd");
    if (StringUtils.isNotBlank(startDateEnd)) {
      propertyFilterBuilder.matchTye(MatchType.LE).propertyType(PropertyType.D).add("trc.start_date", StringUtils.trimToEmpty(startDateEnd));
    }

    /** 合同结束日期范围 **/
    String expiredDateBegin = request.getParameter("expiredDateBegin");
    if (StringUtils.isNotBlank(expiredDateBegin)) {
      propertyFilterBuilder.matchTye(MatchType.GE).propertyType(PropertyType.D).add("trc.expired_date", StringUtils.trimToEmpty(expiredDateBegin));
    }
    String expiredDateEnd = request.getParameter("expiredDateEnd");
    if (StringUtils.isNotBlank(expiredDateEnd)) {
      propertyFilterBuilder.matchTye(MatchType.LE).propertyType(PropertyType.D).add("trc.expired_date", StringUtils.trimToEmpty(expiredDateEnd));
    }

    return propertyFilterBuilder.end();
  }

}
