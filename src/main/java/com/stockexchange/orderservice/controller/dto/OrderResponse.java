package com.stockexchange.orderservice.controller.dto;

import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.OrderStatus;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(UUID orderId,
        OrderStatus orderStatus,
        int executedQuantity,
        int totalQuantity,
        Instant orderDate,
        UUID userId) {
    public OrderResponse(Order order) {
        this(order.getId(), 
                order.getStatus(),
                order.getExecutedQuantity(),
                order.getTotalQuantity(),
                order.getCreatedAt(), 
                order.getUserId());
    }
}
