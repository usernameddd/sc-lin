package com.daizening.enums;

public enum StatusEnums {

    SUCCESS(1000, "成功"),
    FAILED(-1000, "失败"),
    PARAMS_ERROR(-1001, "参数错误"),
    EXCEPTION(-1002, "内部异常"),
    TOKEN_ERROR(-1003, "无效token"),
    TOKEN_EXPIRED(-1004, "token已过期"),
    LOGIN_FAILED(-1005, "用户名或密码错误");


    private int code;

    private String info;

    private StatusEnums(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode () {
        return code;
    }

    public String getInfo () {
        return info;
    }
}
