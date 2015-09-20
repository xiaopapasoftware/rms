package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ResultsReport;

@MyBatisDao
public interface ResultsReportDao extends CrudDao<ResultsReport> {
	public List<ResultsReport> resultsReport(ResultsReport resultsReport);
}
