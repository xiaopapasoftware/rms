package com.thinkgem.jeesite.modules.report.dao;

import java.util.List;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.report.entity.HouseReport;

/**
 * 房屋数量统计报表
 * 
 * @author wangshujin
 */
@MyBatisDao
public interface HouseReportDao extends CrudDao<HouseReport> {

  /**
   * 房屋数量统计 
   * 查询条件：全部/某小区(默认为全部) 
   * 查询结果：小区名、总计、待装修、待出租可预订、已预定、部分出租、完全出租、已退待租、已损坏
   */
  List<HouseReport> housesCount(HouseReport houseReport);
}
