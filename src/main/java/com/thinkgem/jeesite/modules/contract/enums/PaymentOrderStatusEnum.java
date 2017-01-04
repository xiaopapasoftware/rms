package com.thinkgem.jeesite.modules.contract.enums;

/**
 * @author wangshujin 支付订单状态
 */
public enum PaymentOrderStatusEnum {

  /**
   * 已支付
   */
  PAID("2"),


  /**
   * 未支付
   */
  TOBEPAY("1");

  PaymentOrderStatusEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
