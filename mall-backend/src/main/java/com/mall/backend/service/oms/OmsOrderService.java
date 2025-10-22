package com.mall.backend.service.oms;

import com.mall.backend.model.dto.OrderRequest;
import com.mall.backend.model.entity.OmsOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.backend.model.query.OrderQuery;
import com.mall.backend.model.vo.Order;
import com.mall.backend.model.vo.OrderDetail;
import com.mall.backend.util.PageResult;

/**
 * <p>
 * 订单详情表 服务类
 * </p>
 *
 * @author lsy
 * @since 2025-10-21
 */
public interface OmsOrderService extends IService<OmsOrder> {

    PageResult<Order> getOrderPage(OrderQuery query);
    
    /**
     * 创建订单
     * @param orderRequest 订单请求参数
     * @return 生成的订单ID
     */
    Long createOrder(OrderRequest orderRequest);
    
    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderDetail getOrderDetail(Long orderId);
}