package com.stockexchange.orderservice.handler;

import com.stockexchange.orderservice.model.event.BalanceEvent;
import com.stockexchange.orderservice.service.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class BalanceHandler {
    private final OrderService orderService;

    public BalanceHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Bean
    public Function<Flux<Message<BalanceEvent.BalanceReservationFailed>>, Mono<Void>> handleBalance(){
        return flux -> flux
                .filter(msg -> "balance.failed".equals(msg.getHeaders().get("eventType")))
                .map(Message::getPayload)
                .flatMap(orderService::rejectOrder)
                .then();
    }
}
