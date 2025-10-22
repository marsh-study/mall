package com.mall.backend.controller;

import com.mall.backend.annotation.SystemLog;
import com.mall.backend.model.dto.OrderRequest;
import com.mall.backend.model.query.OrderQuery;
import com.mall.backend.model.vo.Order;
import com.mall.backend.model.vo.OrderDetail;
import com.mall.backend.service.oms.OmsOrderService;
import com.mall.backend.util.PageResult;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单详情表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2025-10-20
 */
@RestController
@RequestMapping("/orders")
public class OmsOrderController {

    @Autowired
    private OmsOrderService omsOrderService;

    @GetMapping
    public Result<PageResult<Order>> getOrderPage(OrderQuery  query){
        return Result.success(omsOrderService.getOrderPage(query));
    }

    /**
     * 用户下单接口
     * @param orderRequest 订单信息
     * @return 下单结果
     */
    @PostMapping
    @SystemLog(module = "订单管理", operation = "用户下单")
    public Result<Long> createOrder(@RequestBody OrderRequest orderRequest) {
        Long orderId = omsOrderService.createOrder(orderRequest);
        return Result.success(orderId);
    }
    
    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/{orderId}")
    public Result<OrderDetail> getOrderDetail(@PathVariable Long orderId) {
        OrderDetail orderDetail = omsOrderService.getOrderDetail(orderId);
        return Result.success(orderDetail);
    }
    

}