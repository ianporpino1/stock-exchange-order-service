package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderHandler {
    private final OrderRepository orderRepository;

    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public Mono<Void> handleOrders(MatchResponse matchResponse) {
        return Flux.fromIterable(matchResponse.orders())
                .flatMap(orderDto -> orderRepository.updateOrderFromMatch(
                        orderDto.orderId(),
                        orderDto.orderStatus(),
                        orderDto.executedQuantity()
                ))
                .then();
    }
}
