package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.model.dto.OrderResponse;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderProcessingService {
    private final OrderRepository orderRepository;
    private final MatchService matchService;
    private final TradeService tradeService;
    private final PortfolioService portfolioService;

    public OrderProcessingService(OrderRepository orderRepository, MatchService matchService, TradeService tradeService, PortfolioService portfolioService) {
        this.orderRepository = orderRepository;
        this.matchService = matchService;
        this.tradeService = tradeService;
        this.portfolioService = portfolioService;
    }


    public void sendOrderToMatching(CreateOrderCommand command) {
        MatchResponse matchResponse = matchService.match(command);
        this.handleMatch(matchResponse);
    }

    public void handleOrders(MatchResponse matchResponse) {
        Map<UUID, OrderResponse> latestOrderStates = matchResponse.orders().stream()
                .collect(Collectors.toMap(
                        OrderResponse::orderId, dto -> dto, (_, replacement) -> replacement
                ));

        if (!latestOrderStates.isEmpty()) {
            List<UUID> orderIds = new ArrayList<>(latestOrderStates.keySet());

            List<Order> existingOrders = orderRepository.findAllById(orderIds);

            existingOrders.forEach(order -> {
                OrderResponse dto = latestOrderStates.get(order.getOrderId());
                order.setStatus(dto.orderStatus());
                order.setExecutedQuantity(dto.executedQuantity());
            });

            orderRepository.saveAll(existingOrders);
        }
    }

    public void handleMatch(MatchResponse matchResponse) {
        handleOrders(matchResponse);
        tradeService.handleTrade(matchResponse);
//        portfolioService.handlePortfolioUpdates(matchResponse);
    }
    public Order convertOrderResponseToOrder(OrderResponse orderResponse) {
        return new Order(
                orderResponse.orderId(),
                orderResponse.symbol(),
                orderResponse.orderType(),
                orderResponse.orderStatus(),
                orderResponse.price(),
                orderResponse.executedQuantity(),
                orderResponse.totalQuantity(),
                orderResponse.orderDate(),
                orderResponse.userId()

        );
    }
}
