package com.stockexchange.orderservice.model.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record UpdatePortfolioCommand(UUID tradeId,
                                     UUID buyOrderId,
                                     UUID sellOrderId,
                                     UUID buyerUserId,
                                     UUID sellerUserId,
                                     String symbol,
                                     int quantity,
                                     BigDecimal price,
                                     Instant executedAt) {
    public UpdatePortfolioCommand(TradeExecutedEvent event) {
        this(event.tradeId(), event.buyOrderId(), event.sellOrderId(), event.buyerUserId(), event.sellerUserId(), event.symbol(), event.quantity(), event.price(), event.executedAt());
    }
}
