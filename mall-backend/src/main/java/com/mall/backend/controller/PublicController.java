package com.mall.backend.controller;

import com.mall.backend.annotation.SystemLog;
import com.mall.backend.model.dto.OrderRequest;
import com.mall.backend.service.oms.OmsOrderService;
import com.mall.backend.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private OmsOrderService omsOrderService;

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
}
