package com.stockexchange.orderservice.model.dto;

import com.stockexchange.orderservice.model.OrderType;
import java.time.Instant;
import java.util.UUID;

public record CreateOrderCommand(UUID commandId,
                                 UUID orderId,
                                 UUID userId,
                                 String symbol,
                                 double price,
                                 int quantity,
                                 OrderType orderType,
                                 Instant createdAt) {
    public CreateOrderCommand(OrderRequest orderRequest, UUID commandId, UUID orderId, UUID userId, Instant createdAt) {
        this(
                commandId,
                orderId,
                userId,
                orderRequest.symbol(),
                orderRequest.price(),
                orderRequest.quantity(),
                orderRequest.orderType(),
                createdAt);
    }
}
