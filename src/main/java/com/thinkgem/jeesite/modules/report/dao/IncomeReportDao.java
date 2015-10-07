package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.IncomeReport;

@MyBatisDao
public interface IncomeReportDao extends CrudDao<IncomeReport> {
	public List<IncomeReport> report(IncomeReport incomeReport);
}
