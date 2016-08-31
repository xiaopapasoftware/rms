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
    private AttachmentService attachmentService;

    public Page<House> findFeaturePage(Page<House> page) {
	page.setList(dao.findFeatureList());
	return page;
    }

    @Transactional(readOnly = false)
    public void saveRoom(Room room) {
	if (room.getIsNewRecord()) {// 新增
	    String id = super.saveAndReturnId(room);
	    if (StringUtils.isNotEmpty(room.getAttachmentPath())) {
		Attachment attachment = new Attachment();
		attachment.setRoomId(id);
		attachment.setAttachmentType(FileType.ROOM_MAP.getValue());
		attachment.setAttachmentPath(room.getAttachmentPath());
		attachmentService.save(attachment);
	    }
	} else {// 更新
	    Attachment attachment = new Attachment();
	    attachment.setRoomId(room.getId());
	    List<Attachment> attachmentList = attachmentService.findList(attachment);
	    String id = super.saveAndReturnId(room);
	    if (CollectionUtils.isNotEmpty(attachmentList)) {// 更新时候，不管AttachmentPath有值无值，都要更新，防止空值不更新的情况。
		Attachment atta = new Attachment();
		atta.setRoomId(id);
		atta.setAttachmentPath(room.getAttachmentPath());
		attachmentService.updateAttachmentPathByType(atta);
	    } else {// 修改时，新增附件
		Attachment toAddattachment = new Attachment();
		toAddattachment.setRoomId(id);
		toAddattachment.setAttachmentType(FileType.ROOM_MAP.getValue());
		toAddattachment.setAttachmentPath(room.getAttachmentPath());
		attachmentService.save(toAddattachment);
	    }
	}
    }

    @Transactional(readOnly = false)
    public void delete(Room room) {
	room.preUpdate();
	super.delete(room);
	Attachment atta = new Attachment();
	atta.setRoomId(room.getId());
	attachmentService.delete(atta);
    }

    @Transactional(readOnly = true)
    public List<Room> findRoomByPrjAndBldAndHouNoAndRomNo(Room room) {
	return dao.findRoomByPrjAndBldAndHouNoAndRomNo(room);
    }
}