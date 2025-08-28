package com.stockexchange.orderservice.model.dto;

import java.util.UUID;

public record MatchRequest(OrderRequest orderRequest, UUID userId) {
}
