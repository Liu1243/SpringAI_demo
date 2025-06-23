package com.liu.springai_demo.constant;

/**
 * @author 1
 * &#064;Description  会话类型常量类
 */
public enum ChatType {
    CHAT("chat"),
    GAME("game"),
    SERVICE("service"),
    PDF("pdf");

    private final String value;

    ChatType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}