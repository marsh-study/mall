package com.mall.backend.model.vo;

import com.mall.backend.model.entity.OmsOrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long skuId;
    private String skuName;
    private String picUrl;
    private Long price;
    private Integer count;
    private Long totalAmount;

    public static OrderItem fromEntity(OmsOrderItem entity) {
        OrderItem item = new OrderItem();
        item.setId(entity.getId());
        item.setOrderId(entity.getOrderId());
        item.setSkuId(entity.getSkuId());
        item.setSkuName(entity.getSkuName());
        item.setPicUrl(entity.getPicUrl());
        item.setPrice(entity.getPrice());
        item.setCount(entity.getQuantity());

        item.setTotalAmount(entity.getTotalAmount());
        return item;
    }
}
