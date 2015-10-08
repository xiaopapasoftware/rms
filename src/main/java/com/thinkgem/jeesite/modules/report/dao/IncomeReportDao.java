package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.IncomeReport;

@MyBatisDao
public interface IncomeReportDao extends CrudDao<IncomeReport> {
	public List<IncomeReport> report(IncomeReport incomeReport);
	public List<IncomeReport> fangzuReport(IncomeReport incomeReport);
	public List<IncomeReport> receivableReport(IncomeReport incomeReport);
	public List<IncomeReport> refundReport(IncomeReport incomeReport);
}
