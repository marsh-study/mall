package com.mall.backend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 订单来源(1:APP；2:网页)
     */
    private Integer source;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 使用的优惠券
     */
    private Long couponId;

    /**
     * 订单商品列表
     */
    private List<OrderSkuRequest> items;
}