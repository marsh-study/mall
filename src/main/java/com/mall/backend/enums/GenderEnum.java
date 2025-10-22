package com.mall.backend.enums;

//性别枚举,0：未知，1：男，2：女
public enum GenderEnum {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    private Integer code;
    private String message;

    GenderEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // 根据code获取对应的message
    public static String getMessageByCode(Integer code) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.getCode().equals(code)) {
                return gender.getMessage();
            }
        }
        return null; // 或者抛出异常，取决于业务需求
    }
}
