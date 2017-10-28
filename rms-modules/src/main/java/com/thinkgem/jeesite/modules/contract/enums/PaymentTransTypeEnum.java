package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 
 * 交易款项类型
 * 
 * @author wangshujin
 *
 */
public enum PaymentTransTypeEnum {

  /**
   * 定金（收）
   */
  RECEIVABLE_DEPOSIT("0"),

  /**
   * 应退定金（出）
   */
  OUT_DEPOSIT_AMOUNT("27"),

  /**
   * 预定违约金（收）
   */
  LIQUIDATED_DEPOSIT("1"),

  /**
   * 预定违约退款（出）
   */
  DEPOSIT_REFUND_FEE("26"),

  /**
   * 水电费押金
   */
  WATER_ELECT_DEPOSIT("2"),

  /**
   * 补水电费押金（收）
   */
  SUPPLY_WATER_ELECT_DEPOSIT("3"),

  /**
   * 房租押金
   */
  RENT_DEPOSIT("4"),

  /**
   * 补房租押金（收）
   */
  SUPPLY_RENT_DEPOSIT("5"),

  /**
   * 房租（收）
   */
  RENT_AMOUNT("6"),

  /**
   * 房租（出）
   */
  RETURN_RENT_AMOUNT("7"),

  /**
   * 逾补房租（收）
   */
  OVERDUE_RENT_AMOUNT("8"),

  /**
   * 早退违约罚金（收）
   */
  LEAVE_EARLY_DEPOSIT("9"),

  /**
   * 损坏赔偿款（收）
   */
  DAMAGE_COMPENSATE("10"),

  /**
   * 电费自用（收）
   */
  ELECT_SELF_AMOUNT("11"),

  /**
   * 电费分摊（收）
   */
  ELECT_SHARE_AMOUNT("12"),

  /**
   * 电费退款（出）
   */
  ELECT_SURPLUS_AMOUNT("13"),

  /**
   * 水费（收）
   */
  WATER_AMOUNT("14"),

  /**
   * 水费退款（出）
   */
  WATER_SURPLUS_AMOUNT("15"),

  /**
   * 燃气费（收）
   */
  GAS_AMOUNT("16"),

  /**
   * 燃气费退款（出）
   */
  GAS_REFUND_AMOUNT("17"),

  /**
   * 电视费（收）
   */
  TV_AMOUNT("18"),

  /**
   * 电视费退款（出）
   */
  TV_SURPLUS_AMOUNT("19"),

  /**
   * 宽带费（收）
   */
  NET_AMOUNT("20"),

  /**
   * 宽带费退款（出）
   */
  NET_SURPLUS_AMOUNT("21"),

  /**
   * 服务费（收）
   */
  SERVICE_AMOUNT("22"),

  /**
   * 服务费退款（出）
   */
  SERVICE_SURPLUS_AMOUNT("23"),

  /**
   * 退租补偿税金（收）
   */
  RETURN_SUPPLY_TAX("24"),

  /**
   * 转租手续费（收）
   */
  SUBLET_COUNTER_FEE("25");

  PaymentTransTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }
}
