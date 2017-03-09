package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.HouseRoomReport;

/**
 * 房间数量统计报表
 * @author wangshujin
 */
@MyBatisDao
public interface HouseRoomReportDao extends CrudDao<HouseRoomReport> {
  
  /**
   *  房间数量统计
   *  查询条件：全部/某小区(默认为全部)
   *  查询结果：小区名、总计、待装修、待出租可预订、已预定、已出租、已退租可预订、已损坏
   * */
  List<HouseRoomReport> roomsCount(HouseRoomReport houseRoomReport);
}
