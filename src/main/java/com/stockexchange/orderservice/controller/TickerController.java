package com.stockexchange.orderservice.controller;

import com.stockexchange.orderservice.service.TickerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("tickers")
public class TickerController {

    private final TickerService tickerService;

    public TickerController(TickerService tickerService) {
        this.tickerService = tickerService;
    }


    @GetMapping("/{symbol}/last-price")
    public Mono<BigDecimal> getLastPriceForTicker(@PathVariable String symbol) {
        return tickerService.findLastPriceBySymbol(symbol);
    }
}
