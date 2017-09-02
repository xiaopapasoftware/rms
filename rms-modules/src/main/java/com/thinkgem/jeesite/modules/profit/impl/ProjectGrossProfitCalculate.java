package com.thinkgem.jeesite.modules.profit.impl;

import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculate;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectGrossProfitCalculate implements GrossProfitCalculate{

    @Autowired
    private HouseService houseService;

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Override
    public String getName(GrossProfitCondition condition) {
        return Optional.ofNullable(propertyProjectService.getPropertyProjectById(condition.getId())).map(PropertyProject::getProjectName).orElse("");
    }

    @Override
    public List<GrossProfitCondition> getChildIdList(GrossProfitCondition condition) {
        return Optional.ofNullable(houseService.findHouseListByProjectId(condition.getId()))
                .map(list -> list.stream()
                        .map(house -> {
                            GrossProfitCondition profitCondition = new GrossProfitCondition();
                            BeanUtils.copyProperties(condition, profitCondition);
                            profitCondition.setId(house.getId());
                            profitCondition.setTypeEnum(GrossProfitTypeEnum.House);
                            return profitCondition;
                        }).collect(Collectors.toList())).orElse(null);

    }

}
