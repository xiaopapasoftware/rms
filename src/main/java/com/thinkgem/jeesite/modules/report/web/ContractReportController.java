package com.thinkgem.jeesite.modules.report.web;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.MapKeyHandle;
import com.thinkgem.jeesite.common.utils.excels.utils.ExcelUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.report.service.ContractReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

  @RequestMapping("page")
  public String forwardPage(){
    return "modules/report/sales/ContractReport";
  }

  @RequestMapping("export")
  public void exportContract(HttpServletRequest request, HttpServletResponse response, Model model){
    Page pageParam = new Page<Map>(request, response);
    pageParam.setPageSize(250);
    Page<Map> page = contractReportService.queryContractReport(pageParam);
    List<Map> reportEntities = page.getList();
    reportEntities = MapKeyHandle.keyToJavaProperty(reportEntities);
    logger.info("查询到合同数据为:" + reportEntities.toString());
    List<Map> dataList = new ArrayList<>();
    Map dataMap = new HashMap();
    dataMap.put("fieldsList",reportEntities);
    dataList.add(dataMap);
    ExcelUtils excelUtils = new ExcelUtils(dataList);
    excelUtils.setTemplatePath("/templates/report/contract_template.xls");
    try {
      excelUtils.export(request,response);
    } catch (IOException e) {
      logger.error(e.getMessage());
      e.printStackTrace();
    }
  }




}
