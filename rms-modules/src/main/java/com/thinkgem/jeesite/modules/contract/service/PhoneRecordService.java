package com.thinkgem.jeesite.modules.contract.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.dao.PhoneRecordDao;
import com.thinkgem.jeesite.modules.contract.entity.PhoneRecord;
import com.thinkgem.jeesite.modules.inventory.dao.HouseDao;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PhoneRecordService extends CrudService<PhoneRecordDao, PhoneRecord> {

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private RoomDao roomDao;

    public Page<PhoneRecord> findPage(Page<PhoneRecord> page, PhoneRecord phoneRecord) {
        Page<PhoneRecord> pageResult = super.findPage(page, phoneRecord);
        pageResult.getList().forEach(this::completeInfo);
        return pageResult;
    }

    private void completeInfo(PhoneRecord phoneRecord) {
        House house = houseDao.get(phoneRecord.getHouseId());
        phoneRecord.setProjectName(house.getPropertyProject().getProjectName());
        phoneRecord.setBuildingName(house.getBuilding().getBuildingName());
        phoneRecord.setHouseCode(house.getHouseNo());
        if (StringUtils.isNotBlank(phoneRecord.getRoomId())) {
            Room room = roomDao.get(phoneRecord.getRoomId());
            phoneRecord.setRoomNo(room.getRoomNo());
        }
    }

}
