package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ReletReport;

@MyBatisDao
public interface ReletReportDao extends CrudDao<ReletReport> {
	public List<ReletReport> reletReport(ReletReport reletReport);
}
