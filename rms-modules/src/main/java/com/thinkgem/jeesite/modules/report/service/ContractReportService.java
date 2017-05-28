package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.modules.report.dao.ContractReportDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangganggang on 17/2/26.
 */
@Service
public class ContractReportService extends BaseService{

    @Autowired
    private ContractReportDao contractReportDao;


    public List queryContract(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
        Criterion criterion = new Criterion(propertyFilters, sorts);
        criterion.setCustomCriteria(areaScopeFilter("tpp.area_id=sua.area_id"));
        return contractReportDao.queryContract(criterion);
    }
}
