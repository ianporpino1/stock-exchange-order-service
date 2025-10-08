package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class OrderProcessingService {
    private final MatchService matchService;
    private final TradeService tradeService;
    private final PortfolioService portfolioService;
    private final OrderHandler orderHandler;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderProcessingService.class);

    public OrderProcessingService(MatchService matchService, TradeService tradeService, PortfolioService portfolioService, OrderHandler orderHandler) {
        this.matchService = matchService;
        this.tradeService = tradeService;
        this.portfolioService = portfolioService;
        this.orderHandler = orderHandler;
    }


    public Mono<Void> processOrder(CreateOrderCommand command) {
        return matchService.match(command)
                .flatMap(matchResponse -> {

                    Mono<Void> ordersTask = orderHandler.handleOrders(matchResponse)
                            .subscribeOn(Schedulers.boundedElastic());

                    Mono<Void> tradesTask = tradeService.handleTrade(matchResponse)
                            .subscribeOn(Schedulers.boundedElastic());

                    Mono<Void> portfolioTask = portfolioService.handlePortfolioUpdates(matchResponse)
                            .subscribeOn(Schedulers.boundedElastic());

                    return Mono.when(ordersTask, tradesTask, portfolioTask);
                })
                .doOnError(error -> {
                    log.error("Falha ao processar o comando da ordem: {}. Causa do erro: {}", command, error.getMessage());
                })
                .onErrorResume(_ -> Mono.empty());
    }

}
