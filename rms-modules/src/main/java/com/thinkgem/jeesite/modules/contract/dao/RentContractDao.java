/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.contract.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 出租合同DAO接口
 */
@MyBatisDao
public interface RentContractDao extends CrudDao<RentContract> {

  Integer getAllValidRentContractCounts();

  RentContract getByHouseId(RentContract rentContract);

  RentContract findContractByCode(String contractCode);

  /**
   * 查询指定时间内某房屋的有效合同
   */
  List<RentContract> queryHousesByHouseId(@Param("houseId") String houseId);

  /**
   * 查询某个时间段下合同列表
   * @param projectId
   * @param startDate
   * @param endDate
   * @return
   */
  List<RentContract> queryContractListByProjectIdAndDate(@Param("projectId") String projectId, @Param("startDate")Date startDate, @Param("endDate")Date endDate);

  Integer isWholeRentHouse(@Param("houseId")String houseId);

  RentContract getByRoomId(@Param("roomId")String roomId);

  List<RentContract> getByRentContract(RentContract rentContract);
}
