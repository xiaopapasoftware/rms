package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.RentReport;

@MyBatisDao
public interface RentReportDao extends CrudDao<RentReport> {
	public List<RentReport> rentReport(RentReport rentReport);
}
