package com.stockexchange.orderservice.controller.dto;

import java.util.UUID;

public record MatchRequest(OrderRequest orderRequest, UUID userId) {
}
