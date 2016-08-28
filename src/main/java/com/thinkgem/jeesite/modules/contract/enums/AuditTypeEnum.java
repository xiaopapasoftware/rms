package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 
 * 审核类型
 * 
 * @author wangshujin
 */
public enum AuditTypeEnum {

    /**
     * 承租合同内容审核
     */
    LEASE_CONTRACT_CONTENT("0"),

    /**
     * 定金协议内容审核
     */
    DEPOSIT_AGREEMENT_CONTENT("1"),

    /**
     * 出租合同内容审核
     */
    RENT_CONTRACT_CONTENT("2"),

    /**
     * 出租合同变更协议审核
     */
    RENT_CONTRACT_CHANGE("3"),

    /**
     * 账务交易审核
     */
    TRADING_ACCOUNT("4"),

    /**
     * 工作总结审核
     */
    WORK_SUMMARY("5"),

    /**
     * 工作备忘审核
     */
    WORK_BACK("6"),

    /**
     * 工作计划审核
     */
    WORK_PLAN("7");

    AuditTypeEnum(String value) {
	this.value = value;
    }

    private String value;

    public String getValue() {
	return value;
    }
}
