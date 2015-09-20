package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ContractReport;

@MyBatisDao
public interface ContractReportDao extends CrudDao<ContractReport> {
	public List<ContractReport> report(ContractReport contractReport);
}
