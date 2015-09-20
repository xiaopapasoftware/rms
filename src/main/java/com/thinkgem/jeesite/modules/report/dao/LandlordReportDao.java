package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.LandlordReport;

@MyBatisDao
public interface LandlordReportDao extends CrudDao<LandlordReport> {
	public List<LandlordReport> landlordReport(LandlordReport landlordReport);
}
