package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.LeaseContractReport;

@MyBatisDao
public interface LeaseContractReportDao extends CrudDao<LeaseContractReport> {
	public List<LeaseContractReport> findLeaseContractList(LeaseContractReport leaseContractReport);
}
