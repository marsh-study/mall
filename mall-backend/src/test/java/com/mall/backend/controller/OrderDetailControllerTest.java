package com.mall.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.backend.model.dto.OrderRequest;
import com.mall.backend.model.dto.OrderSkuRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetOrderDetail() throws Exception {
        // 先创建一个订单
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setMemberId(1L);
        orderRequest.setSource(2);

        OrderSkuRequest itemRequest = new OrderSkuRequest();
        itemRequest.setSkuId(1L);
        itemRequest.setQuantity(2);

        List<OrderSkuRequest> items = new ArrayList<>();
        items.add(itemRequest);
        orderRequest.setItems(items);

        // 创建订单并获取订单ID
        String createOrderResponse = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00000"))
                .andExpect(jsonPath("$.data").isNumber())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 解析订单ID
        Long orderId = objectMapper.readTree(createOrderResponse).get("data").asLong();
        System.out.println("创建的订单ID: " + orderId);

        // 等待一段时间确保事务提交
        Thread.sleep(1000);

        // 测试获取订单详情
        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00000"))
                .andExpect(jsonPath("$.data.id").value(orderId))
                .andExpect(jsonPath("$.data.orderSn").isString())
                .andExpect(jsonPath("$.data.totalAmount").isNumber())
                .andExpect(jsonPath("$.data.orderItems").isArray());

        System.out.println("获取订单详情接口测试通过");
    }
}