package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {
    private final MatchService matchService;
    private final TradeService tradeService;
    private final PortfolioService portfolioService;
    private final OrderHandler orderHandler;

    public OrderProcessingService(MatchService matchService, TradeService tradeService, PortfolioService portfolioService, OrderHandler orderHandler) {
        this.matchService = matchService;
        this.tradeService = tradeService;
        this.portfolioService = portfolioService;
        this.orderHandler = orderHandler;
    }

    @Async
    public void processOrder(CreateOrderCommand command) {
        MatchResponse matchResponse = matchService.match(command);
        orderHandler.handleOrders(matchResponse);
        tradeService.handleTrade(matchResponse);
        portfolioService.handlePortfolioUpdates(matchResponse);
    }

}
