package com.stockexchange.orderservice.model.dto;

import java.util.List;

public record TradeListResponse(List<TradeResponse> trades) {}
