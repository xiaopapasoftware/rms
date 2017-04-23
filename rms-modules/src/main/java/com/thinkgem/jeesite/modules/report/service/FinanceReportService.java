package com.thinkgem.jeesite.modules.report.service;

import com.thinkgem.jeesite.common.filter.search.Criterion;
import com.thinkgem.jeesite.common.filter.search.PropertyFilter;
import com.thinkgem.jeesite.common.filter.search.Sort;
import com.thinkgem.jeesite.common.utils.MapKeyHandle;
import com.thinkgem.jeesite.modules.report.dao.ContractReportDao;
import com.thinkgem.jeesite.modules.report.dao.FinanceReportDao;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    public List convertOutFinance(List<Map> financeData){
        return financeData.stream().map(map -> {
            double totamAmount = MapUtils.getDoubleValue(map,"house_amount") + MapUtils.getDoubleValue(map,"water_deposit")
                    + MapUtils.getDoubleValue(map,"house_deposit") + MapUtils.getDoubleValue(map,"agree_amount")
                    + MapUtils.getDoubleValue(map,"service_amount")  + MapUtils.getDoubleValue(map,"first_ele_amount");
            map.put("total_amount",totamAmount);
            return MapKeyHandle.keyToJavaProperty(map);
        }).collect(Collectors.toList());
    }


}
