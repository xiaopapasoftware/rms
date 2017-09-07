package com.tangchao.fee.bill.enums;

/**
 * @author wangganggang
 * @date 2017年09月07日 下午9:04
 */
public enum BillStatusEnum {

    APP(0),APV(1),PASS(3),REHECT(4);

    private int value;

    BillStatusEnum(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }
}
