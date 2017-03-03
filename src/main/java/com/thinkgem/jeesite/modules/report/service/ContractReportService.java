package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.modules.report.dao.ContractReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangshujin
 */
@Service
public class ContractReportService {

    @Autowired
    private ContractReportDao contractReportDao;


    public List queryContractReport(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
        return contractReportDao.queryContractReport(new Criterion(propertyFilters, sorts));
    }
}
