package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.report.dao.ReportComponentDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangganggang on 17/3/4.
 *
 * @author wangganggang
 * @date 2017/03/04
 */
@Service
public class ReportComponentSrervice {

    @Autowired
    private ReportComponentDao reportComponentDao;

    public List<Dict> queryDict(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
        return reportComponentDao.queryDict(new Criterion(propertyFilters, sorts));
    }

    public List<PropertyProject> queryProject(List<PropertyFilter> propertyFilters, List<Sort> sorts) {
        return reportComponentDao.queryProject(new Criterion(propertyFilters, sorts));
    }


}
