/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.entity.Dict;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 房间信息
 */
public class Room extends BaseHousingEntity<Room> {
    private static final long serialVersionUID = 1L;
    private House house; // 房屋
    private String roomNo; // 房间号
    private String meterNo; // 电表号
    private String roomSpace; // 房间面积
    private String roomConfig;
    private List<Dict> roomConfigList = Lists.newArrayList(new Dict());// 物品配置列表
    private String roomStatus; // 房间状态
    private String buildingType;//公寓类型，用于查询

    public Room() {
        super();
    }

    public Room(String id) {
        super(id);
    }

    @NotNull(message = "房屋号不能为空")
    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    @Length(min = 1, max = 100, message = "房间号长度必须介于 1 和 100 之间")
    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @Length(min = 0, max = 100, message = "电表号长度必须介于 0 和 100 之间")
    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getRoomSpace() {
        return roomSpace;
    }

    public void setRoomSpace(String roomSpace) {
        this.roomSpace = roomSpace;
    }

    @Length(min = 1, max = 100, message = "房间状态长度必须介于 1 和 100 之间")
    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomConfig() {
        return roomConfig;
    }

    public void setRoomConfig(String roomConfig) {
        this.roomConfig = roomConfig;
    }

    public List<Dict> getRoomConfigList() {
        return roomConfigList;
    }

    public void setRoomConfigList(List<Dict> roomConfigList) {
        this.roomConfigList = roomConfigList;
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }
}