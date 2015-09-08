package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.RentDataReport;

@MyBatisDao
public interface RentDataReportDao extends CrudDao<RentDataReport> {
	public List<RentDataReport> rentDataReport(RentDataReport rentDataReport);
}
