package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 
 * 出租合同审核状态
 * 
 * @author wangshujin
 */
public enum ContractAuditStatusEnum {

  /**
   * 暂存
   */
  TEMP_EXIST("0"),

  /**
   * 录入完成到账收据待登记
   */
  FINISHED_TO_SIGN("1"),

  /**
   * 到账收据完成合同内容待审核
   */
  SIGNED_TO_AUDIT_CONTENT("2"),

  /**
   * 内容审核拒绝
   */
  CONTENT_AUDIT_REFUSE("3"),

  /**
   * 内容审核通过到账收据待审核
   */
  INVOICE_TO_AUDITED("4"),

  /**
   * 到账收据审核拒绝
   */
  INVOICE_AUDITED_REFUSE("5"),

  /**
   * 到账收据审核通过
   */
  INVOICE_AUDITED_PASS("6");

  ContractAuditStatusEnum(String value) {
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
