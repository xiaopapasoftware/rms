package com.thinkgem.jeesite.modules.inventory.entity;

import com.thinkgem.jeesite.modules.entity.Dict;
import com.thinkgem.jeesite.modules.entity.User;
import com.thinkgem.jeesite.modules.person.entity.Owner;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 房屋信息
 */
public class House extends BaseHousingEntity<House> {
    private static final long serialVersionUID = 1L;
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
    private List<Owner> ownerList = new ArrayList<>();// 用来渲染业主查询条件下拉框数据源
    private String projectAddr;
    private String houseId;
    private String roomId;
    private String intentMode;// 意向租赁类型
    private User serviceUser;// 服务管家
    private User salesUser;//跟进销售
    /* 电户号 */
    private String eleAccountNum;
    /* 水户号 */
    private String waterAccountNum;
    /* 煤气户号 */
    private String gasAccountNum;
    private String type;//公寓类型，集中式/分散式 作为查询条件

    public House() {
        super();
    }

    public House(String id) {
        super(id);
    }

    private String shareAreaConfig;//用于支付宝租房同步，公共区域物品配置

    private List<Dict> shareAreaConfigList;

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


    public String getProjectAddr() {
        return projectAddr;
    }

    public void setProjectAddr(String projectAddr) {
        this.projectAddr = projectAddr;
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

    public User getSalesUser() {
        return salesUser;
    }

    public void setSalesUser(User salesUser) {
        this.salesUser = salesUser;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
