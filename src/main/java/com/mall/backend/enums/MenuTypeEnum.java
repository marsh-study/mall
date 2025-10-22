package com.mall.backend.enums;

/**
 * 菜单类型枚举
 * (1：菜单；2：目录；3：外链；4：按钮)
 */

public enum MenuTypeEnum {
    MENU(1,"MENU" ),
    CATALOG(2,"CATALOG"),
    EXTLINK(3,"EXTLINK"),
    BUTTON(4,"BUTTON");

    private final Integer code;
    private final String description;

    MenuTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据code获取枚举值
     */
    public static MenuTypeEnum fromCode(Integer code) {
        for (MenuTypeEnum type : MenuTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的菜单类型code: " + code);
    }

    /**
     * 根据code获取描述字符串
     */
    public static String getDescriptionByCode(Integer code) {
        for (MenuTypeEnum type : MenuTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type.getDescription();
            }
        }
        return "未知类型";
    }

    /**
     * 根据描述字符串获取code
     */
    public static Integer getCodeByDescription(String description) {

        for (MenuTypeEnum type : MenuTypeEnum.values()) {
            if (type.getDescription().equals(description)) {
                return type.getCode();
            }
        }
        return null;
    }

}
