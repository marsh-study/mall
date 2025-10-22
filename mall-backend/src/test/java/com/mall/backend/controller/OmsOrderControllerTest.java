package com.mall.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class OmsOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Map<String, Object> orderRequest;

    @BeforeEach
    public void setUp() {
        // 构造订单请求数据
        orderRequest = new HashMap<>();
        orderRequest.put("memberId", 1L);
        orderRequest.put("totalAmount", 9900L);
        orderRequest.put("totalQuantity", 1);
        orderRequest.put("source", 2);
        
        Map<String, Object> orderItem = new HashMap<>();
        orderItem.put("skuId", 1L);
        orderItem.put("skuName", "测试商品");
        orderItem.put("price", 9900L);
        orderItem.put("quantity", 1);
        orderItem.put("totalAmount", 9900L);
        
        orderRequest.put("orderItem", orderItem);
    }

    @Test
    public void testCreateOrder() throws Exception {
        // 测试下单接口
        mockMvc.perform(post("/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00000"))
                .andExpect(jsonPath("$.data.memberId").value(1L))
                .andExpect(jsonPath("$.data.totalAmount").value(9900L));
        
        System.out.println("下单接口测试通过");
    }
}