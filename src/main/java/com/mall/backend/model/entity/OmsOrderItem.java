package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.mall.backend.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 订单商品信息表
 * </p>
 *
 * @author lsy
 * @since 2025-10-21
 */
@Getter
@Setter
@TableName("oms_order_item")
@AllArgsConstructor
@NoArgsConstructor
public class OmsOrderItem extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 商品ID
     */
    private Long skuId;

    /**
     * 商品编码
     */
    private String skuSn;

    /**
     * 规格名称
     */
    private String skuName;

    /**
     * 商品图片
     */
    private String picUrl;

    /**
     * 商品单价(单位：分)
     */
    private Long price;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品总价(单位：分)
     */
    private Long totalAmount;

    /**
     * 逻辑删除标识(1:已删除；0:正常)
     */
    private Integer deleted;

}
