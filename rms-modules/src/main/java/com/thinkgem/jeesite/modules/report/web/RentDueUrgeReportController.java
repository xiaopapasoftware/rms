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
import com.thinkgem.jeesite.modules.report.service.RentDueUrgeReportService;
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
@RequestMapping(value = "${adminPath}/report/rentdueurge/")
public class RentDueUrgeReportController extends BaseController {

    @Autowired
    private RentDueUrgeReportService rentDueUrgeReportService;

    @Autowired
    private ReportComponentService reportComponentService;

    @RequestMapping("index")
    public String redirectIndex(){
        return "modules/report/contract/rentDueUrgeReport";
    }


    @RequestMapping("query")
    @ResponseBody
    public Object queryRentDueUrge(HttpServletRequest request) {
        List<Sort> sorts = SortBuilder.create().addAsc("trc.id").end();
        Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
        List<Map> reportEntities = rentDueUrgeReportService.queryRentDueUrge(getFilterParams(request), sorts);

        reportEntities = reportComponentService.fillTenantInfo(reportEntities);

        return MessageSupport.successDataTableMsg(page, reportEntities);
    }

    @RequestMapping("export")
    public void exportRentDueUrge(HttpServletRequest request, HttpServletResponse response) {
        List<Sort> sorts = SortBuilder.create().addAsc("trc.id").end();
        //Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
        List<Map> reportEntities = rentDueUrgeReportService.queryRentDueUrge(getFilterParams(request), sorts);
        reportEntities = reportComponentService.fillTenantInfo(reportEntities);

        logger.debug("查询到待催款数据为:" + reportEntities.toString());
        List<Map> dataList = new ArrayList<>();
        Map dataMap = new HashMap();
        dataMap.put("fieldsList", reportEntities);
        dataMap.put("parametersMap", request.getParameterMap());
        dataList.add(dataMap);
        ExcelUtils excelUtils = new ExcelUtils(dataList);
        excelUtils.setTemplatePath("/templates/report/rent_due_urge_template.xls");
        excelUtils.setFilename("房租到期催款报表_" + DateUtils.getDate() + ".xls");
        try {
            excelUtils.export(request, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }


    /** 添加查询判断的条件 **/
    private List<PropertyFilter> getFilterParams(HttpServletRequest request) {
        /** 模糊条件查询 **/
        PropertyFilterBuilder propertyFilterBuilder = PropertyFilterBuilder.create().matchTye(MatchType.LIKE).propertyType(PropertyType.S)
                .add("trc.contract_code", StringUtils.trimToEmpty(request.getParameter("contractCode")));

        /** 服务管家 **/
        String serverName = request.getParameter("serverName");
        if (StringUtils.isNotBlank(serverName)) {
            propertyFilterBuilder.add("su.name", StringUtils.trimToEmpty(serverName));
        }

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


        /** 物业项目 **/
        String projectValue = request.getParameter("projectValue");
        if (StringUtils.isNotBlank(projectValue)) {
            propertyFilterBuilder.matchTye(MatchType.EQ).propertyType(PropertyType.S)
                    .add("tpp.id", StringUtils.trimToEmpty(projectValue));
        }


        /** 到期提醒范围 **/
        String freeDayBegin = request.getParameter("freeDayBegin");
        if (StringUtils.isNotBlank(freeDayBegin)) {
            propertyFilterBuilder.matchTye(MatchType.GE).propertyType(PropertyType.I)
                    .add("temp.free_day", StringUtils.trimToEmpty(freeDayBegin));
        }
        String freeDayEnd = request.getParameter("freeDayEnd");
        if (StringUtils.isNotBlank(freeDayEnd)) {
            propertyFilterBuilder.matchTye(MatchType.LE).propertyType(PropertyType.I)
                    .add("temp.free_day", StringUtils.trimToEmpty(freeDayEnd));
        }

        return propertyFilterBuilder.end();
    }

}
