package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ReletRateReport;

@MyBatisDao
public interface ReletRateReportDao extends CrudDao<ReletRateReport> {
	public List<ReletRateReport> reletRateReport(ReletRateReport reletRateReport);
}
