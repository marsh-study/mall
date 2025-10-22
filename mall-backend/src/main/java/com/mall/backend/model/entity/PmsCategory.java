package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.mall.backend.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Getter
@Setter
@TableName("pms_category")
public class PmsCategory extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品分类名称
     */
    private String name;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 图标地址
     */
    private String iconUrl;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 显示状态:( 0:隐藏 1:显示)
     */
    private Integer visible;

}
