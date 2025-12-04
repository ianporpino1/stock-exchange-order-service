package com.stockexchange.orderservice.model.event;

import com.stockexchange.orderservice.model.OrderType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record MatchOrderCommand(UUID orderId,
                                UUID userId,
                                String symbol,
                                BigDecimal price,
                                int quantity,
                                OrderType orderType,
                                Instant createdAt) {
    public MatchOrderCommand(ReserveBalanceResponse response) {
        this(response.orderId(), response.userId(), response.symbol(), response.price(), response.quantity(), response.orderType(), response.createdAt());
    }
}
