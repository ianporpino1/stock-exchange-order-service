package com.stockexchange.orderservice.client;

import com.stockexchange.orderservice.controller.dto.MatchRequest;
import com.stockexchange.orderservice.controller.dto.OrderResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/match")
public interface MatchingClient {
    
    @PostExchange
    OrderResponse match(@RequestBody MatchRequest matchRequest);
}
