package com.stockexchange.orderservice.controller.dto;

import com.stockexchange.orderservice.model.OrderType;

public record OrderRequest(String symbol, double price, int quantity, OrderType orderType) {
}
