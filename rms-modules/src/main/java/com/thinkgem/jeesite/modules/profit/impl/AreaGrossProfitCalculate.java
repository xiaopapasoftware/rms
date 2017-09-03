package com.thinkgem.jeesite.modules.profit.impl;

import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculate;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import com.thinkgem.jeesite.modules.service.AreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AreaGrossProfitCalculate implements GrossProfitCalculate{

    @Autowired
    private AreaService areaService;

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Override
    public String getName(GrossProfitCondition condition) {
        return Optional.ofNullable(areaService.get(condition.getId())).map(Area::getName).orElse("");
    }

    @Override
    public List<GrossProfitCondition> getChildConditionList(GrossProfitCondition condition) {
        return Optional.ofNullable(propertyProjectService.getPropertyProjectByAreaId(condition.getId()))
                .map(list -> list.stream()
                        .map(house -> {
                            GrossProfitCondition profitCondition = new GrossProfitCondition();
                            BeanUtils.copyProperties(condition, profitCondition);
                            profitCondition.setId(house.getId());
                            profitCondition.setTypeEnum(GrossProfitTypeEnum.Project);
                            return profitCondition;
                        }).collect(Collectors.toList())).orElse(null);
    }

}
