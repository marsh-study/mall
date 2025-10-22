package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.mall.backend.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Getter
@Setter
@TableName("pms_spu")
@AllArgsConstructor
@NoArgsConstructor
public class PmsSpu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品类型ID
     */
    private Long categoryId;

    /**
     * 商品品牌ID
     */
    private Long brandId;

    /**
     * 原价【起】
     */
    private Long originPrice;

    /**
     * 现价【起】
     */
    private Long price;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 商品主图
     */
    private String picUrl;

    /**
     * 商品图册
     */
    private String album;

    /**
     * 单位
     */
    private String unit;

    /**
     * 商品简介
     */
    private String description;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 商品状态(0:下架 1:上架)
     */
    private Integer status;

    @TableField(exist = false)
    private List<PmsSku> skuList;

    @TableField(exist = false)
    private List<PmsSpuAttribute> attributeList;

    @TableField(exist = false)
    private List<PmsSpuAttribute> specList;

}
