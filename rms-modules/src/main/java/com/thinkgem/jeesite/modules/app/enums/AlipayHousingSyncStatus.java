package com.thinkgem.jeesite.modules.app.enums;

/**
 * 支付宝房源同步状态
 */
public enum AlipayHousingSyncStatus {

    /**
     * 同步处理中
     */
    PROCESSING("0"),

    /**
     * 同步成功
     */
    SUCCESS("1"),

    /**
     * 同步失败
     */
    FAIL("2");

    AlipayHousingSyncStatus(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;

    }
}