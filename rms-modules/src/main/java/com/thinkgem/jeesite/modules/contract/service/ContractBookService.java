/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.service;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.contract.dao.ContractBookDao;
import com.thinkgem.jeesite.modules.contract.entity.ContractBook;
import com.thinkgem.jeesite.modules.dao.UserDao;
import com.thinkgem.jeesite.modules.inventory.dao.HouseDao;
import com.thinkgem.jeesite.modules.inventory.dao.RoomDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.person.dao.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 预约看房信息Service
 * @author huangsc
 */
@Service
@Transactional(readOnly = true)
public class ContractBookService extends CrudService<ContractBookDao, ContractBook> {

  @Autowired
  private RoomDao roomDao;
  @Autowired
  private CustomerDao customerDao;
  @Autowired
  private HouseDao houseDao;
  @Autowired
  private UserDao userDao;

  public ContractBook get(String id) {
    ContractBook contractBook = super.get(id);
    if (contractBook != null) {
      contractBook.setCustomer(customerDao.get(contractBook.getCustomer().getId()));
    }
    return contractBook;
  }

  @Transactional(readOnly = false)
  public void distribution(ContractBook contractBook) {
    contractBook.preUpdate();
    dao.distribution(contractBook);
  }

  public List<ContractBook> findList(ContractBook contractBook) {
    List<ContractBook> listResult = super.findList(contractBook);
    listResult.forEach(this::completeInfo);
    return listResult;
  }

  public Page<ContractBook> findPage(Page<ContractBook> page, ContractBook contractBook) {
    Page<ContractBook> pageResult = super.findPage(page, contractBook);
    pageResult.getList().forEach(this::completeInfo);
    return pageResult;
  }

  private void completeInfo(ContractBook contractBook) {
    if (contractBook.getCustomer() != null && StringUtils.isNotBlank(contractBook.getCustomer().getId())) {
      contractBook.setCustomer(customerDao.get(contractBook.getCustomer().getId()));
    }
    House house = houseDao.get(contractBook.getHouseId());
    contractBook.setProjectName(house.getPropertyProject().getProjectName());
    contractBook.setBuildingName(house.getBuilding().getBuildingName());
    contractBook.setHouseNo(house.getHouseNo());
    contractBook.setHouseType(house.getType());
    contractBook.setServiceUserName(house.getServcieUserName());
    if (StringUtils.isNotBlank(contractBook.getRoomId())) {
      Room room = roomDao.get(contractBook.getRoomId());
      contractBook.setRoomNo(room.getRoomNo());
    }
    if (StringUtils.isNotBlank(contractBook.getSalesId())) {
      contractBook.setSalesName(userDao.get(contractBook.getSalesId()).getLoginName());
    }
  }

}
