package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 账务交易状态
 * 
 * @author wangshujin
 */
public enum TradingAccountsStatusEnum {

  /**
   * 待审核
   */
  TO_AUDIT("0"),

  /**
   * 审核通过
   */
  AUDIT_PASS("1"),

  /**
   * 审核拒绝
   */
  AUDIT_REFUSE("2"),

  /**
   * 发票已开
   */
  INVOICED("3");

  TradingAccountsStatusEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
