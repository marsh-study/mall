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
 * 商品库存表
 * </p>
 *
 * @author lsy
 * @since 2025-10-16
 */
@Getter
@Setter
@TableName("pms_sku")
public class PmsSku extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品编码
     */
    private String skuSn;

    /**
     * SPU ID
     */
    private Long spuId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品规格值，以英文逗号(,)分割
     */
    private String specIds;

    /**
     * 商品价格(单位：分)
     */
    private Long price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 库存锁定数量
     */
    private Integer lockedStock;

    /**
     * 商品图片地址
     */
    private String picUrl;

}
