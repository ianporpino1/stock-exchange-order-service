package com.stockexchange.orderservice.client;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.MatchRequest;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.model.dto.OrderResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@HttpExchange(accept = "application/json", contentType = "application/json")
public interface MatchingClient {

    @PostExchange(value = "/match")
    @CircuitBreaker(name = "matching-service", fallbackMethod = "matchFallback")
    @Bulkhead(name = "matching-service")
    @Retry(name = "matching-service")
    @RateLimiter(name = "matching-service")
    MatchResponse match(@RequestBody CreateOrderCommand request);

    default MatchResponse matchFallback(CreateOrderCommand command, Throwable t) {
        System.err.printf("FALLBACK ATIVADO para a ordem %s. Causa: %s%n",
                command.orderId(), t.getMessage());

        return new MatchResponse(Collections.emptyList(), Collections.emptyList());
    }

    @GetExchange(value = "/orders/{orderId}")
    OrderResponse getOrderById(@PathVariable UUID orderId);
}