package com.stockexchange.orderservice.model.dto;

import java.util.List;

public record MatchResponse(List<OrderResponse> orders, List<TradeResponse> trades) {
}
