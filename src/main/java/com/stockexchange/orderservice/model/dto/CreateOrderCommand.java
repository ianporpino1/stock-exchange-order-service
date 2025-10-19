package com.stockexchange.orderservice.model.dto;

import com.stockexchange.orderservice.model.OrderType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateOrderCommand(UUID commandId,
                                 UUID orderId,
                                 UUID userId,
                                 String symbol,
                                 BigDecimal price,
                                 int quantity,
                                 OrderType orderType,
                                 Instant createdAt) {
    public CreateOrderCommand(OrderRequest orderRequest, UUID commandId, UUID orderId, UUID userId) {
        this(
                commandId,
                orderId,
                userId,
                orderRequest.symbol(),
                orderRequest.price(),
                orderRequest.quantity(),
                orderRequest.orderType(),
                Instant.now());
    }
}
