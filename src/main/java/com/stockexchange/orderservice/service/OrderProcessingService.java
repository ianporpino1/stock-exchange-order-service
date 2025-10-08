package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderProcessingService {
    private final MatchService matchService;
    private final TradeService tradeService;
    private final PortfolioService portfolioService;
    private final TickerService tickerService;
    private final OrderHandler orderHandler;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderProcessingService.class);

    public OrderProcessingService(MatchService matchService, TradeService tradeService, PortfolioService portfolioService, TickerService tickerService, OrderHandler orderHandler) {
        this.matchService = matchService;
        this.tradeService = tradeService;
        this.portfolioService = portfolioService;
        this.tickerService = tickerService;
        this.orderHandler = orderHandler;
    }


    public Mono<Void> processOrder(CreateOrderCommand command) {
        return matchService.match(command)
                .flatMap(matchResponse -> {
                    Mono<Void> tickerTask = tickerService.handleTickers(matchResponse);
                    Mono<Void> ordersTask = orderHandler.handleOrders(matchResponse);
                    Mono<Void> tradesTask = tradeService.handleTrade(matchResponse);
                    Mono<Void> portfolioTask = portfolioService.handlePortfolioUpdates(matchResponse);
                    return Mono.when(ordersTask, tradesTask, portfolioTask, tickerTask);
                })
                .doOnError(error -> {
                    log.error("Falha ao processar o comando da ordem: {}. Causa do erro: {}", command, error.getMessage());
                })
                .onErrorResume(_ -> Mono.empty());
    }

}
