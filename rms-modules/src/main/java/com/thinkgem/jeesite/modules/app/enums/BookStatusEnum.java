package com.thinkgem.jeesite.modules.app.enums;

/**
 * @author wangganggang
 * @date 2017年08月19日 下午1:26
 */
public enum BookStatusEnum {

    BOOK_APP("0"),
    BOOK_APV("1"),
    BOOK_SUCCESS("2"),
    BOOK_CANCEL("3");

    BookStatusEnum(String value) {
        this.value =value;
    }

    private String value;

    public String value() {
        return value;
    }
    }
