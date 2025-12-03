package com.stockexchange.orderservice.handler;

import com.stockexchange.orderservice.model.OrderStatus;
import com.stockexchange.orderservice.model.event.OrderEvent;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.function.Function;

@Configuration
public class OrderHandler {
    private final OrderRepository orderRepository;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderHandler.class);

    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Bean
    public Function<Flux<Message<OrderEvent.OrderUpdated>>, Mono<Void>> handleOrderUpdate() {
        return flux -> flux
                .filter(msg -> "order.updated".equals(msg.getHeaders().get("eventType")))
                .concatMap(message -> {
            OrderEvent.OrderUpdated event = message.getPayload();
            return orderRepository.updateOrderFromMatch(
                    event.orderId(), event.orderStatus(), event.executedQuantity()
            ).onErrorResume(e -> Mono.empty());
        }).then();
    }

    @Bean
    public Function<Flux<Message<OrderEvent.OrderRejected>>, Mono<Void>> handleOrderRejected() {
        return flux -> flux
                .filter(msg -> "order.rejected".equals(msg.getHeaders().get("eventType")))
                .concatMap(message -> {
            OrderEvent.OrderRejected event = message.getPayload();
            return orderRepository.updateStatus(
                    event.orderId(), OrderStatus.REJECTED,
                    OrderStatus.PENDING
            ).onErrorResume(e -> Mono.empty());
        }).then();
    }
}