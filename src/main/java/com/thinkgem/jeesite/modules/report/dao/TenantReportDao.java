package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.TenantReport;

@MyBatisDao
public interface TenantReportDao extends CrudDao<TenantReport> {
	public List<TenantReport> findTenantList(TenantReport tenantReport);
}
