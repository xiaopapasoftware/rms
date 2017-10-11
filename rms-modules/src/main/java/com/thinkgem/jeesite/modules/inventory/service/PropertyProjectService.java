/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.common.service.AttachmentService;
import com.thinkgem.jeesite.modules.contract.enums.FileType;
import com.thinkgem.jeesite.modules.inventory.dao.PropertyProjectDao;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 物业项目Service
 *
 * @author huangsc
 * @version 2015-06-03
 */
@Service
@Transactional(readOnly = true)
public class PropertyProjectService extends CrudService<PropertyProjectDao, PropertyProject> {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    private AttachmentService attachmentService;

    public PropertyProject get(String id) {
        return super.get(id);
    }

    public List<PropertyProject> findList(PropertyProject propertyProject) {
        areaScopeFilter(propertyProject,"dsf","a.area_id=sua.area_id");
        return super.findList(propertyProject);
    }

    public Page<PropertyProject> findPage(Page<PropertyProject> page, PropertyProject propertyProject) {
        areaScopeFilter(propertyProject,"dsf","a.area_id=sua.area_id");
        return super.findPage(page, propertyProject);
    }

    @Transactional(readOnly = false)
    public void save(PropertyProject propertyProject) {
        if (propertyProject.getIsNewRecord()) {// 新增时，AttachmentPath有值才需要添加，无值则不需添加附件对象
            String id = super.saveAndReturnId(propertyProject);
            if (StringUtils.isNotEmpty(propertyProject.getAttachmentPath())) {
                Attachment attachment = new Attachment();
                attachment.preInsert();
                attachment.setPropertyProjectId(id);
                attachment.setAttachmentType(FileType.PROJECT_CHART.getValue());
                attachment.setAttachmentPath(propertyProject.getAttachmentPath());
                attachmentDao.insert(attachment);
            }
        } else {
            // 先查询原先该物业项目下是否有附件，有的话则直接更新。
            Attachment attachment = new Attachment();
            attachment.setPropertyProjectId(propertyProject.getId());
            List<Attachment> attachmentList = attachmentDao.findList(attachment);
            String id = super.saveAndReturnId(propertyProject);
            if (CollectionUtils.isNotEmpty(attachmentList)) {// 更新时候，不管AttachmentPath有值无值，都要更新，防止空值不更新的情况。
                Attachment atta = new Attachment();
                atta.setPropertyProjectId(id);
                atta.setAttachmentPath(propertyProject.getAttachmentPath());
                attachmentService.updateAttachmentPathByType(atta);
            } else {// 修改时新增附件
                Attachment toAddattachment = new Attachment();
                toAddattachment.preInsert();
                toAddattachment.setPropertyProjectId(id);
                toAddattachment.setAttachmentType(FileType.PROJECT_CHART.getValue());
                toAddattachment.setAttachmentPath(propertyProject.getAttachmentPath());
                attachmentDao.insert(toAddattachment);
            }
        }
    }

    @Transactional(readOnly = false)
    public void delete(PropertyProject propertyProject) {
        super.delete(propertyProject);
        Attachment atta = new Attachment();
        atta.setPropertyProjectId(propertyProject.getId());
        atta.preUpdate();
        attachmentDao.delete(atta);

        Building bld = new Building();
        bld.setPropertyProject(propertyProject);
        List<Building> buildings = buildingService.findList(bld);
        if (CollectionUtils.isNotEmpty(buildings)) {
            for (Building b : buildings) {
                buildingService.delete(b);
            }
        }

    }

    /**
     * 根据物业名称+地址查询状态为正常/审核的物业项目数量
     */
    @Transactional(readOnly = true)
    public List<PropertyProject> findPropertyProjectByNameAndAddress(PropertyProject propertyProject) {
        return dao.findPropertyProjectByNameAndAddress(propertyProject);
    }

    public PropertyProject getPropertyProjectById(String id){
        return dao.getPropertyProjectById(id);
    }

    public List<PropertyProject> getPropertyProjectByAreaId(String areaId){
        return dao.getPropertyProjectByAreaId(areaId);
    }

    public List<PropertyProject> getPropertyProjectList(){
        return dao.getPropertyProjectList();
    }
}