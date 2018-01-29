package com.thinkgem.jeesite.modules.person.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.inventory.dao.HouseOwnerDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.HouseOwner;
import com.thinkgem.jeesite.modules.person.dao.OwnerDao;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 业主信息Service
 */
@Service
@Transactional(readOnly = true)
public class OwnerService extends CrudService<OwnerDao, Owner> {
    @Autowired
    private HouseOwnerDao houseOwnerDao;
    @Autowired
    private OwnerDao ownerDao;

    public Owner get(String id) {
        return super.get(id);
    }

    public List<Owner> findList(Owner owner) {
        return super.findList(owner);
    }

    public List<Owner> findListByWord(String word) {
        return dao.findListByWord(word);
    }

    public List<Owner> findByHouse(House house) {
        HouseOwner houseOwner = new HouseOwner();
        houseOwner.setHouseId(house.getId());
        List<Owner> ownerList = new ArrayList<>();
        for (HouseOwner tmpHouseOwner : houseOwnerDao.findList(houseOwner)) {
            ownerList.add(ownerDao.get(tmpHouseOwner.getOwnerId()));
        }
        return ownerList;
    }

    public Page<Owner> findPage(Page<Owner> page, Owner owner) {
        return super.findPage(page, owner);
    }

    @Transactional(readOnly = false)
    public void save(Owner owner) {
        super.save(owner);
    }

    @Transactional(readOnly = false)
    public void delete(Owner owner) {
        super.delete(owner);
    }

    @Transactional(readOnly = true)
    public List<Owner> findOwnersByCerNoOrMobNoOrTelNo(Owner owner) {
        return dao.findOwnersByCerNoOrMobNoOrTelNo(owner);
    }
}