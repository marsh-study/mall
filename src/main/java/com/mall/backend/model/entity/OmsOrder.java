package com.mall.backend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.mall.backend.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单详情表
 * </p>
 *
 * @author lsy
 * @since 2025-10-21
 */
@Getter
@Setter
@TableName("oms_order")
public class OmsOrder extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 订单总额（分）
     */
    private Long totalAmount;

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
     * 优惠券抵扣金额（分）
     */
    private Long couponAmount;

    /**
     * 运费金额（分）
     */
    private Long freightAmount;

    /**
     * 应付总额（分）
     */
    private Long paymentAmount;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 支付方式(1：微信JSAPI；2：支付宝；3：余额；4：微信APP)
     */
    private Integer paymentMethod;

    /**
     * 微信支付等第三方支付平台的商户订单号
     */
    private String outTradeNo;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 微信退款单号
     */
    private String refundId;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 确认收货时间
     */
    private Date receiveTime;

    /**
     * 评价时间
     */
    private Date commentTime;

    /**
     * 逻辑删除【0->正常；1->已删除】
     */
    private Integer deleted;

    @TableField(exist = false)
    private OmsOrderItem orderItem;

}
