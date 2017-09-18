package com.thinkgem.jeesite.modules.inventory.enums;

/**
 * @author wangganggang
 * @date 2017年09月18日 下午10:36
 */
public enum HouseRentMethod {
    FULL_RENT("0"),SEPARATE_RENT("1");

    HouseRentMethod(String value) {
        this.value = value;
    }

    private String value;

    public String value(){
        return value;
    }
}
