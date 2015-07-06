/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.device.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 设备信息Entity
 * 
 * @author huangsc
 * @version 2015-06-13
 */
public class Devices extends DataEntity<Devices> {

	private static final long serialVersionUID = 1L;
	private String deviceId; // 设备编号
	private String deviceName; // 设备名称
	private String deviceModel; // 设备型号
	private String deviceType; // 设备类型
	private String deviceTypeDesc; // 设备类型描述
	private Double devicePrice; // 设备采购价格
	private String deviceBrand; // 设备品牌
	private String deviceStatus; // 设备状态
	private String distrSerlNum; // 设备分配序号
	private String devicesChooseFlag; // 设备分配状态，默认为0未分配，1为已分配。

	public Devices() {
		super();
	}

	public Devices(String id) {
		super(id);
	}

	@Length(min = 1, max = 64, message = "设备ID长度必须介于 1 和 64 之间")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Length(min = 0, max = 64, message = "设备分配序号长度必须介于 0和 100 之间")
	public String getDistrSerlNum() {
		return distrSerlNum;
	}

	public void setDistrSerlNum(String distrSerlNum) {
		this.distrSerlNum = distrSerlNum;
	}

	@Length(min = 1, max = 100, message = "设备名称长度必须介于 1 和 100 之间")
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Length(min = 1, max = 100, message = "设备型号长度必须介于 1 和 100 之间")
	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	@Length(min = 1, max = 100, message = "设备类型长度必须介于 1 和 100 之间")
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceTypeDesc() {
		return deviceTypeDesc;
	}

	public void setDeviceTypeDesc(String deviceTypeDesc) {
		this.deviceTypeDesc = deviceTypeDesc;
	}
	@NotNull(message = "设备采购价格不能为空")
	public Double getDevicePrice() {
		return devicePrice;
	}

	public void setDevicePrice(Double devicePrice) {
		this.devicePrice = devicePrice;
	}

	@Length(min = 1, max = 64, message = "设备品牌长度必须介于 1 和 64 之间")
	public String getDeviceBrand() {
		return deviceBrand;
	}

	public void setDeviceBrand(String deviceBrand) {
		this.deviceBrand = deviceBrand;
	}

	@Length(min = 1, max = 100, message = "设备状态长度必须介于 1 和 100 之间")
	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getDevicesChooseFlag() {
		return devicesChooseFlag;
	}
	public void setDevicesChooseFlag(String devicesChooseFlag) {
		this.devicesChooseFlag = devicesChooseFlag;
	}
}