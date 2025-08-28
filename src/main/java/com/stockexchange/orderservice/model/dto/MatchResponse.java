package com.stockexchange.orderservice.model.dto;

import com.stockexchange.orderservice.model.Trade;

import java.util.List;

public record MatchResponse(List<OrderResponse> orders, List<TradeResponse> trades) {
}
