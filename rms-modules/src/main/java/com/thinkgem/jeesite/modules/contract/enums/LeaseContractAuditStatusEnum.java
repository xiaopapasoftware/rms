package com.thinkgem.jeesite.modules.contract.enums;

/**
 * 承租合同审核状态
 *
 * @author wangshujin
 */
public enum LeaseContractAuditStatusEnum {

    /**
     * 待审核
     */
    TO_BE_AUDIT("0"),

    /**
     * 审核通过
     */
    PASS("1"),

    /**
     * 审核拒绝
     */
    REFUSE("2");

    LeaseContractAuditStatusEnum(String value) {
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
