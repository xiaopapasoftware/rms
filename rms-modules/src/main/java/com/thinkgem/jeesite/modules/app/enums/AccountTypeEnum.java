package com.thinkgem.jeesite.modules.app.enums;

/**
 * 第三方帐号类型
 * 
 * @author xiao
 */
public enum AccountTypeEnum {

  /**
   * 科技侠
   */
  KEJIXIA("1"),

  /**
   * 支付宝
   */
  ALIPAY("2"),

  /**
   * 芝麻信用
   */
  ZHIMA("3");

  AccountTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
