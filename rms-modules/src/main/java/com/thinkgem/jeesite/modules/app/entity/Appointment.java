/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * 消息Entity
 * 
 * @author huangsc
 * @version 2015-12-14
 */
public class Appointment extends DataEntity<Appointment> {

  private static final long serialVersionUID = 1L;

  /** server_user_id - 服务管家id */
  private String serverUserId;
  /** user_name - 用户id */
  private String userName;
  /** user_sex - 0:男，1:女 2:保密 */
  private Byte userSex;
  /** telphone - 电话号码 */
  private String telphone;
  /** associate_type - 0:房屋，1: 房间 */
  private Byte associateType;
  /** associate_id - 关联Id */
  private String associateId;
  /** app_time - 预约时间 */
  private Date appTime;
  /** status - 0:申请预约1:管家确认2:预约成功3:取消预约4:预约失败 */
  private Byte status;

  public String getServerUserId() {
    return serverUserId;
  }

  public void setServerUserId(String serverUserId) {
    this.serverUserId = serverUserId;
  }

  @Length(min = 0, max = 30, message = "姓名长度必须介于 0 和 30 之间")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Length(min = 0, max = 1, message = "性别1-男 2-女长度必须介于 0 和 1 之间")
  public Byte getUserSex() {
    return userSex;
  }

  public void setUserSex(Byte userSex) {
    this.userSex = userSex;
  }

  @Length(min=0, max=20, message="手机号码长度必须介于 0 和 20 之间")
  public String getTelphone() {
    return telphone;
  }

  public void setTelphone(String telphone) {
    this.telphone = telphone;
  }

  @Length(min = 0, max = 1, message = "房屋类型 0:房屋，1: 房间")
  public Byte getAssociateType() {
    return associateType;
  }

  public void setAssociateType(Byte associateType) {
    this.associateType = associateType;
  }

  @NotEmpty(message = "房屋号不能为空")
  public String getAssociateId() {
    return associateId;
  }

  public void setAssociateId(String associateId) {
    this.associateId = associateId;
  }

  public Date getAppTime() {
    return appTime;
  }

  public void setAppTime(Date appTime) {
    this.appTime = appTime;
  }

  public Byte getStatus() {
    return status;
  }

  public void setStatus(Byte status) {
    this.status = status;
  }
}
