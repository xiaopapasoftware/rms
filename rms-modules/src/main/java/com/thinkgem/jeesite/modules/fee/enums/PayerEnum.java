package com.thinkgem.jeesite.modules.fee.enums;

/**
 * @author wangganggang
 * @date 2017年11月03日 15:06
 */
public enum PayerEnum {
    COMPANY(1, "公司"), RENT_USER(0, "租客");

    private int value;
    private String name;

    PayerEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}
