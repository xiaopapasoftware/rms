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
 * 预约看房
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
        completeInfo(contractBook);
        return contractBook;
    }

    @Transactional(readOnly = false)
    public void distribution(ContractBook contractBook) {
        contractBook.preUpdate();
        dao.distribution(contractBook);
    }

    /**
     * 被预约的销售只能看到其名下的预约数据
     */
    public List<ContractBook> findList(ContractBook contractBook) {
        salesUserDataScopeFilter(contractBook, "filterSQLKey", "a.sales_id=su.id");
        List<ContractBook> listResult = super.findList(contractBook);
        listResult.forEach(this::completeInfo);
        return listResult;
    }

    /**
     * 被预约的销售只能看到其名下的预约数据
     */
    public Page<ContractBook> findPage(Page<ContractBook> page, ContractBook contractBook) {
        salesUserDataScopeFilter(contractBook, "filterSQLKey", "a.sales_id=su.id");
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
        if (StringUtils.isNotBlank(contractBook.getRoomId())) {
            Room room = roomDao.get(contractBook.getRoomId());
            contractBook.setRoomNo(room.getRoomNo());
        }
        if (StringUtils.isNotBlank(contractBook.getSalesId())) {
            contractBook.setSalesName(userDao.get(contractBook.getSalesId()).getLoginName());
        }
    }

}
