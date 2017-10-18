package com.thinkgem.jeesite.modules.fee.enums;

/**
 * @author wangganggang
 * @date 2017年10月18日 10:47
 */
public enum GenerateOrderEnum {
    YES(0), NO(1);

    private int value;

    GenerateOrderEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
