package com.thinkgem.jeesite.modules.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.report.dao.ReletRateReportDao;
import com.thinkgem.jeesite.modules.report.dao.ResultsReportDao;
import com.thinkgem.jeesite.modules.report.entity.ReletRateReport;
import com.thinkgem.jeesite.modules.report.entity.ResultsReport;

@Service
public class ReportService {
	@Autowired
	private ReletRateReportDao reletRateReportDao;
	@Autowired
	private ResultsReportDao resultsReportDao;
	
	public Page<ReletRateReport> reletRateReport(Page<ReletRateReport> page, ReletRateReport reletRateReport) {
		reletRateReport.setPage(page);
		page.setList(reletRateReportDao.reletRateReport(reletRateReport));
		return page;
    }
	
	public Page<ResultsReport> resultsReport(Page<ResultsReport> page, ResultsReport resultsReport) {
		resultsReport.setPage(page);
		page.setList(resultsReportDao.resultsReport(resultsReport));
		return page;
    }
}
