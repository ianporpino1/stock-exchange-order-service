package com.stockexchange.orderservice.controller.dto;

import java.util.List;

public record MatchResponse(List<OrderResponse> orders) {
}
