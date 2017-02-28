package com.thinkgem.jeesite.modules.report.web;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.thinkgem.jeesite.common.support.MessageSupport;
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
    public Object queryContract(HttpServletRequest request, HttpServletResponse response) {
        Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
        List<Map> reportEntities = contractReportService.queryContractReport();
        reportEntities = MapKeyHandle.keyToJavaProperty(reportEntities);
        return MessageSupport.successDataTableMsg(page, reportEntities);
    }


    @RequestMapping("export")
    public void exportContract(HttpServletRequest request, HttpServletResponse response) {
        Page page = PageHelper.startPage(StringUtils.isNull(request.getParameter("pageNum"), 1), StringUtils.isNull(request.getParameter("pageSize"), 15));
        List<Map> reportEntities = contractReportService.queryContractReport();
        reportEntities = MapKeyHandle.keyToJavaProperty(reportEntities);
        logger.info("查询到合同数据为:" + reportEntities.toString());
        List<Map> dataList = new ArrayList<>();
        Map dataMap = new HashMap();
        dataMap.put("fieldsList", reportEntities);
        dataList.add(dataMap);
        ExcelUtils excelUtils = new ExcelUtils(dataList);
        excelUtils.setTemplatePath("/templates/report/contract_template.xls");
        try {
            excelUtils.export(request, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }


}
