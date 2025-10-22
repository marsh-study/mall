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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateOrder() throws Exception {
        // 构造订单请求数据
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setMemberId(1L);
        orderRequest.setSource(2); // 网页下单
        
        // 构造订单商品项
        OrderSkuRequest itemRequest = new OrderSkuRequest();
        itemRequest.setSkuId(762L);
        itemRequest.setQuantity(2);
        
        List<OrderSkuRequest> items = new ArrayList<>();
        items.add(itemRequest);
        orderRequest.setItems(items);

        // 发送POST请求测试下单接口
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("00000"))
                .andExpect(jsonPath("$.data").isNumber())
                .andExpect(jsonPath("$.msg").value("成功"));
        
        System.out.println("下单接口测试通过");
    }
}