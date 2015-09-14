package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.PaymentReport;

@MyBatisDao
public interface PaymentReportDao extends CrudDao<PaymentReport> {
	public List<PaymentReport> paymentReport(PaymentReport paymentReport);
}
