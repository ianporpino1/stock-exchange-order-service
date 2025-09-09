package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {
    private final OrderRepository orderRepository;
    private final MatchService matchService;
    private final TradeService tradeService;
    private final PortfolioService portfolioService;
    private final OrderHandler orderHandler;

    public OrderProcessingService(OrderRepository orderRepository, MatchService matchService, TradeService tradeService, PortfolioService portfolioService, OrderHandler orderHandler) {
        this.orderRepository = orderRepository;
        this.matchService = matchService;
        this.tradeService = tradeService;
        this.portfolioService = portfolioService;
        this.orderHandler = orderHandler;
    }

    @Async //foi pior
    @Transactional
    public void processOrder(CreateOrderCommand command) {
        MatchResponse matchResponse = matchService.match(command);
        orderHandler.handleOrders(matchResponse);
        tradeService.handleTrade(matchResponse);
        portfolioService.handlePortfolioUpdates(matchResponse);
    }

}
