package com.thinkgem.jeesite.modules.common.service;

import com.thinkgem.jeesite.common.enums.AreaTypeEnum;
import com.thinkgem.jeesite.modules.cache.MyCache;
import com.thinkgem.jeesite.modules.cache.MyCacheBuilder;
import com.thinkgem.jeesite.modules.cache.enums.MyCacheConstant;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.common.entity.SelectItemCondition;
import com.thinkgem.jeesite.modules.common.enums.SelectItemConstants;
import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.BuildingService;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.PropertyProjectService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Comparator;
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

  @Autowired
  private RoomService roomService;

  @Autowired
  private BuildingService buildingService;

  private static final SelectItem countyItem = new SelectItem(SelectItemConstants.COUNTY, SelectItemConstants.COUNTY_Str);
  private static final SelectItem centerItem = new SelectItem(SelectItemConstants.CENTER, SelectItemConstants.CENTER_STR);
  private static final SelectItem areaItem = new SelectItem(SelectItemConstants.AREA, SelectItemConstants.AREA_STR);
  private static final SelectItem projectItem = new SelectItem(SelectItemConstants.PROJECT, SelectItemConstants.PROJECT_STR);

  private MyCache selectCache = MyCacheBuilder.getInstance().getSoftCache(MyCacheConstant.SCHEDULED_SELECT);

  private String getCacheKey(SelectItemCondition condition) {
    return condition.getBusiness() + condition.getType() + condition.getId();
  }

  @SuppressWarnings("unchecked")
  public List<SelectItem> getSelectListByBusinessCode(SelectItemCondition condition) {
    if (selectCache.getObject(getCacheKey(condition)) != null) {
      return (List<SelectItem>) selectCache.getObject(getCacheKey(condition));
    }
    switch (condition.getBusiness()) {
      case SelectItemConstants.ORG:
        return getOrgSelectListByCondition(condition);
      case SelectItemConstants.CATEGORY:
        return getCategorySelectListByCondition(condition);
      default:
        return null;
    }
  }

  private List<SelectItem> getCategorySelectListByCondition(SelectItemCondition condition) {
    List<SelectItem> result;
    switch (condition.getType()) {
      case SelectItemConstants.COUNTY:
        result = convertToItemList(areaService.getCountyList().stream().collect(Collectors.toMap(Area::getId, Area::getName)));
        break;
      case SelectItemConstants.CENTER:
        result = convertToItemList(areaService.getCenterList().stream().collect(Collectors.toMap(Area::getId, Area::getName)));
        break;
      case SelectItemConstants.AREA:
        result = convertToItemList(areaService.getAreaList().stream().collect(Collectors.toMap(Area::getId, Area::getName)));
        break;
      case SelectItemConstants.PROJECT:
        result = convertToItemList(propertyProjectService.getPropertyProjectList().stream().collect(Collectors.toMap(PropertyProject::getId, PropertyProject::getProjectName)));
        break;
      default:
        result = null;
    }
    selectCache.putObject(getCacheKey(condition), result);
    return result;
  }

  private List<SelectItem> getOrgSelectListByCondition(SelectItemCondition condition) {
    List<SelectItem> result;
    switch (condition.getType()) {
      case SelectItemConstants.COUNTY:
        result = convertToItemList(areaService.getCountyList().stream().collect(Collectors.toMap(Area::getId, Area::getName)));
        break;
      case SelectItemConstants.CENTER:
        result = convertToItemList(areaService.getAreaByParentId(condition.getId(), AreaTypeEnum.CENTER.getValue()).stream().collect(Collectors.toMap(Area::getId, Area::getName)));
        break;
      case SelectItemConstants.AREA:
        result = convertToItemList(areaService.getAreaByParentId(condition.getId(), AreaTypeEnum.AREA.getValue()).stream().collect(Collectors.toMap(Area::getId, Area::getName)));
        break;
      case SelectItemConstants.PROJECT:
        result = convertToItemList(propertyProjectService.getPropertyProjectByAreaId(condition.getId()).stream().collect(Collectors.toMap(PropertyProject::getId, PropertyProject::getProjectName)));
        break;
      case SelectItemConstants.BUILDING:
        result = convertToItemList(buildingService.getBuildingListByProjectId(condition.getId()).stream().collect(Collectors.toMap(Building::getId, Building::getBuildingName)));
        break;
      case SelectItemConstants.HOUSE:
        result = convertToItemList(houseService.findHouseListByBuildingId(condition.getId()).stream().collect(Collectors.toMap(House::getId, House::getHouseNo)));
        break;
      case SelectItemConstants.ROOM:
        result = convertToItemList(roomService.findRoomListByHouseId(condition.getId()).stream().collect(Collectors.toMap(Room::getId, Room::getRoomNo)));
        break;
      default:
        result = null;
    }
    selectCache.putObject(getCacheKey(condition), result);
    return result;
  }

  private List<SelectItem> convertToItemList(Map<String, String> map) {
    if (CollectionUtils.isEmpty(map)) {
      return null;
    }
    List<SelectItem> result = map.entrySet().stream().map(entry -> new SelectItem(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    result.sort(Comparator.comparing(SelectItem::getName));
    return result;
  }

  public static List<SelectItem> getCommonReportCompareItem() {
    return Arrays.asList(countyItem, centerItem, areaItem, projectItem);
  }
}
