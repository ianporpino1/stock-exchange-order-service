package com.stockexchange.orderservice.model.event;


import com.stockexchange.orderservice.model.OrderType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface BalanceEvent extends BaseEvent{
    record BalanceReserved(UUID orderId,
                           UUID userId,
                           String symbol,
                           BigDecimal price,
                           int quantity,
                           OrderType orderType,
                           Instant createdAt)implements BalanceEvent{
        @Override
        public UUID id() {
            return orderId;
        }
    }

    record BalanceReservationFailed(UUID orderId,
                               UUID userId,
                               String symbol,
                               BigDecimal price,
                               int quantity,
                               OrderType orderType,
                               Instant createdAt)implements BalanceEvent{
        @Override
        public UUID id() {
            return orderId;
        }
    }
}
