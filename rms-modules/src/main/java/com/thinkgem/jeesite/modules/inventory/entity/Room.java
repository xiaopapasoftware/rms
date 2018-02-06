/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.entity.Dict;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 房间信息Entity
 *
 * @author huangsc
 * @version 2015-06-09
 */
public class Room extends DataEntity<Room> {

    private static final long serialVersionUID = 1L;
    private PropertyProject propertyProject; // 物业项目
    private Building building; // 楼宇
    private House house; // 房屋
    private String roomNo; // 房间号
    private String meterNo; // 电表号
    private String roomSpace; // 房间面积
    private String orientation;
    private List<Dict> orientationList = Lists.newArrayList(new Dict());// 朝向列表
    private String roomConfig;
    private List<Dict> roomConfigList = Lists.newArrayList(new Dict());// 物品配置列表
    private String roomStatus; // 房间状态
    private String attachmentPath;// 房间图片
    private String choose;
    private String isFeature;//是否精选房源
    private Double rental;//意向租金
    private String shortDesc;//描述
    private String shortLocation;//地址描述
    private Long newId;
    private Integer rentMonthGap;
    private Integer deposMonthCount;
    private Integer alipayStatus;
    private Integer up;
    private String reservationPhone;

    /**
     * 支付宝租房同步用
     */
    private String feeConfigInfo;//用于同步支付宝的各种费用，配置形如：feeName1=feeAmt1,feeName2=feeAmt2,feeName3=feeAmt3,feeName4=feeAmt4,feeName5=feeAmt5
    private String feeDesc1;
    private String feeAmt1;
    private String feeDesc2;
    private String feeAmt2;
    private String feeDesc3;
    private String feeAmt3;
    private String feeDesc4;
    private String feeAmt4;
    private String feeDesc5;
    private String feeAmt5;

    public Room() {
        super();
    }

    public Room(String id) {
        super(id);
    }

    @NotNull(message = "物业项目不能为空")
    public PropertyProject getPropertyProject() {
        return propertyProject;
    }

    public void setPropertyProject(PropertyProject propertyProject) {
        this.propertyProject = propertyProject;
    }

    @NotNull(message = "楼宇不能为空")
    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
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

    public List<Dict> getOrientationList() {
        return orientationList;
    }

    public void setOrientationList(List<Dict> orientationList) {
        this.orientationList = orientationList;
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

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public String getIsFeature() {
        return isFeature;
    }

    public void setIsFeature(String isFeature) {
        this.isFeature = isFeature;
    }

    public Double getRental() {
        return rental;
    }

    public void setRental(Double rental) {
        this.rental = rental;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getShortLocation() {
        return shortLocation;
    }

    public void setShortLocation(String shortLocation) {
        this.shortLocation = shortLocation;
    }

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

    public Integer getRentMonthGap() {
        return rentMonthGap;
    }

    public void setRentMonthGap(Integer rentMonthGap) {
        this.rentMonthGap = rentMonthGap;
    }

    public Integer getDeposMonthCount() {
        return deposMonthCount;
    }

    public void setDeposMonthCount(Integer deposMonthCount) {
        this.deposMonthCount = deposMonthCount;
    }

    public Integer getAlipayStatus() {
        return alipayStatus;
    }

    public void setAlipayStatus(Integer alipayStatus) {
        this.alipayStatus = alipayStatus;
    }

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public String getReservationPhone() {
        return reservationPhone;
    }

    public void setReservationPhone(String reservationPhone) {
        this.reservationPhone = reservationPhone;
    }

    public String getFeeConfigInfo() {
        return feeConfigInfo;
    }

    public void setFeeConfigInfo(String feeConfigInfo) {
        this.feeConfigInfo = feeConfigInfo;
    }

    public String getFeeDesc1() {
        return feeDesc1;
    }

    public void setFeeDesc1(String feeDesc1) {
        this.feeDesc1 = feeDesc1;
    }

    public String getFeeAmt1() {
        return feeAmt1;
    }

    public void setFeeAmt1(String feeAmt1) {
        this.feeAmt1 = feeAmt1;
    }

    public String getFeeDesc2() {
        return feeDesc2;
    }

    public void setFeeDesc2(String feeDesc2) {
        this.feeDesc2 = feeDesc2;
    }

    public String getFeeAmt2() {
        return feeAmt2;
    }

    public void setFeeAmt2(String feeAmt2) {
        this.feeAmt2 = feeAmt2;
    }

    public String getFeeDesc3() {
        return feeDesc3;
    }

    public void setFeeDesc3(String feeDesc3) {
        this.feeDesc3 = feeDesc3;
    }

    public String getFeeAmt3() {
        return feeAmt3;
    }

    public void setFeeAmt3(String feeAmt3) {
        this.feeAmt3 = feeAmt3;
    }

    public String getFeeDesc4() {
        return feeDesc4;
    }

    public void setFeeDesc4(String feeDesc4) {
        this.feeDesc4 = feeDesc4;
    }

    public String getFeeAmt4() {
        return feeAmt4;
    }

    public void setFeeAmt4(String feeAmt4) {
        this.feeAmt4 = feeAmt4;
    }

    public String getFeeDesc5() {
        return feeDesc5;
    }

    public void setFeeDesc5(String feeDesc5) {
        this.feeDesc5 = feeDesc5;
    }

    public String getFeeAmt5() {
        return feeAmt5;
    }

    public void setFeeAmt5(String feeAmt5) {
        this.feeAmt5 = feeAmt5;
    }
}