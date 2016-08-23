package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 
 * 审核状态
 * 
 * @author wangshujin
 *
 */
public enum AuditStatusEnum {

    /**
     * 通过
     */
    PASS("1"),

    /**
     * 拒绝
     */
    REFUSE("2");

    AuditStatusEnum(String value) {
	this.value = value;
    }

    private String value;

    public String getValue() {
	return value;
    }

}
