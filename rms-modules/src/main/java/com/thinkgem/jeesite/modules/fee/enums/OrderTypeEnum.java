package com.thinkgem.jeesite.modules.fee.enums;

/**
 * @author wangganggang
 * @date 2017年09月28日 9:39
 */
public enum OrderTypeEnum {
    /**
     * 0：电费 1：水费 2 燃气费  3：宽带 4：电视 5:房租 6:房租押金 7:定金 8:违约金
     */
    ELECTRICITY(0, "电费"),
    WATER(1, "水费"),
    GAS(2, "燃气费"),
    NET(3, "宽带"),
    TV(4, "电视"),
    RENT(5, "房租"),
    DEPOSIT(6, "房租押金");

    private int value;
    private String name;

    OrderTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
