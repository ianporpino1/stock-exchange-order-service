package com.stockexchange.orderservice.controller.dto;



import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.OrderStatus;

import java.time.Instant;

public record OrderResponse(Long orderId,
        OrderStatus orderStatus,
        int executedQuantity,
        int totalQuantity,
        Instant orderDate,
        Long userId) {
    public OrderResponse(Order order) {
        this(order.getId(), 
                order.getStatus(),
                order.getExecutedQuantity(),
                order.getTotalQuantity(),
                order.getCreatedAt(), 
                order.getUser().getId());
    }
}
