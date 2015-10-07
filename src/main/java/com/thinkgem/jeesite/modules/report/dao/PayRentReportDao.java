package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.PayRentReport;

@MyBatisDao
public interface PayRentReportDao extends CrudDao<PayRentReport> {
	public List<PayRentReport> report(PayRentReport payRentReport);
}
