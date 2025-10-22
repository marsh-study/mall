package com.mall.backend.service.oms.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.model.dto.OrderRequest;
import com.mall.backend.model.dto.OrderSkuRequest;
import com.mall.backend.model.entity.OmsOrder;
import com.mall.backend.mapper.oms.OmsOrderMapper;
import com.mall.backend.model.entity.OmsOrderItem;
import com.mall.backend.model.entity.PmsSku;
import com.mall.backend.model.query.OrderQuery;
import com.mall.backend.model.vo.Order;
import com.mall.backend.model.vo.OrderDetail;
import com.mall.backend.service.oms.OmsOrderItemService;
import com.mall.backend.service.oms.OmsOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.backend.service.pms.PmsSkuService;
import com.mall.backend.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 订单详情表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2025-10-21
 */
@Service
public class OmsOrderServiceImpl extends ServiceImpl<OmsOrderMapper, OmsOrder> implements OmsOrderService {

    @Autowired
    private OmsOrderItemService omsOrderItemService;
    
    @Autowired
    private PmsSkuService pmsSkuService;

    @Override
    public PageResult<Order> getOrderPage(OrderQuery query) {
        Page page = new Page(query.getPageNum(), query.getPageSize());
        QueryWrapper<OmsOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(query.getOrderSn()!=null,"order_sn", query.getOrderSn())
                .eq(query.getStatus()!=null,"status", query.getStatus());
        Page<OmsOrder> omsOrderPage = this.page(page, wrapper);

        List<Order> collect = new ArrayList<>() ;
        for (OmsOrder record : omsOrderPage.getRecords()) {
            OmsOrderItem orderItem = omsOrderItemService.getOne(new QueryWrapper<OmsOrderItem>().eq("order_id",record.getId()));
            Order order = Order.fromEntity(record, orderItem);
            collect.add(order);

        }

        return new PageResult<>(collect,omsOrderPage.getTotal() );
    }
    
    @Override
    @Transactional
    public Long createOrder(OrderRequest orderRequest) {
        // 计算订单总金额和总数量
        Long totalAmount = 0L;
        Integer totalQuantity = 0;
        
        List<OmsOrderItem> orderItems = new ArrayList<>();
        for (OrderSkuRequest itemRequest : orderRequest.getItems()) {
            // 获取SKU信息
            PmsSku sku = pmsSkuService.getById(itemRequest.getSkuId());
            if (sku == null) {
                throw new RuntimeException("SKU不存在: " + itemRequest.getSkuId());
            }
            
            // 计算商品总价
            Long itemTotalAmount = sku.getPrice() * itemRequest.getQuantity();
            
            // 累加到订单总额和总数量
            totalAmount += itemTotalAmount;
            totalQuantity += itemRequest.getQuantity();
            
            // 创建订单商品项
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setSpuName(sku.getName()); // 这里应该从SPU获取名称，简化处理
            orderItem.setSkuId(sku.getId());
            orderItem.setSkuSn(sku.getSkuSn());
            orderItem.setSkuName(sku.getName());
            orderItem.setPicUrl(sku.getPicUrl());
            orderItem.setPrice(sku.getPrice());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setTotalAmount(itemTotalAmount);
            orderItem.setDeleted(0);
            
            orderItems.add(orderItem);
        }
        
        // 创建订单主表
        OmsOrder order = new OmsOrder();
        order.setOrderSn("O" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        order.setTotalAmount(totalAmount);
        order.setTotalQuantity(totalQuantity);
        order.setSource(orderRequest.getSource());
        order.setRemark(orderRequest.getRemark());
        order.setMemberId(orderRequest.getMemberId());
        order.setCouponId(orderRequest.getCouponId());
        order.setCouponAmount(0L); // 简化处理，没有优惠券逻辑
        order.setFreightAmount(0L); // 简化处理，免运费
        order.setPaymentAmount(totalAmount); // 实付金额等于总金额
        order.setStatus(101); // 待付款
        order.setDeleted(0); // 未删除
        
        // 保存订单主表
        this.save(order);
        
        // 设置订单ID并保存订单商品项
        for (OmsOrderItem orderItem : orderItems) {
            orderItem.setOrderId(order.getId());
        }
        omsOrderItemService.saveBatch(orderItems);
        
        return order.getId();
    }

    @Override
    public OrderDetail getOrderDetail(Long orderId) {
        OrderDetail detail = new OrderDetail();
        OmsOrder order = this.getById(orderId);

        detail = OrderDetail.fromEntity(order, omsOrderItemService.list(new QueryWrapper<OmsOrderItem>().eq("order_id",orderId)));
        return detail;
    }
}