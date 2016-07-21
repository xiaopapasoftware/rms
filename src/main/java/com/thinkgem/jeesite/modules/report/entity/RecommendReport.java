package com.thinkgem.jeesite.modules.report.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import com.thinkgem.jeesite.modules.inventory.entity.Building;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.PropertyProject;
import com.thinkgem.jeesite.modules.inventory.entity.Room;

public class RecommendReport extends DataEntity<RecommendReport> {
    private static final long serialVersionUID = 2731401513440319961L;
    private PropertyProject propertyProject; // 物业项目
    private Building building; // 楼宇
    private House house; // 房屋
    private Room room; // 房间
    private String projectName; // 物业项目

    private Double contractTotal;
    private Double recommendTotal;
    private Double roomTotal;
    private Double recommendRoomTotal;
    private Double ratio;
    private Double roomRatio;

    public PropertyProject getPropertyProject() {
	return propertyProject;
    }

    public void setPropertyProject(PropertyProject propertyProject) {
	this.propertyProject = propertyProject;
    }

    public Building getBuilding() {
	return building;
    }

    public void setBuilding(Building building) {
	this.building = building;
    }

    public House getHouse() {
	return house;
    }

    public void setHouse(House house) {
	this.house = house;
    }

    public Room getRoom() {
	return room;
    }

    public void setRoom(Room room) {
	this.room = room;
    }

    @ExcelField(title = "物业项目", align = 2, sort = 1)
    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    @ExcelField(title = "成交合同总份数", align = 2, sort = 2)
    public Double getContractTotal() {
	return contractTotal;
    }

    public void setContractTotal(Double contractTotal) {
	this.contractTotal = contractTotal;
    }

    @ExcelField(title = "第三方推介客户成交合同数", align = 2, sort = 3)
    public Double getRecommendTotal() {
	return recommendTotal;
    }

    public void setRecommendTotal(Double recommendTotal) {
	this.recommendTotal = recommendTotal;
    }

    @ExcelField(title = "成交总间数", align = 2, sort = 5)
    public Double getRoomTotal() {
	return roomTotal;
    }

    public void setRoomTotal(Double roomTotal) {
	this.roomTotal = roomTotal;
    }

    @ExcelField(title = "第三方推介客户成交间数", align = 2, sort = 6)
    public Double getRecommendRoomTotal() {
	return recommendRoomTotal;
    }

    public void setRecommendRoomTotal(Double recommendRoomTotal) {
	this.recommendRoomTotal = recommendRoomTotal;
    }

    @ExcelField(title = "第三方推介客户成交合同数占比", align = 2, sort = 4)
    public Double getRatio() {
	return ratio;
    }

    public void setRatio(Double ratio) {
	this.ratio = ratio;
    }

    @ExcelField(title = "第三方推介客户成交间数占比", align = 2, sort = 7)
    public Double getRoomRatio() {
	return roomRatio;
    }

    public void setRoomRatio(Double roomRatio) {
	this.roomRatio = roomRatio;
    }
}
