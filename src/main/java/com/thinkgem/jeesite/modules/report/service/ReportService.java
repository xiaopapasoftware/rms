package com.thinkgem.jeesite.modules.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.report.dao.ReletRateReportDao;
import com.thinkgem.jeesite.modules.report.entity.ReletRateReport;

@Service
public class ReportService {
	@Autowired
	private ReletRateReportDao reletRateReportDao;
	
	public Page<ReletRateReport> reletRateReport(Page<ReletRateReport> page, ReletRateReport reletRateReport) {
		reletRateReport.setPage(page);
		page.setList(reletRateReportDao.reletRateReport(reletRateReport));
		return page;
    }
}
