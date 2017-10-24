package com.thinkgem.jeesite.modules.fee.enums;

/**
 * @author wangganggang
 * @date 2017年10月23日 15:27
 */
public enum ChargeMethodEnum {
    //固定模式
    FIX_MODEL(0,"固定模式"),
    // 账单模式
    ACCOUNT_MODEL(1,"账单模式");

    private int value;
    private String name;

    ChargeMethodEnum(int status, String name){
        this.value = status;
        this.name = name;
    }

    public int getValue(){
        return value;
    }

    public String getName(){
        return name;
    }

    public static ChargeMethodEnum fromValue(int value){
        for(ChargeMethodEnum val : ChargeMethodEnum.values()){
            if(val.getValue() == value){
                return val;
            }
        }
        return null;
    }
}
