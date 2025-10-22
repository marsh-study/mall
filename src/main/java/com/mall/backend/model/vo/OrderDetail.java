package com.mall.backend.model.vo;

import com.mall.backend.model.entity.OmsOrder;
import com.mall.backend.model.entity.OmsOrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDetail {
    /**
     * 订单ID
     */
    private Long id;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 订单总额（元）
     */
    private BigDecimal totalAmount;

    /**
     * 商品总数
     */
    private Integer totalQuantity;

    /**
     * 订单来源(1:APP；2:网页)
     */
    private Integer source;

    /**
     * 订单状态：	101->待付款；	102->用户取消；	103->系统取消；	201->已付款；	202->申请退款；	203->已退款；	301->待发货；	401->已发货；	501->用户收货；	502->系统收货；	901->已完成；
     */
    private Integer status;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 使用的优惠券
     */
    private Long couponId;

    /**
     * 优惠券抵扣金额（元）
     */
    private BigDecimal couponAmount;

    /**
     * 运费金额（元）
     */
    private BigDecimal freightAmount;

    /**
     * 应付总额（元）
     */
    private BigDecimal paymentAmount;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 支付方式(1：微信JSAPI；2：支付宝；3：余额；4：微信APP)
     */
    private Integer paymentMethod;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 确认收货时间
     */
    private Date receiveTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 订单商品项列表
     */
    private List<OmsOrderItem> orderItems;

    /**
     * 从实体类转换为VO类
     * @param order 订单实体
     * @param orderItems 订单商品项列表
     * @return OrderDetail
     */
    public static OrderDetail fromEntity(OmsOrder order, List<OmsOrderItem> orderItems) {
        OrderDetail detail = new OrderDetail();
        detail.setId(order.getId());
        detail.setOrderSn(order.getOrderSn());
        detail.setTotalAmount(convertAmount(order.getTotalAmount()));
        detail.setTotalQuantity(order.getTotalQuantity());
        detail.setSource(order.getSource());
        detail.setStatus(order.getStatus());
        detail.setRemark(order.getRemark());
        detail.setMemberId(order.getMemberId());
        detail.setCouponId(order.getCouponId());
        detail.setCouponAmount(convertAmount(order.getCouponAmount()));
        detail.setFreightAmount(convertAmount(order.getFreightAmount()));
        detail.setPaymentAmount(convertAmount(order.getPaymentAmount()));
        detail.setPaymentTime(order.getPaymentTime());
        detail.setPaymentMethod(order.getPaymentMethod());
        detail.setDeliveryTime(order.getDeliveryTime());
        detail.setReceiveTime(order.getReceiveTime());
        detail.setCreateTime(order.getCreateTime());
        detail.setOrderItems(orderItems);
        return detail;
    }

    /**
     * 将分转换为元
     * @param amount 分
     * @return 元
     */
    private static BigDecimal convertAmount(Long amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(amount).divide(new BigDecimal(100));
    }
}