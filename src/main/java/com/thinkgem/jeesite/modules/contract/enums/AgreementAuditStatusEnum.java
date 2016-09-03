package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 
 * 定金协议审核状态
 * 
 * @author wangshujin
 *
 */
public enum AgreementAuditStatusEnum {

  /**
   * 录入完成到账收据待登记
   */
  FINISHED_TO_SIGN("0"),

  /**
   * 到账收据登记完成内容待审核
   */
  SIGNED_TO_AUDIT_CONTENT("1"),

  /**
   * 内容审核拒绝
   */
  CONTENT_AUDIT_REFUSE("2"),

  /**
   * 内容审核通过到账收据待审核
   */
  INVOICE_TO_AUDITED("3"),

  /**
   * 到账收据审核拒绝
   */
  INVOICE_AUDITED_REFUSE("4"),

  /**
   * 到账收据审核通过
   */
  INVOICE_AUDITED_PASS("5"),

  /**
   * 暂存
   */
  TEMP_EXIST("6");

  AgreementAuditStatusEnum(String value) {
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
