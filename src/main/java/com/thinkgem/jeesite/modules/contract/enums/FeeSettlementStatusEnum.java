package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 
 * 费用结算状态
 * 
 * @author wangshujin
 *
 */
public enum FeeSettlementStatusEnum {

  /**
   * 待结算
   */
  NOT_SETTLED("0"),

  /**
   * 结算待审核
   */
  NOT_AUDITED("1"),

  /**
   * 审核拒绝
   */
  AUDIT_REFUSED("2"),

  /**
   * 审核通过
   */
  AUDIT_PASSED("3");


  FeeSettlementStatusEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
