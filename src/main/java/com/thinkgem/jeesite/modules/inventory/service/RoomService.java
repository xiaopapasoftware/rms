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
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

/**
 * 房间信息Service
 * 
 * @author huangsc
 * @version 2015-06-09
 */
@Service
@Transactional(readOnly = true)
public class RoomService extends CrudService<RoomDao, Room> {

    @Autowired
    private AttachmentDao attachmentDao;

    @Autowired
    private AttachmentService attachmentService;

    public Room get(String id) {
	return super.get(id);
    }

    public List<Room> findList(Room room) {
	return super.findList(room);
    }

    public Page<Room> findPage(Page<Room> page, Room room) {
	return super.findPage(page, room);
    }

    public Page<House> findFeaturePage(Page<House> page) {
	page.setList(dao.findFeatureList());
	return page;
    }

    @Transactional(readOnly = false)
    public void save(Room room) {
	if (room.getIsNewRecord()) {// 新增
	    String id = super.saveAndReturnId(room);
	    if (StringUtils.isNotEmpty(room.getAttachmentPath())) {
		Attachment attachment = new Attachment();
		attachment.preInsert();
		attachment.setRoomId(id);
		attachment.setAttachmentType(FileType.ROOM_MAP.getValue());
		attachment.setAttachmentPath(room.getAttachmentPath());
		attachmentDao.insert(attachment);
	    }
	} else {// 更新
		// 先查询原先该房间下是否有附件，有的话则直接更新。
	    Attachment attachment = new Attachment();
	    attachment.setRoomId(room.getId());
	    List<Attachment> attachmentList = attachmentDao.findList(attachment);
	    String id = super.saveAndReturnId(room);
	    if (CollectionUtils.isNotEmpty(attachmentList)) {// 更新时候，不管AttachmentPath有值无值，都要更新，防止空值不更新的情况。
		Attachment atta = new Attachment();
		atta.setRoomId(id);
		atta.setAttachmentPath(room.getAttachmentPath());
		attachmentService.updateAttachmentPathByType(atta);
	    } else {// 修改时，新增附件
		Attachment toAddattachment = new Attachment();
		toAddattachment.preInsert();
		toAddattachment.setRoomId(id);
		toAddattachment.setAttachmentType(FileType.ROOM_MAP.getValue());
		toAddattachment.setAttachmentPath(room.getAttachmentPath());
		attachmentDao.insert(toAddattachment);
	    }
	}
    }

    @Transactional(readOnly = false)
    public void delete(Room room) {
	super.delete(room);
	Attachment atta = new Attachment();
	atta.setRoomId(room.getId());
	atta.preUpdate();
	attachmentDao.delete(atta);
    }

    @Transactional(readOnly = true)
    public List<Room> findRoomByPrjAndBldAndHouNoAndRomNo(Room room) {
	return dao.findRoomByPrjAndBldAndHouNoAndRomNo(room);
    }

    @Transactional(readOnly = false)
    public int update(Room room) {
	return dao.update(room);
    }
}