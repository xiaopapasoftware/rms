package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.ReportEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by wangganggang on 17/2/26.
 */
@MyBatisDao
public interface ContractReportDao {

    List<Map> queryContractReport();
}
