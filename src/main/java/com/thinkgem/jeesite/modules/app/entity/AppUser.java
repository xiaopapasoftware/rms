/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * APP用户Entity
 * @author mabindong
 * @version 2015-11-24
 */
public class AppUser extends DataEntity<AppUser> {
	
	private static final long serialVersionUID = 1L;
	private String phone;		// 手机号码
	private String password;		// 密码
	private String idCardNo;		// 身份证号
	private String name;		// 姓名
	private String sex;		// 性别1-男 2-女
	private String birth;		// 生日
	private String age;		// 年龄
	private String profession;		// 职业
	private String corp;		// 公司
	private String avatar;
	private String idCardPhoto＿front;		// 身份证正面,附件ID
	private String idCardPhoto＿back;		// 身份证反面,附件ID
    private String scienerUserName; //科技侠账号
    private String scienerPassword; //科技侠密码
	
	public AppUser() {
		super();
	}

	public AppUser(String id){
		super(id);
	}

	@Length(min=0, max=20, message="手机号码长度必须介于 0 和 20 之间")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=20, message="密码长度必须介于 0 和 20 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Length(min=0, max=18, message="身份证号长度必须介于 0 和 18 之间")
	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	
	@Length(min=0, max=30, message="姓名长度必须介于 0 和 30 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=1, message="性别1-男 2-女长度必须介于 0 和 1 之间")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@Length(min=0, max=10, message="生日长度必须介于 0 和 10 之间")
	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}
	
	@Length(min=0, max=11, message="年龄长度必须介于 0 和 11 之间")
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	@Length(min=0, max=100, message="职业长度必须介于 0 和 100 之间")
	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}
	
	@Length(min=0, max=200, message="公司长度必须介于 0 和 200 之间")
	public String getCorp() {
		return corp;
	}

	public void setCorp(String corp) {
		this.corp = corp;
	}
	
	@Length(min=0, max=64, message="身份证正面,附件ID长度必须介于 0 和 64 之间")
	public String getIdCardPhoto＿front() {
		return idCardPhoto＿front;
	}

	public void setIdCardPhoto＿front(String idCardPhoto＿front) {
		this.idCardPhoto＿front = idCardPhoto＿front;
	}
	
	@Length(min=0, max=64, message="身份证反面,附件ID长度必须介于 0 和 64 之间")
	public String getIdCardPhoto＿back() {
		return idCardPhoto＿back;
	}

	public void setIdCardPhoto＿back(String idCardPhoto＿back) {
		this.idCardPhoto＿back = idCardPhoto＿back;
	}

	public String getAvatar() {
		return avatar;
	}
	@Length(min=0, max=64, message="头像,附件ID长度必须介于 0 和 64 之间")
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

    public String getScienerUserName() {
        return scienerUserName;
    }
    @Length(min=0, max=64, message="科技侠账号,附件ID长度必须介于 0 和 64 之间")
    public void setScienerUserName(String scienerUserName) {
        this.scienerUserName = scienerUserName;
    }

    public String getScienerPassword() {
        return scienerPassword;
    }
    @Length(min=0, max=64, message="科技侠密码,附件ID长度必须介于 0 和 64 之间")
    public void setScienerPassword(String scienerPassword) {
        this.scienerPassword = scienerPassword;
    }




}