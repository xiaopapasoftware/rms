/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.app.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 消息Entity
 * 
 * @author huangsc
 * @version 2015-12-14
 */
public class Message extends DataEntity<Message> {

  private static final long serialVersionUID = 1L;
  private String title; // 消息标题
  private String content; // 消息内容
  private String type; // 消息类型
  private String sender; // 发送人
  private String receiver; // 接收人
  private String status; // 状态

  public Message() {
    super();
  }

  public Message(String id) {
    super(id);
  }

  @Length(min = 0, max = 200, message = "消息标题长度必须介于 0 和 200 之间")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Length(min = 0, max = 500, message = "消息内容长度必须介于 0 和 500 之间")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Length(min = 0, max = 10, message = "消息类型长度必须介于 0 和 10 之间")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Length(min = 0, max = 30, message = "发送人长度必须介于 0 和 30 之间")
  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  @Length(min = 0, max = 30, message = "接收人长度必须介于 0 和 30 之间")
  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  @Length(min = 0, max = 10, message = "状态长度必须介于 0 和 10 之间")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
