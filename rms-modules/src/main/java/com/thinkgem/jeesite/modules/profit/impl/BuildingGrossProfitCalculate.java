package com.thinkgem.jeesite.modules.profit.impl;

import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.profit.GrossProfitCalculate;
import com.thinkgem.jeesite.modules.profit.condition.GrossProfitCondition;
import com.thinkgem.jeesite.modules.profit.enums.GrossProfitTypeEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BuildingGrossProfitCalculate implements GrossProfitCalculate{

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private HouseService houseService;

    @Override
    public String getName(GrossProfitCondition condition) {
        return Optional.ofNullable(buildingService.get(condition.getId())).map(Building::getBuildingName).orElse("");
    }

    @Override
    public List<GrossProfitCondition> getChildConditionList(GrossProfitCondition condition) {
        return Optional.ofNullable(houseService.findHouseListByBuildingId(condition.getId()))
                .map(list -> list.stream()
                        .map(house -> {
                            GrossProfitCondition profitCondition = new GrossProfitCondition();
                            BeanUtils.copyProperties(condition, profitCondition);
                            profitCondition.setId(house.getId());
                            profitCondition.setTypeEnum(GrossProfitTypeEnum.HOUSE);
                            return profitCondition;
                        }).collect(Collectors.toList())).orElse(null);

    }

}
