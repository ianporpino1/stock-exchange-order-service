package com.stockexchange.orderservice.model.dto;

import com.stockexchange.orderservice.model.OrderType;
import java.math.BigDecimal;

public record OrderRequest(String symbol, BigDecimal price, int quantity, OrderType orderType) {
}
