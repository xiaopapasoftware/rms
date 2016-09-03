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
   * 应收定金
   */
  RECEIVABLE_DEPOSIT("0"),

  /**
   * 定金违约金
   */
  LIQUIDATED_DEPOSIT("1"),

  /**
   * 水电费押金
   */
  WATER_ELECT_DEPOSIT("2"),

  /**
   * 续补水电费押金
   */
  SUPPLY_WATER_ELECT_DEPOSIT("3"),

  /**
   * 房租押金
   */
  RENT_DEPOSIT("4"),

  /**
   * 续补房租押金
   */
  SUPPLY_RENT_DEPOSIT("5"),

  /**
   * 房租金额
   */
  RENT_AMOUNT("6"),

  /**
   * 应退房租
   */
  RETURN_RENT_AMOUNT("7"),

  /**
   * 逾赔房租
   */
  OVERDUE_RENT_AMOUNT("8"),

  /**
   * 早退违约金
   */
  LEAVE_EARLY_DEPOSIT("9"),

  /**
   * 损坏赔偿金
   */
  DAMAGE_COMPENSATE("10"),

  /**
   * 电费自用金额
   */
  ELECT_SELF_AMOUNT("11"),

  /**
   * 电费分摊金额
   */
  ELECT_SHARE_AMOUNT("12"),

  /**
   * 智能电表剩余电费
   */
  ELECT_SURPLUS_AMOUNT("13"),

  /**
   * 水费金额
   */
  WATER_AMOUNT("14"),

  /**
   * 水费剩余金额
   */
  WATER_SURPLUS_AMOUNT("15"),

  /**
   * 燃气金额
   */
  GAS_AMOUNT("16"),

  /**
   * 有线电视费
   */
  TV_AMOUNT("18"),

  /**
   * 有线电视费剩余金额
   */
  TV_SURPLUS_AMOUNT("19"),

  /**
   * 宽带费
   */
  NET_AMOUNT("20"),

  /**
   * 宽带费剩余金额
   */
  NET_SURPLUS_AMOUNT("21"),

  /**
   * 服务费
   */
  SERVICE_AMOUNT("22"),

  /**
   * 服务费剩余金额
   */
  SERVICE_SURPLUS_AMOUNT("23"),

  /**
   * 退租补偿税金
   */
  RETURN_SUPPLY_TAX("24"),

  /**
   * 转租手续费
   */
  SUBLET_COUNTER_FEE("25"),

  /**
   * 定金转违约退费
   */
  DEPOSIT_REFUND_FEE("26"),

  /**
   * 应出定金
   */
  OUT_DEPOSIT_AMOUNT("27");

  PaymentTransTypeEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }
}
