package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.dao.HouseOwnerDao;
import com.thinkgem.jeesite.modules.inventory.entity.HouseOwner;

@Service
@Transactional(readOnly = true)
public class HouseOwnerService extends CrudService<HouseOwnerDao, HouseOwner> {

    public List<HouseOwner> findList(HouseOwner houseOwner) {
	return super.findList(houseOwner);
    }
}
