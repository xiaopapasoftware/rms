package com.thinkgem.jeesite.modules.fee.common;

import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangganggang
 * @date 2017年09月18日 下午9:57
 */
@Service
public class FeeCommonService {

    @Autowired
    private HouseService houseService;

    public House getHouseById(String houseId){
        return houseService.get(houseId);
    }
}
