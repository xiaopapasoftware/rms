/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.entity.Dict;
import com.thinkgem.jeesite.modules.entity.User;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 房屋信息Entity
 *
 * @author huangsc
 * @version 2015-06-06
 */
public class House extends DataEntity<House> {

    private static final long serialVersionUID = 1L;
    private PropertyProject propertyProject; // 物业项目
    private Building building; // 楼宇
    private Owner owner; // 业主
    private String houseCode;// 房屋序号
    private String houseNo; // 房屋号
    private String certificateNo;// 产权证号
    private Integer houseFloor; // 楼层
    private String houseSpace; // 原始建筑面积
    private String decorationSpance; // 装修建筑面积
    private Integer oriStrucRoomNum;// 原始房屋结构-房数
    private Integer oriStrucCusspacNum;// 原始房屋结构-厅数
    private Integer oriStrucWashroNum;// 原始房屋结构-卫数
    private Integer decoraStrucRoomNum;// 装修后房屋结构-房数
    private Integer decoraStrucCusspacNum;// 装修后房屋结构-厅数
    private Integer decoraStrucWashroNum;// 装修后房屋结构-卫数
    private String ownerNamesOfHouse;// 用于查询房屋时，显示该房屋下所有的业主姓名
    private String houseStatus; // 房屋状态
    private String attachmentPath; // 房屋图片路径
    private String choose;
    private List<Owner> ownerList = new ArrayList<>();// 用来渲染业主查询条件下拉框数据源
    private String projectAddr;
    private String orientation;
    private String houseId;
    private String roomId;
    private String intentMode;// 意向租赁类型
    private String isFeature;// 是否精选房源
    private Double rental;// 意向租金   0 整套 1 单间
    private String shortDesc;// 描述
    private String shortLocation;// 地址描述
    private User serviceUser;// 服务管家
    private String servcieUserName;
    /* 电户号 */
    private String eleAccountNum;
    /* 水户号 */
    private String waterAccountNum;
    /* 煤气户号 */
    private String gasAccountNum;

    private Long newId;

    private Integer rentMonthGap;//房租支付间隔月数

    private Integer deposMonthCount;//押金月数

    private Integer alipayStatus;//支付宝同步状态

    private Integer up;

    private String reservationPhone;

    private String type;//公寓类型，作为查询条件

    public House() {
        super();
    }

    public House(String id) {
        super(id);
    }

    private String shareAreaConfig;

    private List<Dict> shareAreaConfigList;

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

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getHouseCode() {
        return houseCode;
    }

    public void setHouseCode(String houseCode) {
        this.houseCode = houseCode;
    }

    @Length(min = 1, max = 100, message = "房屋号长度必须介于 1 和 100 之间")
    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    @NotNull(message = "楼层不能为空")
    public Integer getHouseFloor() {
        return houseFloor;
    }

    public void setHouseFloor(Integer houseFloor) {
        this.houseFloor = houseFloor;
    }

    public String getHouseSpace() {
        return houseSpace;
    }

    public void setHouseSpace(String houseSpace) {
        this.houseSpace = houseSpace;
    }

    public String getDecorationSpance() {
        return decorationSpance;
    }

    public void setDecorationSpance(String decorationSpance) {
        this.decorationSpance = decorationSpance;
    }

    @Length(min = 1, max = 100, message = "房屋状态长度必须介于 1 和 100 之间")
    public String getHouseStatus() {
        return houseStatus;
    }

    public void setHouseStatus(String houseStatus) {
        this.houseStatus = houseStatus;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public Integer getOriStrucRoomNum() {
        return oriStrucRoomNum;
    }

    public void setOriStrucRoomNum(Integer oriStrucRoomNum) {
        this.oriStrucRoomNum = oriStrucRoomNum;
    }

    public Integer getOriStrucCusspacNum() {
        return oriStrucCusspacNum;
    }

    public void setOriStrucCusspacNum(Integer oriStrucCusspacNum) {
        this.oriStrucCusspacNum = oriStrucCusspacNum;
    }

    public Integer getOriStrucWashroNum() {
        return oriStrucWashroNum;
    }

    public void setOriStrucWashroNum(Integer oriStrucWashroNum) {
        this.oriStrucWashroNum = oriStrucWashroNum;
    }

    public Integer getDecoraStrucRoomNum() {
        return decoraStrucRoomNum;
    }

    public void setDecoraStrucRoomNum(Integer decoraStrucRoomNum) {
        this.decoraStrucRoomNum = decoraStrucRoomNum;
    }

    public Integer getDecoraStrucCusspacNum() {
        return decoraStrucCusspacNum;
    }

    public void setDecoraStrucCusspacNum(Integer decoraStrucCusspacNum) {
        this.decoraStrucCusspacNum = decoraStrucCusspacNum;
    }

    public Integer getDecoraStrucWashroNum() {
        return decoraStrucWashroNum;
    }

    public void setDecoraStrucWashroNum(Integer decoraStrucWashroNum) {
        this.decoraStrucWashroNum = decoraStrucWashroNum;
    }

    public String getOwnerNamesOfHouse() {
        return ownerNamesOfHouse;
    }

    public void setOwnerNamesOfHouse(String ownerNamesOfHouse) {
        this.ownerNamesOfHouse = ownerNamesOfHouse;
    }

    public List<Owner> getOwnerList() {
        return ownerList;
    }

    public void setOwnerList(List<Owner> ownerList) {
        this.ownerList = ownerList;
    }

    public String getIntentMode() {
        return intentMode;
    }

    public void setIntentMode(String intentMode) {
        this.intentMode = intentMode;
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

    public String getProjectAddr() {
        return projectAddr;
    }

    public void setProjectAddr(String projectAddr) {
        this.projectAddr = projectAddr;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public User getServiceUser() {
        return serviceUser;
    }

    public void setServiceUser(User serviceUser) {
        this.serviceUser = serviceUser;
    }

    public String getServcieUserName() {
        return servcieUserName;
    }

    public void setServcieUserName(String servcieUserName) {
        this.servcieUserName = servcieUserName;
    }

    public String getEleAccountNum() {
        return eleAccountNum;
    }

    public void setEleAccountNum(String eleAccountNum) {
        this.eleAccountNum = eleAccountNum;
    }

    public String getWaterAccountNum() {
        return waterAccountNum;
    }

    public void setWaterAccountNum(String waterAccountNum) {
        this.waterAccountNum = waterAccountNum;
    }

    public String getGasAccountNum() {
        return gasAccountNum;
    }

    public void setGasAccountNum(String gasAccountNum) {
        this.gasAccountNum = gasAccountNum;
    }

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

    public String getShareAreaConfig() {
        return shareAreaConfig;
    }

    public void setShareAreaConfig(String shareAreaConfig) {
        this.shareAreaConfig = shareAreaConfig;
    }

    public List<Dict> getShareAreaConfigList() {
        return shareAreaConfigList;
    }

    public void setShareAreaConfigList(List<Dict> shareAreaConfigList) {
        this.shareAreaConfigList = shareAreaConfigList;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
