package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.*;
import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Mono<OrderResponse> getOrder(UUID orderId, UUID userId) {
        return orderRepository.findOrderByOrderId(orderId)
                .filter(order -> order.getUserId().equals(userId))
                .map(OrderResponse::new);
    }

    public Flux<CreateOrderCommand> recoverOrders() {
        return orderRepository.findAll()
                .map(order -> new CreateOrderCommand(
                        new OrderRequest(order.getSymbol(),
                                order.getPrice(),
                                order.getTotalQuantity() - order.getExecutedQuantity(),
                                order.getType()),
                        UUID.randomUUID(),
                        order.getOrderId(),
                        order.getUserId()
                ));
    }

    public void saveAll(List<Order> orders) {
        orderRepository.saveAll(orders);
    }
}
