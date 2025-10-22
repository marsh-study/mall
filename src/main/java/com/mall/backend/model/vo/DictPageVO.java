package com.mall.backend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictPageVO {
/**
     * 字典ID
     */
    private Long id;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 状态(1:启用;0:禁用)
     */
    private Integer status;
    /**
     * 字典值
     */
    private String value;
}
