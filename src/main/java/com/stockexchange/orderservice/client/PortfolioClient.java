package com.stockexchange.orderservice.client;

import com.stockexchange.orderservice.model.dto.TradeListResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange(accept = "application/json", contentType = "application/json")
public interface PortfolioClient {

    @PostExchange("/trades")
    Mono<Void> processExecutedTrades(@RequestBody TradeListResponse trades);
}
