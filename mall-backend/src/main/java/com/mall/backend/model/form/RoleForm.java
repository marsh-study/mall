package com.mall.backend.model.form;

import lombok.Data;

@Data
public class RoleForm {
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 角色状态(1-正常；0-停用)
     */
    private Integer status;
}
