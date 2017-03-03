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
import com.thinkgem.jeesite.common.utils.MapKeyHandle;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.excels.utils.ExcelUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.service.ContractReportService;
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

    @RequestMapping("query")
    @ResponseBody
    public Object queryContract(HttpServletRequest request) {
        List<Sort> sorts = SortBuilder.create().addAsc("").end();
        Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
        List<Map> reportEntities = contractReportService.queryContractReport(getFilterParams(request), sorts);
        reportEntities = MapKeyHandle.keyToJavaProperty(reportEntities);
        return MessageSupport.successDataTableMsg(page, reportEntities);
    }

    @RequestMapping("export")
    public void exportContract(HttpServletRequest request, HttpServletResponse response) {
        List<Sort> sorts = SortBuilder.create().addAsc("").end();
        Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
        List<Map> reportEntities = contractReportService.queryContractReport(getFilterParams(request), sorts);
        reportEntities = MapKeyHandle.keyToJavaProperty(reportEntities);

        logger.debug("查询到合同数据为:" + reportEntities.toString());
        List<Map> dataList = new ArrayList<>();
        Map dataMap = new HashMap();
        dataMap.put("fieldsList", reportEntities);
        dataMap.put("parametersMap", request.getParameterMap());
        dataList.add(dataMap);
        ExcelUtils excelUtils = new ExcelUtils(dataList);
        excelUtils.setTemplatePath("/templates/report/contract_template.xls");
        excelUtils.setFilename("合同表报_" + DateUtils.getDate());
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
                .add("th.house_no", StringUtils.trimToEmpty(request.getParameter("houseNo")))
                .add("tb.building_name", StringUtils.trimToEmpty(request.getParameter("buildingName")))
                .add("tr.room_no", StringUtils.trimToEmpty(request.getParameter("roomNo")));

        /** 签订日期范围 **/
        if (StringUtils.isNotBlank(request.getParameter("signDateStart"))) {
            propertyFilterBuilder.matchTye(MatchType.GE).propertyType(PropertyType.D)
                    .add("trc.sign_date", StringUtils.trimToEmpty(request.getParameter("signDateStart")));
        }
        if (StringUtils.isNotBlank(request.getParameter("signDateEnd"))) {
            propertyFilterBuilder.matchTye(MatchType.LE).propertyType(PropertyType.D)
                    .add("trc.sign_date", StringUtils.trimToEmpty(request.getParameter("signDateEnd")));
        }

        /** 合同开始日期范围 **/
        if (StringUtils.isNotBlank(request.getParameter("startDateBegin"))) {
            propertyFilterBuilder.matchTye(MatchType.GE).propertyType(PropertyType.D)
                    .add("trc.start_date", StringUtils.trimToEmpty(request.getParameter("startDateBegin")));
        }
        if (StringUtils.isNotBlank(request.getParameter("startDateEnd"))) {
            propertyFilterBuilder.matchTye(MatchType.LE).propertyType(PropertyType.D)
                    .add("trc.start_date", StringUtils.trimToEmpty(request.getParameter("startDateEnd")));
        }

        /** 合同结束日期范围 **/
        if (StringUtils.isNotBlank(request.getParameter("expiredDateBegin"))) {
            propertyFilterBuilder.matchTye(MatchType.GE).propertyType(PropertyType.D)
                    .add("trc.expired_date", StringUtils.trimToEmpty(request.getParameter("expiredDateBegin")));
        }
        if (StringUtils.isNotBlank(request.getParameter("expiredDateEnd"))) {
            propertyFilterBuilder.matchTye(MatchType.LE).propertyType(PropertyType.D)
                    .add("trc.expired_date", StringUtils.trimToEmpty(request.getParameter("expiredDateEnd")));
        }

        return propertyFilterBuilder.end();
    }

}
