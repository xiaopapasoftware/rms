package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.modules.report.dao.ContractReportDao;
import com.thinkgem.jeesite.modules.report.dao.FinanceReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangganggang on 17/2/26.
 */
@Service
public class FinanceReportService {

    @Autowired
    private FinanceReportDao financeReportDao;

    public List queryFinace(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
        return financeReportDao.queryFinance(new Criterion(propertyFilters, sorts));
    }


    public List convertOutFinance(List financeData){

        return null;
    }
}
