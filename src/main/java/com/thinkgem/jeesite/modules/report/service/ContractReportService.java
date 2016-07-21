package com.thinkgem.jeesite.modules.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.ContractReportDao;
import com.thinkgem.jeesite.modules.report.dao.ExpireReportDao;
import com.thinkgem.jeesite.modules.report.dao.RecommendReportDao;
import com.thinkgem.jeesite.modules.report.dao.ReletReportDao;
import com.thinkgem.jeesite.modules.report.dao.RentReportDao;
import com.thinkgem.jeesite.modules.report.entity.ContractReport;
import com.thinkgem.jeesite.modules.report.entity.ExpireReport;
import com.thinkgem.jeesite.modules.report.entity.RecommendReport;
import com.thinkgem.jeesite.modules.report.entity.ReletReport;
import com.thinkgem.jeesite.modules.report.entity.RentReport;

@Service
public class ContractReportService extends CrudService<ContractReportDao, ContractReport> {
	@Autowired
	private ExpireReportDao expireReportDao;
	@Autowired
	private ReletReportDao reletReportDao;
	@Autowired
	private RecommendReportDao recommendReportDao;
	@Autowired
	private RentReportDao rentReportDao;
	
	public Page<ContractReport> report(Page<ContractReport> page, ContractReport contractReport) {
    	contractReport.setPage(page);
		page.setList(dao.report(contractReport));
		return page;
    }
	
	public Page<ExpireReport> expireReport(Page<ExpireReport> page, ExpireReport expireReport) {
		expireReport.setPage(page);
		page.setList(expireReportDao.expireReport(expireReport));
		return page;
    }
	
	public Page<ReletReport> reletReport(Page<ReletReport> page, ReletReport reletReport) {
		reletReport.setPage(page);
		page.setList(reletReportDao.reletReport(reletReport));
		return page;
    }
	
	public Page<RecommendReport> recommendReport(Page<RecommendReport> page, RecommendReport recommendReport) {
		recommendReport.setPage(page);
		page.setList(recommendReportDao.recommendReport(recommendReport));
		return page;
    }
	
	public Page<RentReport> rentReport(Page<RentReport> page, RentReport rentReport) {
		rentReport.setPage(page);
		page.setList(rentReportDao.rentReport(rentReport));
		return page;
    }
}
