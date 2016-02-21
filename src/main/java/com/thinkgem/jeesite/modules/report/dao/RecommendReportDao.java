package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.RecommendReport;

@MyBatisDao
public interface RecommendReportDao extends CrudDao<RecommendReport> {
	public List<RecommendReport> recommendReport(RecommendReport recommendReport);
}
