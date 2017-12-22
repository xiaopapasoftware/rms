/**
 * 小爬爬工作室
 * Copyright (c) 1994-2015 All Rights Reserved.
 */
package com.thinkgem.jeesite.modules.fee.common.dao;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>电费收取流水实现类service</p>
 * <p>Table: fee_ele_charged_flow - 电费收取流水</p>
 * @since 2017-09-18 08:24:32
 * @author generator code
*/
@MyBatisDao
public interface FeeCommonDao{

    String getRangeIdByRoomId(@Param("roomId") String roomId);

    String getRangeIdByHouseId(@Param("houseId") String houseId);

    List<Room> getJoinRentAllRoom(@Param("scope")String scope,@Param("businessId")String businessId);

    List<House> getWholeRentAllHouse(@Param("scope")String scope,@Param("businessId")String businessId);
}