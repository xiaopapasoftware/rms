package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.modules.report.dao.RentDueUrgeReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangganggang on 17/2/26.
 */
@Service
public class RentDueUrgeReportService {

    @Autowired
    private RentDueUrgeReportDao rentDueUrgeReportDao;


    public List queryRentDueUrge(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
        return rentDueUrgeReportDao.queryRentDueUrge(new Criterion(propertyFilters, sorts));
    }
}
