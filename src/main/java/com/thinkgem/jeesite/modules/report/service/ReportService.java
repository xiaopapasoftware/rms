package com.thinkgem.jeesite.modules.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.report.dao.LandlordReportDao;
import com.thinkgem.jeesite.modules.report.dao.PaymentReportDao;
import com.thinkgem.jeesite.modules.report.dao.ReletRateReportDao;
import com.thinkgem.jeesite.modules.report.dao.RentDataReportDao;
import com.thinkgem.jeesite.modules.report.dao.ResultsReportDao;
import com.thinkgem.jeesite.modules.report.entity.LandlordReport;
import com.thinkgem.jeesite.modules.report.entity.PaymentReport;
import com.thinkgem.jeesite.modules.report.entity.ReletRateReport;
import com.thinkgem.jeesite.modules.report.entity.RentDataReport;
import com.thinkgem.jeesite.modules.report.entity.ResultsReport;

@Service
public class ReportService {
	@Autowired
	private ReletRateReportDao reletRateReportDao;
	@Autowired
	private ResultsReportDao resultsReportDao;
	
	@Autowired
	private RentDataReportDao rentDataReportDao;
	@Autowired
	private LandlordReportDao landlordReportDao;
	@Autowired
	private PaymentReportDao paymentReportDao;
	
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
	
	public Page<RentDataReport> rentDataReport(Page<RentDataReport> page, RentDataReport rentDataReport) {
		rentDataReport.setPage(page);
		page.setList(rentDataReportDao.rentDataReport(rentDataReport));
		return page;
    }
	
	public Page<LandlordReport> landlordReport(Page<LandlordReport> page, LandlordReport landlordReport) {
		landlordReport.setPage(page);
		page.setList(landlordReportDao.landlordReport(landlordReport));
		return page;
    }
	
	public Page<PaymentReport> paymentReport(Page<PaymentReport> page, PaymentReport paymentReport) {
		paymentReport.setPage(page);
		page.setList(paymentReportDao.paymentReport(paymentReport));
		return page;
    }
}
