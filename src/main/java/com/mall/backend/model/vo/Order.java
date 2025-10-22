package com.mall.backend.model.vo;

import com.mall.backend.model.entity.OmsOrder;
import com.mall.backend.model.entity.OmsOrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String orderSn;
    private String totalAmount;
    private String payAmount;
    private Integer payType;
    private Integer status;
    private Integer totalQuantity;
    private String createTime;
    private Long memberId;
    private Integer sourceType;
    private OrderItem orderItem;

    public static Order fromEntity(OmsOrder order, OmsOrderItem item) {
        Order vo = new Order();
        vo.setId(order.getId());
        vo.setOrderSn(order.getOrderSn());
        vo.setTotalAmount(String.valueOf(order.getTotalAmount()));
        vo.setPayAmount(String.valueOf(order.getTotalAmount()));
        vo.setPayType(order.getPaymentMethod());
        vo.setStatus(order.getStatus());
        vo.setTotalQuantity(order.getTotalQuantity());
        // 修复空指针异常：检查createTime是否为null
        Date createTime = order.getCreateTime();
        vo.setCreateTime(createTime != null ? createTime.toString() : null);
        vo.setMemberId(order.getMemberId());
        vo.setSourceType(order.getSource());
        vo.setOrderItem(OrderItem.fromEntity(item));
        return vo;
        }
}