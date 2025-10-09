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
    private final TickerService tickerService;
    private final OrderHandler orderHandler;

    public OrderProcessingService(MatchService matchService, TradeService tradeService, PortfolioService portfolioService, TickerService tickerService, OrderHandler orderHandler) {
        this.matchService = matchService;
        this.tradeService = tradeService;
        this.portfolioService = portfolioService;
        this.tickerService = tickerService;
        this.orderHandler = orderHandler;
    }

//    @Async
    public void processOrder(CreateOrderCommand command) {
        MatchResponse matchResponse = matchService.match(command);
        tickerService.handleTickers(matchResponse);
        orderHandler.handleOrders(matchResponse);
        tradeService.handleTrade(matchResponse);
        portfolioService.handlePortfolioUpdates(matchResponse);
    }

}
