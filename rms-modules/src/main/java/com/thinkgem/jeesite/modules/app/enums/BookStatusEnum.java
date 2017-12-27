package com.thinkgem.jeesite.modules.app.enums;

/**
 * @author wangganggang
 * @date 2017年08月19日 下午1:26
 */
public enum BookStatusEnum {

  /**
   * 等待管家确认
   */
  BOOK_APP("0"),

  /**
   * 预约成功
   */
  BOOK_SUCCESS("1"),

  /**
   * 用户取消预约
   */
  USER_CANCEL("2"),

  /**
   * 管家取消预约
   */
  ADMIN_CANCEL("3");

  BookStatusEnum(String value) {
    this.value = value;
  }

  private String value;

  public String value() {
    return value;
  }
}
