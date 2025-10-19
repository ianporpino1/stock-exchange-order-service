package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.OrderRequest;
import com.stockexchange.orderservice.model.dto.OrderResponse;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
public class OrderCreationService {

    private final OrderProcessingService orderProcessingService;
    private final OrderRepository orderRepository;

    public OrderCreationService(OrderProcessingService orderProcessingService, OrderRepository orderRepository) {
        this.orderProcessingService = orderProcessingService;
        this.orderRepository = orderRepository;
    }


    public Mono<OrderResponse> createOrder(OrderRequest orderRequest, UUID userId) {
        Order pendingOrder = new Order(
                userId,
                orderRequest.orderType(),
                orderRequest.quantity(),
                orderRequest.price(),
                orderRequest.symbol()
        );

        return orderRepository.save(pendingOrder)
                .flatMap(savedOrder -> {
                    CreateOrderCommand command = new CreateOrderCommand(
                            orderRequest,
                            UUID.randomUUID(),
                            savedOrder.getOrderId(),
                            userId
                    );

                    return orderProcessingService.processOrder(command)
                            .then(Mono.just(savedOrder));
                })
                .map(OrderResponse::new);
    }

}
