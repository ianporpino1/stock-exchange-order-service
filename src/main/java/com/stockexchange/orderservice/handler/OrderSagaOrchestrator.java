package com.stockexchange.orderservice.handler;

import com.stockexchange.orderservice.model.OrderStatus;
import com.stockexchange.orderservice.model.event.BalanceStatus;
import com.stockexchange.orderservice.model.event.MatchOrderCommand;
import com.stockexchange.orderservice.model.event.ReserveBalanceResponse;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class OrderSagaOrchestrator {

    private final OrderRepository orderRepository;

    public OrderSagaOrchestrator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Bean
    public Function<Flux<Message<ReserveBalanceResponse>>, Flux<Message<MatchOrderCommand>>> onPortfolioResponse() {
        return flux -> flux.flatMap(message -> {
            ReserveBalanceResponse response = message.getPayload();
            if (response.balanceStatus().equals(BalanceStatus.SUCCESS)) {
                MatchOrderCommand cmd = new MatchOrderCommand(response);
                return Mono.just(
                        MessageBuilder.withPayload(cmd)
                                .build()
                );

            } else {
                return orderRepository.updateStatus(response.orderId(), OrderStatus.REJECTED, OrderStatus.PENDING)
                        .then(Mono.empty());
            }
        });
    }


}
