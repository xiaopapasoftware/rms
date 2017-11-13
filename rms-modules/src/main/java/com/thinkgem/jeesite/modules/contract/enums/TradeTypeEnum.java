package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 账务交易类型
 * 
 * @author wangshujin
 */
public enum TradeTypeEnum {

  /**
   * 承租合同
   */
  LEASE_CONTRACT_TRADE("0"),

  /**
   * 预定
   */
  DEPOSIT_AGREEMENT("1"),

  /**
   * 定金转违约
   */
  DEPOSIT_TO_BREAK("2"),

  /**
   * 新签合同
   */
  SIGN_NEW_CONTRACT("3"),

  /**
   * 续签合同
   */
  NORMAL_RENEW("4"),

  /**
   * 提前退租
   */
  ADVANCE_RETURN_RENT("6"),

  /**
   * 正常退租
   */
  NORMAL_RETURN_RENT("7"),

  /**
   * 逾期退租
   */
  OVERDUE_RETURN_RENT("8"),

  /**
   * 特殊退租
   */
  SPECIAL_RETURN_RENT("9"),

  /**
   * 电费充值
   */
  ELECTRICITY_CHARGE("11"),

  /**
   * 公共事业费后付
   */
  PUB_FEE_POSTPAID("12");

  TradeTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
