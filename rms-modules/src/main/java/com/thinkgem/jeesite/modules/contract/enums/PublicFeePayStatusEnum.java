package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 公共事业费付费状态
 * 
 * @author wangshujin
 */
public enum PublicFeePayStatusEnum {

  /**
   * 付费中
   */
  PROCESSING("0"),

  /**
   * 付费成功
   */
  SUCCESSED("1"),

  /**
   * 付费失败
   */
  FAILED("2");


  PublicFeePayStatusEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
