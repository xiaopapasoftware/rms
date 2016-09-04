package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 资金交易方式途径
 * 
 * @author wangshujin
 */
public enum TradeModeTypeEnum {

  /**
   * POS
   */
  POS("0"),

  /**
   * 转账
   */
  TRANSFER("1"),

  /**
   * 支票
   */
  CHECK("2"),

  /**
   * 现金
   */
  CASH("3"),

  /**
   * 支付宝
   */
  ALIPAY("4");

  TradeModeTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
