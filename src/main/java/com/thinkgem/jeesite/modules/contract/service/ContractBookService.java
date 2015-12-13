/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.dao.ContractBookDao;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.funds.dao.PaymentOrderDao;
import com.thinkgem.jeesite.modules.funds.entity.PaymentOrder;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

/**
 * 预约看房信息Service
 * @author huangsc
 */
@Service
@Transactional(readOnly = true)
public class ContractBookService extends CrudService<ContractBookDao, ContractBook> {
	@Autowired
	private ContractBookDao contractBookDao;
	@Autowired
	private PaymentOrderDao paymentOrderDao;
	@Autowired
	private RoomDao roomDao;
	
	public ContractBook get(String id) {
		return super.get(id);
	}
	
	public List<ContractBook> findList(ContractBook contractBook) {
		List<ContractBook> listResult = super.findList(contractBook);
		for(ContractBook tmpContractBook : listResult) {
			if(!StringUtils.isBlank(tmpContractBook.getRoomId())) {
				Room room = this.roomDao.get(tmpContractBook.getRoomId());
				tmpContractBook.setRoomNo(room.getRoomNo());
				tmpContractBook.setAttachmentPath(room.getAttachmentPath());
				tmpContractBook.setShortDesc(room.getShortDesc());
				tmpContractBook.setShortLocation(room.getShortLocation());
				tmpContractBook.setHouseId(room.getId());
			}
		}
		return listResult;
	}
	
	public Page<ContractBook> findPage(Page<ContractBook> page, ContractBook contractBook) {
		Page<ContractBook> pageResult = super.findPage(page, contractBook);
		List<ContractBook> listContractBook = pageResult.getList();
		for(ContractBook tmpContractBook : listContractBook) {
			if(!StringUtils.isBlank(tmpContractBook.getRoomId())) {
				Room room = this.roomDao.get(tmpContractBook.getRoomId());
				tmpContractBook.setRoomNo(room.getRoomNo());
				tmpContractBook.setAttachmentPath(room.getAttachmentPath());
				tmpContractBook.setShortDesc(room.getShortDesc());
				tmpContractBook.setShortLocation(room.getShortLocation());
				tmpContractBook.setHouseId(room.getId());
			}
		}
		pageResult.setList(listContractBook);
		return pageResult;
	}
	
	@Transactional(readOnly = false)
	public void save(ContractBook contractBook) {
		super.save(contractBook);
	}
	
	@Transactional(readOnly = false)
	public void delete(ContractBook contractBook) {
		super.delete(contractBook);
	}
	
	public ContractBook findOne(ContractBook contractBook) {
		ContractBook contractBookResult = super.get(contractBook);
		if(!StringUtils.isBlank(contractBookResult.getRoomId())) {
			Room room = this.roomDao.get(contractBookResult.getRoomId());
			contractBookResult.setRoomNo(room.getRoomNo());
			contractBookResult.setAttachmentPath(room.getAttachmentPath());
			contractBookResult.setShortDesc(room.getShortDesc());
			contractBookResult.setShortLocation(room.getShortLocation());
			contractBookResult.setHouseId(room.getId());
		}
		return contractBookResult;
	}
	
	public List<ContractBook> findBookedContract(ContractBook contractBook) {
		List<ContractBook> listContractBook = contractBookDao.findBookedContract(contractBook);
		for(ContractBook tmpContractBook : listContractBook) {
			if(!StringUtils.isBlank(tmpContractBook.getRoomId())) {
				Room room = this.roomDao.get(tmpContractBook.getRoomId());
				tmpContractBook.setRoomNo(room.getRoomNo());
				tmpContractBook.setAttachmentPath(room.getAttachmentPath());
				tmpContractBook.setShortDesc(room.getShortDesc());
				tmpContractBook.setShortLocation(room.getShortLocation());
				tmpContractBook.setHouseId(room.getId());
			}
		}
		return listContractBook;
	}
	
	public List<ContractBook> findRentContract(ContractBook contractBook) {
		List<ContractBook> listContractBook = contractBookDao.findRentContract(contractBook);
		
		for(ContractBook tmpContractBook : listContractBook) {
			if(!StringUtils.isBlank(tmpContractBook.getRoomId())) {
				Room room = this.roomDao.get(tmpContractBook.getRoomId());
				tmpContractBook.setRoomNo(room.getRoomNo());
				tmpContractBook.setAttachmentPath(room.getAttachmentPath());
				tmpContractBook.setShortDesc(room.getShortDesc());
				tmpContractBook.setShortLocation(room.getShortLocation());
				tmpContractBook.setHouseId(room.getId());
			}
		}
		return listContractBook;
	}
	
	@Transactional(readOnly = false)
	public void saveOrder(PaymentOrder paymentOrder) {
		paymentOrder.setId(IdGen.uuid());
		this.paymentOrderDao.insert(paymentOrder);
	}
	
	public PaymentOrder findByOrderId(String orderId) {
		PaymentOrder paymentOrder = new PaymentOrder();
		paymentOrder.setOrderId(orderId);
		List<PaymentOrder> list = this.paymentOrderDao.findList(paymentOrder);
		if(null != list && list.size() > 0)
			paymentOrder = list.get(0);
		return paymentOrder;
	}
	
	public String generateOrderId() {
		return DateFormatUtils.format(new Date(),"SSSyyyyMMddHHmmss");
	}
	
	@Transactional(readOnly = false)
	public void updateStatusByHouseId(ContractBook contractBook) {
		contractBookDao.updateStatusByHouseId(contractBook);
	}
}