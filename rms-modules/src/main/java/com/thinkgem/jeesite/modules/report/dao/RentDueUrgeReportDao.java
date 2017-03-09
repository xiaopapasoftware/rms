package com.thinkgem.jeesite.modules.report.dao;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

/**
 * Created by wangganggang on 17/2/26.
 */
@MyBatisDao
public interface RentDueUrgeReportDao {

    List<Map> queryRentDueUrge(Criterion criterion);
}
