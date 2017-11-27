package com.thinkgem.jeesite.modules.lease.impl;

import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.lease.LeaseCalculate;
import com.thinkgem.jeesite.modules.lease.condition.LeaseStatisticsCondition;
import com.thinkgem.jeesite.modules.service.AreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AreaLeaseCalculate implements LeaseCalculate {

    @Autowired
    private AreaService areaService;

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Override
    public String getName(LeaseStatisticsCondition condition) {
        return Optional.ofNullable(areaService.get(condition.getId())).map(Area::getName).orElse("");
    }

    @Override
    public List<LeaseStatisticsCondition> getChildConditionList(LeaseStatisticsCondition condition) {
        return Optional.ofNullable(propertyProjectService.getPropertyProjectByAreaId(condition.getId()))
                .map(list -> list.stream()
                        .map(project -> {
                            LeaseStatisticsCondition statisticsCondition = new LeaseStatisticsCondition();
                            BeanUtils.copyProperties(condition, statisticsCondition);
                            statisticsCondition.setId(project.getId());
                            statisticsCondition.setType(SelectItemConstants.PROJECT);
                            return statisticsCondition;
                        }).collect(Collectors.toList())).orElse(null);
    }

}
