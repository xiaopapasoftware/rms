package com.thinkgem.jeesite.modules.common.service;

import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.profit.enums.AreaTypeEnum;
import com.thinkgem.jeesite.modules.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SelectItemService {

    @Autowired
    private AreaService areaService;

    @Autowired
    private PropertyProjectService propertyProjectService;

    @Autowired
    private HouseService houseService;

    public List<SelectItem> getSelectListByCondtion(SelectItemCondition condition) {
        switch (condition.getType()) {
            case "company" :
                return convertToItemList(areaService.getCompanyList()
                        .stream()
                        .collect(Collectors.toMap(Area::getId, Area::getName)));
            case "center" :
                return convertToItemList(areaService.getAreaByParentId(condition.getId(), AreaTypeEnum.COMPANY.getValue())
                        .stream()
                        .collect(Collectors.toMap(Area::getId, Area::getName)));
            case "area" :
                return convertToItemList(areaService.getAreaByParentId(condition.getId(), AreaTypeEnum.AREA.getValue())
                        .stream()
                        .collect(Collectors.toMap(Area::getId, Area::getName)));
            case "project" :
                return convertToItemList(propertyProjectService.getPropertyProjectByAreaId(condition.getId())
                        .stream()
                        .collect(Collectors.toMap(PropertyProject::getId, PropertyProject::getProjectName)));
            case "house" :
                return convertToItemList(houseService.findHouseListByProjectId(condition.getId())
                        .stream()
                        .collect(Collectors.toMap(House::getId, House::getHouseNo)));
            default:
                return null;
        }
    }

    private List<SelectItem> convertToItemList(Map<String, String> map) {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        return map.entrySet().stream().map(entry -> new SelectItem(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }


}
