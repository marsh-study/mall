package com.mall.backend.model.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
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
}