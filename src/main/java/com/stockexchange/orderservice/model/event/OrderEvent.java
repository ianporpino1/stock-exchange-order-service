package com.stockexchange.orderservice.model.event;


import com.stockexchange.orderservice.model.OrderStatus;
import com.stockexchange.orderservice.model.OrderType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface OrderEvent extends BaseEvent {
    record OrderCreated(UUID orderId,
                        UUID userId,
                        String symbol,
                        BigDecimal price,
                        int quantity,
                        OrderType orderType,
                        Instant createdAt) implements OrderEvent {
        @Override
        public UUID id() {
            return orderId;
        }
    }

    record OrderUpdated(UUID orderId,
                        OrderStatus orderStatus,
                        OrderType orderType,
                        String symbol,
                        BigDecimal price,
                        int executedQuantity,
                        int totalQuantity,
                        Instant orderDate,
                        UUID userId) implements OrderEvent {
        @Override
        public UUID id() {
            return orderId;
        }
    }

    record OrderRejected(UUID orderId,
                         UUID userId,
                         BigDecimal price,
                         int quantity,
                         OrderType orderType) implements OrderEvent {
        @Override
        public UUID id() {
            return orderId;
        }
    }

}
