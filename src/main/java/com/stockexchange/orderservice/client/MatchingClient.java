package com.stockexchange.orderservice.client;

import com.stockexchange.orderservice.controller.dto.MatchRequest;
import com.stockexchange.orderservice.controller.dto.MatchResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient("matching-service")
public interface MatchingClient {
    
    @RequestMapping(method = RequestMethod.POST, value ="/match",consumes ="application/json")
    @CircuitBreaker(name = "matching-service", fallbackMethod = "matchFallback")
    @Bulkhead(name = "matching-service")
    @Retry(name = "matching-service")
    @RateLimiter(name = "matching-service")
    MatchResponse match(@RequestBody MatchRequest request);
    
    default MatchResponse matchFallback(MatchRequest request, Throwable ex) {
        System.out.println("Servico de Match Indisponivel, Tente novamente mais tarde");
        return null;
    }
}
