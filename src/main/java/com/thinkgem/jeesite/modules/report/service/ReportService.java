package com.thinkgem.jeesite.modules.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.report.dao.HouseReportDao;
import com.thinkgem.jeesite.modules.report.dao.HouseRoomReportDao;
import com.thinkgem.jeesite.modules.report.entity.HouseReport;
import com.thinkgem.jeesite.modules.report.entity.HouseRoomReport;

/**
 * @author wangshujin
 */
@Service
public class ReportService {
  @Autowired
  private HouseRoomReportDao houseRoomReportDao;
  @Autowired
  private HouseReportDao houseReportDao;

  public Page<HouseReport> housesCount(Page<HouseReport> page, HouseReport houseReport) {
    houseReport.setPage(page);
    page.setList(houseReportDao.housesCount(houseReport));
    return page;
  }

  public Page<HouseRoomReport> roomsCount(Page<HouseRoomReport> page, HouseRoomReport houseRoomReport) {
    houseRoomReport.setPage(page);
    page.setList(houseRoomReportDao.roomsCount(houseRoomReport));
    return page;
  }
}
