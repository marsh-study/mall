package com.mall.backend.model.dto;

import lombok.Data;

@Data
public class OrderSkuRequest {
    /**
     * SKU ID
     */
    private Long skuId;
    
    /**
     * 购买数量
     */
    private Integer quantity;
}