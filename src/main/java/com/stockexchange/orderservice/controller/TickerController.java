package com.stockexchange.orderservice.controller;

import com.stockexchange.orderservice.service.TickerService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;


@Controller
public class TickerController {

    private final TickerService tickerService;

    public TickerController(TickerService tickerService) {
        this.tickerService = tickerService;
    }


    @QueryMapping
    public Mono<BigDecimal> price(@Argument String symbol) {
        return tickerService.findLastPriceBySymbol(symbol);
    }
}
