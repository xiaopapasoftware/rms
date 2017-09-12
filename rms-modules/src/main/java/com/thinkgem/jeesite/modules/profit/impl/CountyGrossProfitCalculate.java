package com.thinkgem.jeesite.modules.profit.impl;


import com.thinkgem.jeesite.common.enums.AreaTypeEnum;
import com.thinkgem.jeesite.modules.entity.Area;
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
public class CountyGrossProfitCalculate implements GrossProfitCalculate {

  @Autowired
  private AreaService areaService;

  @Override
  public String getName(GrossProfitCondition condition) {
    return Optional.ofNullable(areaService.get(condition.getId())).map(Area::getName).orElse("");
  }

  @Override
  public List<GrossProfitCondition> getChildConditionList(GrossProfitCondition condition) {
    return Optional.ofNullable(areaService.getAreaByParentId(condition.getId(), AreaTypeEnum.CENTER.getValue())).map(list -> list.stream().map(center -> {
      GrossProfitCondition profitCondition = new GrossProfitCondition();
      BeanUtils.copyProperties(condition, profitCondition);
      profitCondition.setId(center.getId());
      profitCondition.setTypeEnum(GrossProfitTypeEnum.Center);
      return profitCondition;
    }).collect(Collectors.toList())).orElse(null);
  }
}
