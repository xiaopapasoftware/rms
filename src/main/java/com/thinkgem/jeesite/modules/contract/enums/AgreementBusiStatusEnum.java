package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 定金协议业务状态
 * 
 * @author wangshujin
 *
 */
public enum AgreementBusiStatusEnum {

  /**
   * 待转合同
   */
  TOBE_CONVERTED("0"),

  /**
   * 已转违约
   */
  BE_CONVERTED_BREAK("1"),

  /**
   * 已转合同
   */
  BE_CONVERTED_CONTRACT("2"),

  /**
   * 定金转违约到账待登记
   */
  CONVERTBREAK_TO_SIGN("3"),

  /**
   * 定金转违约到账待审核
   */
  CONVERTBREAK_TO_AUDIT("4"),

  /**
   * 定金转违约到账审核拒绝
   */
  CONVERTBREAK_AUDIT_REFUSE("6");

  AgreementBusiStatusEnum(String value) {
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
