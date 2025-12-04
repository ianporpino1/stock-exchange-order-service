package com.stockexchange.orderservice.model.event;

import com.stockexchange.orderservice.model.OrderType;
import com.stockexchange.orderservice.model.dto.CreateOrderCommand;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ReserveBalanceCommand(UUID orderId,
                                    UUID userId,
                                    String symbol,
                                    BigDecimal price,
                                    int quantity,
                                    OrderType orderType,
                                    Instant createdAt) {
    public ReserveBalanceCommand(CreateOrderCommand command) {
        this(command.orderId(), command.userId(), command.symbol(), command.price(), command.quantity(), command.orderType(), command.createdAt());
    }
}
