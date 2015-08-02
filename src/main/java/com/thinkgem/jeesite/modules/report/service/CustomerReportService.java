package com.thinkgem.jeesite.modules.report.service;

import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.LeaseContractReportDao;
import com.thinkgem.jeesite.modules.report.entity.LeaseContractReport;

@Service
public class CustomerReportService extends CrudService<LeaseContractReportDao, LeaseContractReport> {
	public Page<LeaseContractReport> findLeaseContractList(Page<LeaseContractReport> page, LeaseContractReport leaseContractReport) {
		leaseContractReport.setPage(page);
		page.setList(dao.findLeaseContractList(leaseContractReport));
		return page;
	}
}
