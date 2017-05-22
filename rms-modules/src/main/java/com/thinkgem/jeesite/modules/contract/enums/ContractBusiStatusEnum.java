package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 
 * 出租合同业务状态
 * 
 * @author wangshujin
 */
public enum ContractBusiStatusEnum {

  /**
   * 有效
   */
  VALID("0"),

  /**
   * 提前退租待核算
   */
  EARLY_RETURN_ACCOUNT("1"),

  /**
   * 正常退租待核算
   */
  NORMAL_RETURN_ACCOUNT("2"),

  /**
   * 逾期退租待核算
   */
  LATE_RETURN_ACCOUNT("3"),

  /**
   * 退租核算完成到账收据待登记
   */
  ACCOUNT_DONE_TO_SIGN("4"),

  /**
   * 退租款项待审核
   */
  RETURN_TRANS_TO_AUDIT("5"),

  /**
   * 退租款项审核拒绝
   */
  RETURN_TRANS_AUDIT_REFUSE("6"),

  /**
   * 提前退租
   */
  EARLY_RETURN("7"),

  /**
   * 正常退租
   */
  NORMAL_RETURN("8"),

  /**
   * 逾期退租
   */
  LATE_RETURN("9"),

  /**
   * 特殊退租待结算
   */
  SPECAIL_RETURN_ACCOUNT("10"),

  /**
   * 特殊退租结算待审核
   */
  SPECAIL_RETURN_ACCOUNT_AUDIT("11"),

  /**
   * 特殊退租结算审核拒绝
   */
  SPECAIL_RETURN_ACCOUNT_AUDIT_REFUSE("12"),

  /**
   * 正常人工续签
   */
  NORMAL_RENEW("14"),

  /**
   * 逾期自动续签
   */
  LATE_AUTO_RENEW("15"),

  /**
   * 特殊退租
   */
  SPECIAL_RETURN("16"),

  /**
   * 特殊退租内容待审核
   */
  SPECIAL_RENTURN_CONTENT_AUDIT("17"),

  /**
   * 特殊退租内容审核拒绝
   */
  SPECIAL_RENTURN_CONTENT_AUDIT_REFUSE("18");

  ContractBusiStatusEnum(String value) {
    this.value = value;
  }

  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
