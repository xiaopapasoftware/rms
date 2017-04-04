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
   * 预约定金
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
   * 正常人工续签
   */
  NORMAL_RENEW("4"),

  /**
   * 逾期自动续签
   */
  OVERDUE_AUTO_RENEW("5"),

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
   * 后付费自用电费付费
   */
  POSTPAID_SELF("12"),

  /**
   * 后付费分摊电费付费
   */
  POSTPAID_SHARE("13"),

  /**
   * 后付费水费付费
   */
  POSTPAID_WATER("14"),

  /**
   * 后付费宽带费付费
   */
  POSTPAID_NET("15"),

  /**
   * 后付费电视费付费
   */
  POSTPAID_TV("16"),

  /**
   * 后付费燃气费付费
   */
  POSTPAID_GAS("17"),

  /**
   * 后付费服务费付费
   */
  POSTPAID_SERVICE("18");

  TradeTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

}
