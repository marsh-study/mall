package com.mall.backend.model.vo;

import com.mall.backend.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class RolePageVO{
    /**
     * 角色编码
     */
    private  String code;

    /**
     * 角色ID
     */
    private Long id;
    /**
     * 角色名称
     */
    private  String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 角色状态
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;

}
