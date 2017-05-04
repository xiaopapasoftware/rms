/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.service.AttachmentService;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.inventory.dao.BuildingDao;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;

/**
 * 楼宇Service
 *
 * @author huangsc
 * @version 2015-06-03
 */
@Service
@Transactional(readOnly = true)
public class BuildingService extends CrudService<BuildingDao, Building> {

    @Autowired
    private HouseService houseService;

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    private AttachmentService attachmentService;

    public Building get(String id) {
        return super.get(id);
    }

    public List<Building> findList(Building building) {
        areaScopeFilter(building,"dsf","pp.area_id=sua.area_id");
        return super.findList(building);
    }

    public Page<Building> findPage(Page<Building> page, Building building) {
        areaScopeFilter(building,"dsf","pp.area_id=sua.area_id");
        return super.findPage(page, building);
    }

    @Transactional(readOnly = false)
    public void save(Building building) {
        if (building.getIsNewRecord()) {// 新增时，AttachmentPath有值才需要添加，无值则不需添加附件对象
            String id = super.saveAndReturnId(building);
            if (StringUtils.isNotEmpty(building.getAttachmentPath())) {
                Attachment attachment = new Attachment();
                attachment.preInsert();
                attachment.setBuildingId(id);
                attachment.setAttachmentType(FileType.BUILDING_MAP.getValue());
                attachment.setAttachmentPath(building.getAttachmentPath());
                attachmentDao.insert(attachment);
            }
        } else {
            // 先查询原先该楼宇下是否有附件，有的话则直接更新。
            Attachment attachment = new Attachment();
            attachment.setBuildingId(building.getId());
            List<Attachment> attachmentList = attachmentDao.findList(attachment);
            String id = super.saveAndReturnId(building);
            if (CollectionUtils.isNotEmpty(attachmentList)) {// 更新时候，不管AttachmentPath有值无值，都要更新，防止空值不更新的情况。
                Attachment atta = new Attachment();
                atta.setBuildingId(id);
                atta.setAttachmentPath(building.getAttachmentPath());
                attachmentService.updateAttachmentPathByType(atta);
            } else {// 新增附件
                Attachment toAddattachment = new Attachment();
                toAddattachment.preInsert();
                toAddattachment.setBuildingId(id);
                toAddattachment.setAttachmentType(FileType.BUILDING_MAP.getValue());
                toAddattachment.setAttachmentPath(building.getAttachmentPath());
                attachmentDao.insert(toAddattachment);
            }
        }
    }

    @Transactional(readOnly = false)
    public void delete(Building building) {
        super.delete(building);
        Attachment atta = new Attachment();
        atta.setBuildingId(building.getId());
        atta.preUpdate();
        attachmentDao.delete(atta);

        House h = new House();
        h.setBuilding(building);
        List<House> houses = houseService.findList(h);
        if (CollectionUtils.isNotEmpty(houses)) {
            for (House h2 : houses) {
                houseService.delete(h2);
            }
        }

    }

    @Transactional(readOnly = true)
    public List<Building> findBuildingByBldNameAndProProj(Building building) {
        return dao.findBuildingByBldNameAndProProj(building);
    }
}