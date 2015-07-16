/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.person.entity;

import java.util.Date;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 租客信息Entity
 * 
 * @author huangsc
 * @version 2015-06-13
 */
public class Tenant extends DataEntity<Tenant> {

	private static final long serialVersionUID = 1L;
	private Company company; // 企业信息
	private User user; // 销售
	private String tenantName; // 姓名
	private String gender; // 性别
	private String tenantType; // 租客类型
	private String idType; // 证件类型
	private String idNo; // 证件号码
	private Date birthday; // 出生日期
	private String degrees; // 学历
	private String cellPhone; // 手机号码
	private String email; // 电子邮箱
	private String houseRegister; // 户籍所在地
	private String position; // 职位
	private String customerId;// 当从客户转化为租客时使用

	public Tenant() {
		super();
	}

	public Tenant(String id) {
		super(id);
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Length(min = 1, max = 64, message = "租客类型长度必须介于 1 和 64 之间")
	public String getTenantType() {
		return tenantType;
	}

	public void setTenantType(String tenantType) {
		this.tenantType = tenantType;
	}

	@Length(min = 1, max = 100, message = "姓名长度必须介于 1 和 100 之间")
	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	@Length(min = 1, max = 100, message = "性别长度必须介于 1 和 100 之间")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Length(min = 1, max = 100, message = "证件类型长度必须介于 1 和 100 之间")
	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	@Length(min = 1, max = 100, message = "证件号码长度必须介于 1 和 100 之间")
	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Length(min = 0, max = 64, message = "学历长度必须介于 0 和 64 之间")
	public String getDegrees() {
		return degrees;
	}

	public void setDegrees(String degrees) {
		this.degrees = degrees;
	}

	@Length(min = 1, max = 64, message = "手机号码长度必须介于 1 和 64 之间")
	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	@Length(min = 0, max = 64, message = "电子邮箱长度必须介于 0 和 64 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(min = 0, max = 64, message = "户籍所在地长度必须介于 0 和 64 之间")
	public String getHouseRegister() {
		return houseRegister;
	}

	public void setHouseRegister(String houseRegister) {
		this.houseRegister = houseRegister;
	}

	@Length(min = 0, max = 64, message = "职位长度必须介于 0 和 64 之间")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
}