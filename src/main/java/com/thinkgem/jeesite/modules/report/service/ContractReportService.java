package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.report.dao.ContractReportDao;
import com.thinkgem.jeesite.modules.report.dao.HouseReportDao;
import com.thinkgem.jeesite.modules.report.dao.HouseRoomReportDao;
import com.thinkgem.jeesite.modules.report.entity.HouseReport;
import com.thinkgem.jeesite.modules.report.entity.HouseRoomReport;
import com.thinkgem.jeesite.modules.report.entity.ReportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wangshujin
 */
@Service
public class ContractReportService {

  @Autowired
  private ContractReportDao contractReportDao;


  public Page<Map> queryContractReport(Page<Map> page) {
    page.setList(contractReportDao.queryContractReport());
    return page;
  }
}
