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
 * 商品属性表
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Getter
@Setter
@TableName("pms_category_attribute")
public class PmsCategoryAttribute extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 类型(1:规格;2:属性;)
     */
    private Integer type;

}
