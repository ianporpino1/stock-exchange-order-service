package com.stockexchange.orderservice.model.dto;

import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.OrderStatus;
import com.stockexchange.orderservice.model.OrderType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        OrderStatus orderStatus,
        OrderType orderType,
        String symbol,
        BigDecimal price,
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

    public OrderResponse(CreateOrderCommand command) {
        this(command.orderId(),
                OrderStatus.ACCEPTED,
                command.orderType(),
                command.symbol(),
                command.price(),
                0,
                command.quantity(),
                command.createdAt(),
                command.userId());
    }
}
