package com.thinkgem.jeesite.modules.report.service;

import org.springframework.stereotype.Service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.report.dao.TenantReportDao;
import com.thinkgem.jeesite.modules.report.entity.TenantReport;

@Service
public class TenantReportService extends CrudService<TenantReportDao, TenantReport> {
	public Page<TenantReport> findTenantList(Page<TenantReport> page, TenantReport tenantReport) {
		tenantReport.setPage(page);
		page.setList(dao.findTenantList(tenantReport));
		return page;
	}
}
