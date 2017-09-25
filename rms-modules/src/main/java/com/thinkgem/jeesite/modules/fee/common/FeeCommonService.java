package com.thinkgem.jeesite.modules.fee.common;

import com.thinkgem.jeesite.common.enums.AreaTypeEnum;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangganggang
 * @date 2017年09月18日 下午9:57
 */
@Service
public class FeeCommonService {

    @Autowired
    private HouseService houseService;

    @Autowired
    private AreaService areaService;

    public House getHouseById(House house) {
        List<House> houses = houseService.findList(house);
        if(Optional.ofNullable(houses).isPresent()){
            return houses.get(0);
        }
        return null;
    }

    public List<SelectItem> getAreaWithAuth() {
        Area queryArea = new Area();
        queryArea.setType(AreaTypeEnum.AREA.getValue());
        List<Area> areas = areaService.getAreaWithAuth(queryArea);
        return areas.stream().map(area -> new SelectItem(area.getId(), area.getName())).collect(Collectors.toList());
    }
}
