package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;

/**
 * 房间数量统计报表
 * 
 * @author wangshujin
 */
public class HouseRoomReport extends DataEntity<HouseRoomReport> implements Comparable<HouseRoomReport> {

  private static final long serialVersionUID = -8337624029447634635L;

  private PropertyProject propertyProject; // 物业项目

  private String projectName; // 物业项目名称

  private String totalNum;// 总数数量

  private String renovationNum;// 待装修数量

  private String toBeReservedNum;// 可预订数量

  private String reservedNum;// 已预定数量

  private String leasedNum;// 已出租数量

  public PropertyProject getPropertyProject() {
    return propertyProject;
  }

  public void setPropertyProject(PropertyProject propertyProject) {
    this.propertyProject = propertyProject;
  }

  @ExcelField(title = "物业项目名称", align = 2, sort = 1)
  public String getProjectName() {
    return projectName;
  }

  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  @ExcelField(title = "房间总数", align = 2, sort = 2)
  public String getTotalNum() {
    return totalNum;
  }

  public void setTotalNum(String totalNum) {
    this.totalNum = totalNum;
  }

  @ExcelField(title = "待装修房间数", align = 2, sort = 3)
  public String getRenovationNum() {
    return renovationNum;
  }

  public void setRenovationNum(String renovationNum) {
    this.renovationNum = renovationNum;
  }


  @ExcelField(title = "待出租可预订房间数", align = 2, sort = 4)
  public String getToBeReservedNum() {
    return toBeReservedNum;
  }

  public void setToBeReservedNum(String toBeReservedNum) {
    this.toBeReservedNum = toBeReservedNum;
  }

  @ExcelField(title = "已预定房间数", align = 2, sort = 5)
  public String getReservedNum() {
    return reservedNum;
  }

  public void setReservedNum(String reservedNum) {
    this.reservedNum = reservedNum;
  }

  @ExcelField(title = "已出租房间数", align = 2, sort = 6)
  public String getLeasedNum() {
    return leasedNum;
  }

  public void setLeasedNum(String leasedNum) {
    this.leasedNum = leasedNum;
  }

  @Override
  public int compareTo(HouseRoomReport o) {
    int leftTotalNum = Integer.valueOf(this.getTotalNum());
    int rightTotalNum = Integer.valueOf(o.getTotalNum());
    return leftTotalNum - rightTotalNum;
  }
}
