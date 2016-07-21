package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ExpireReport;

@MyBatisDao
public interface ExpireReportDao extends CrudDao<ExpireReport> {
	public List<ExpireReport> expireReport(ExpireReport expireReport);
}
