package com.stockexchange.orderservice.model.event;

import com.stockexchange.orderservice.model.OrderType;

import java.math.BigDecimal;
import java.util.UUID;

public record RefundCommand(UUID orderId,
                            UUID userId,
                            BigDecimal amount,
                            OrderType orderType) {
}
