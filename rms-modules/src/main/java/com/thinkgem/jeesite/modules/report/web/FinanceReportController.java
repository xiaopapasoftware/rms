package com.thinkgem.jeesite.modules.report.web;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.filter.search.builder.SortBuilder;
import com.thinkgem.jeesite.common.support.MessageSupport;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excels.utils.ExcelUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.service.FinanceReportService;
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
@RequestMapping(value = "${adminPath}/report/finance/")
public class FinanceReportController extends BaseController {

    @Autowired
    private FinanceReportService financeReportService;

    @RequestMapping("index")
    public String redirectIndex() {
        return "modules/report/finance/financeReport";
    }


    @RequestMapping("query")
    @ResponseBody
    public Object queryFinance(HttpServletRequest request) {
        List<Sort> sorts = SortBuilder.create().addDesc("main.receipt_date").end();
        Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
        List<Map> reportEntities = financeReportService.queryFinace(getFilterParams(request), sorts);
        reportEntities = financeReportService.convertOutFinance(reportEntities);
        return MessageSupport.successDataTableMsg(page, reportEntities);
    }

    @RequestMapping("export")
    public void exportContract(HttpServletRequest request, HttpServletResponse response) {
        List<Sort> sorts = SortBuilder.create().addDesc("main.receipt_date").end();
        List<Map> reportEntities = financeReportService.queryFinace(getFilterParams(request), sorts);
        reportEntities = financeReportService.convertOutFinance(reportEntities);
        logger.debug("查询到数据为:" + reportEntities.toString());
        List<Map> dataList = new ArrayList<>();
        Map dataMap = new HashMap();
        dataMap.put("fieldsList", reportEntities);
        dataMap.put("parametersMap", request.getParameterMap());
        dataList.add(dataMap);
        ExcelUtils excelUtils = new ExcelUtils(dataList);
        String tradeDirection = request.getParameter("filter_eqi_trade_direction") == null ? "1" : request.getParameter("filter_eqi_trade_direction");
        String template = StringUtils.equals("0",tradeDirection)?"finance_out_report_template.xls":"finance_import_report_template.xls";
        String fileName = StringUtils.equals("0",tradeDirection)?"收款":"出款";
        excelUtils.setTemplatePath("/templates/report/" + template);
        excelUtils.setFilename(fileName + "报表_" + DateUtils.getDate() + ".xls");
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

        List<PropertyFilter> propertyFilters = getFilter(request);

        return propertyFilters;
    }

}
