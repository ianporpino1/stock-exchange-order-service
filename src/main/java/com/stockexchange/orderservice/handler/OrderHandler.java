package com.stockexchange.orderservice.handler;

import com.stockexchange.orderservice.model.event.OrderUpdatedEvent;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
public class OrderHandler {
    private final OrderRepository orderRepository;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderHandler.class);

    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Bean
    public Consumer<Flux<OrderUpdatedEvent>> handleOrder() {
        return flux -> flux
                .filter(Objects::nonNull)
                .concatMap(event ->
                        orderRepository.updateOrderFromMatch(
                                        event.orderId(),
                                        event.orderStatus(),
                                        event.executedQuantity()
                                )
                                .onErrorResume(e -> {
                                    log.error("Erro ao processar evento: " + event.orderId(), e);
                                    return Mono.empty();
                                })
                )
                .subscribe();
    }
}
