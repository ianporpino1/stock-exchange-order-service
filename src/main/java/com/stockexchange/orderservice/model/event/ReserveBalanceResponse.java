package com.stockexchange.orderservice.model.event;

import com.stockexchange.orderservice.model.OrderType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReserveBalanceResponse(UUID orderId,
                                     UUID userId,
                                     String symbol,
                                     BigDecimal price,
                                     int quantity,
                                     OrderType orderType,
                                     Instant createdAt,
                                     BalanceStatus balanceStatus) {
}
