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
 * 商品品牌表
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Getter
@Setter
@TableName("pms_brand")
public class PmsBrand extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * LOGO图片
     */
    private String logoUrl;

    /**
     * 排序
     */
    private Integer sort;


}
