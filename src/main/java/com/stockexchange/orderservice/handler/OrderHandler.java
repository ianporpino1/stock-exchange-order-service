package com.stockexchange.orderservice.handler;

import com.stockexchange.orderservice.model.event.OrderUpdatedEvent;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Objects;
import java.util.function.Function;

@Configuration
public class OrderHandler {
    private final OrderRepository orderRepository;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderHandler.class);

    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Bean
    public Function<Flux<Message<OrderUpdatedEvent>>, Mono<Void>> handleOrder() {
        return flux -> flux
                .filter(msg -> "order.updated".equals(msg.getHeaders().get("eventType")))
                .map(Message::getPayload)
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
                .then();
    }
}
