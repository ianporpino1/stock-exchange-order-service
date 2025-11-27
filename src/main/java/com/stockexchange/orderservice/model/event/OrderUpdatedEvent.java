package com.stockexchange.orderservice.model.event;

import com.stockexchange.orderservice.model.OrderStatus;
import com.stockexchange.orderservice.model.OrderType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderUpdatedEvent(UUID orderId,
                                OrderStatus orderStatus,
                                OrderType orderType,
                                String symbol,
                                BigDecimal price,
                                int executedQuantity,
                                int totalQuantity,
                                Instant orderDate,
                                UUID userId) {
}
