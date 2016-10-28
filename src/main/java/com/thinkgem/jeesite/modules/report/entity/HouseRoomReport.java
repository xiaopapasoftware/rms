package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

/**
 * 房间数量统计报表
 * 
 * @author wangshujin
 */
public class HouseRoomReport extends DataEntity<HouseRoomReport> {

  private static final long serialVersionUID = -8337624029447634635L;

  private PropertyProject propertyProject; // 物业项目

  private String projectName; // 物业项目名称

  private String totalNum;// 总数数量

  private String renovationNum;// 待装修数量

  private String toBeReservedNum;// 可预订数量

  private String reservedNum;// 已预定数量

  private String leasedNum;// 已出租数量

  private String returned4ReservedNum;// 已退租可预定数量

  private String damagedNum;// 已损坏

  public PropertyProject getPropertyProject() {
    return propertyProject;
  }

  public void setPropertyProject(PropertyProject propertyProject) {
    this.propertyProject = propertyProject;
  }

  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public String getTotalNum() {
    return totalNum;
  }

  public void setTotalNum(String totalNum) {
    this.totalNum = totalNum;
  }

  public String getRenovationNum() {
    return renovationNum;
  }

  public void setRenovationNum(String renovationNum) {
    this.renovationNum = renovationNum;
  }

  public String getToBeReservedNum() {
    return toBeReservedNum;
  }

  public void setToBeReservedNum(String toBeReservedNum) {
    this.toBeReservedNum = toBeReservedNum;
  }

  public String getReservedNum() {
    return reservedNum;
  }

  public void setReservedNum(String reservedNum) {
    this.reservedNum = reservedNum;
  }

  public String getLeasedNum() {
    return leasedNum;
  }

  public void setLeasedNum(String leasedNum) {
    this.leasedNum = leasedNum;
  }

  public String getReturned4ReservedNum() {
    return returned4ReservedNum;
  }

  public void setReturned4ReservedNum(String returned4ReservedNum) {
    this.returned4ReservedNum = returned4ReservedNum;
  }

  public String getDamagedNum() {
    return damagedNum;
  }

  public void setDamagedNum(String damagedNum) {
    this.damagedNum = damagedNum;
  }
}
