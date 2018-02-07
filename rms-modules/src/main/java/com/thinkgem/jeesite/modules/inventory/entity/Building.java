/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.thinkgem.jeesite.modules.inventory.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 楼宇Entity
 *
 * @author huangsc
 * @version 2015-06-03
 */
public class Building extends DataEntity<Building> {

    private static final long serialVersionUID = 1L;
    private PropertyProject propertyProject; // 物业项目
    private String buildingName; // 楼宇名称
    private String attachmentPath; // 楼宇图片路径
    private int totalFloorCount; //总楼层数
    private String choose;
    private String nickName;
    private String minAmount;
    private String maxAmount;
    private String type;//公寓类型

    public Building() {
        super();
    }

    public Building(String id) {
        super(id);
    }

    @NotNull(message = "物业项目不能为空")
    public PropertyProject getPropertyProject() {
        return propertyProject;
    }

    public void setPropertyProject(PropertyProject propertyProject) {
        this.propertyProject = propertyProject;
    }

    @Length(min = 1, max = 100, message = "楼宇名称长度必须介于 1 和 100 之间")
    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public int getTotalFloorCount() {
        return totalFloorCount;
    }

    public void setTotalFloorCount(int totalFloorCount) {
        this.totalFloorCount = totalFloorCount;
    }

    public String getChoose() {
        return choose;
    }

    public void setChoose(String choose) {
        this.choose = choose;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(String minAmount) {
        this.minAmount = minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(String maxAmount) {
        this.maxAmount = maxAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
