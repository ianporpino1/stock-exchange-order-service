package com.stockexchange.orderservice.model.dto;

import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.OrderStatus;
import com.stockexchange.orderservice.model.OrderType;

import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        OrderStatus orderStatus,
        OrderType orderType,
        String symbol,
        double price,
        int executedQuantity,
        int totalQuantity,
        Instant orderDate,
        UUID userId) {
    
    public OrderResponse(Order order) {
        this(order.getOrderId(),
                order.getStatus(),
                order.getType(),
                order.getSymbol(),
                order.getPrice(),
                order.getExecutedQuantity(),
                order.getTotalQuantity(),
                order.getCreatedAt(), 
                order.getUserId());
    }
}
