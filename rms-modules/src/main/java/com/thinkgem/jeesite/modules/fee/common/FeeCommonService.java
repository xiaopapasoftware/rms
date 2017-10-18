package com.thinkgem.jeesite.modules.fee.common;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.enums.AreaTypeEnum;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.enums.FeeTypeEnum;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangganggang
 * @date 2017年09月18日 下午9:57
 */
@Service
public class FeeCommonService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HouseService houseService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private AreaService areaService;

    public House getHouseById(String houseId) {
        return houseService.get(houseId);
    }

    public List<Map> getRoomByHouseId(String houseId){
        House house = houseService.get(houseId);
        if(StringUtils.equals(house.getIntentMode(),"0")){
            return Lists.newArrayList();
        }
        return roomService.getRoomByHouseId(houseId);
    }

    public List<Map> getHouseByQueryWhereAndType(String queryWhere,String type) {
        return houseService.getHouseByQueryWhereAndType(queryWhere,type);
    }

    public List<SelectItem> getAreaWithAuth() {
        Area queryArea = new Area();
        queryArea.setType(AreaTypeEnum.AREA.getValue());
        List<Area> areas = areaService.getAreaWithAuth(queryArea);
        return areas.stream().map(area -> new SelectItem(area.getId(), area.getName())).collect(Collectors.toList());
    }

    public List<SelectItem> getAreaWithAuthByType(String type) {
        Area queryArea = new Area();
        queryArea.setType(type);
        List<Area> areas = areaService.getAreaWithAuth(queryArea);
        return areas.stream().map(area -> new SelectItem(area.getId(), area.getName())).collect(Collectors.toList());
    }

    public FeeConfig getFeeConfig(Map<String, FeeConfig> feeConfigMap, String rangeId, FeeTypeEnum feeTypeEnum) {
        FeeConfig feeConfig = null;
        for (String id : rangeId.split(",")) {
            String key = id + "_" + feeTypeEnum.getValue();
            feeConfig = feeConfigMap.get(key);
            if (Optional.ofNullable(feeConfig).isPresent()) {
                break;
            }
        }
        if (!Optional.ofNullable(feeConfig).isPresent()) {
            String key = 0 + "_" + FeeTypeEnum.ELE_VALLEY_UNIT.getValue();
            feeConfig = feeConfigMap.get(key);
        }

        if (!Optional.of(feeConfig).isPresent()) {
            logger.error("{}没有找到费用配置项", feeTypeEnum.getName());
            throw new IllegalArgumentException(feeTypeEnum.getName() + "没有找到费用配置");
        }
        return feeConfig;
    }

}
