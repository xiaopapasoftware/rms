/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.entity.FileType;
import com.thinkgem.jeesite.modules.inventory.dao.HouseDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 房屋信息Service
 * 
 * @author huangsc
 * @version 2015-06-06
 */
@Service
@Transactional(readOnly = true)
public class HouseService extends CrudService<HouseDao, House> {

	@Autowired
	private RoomService roomService;

	@Autowired
	private AttachmentDao attachmentDao;

	public House get(String id) {
		return super.get(id);
	}

	public List<House> findList(House house) {
		return super.findList(house);
	}

	public Page<House> findPage(Page<House> page, House house) {
		return super.findPage(page, house);
	}

	@Transactional(readOnly = false)
	public void save(House house) {
		if (house.getIsNewRecord()) {// 新增
			String id = super.saveAndReturnId(house);
			if (StringUtils.isNotEmpty(house.getAttachmentPath())) {
				Attachment attachment = new Attachment();
				attachment.setId(IdGen.uuid());
				attachment.setHouseId(id);
				attachment.setAttachmentType(FileType.HOUSE_MAP.getValue());
				attachment.setAttachmentPath(house.getAttachmentPath());
				attachment.setCreateDate(new Date());
				attachment.setCreateBy(UserUtils.getUser());
				attachment.setUpdateDate(new Date());
				attachment.setUpdateBy(UserUtils.getUser());
				attachmentDao.insert(attachment);
			}
		} else {// 修改
			// 先查询原先该房屋下是否有附件，有的话则直接更新。
			Attachment attachment = new Attachment();
			attachment.setHouseId(house.getId());
			List<Attachment> attachmentList = attachmentDao.findList(attachment);
			String id = super.saveAndReturnId(house);
			if (CollectionUtils.isNotEmpty(attachmentList)) {// 更新时候，不管AttachmentPath有值无值，都要更新，防止空值不更新的情况。
				Attachment atta = new Attachment();
				atta.setHouseId(id);
				atta.setAttachmentPath(house.getAttachmentPath());
				attachmentDao.updateAttachmentPathByType(atta);
			} else {// 新增附件
				Attachment toAddattachment = new Attachment();
				toAddattachment.setId(IdGen.uuid());
				toAddattachment.setHouseId(id);
				toAddattachment.setAttachmentType(FileType.HOUSE_MAP.getValue());
				toAddattachment.setAttachmentPath(house.getAttachmentPath());
				toAddattachment.setCreateDate(new Date());
				toAddattachment.setCreateBy(UserUtils.getUser());
				toAddattachment.setUpdateDate(new Date());
				toAddattachment.setUpdateBy(UserUtils.getUser());
				attachmentDao.insert(toAddattachment);
			}
		}
	}

	@Transactional(readOnly = false)
	public void delete(House house) {

		super.delete(house);
		Attachment atta = new Attachment();
		atta.setHouseId(house.getId());
		attachmentDao.delete(atta);

		Room r = new Room();
		r.setHouse(house);
		List<Room> rooms = roomService.findList(r);
		if (CollectionUtils.isNotEmpty(rooms)) {
			for (Room ro : rooms) {
				roomService.delete(ro);
			}
		}
	}
	/**
	 * 根据物业项目ID+楼宇ID+房屋号查询房屋信息
	 * */
	@Transactional(readOnly = true)
	public List<House> findHourseByProPrjAndBuildingAndHouseNo(House house) {
		return dao.findHourseByProPrjAndBuildingAndHouseNo(house);
	}

	/**
	 * 更新房屋状态
	 * */
	@Transactional(readOnly = false)
	public int updateHouseStatus(House house) {
		House upHouse = new House();
		upHouse.setId(house.getId());
		upHouse.setHouseStatus("1");
		int updHouseCount = dao.updateHouseStatus(upHouse);
		int roomCounts = 0;
		if (updHouseCount > 0) {
			Room m = new Room();
			m.setHouse(house);
			List<Room> listRoom = roomService.findList(m);
			if (CollectionUtils.isNotEmpty(listRoom)) {
				for (Room m1 : listRoom) {
					int roomUpCount = roomService.updateRoomStatus(m1);
					roomCounts = roomCounts + roomUpCount;
				}
			}
			return updHouseCount + roomCounts;
		} else {
			return 0;
		}

	}
}