package com.mall.backend.enums;

public enum ResultCode {
    SUCCESS("00000", "成功"),
    ERROR("A0001", "系统异常"),

    AUTHENTICATION_ERROR("A0002", "登录失败"),
    A0230("A0230", "token过期"),

    A0231("A0231", "账号在其他地方登录,请重新登录！"),
    A0232("A0232", "已被强制下线！"),

    A0233("A0233", "token无效"),

    REFRESH_TOKEN_ERROR("A0234", "刷新令牌失败"),


    // 用户相关
    USER_NOT_FOUND("U0001", "用户不存在"),
    USER_PASSWORD_ERROR("U0002", "密码错误"),

    // 验证码相关
    CAPTCHA_EXPIRED("C0001", "验证码已过期"),
    CAPTCHA_ERROR("C0002", "验证码错误"),
    CAPTCHA_ID_REQUIRED("C0003", "缺少验证码ID"),
    CAPTCHA_CODE_REQUIRED("C0004", "缺少验证码");

    private final String code;
    private final String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}