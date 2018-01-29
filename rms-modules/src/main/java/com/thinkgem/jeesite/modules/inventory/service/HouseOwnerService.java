package com.thinkgem.jeesite.modules.inventory.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.dao.HouseOwnerDao;
import com.thinkgem.jeesite.modules.inventory.entity.HouseOwner;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class HouseOwnerService extends CrudService<HouseOwnerDao, HouseOwner> {

    public List<HouseOwner> findList(HouseOwner houseOwner) {
        return super.findList(houseOwner);
    }

    public void processHouseAndOwner(String houseId, List<Owner> ownerList) {
        HouseOwner houseOwner = new HouseOwner();
        houseOwner.setHouseId(houseId);
        houseOwner.preUpdate();
        super.delete(houseOwner);
        if (CollectionUtils.isNotEmpty(ownerList)) {
            for (Owner owner : ownerList) {
                houseOwner = new HouseOwner();
                houseOwner.setOwnerId(owner.getId());
                houseOwner.setHouseId(houseId);
                super.save(houseOwner);
            }
        }
    }

}
