package com.thinkgem.jeesite.modules.report.web;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thinkgem.jeesite.common.filter.search.MatchType;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.PropertyType;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.filter.search.builder.SortBuilder;
import com.thinkgem.jeesite.common.support.MessageSupport;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excels.utils.ExcelUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.contract.enums.TradeDirectionEnum;
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

    @RequestMapping("inIndex")
    public String redirectInIndex() {
        return "modules/report/finance/financeInReport";
    }

    @RequestMapping("outIndex")
    public String redirectPutIndex() {
        return "modules/report/finance/financeOutReport";
    }


    @RequestMapping("query")
    @ResponseBody
    public Object queryFinance(HttpServletRequest request) {
        List<Sort> sorts = SortBuilder.create().addDesc("main.receipt_date").end();
        Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
        List<Map> reportEntities = financeReportService.queryFinace(getFilterParams(request), sorts);
        String tradeDirection = StringUtils.defaultIfBlank(request.getParameter("tradeDirection"), TradeDirectionEnum.IN.getValue());
        Map map = new HashMap(2);
        if (StringUtils.equals(tradeDirection, TradeDirectionEnum.IN.getValue())) {
            reportEntities = financeReportService.convertInFinance(reportEntities);
            map.put("dataList", reportEntities);
            map.put("totalAmount", financeReportService.calculateInTotalAmount(reportEntities));
        } else {
            reportEntities = financeReportService.convertOutFinance(reportEntities);
            map.put("dataList", reportEntities);
            map.put("totalAmount", financeReportService.calculateOutTotalAmount(reportEntities));
        }

        return MessageSupport.successDataTableMsg(page, map);
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
        String tradeDirection = StringUtils.defaultIfBlank(request.getParameter("tradeDirection"), TradeDirectionEnum.IN.getValue());
        String template = StringUtils.equals("0", tradeDirection) ? "finance_out_report_template.xls" : "finance_import_report_template.xls";
        String fileName = StringUtils.equals("0", tradeDirection) ? "收款" : "出款";
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
        String tradeDirection = StringUtils.defaultIfBlank(request.getParameter("tradeDirection"), TradeDirectionEnum.IN.getValue());
        String value = "";
        if (StringUtils.equals(tradeDirection, TradeDirectionEnum.IN.getValue())) {
            value = "1,3,4,5";
        } else {
            value = "6,7,8,9";
        }
        propertyFilters.add(new PropertyFilter(MatchType.IN, PropertyType.I, "main.trade_type", value));
        return propertyFilters;
    }

}
