package com.stockexchange.orderservice.handler;

import com.stockexchange.orderservice.model.OrderStatus;
import com.stockexchange.orderservice.model.OrderType;
import com.stockexchange.orderservice.model.event.OrderEvent;
import com.stockexchange.orderservice.model.event.RefundCommand;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
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
    public Function<Flux<Message<OrderEvent.OrderRejected>>, Flux<Message<RefundCommand>>> handleOrderRejected() {
        return flux -> flux
                .filter(msg -> "order.rejected".equals(msg.getHeaders().get("eventType")))
                .concatMap(message -> {
                    OrderEvent.OrderRejected event = message.getPayload();

                    return orderRepository.updateStatus(event.orderId(), OrderStatus.REJECTED, OrderStatus.PENDING)
                            .then(Mono.defer(() -> {

                                BigDecimal amountToRefund = BigDecimal.ZERO;

                                if (event.orderType() == OrderType.BUY) {
                                    amountToRefund = event.price().multiply(BigDecimal.valueOf(event.quantity()));
                                }

                                RefundCommand cmd = new RefundCommand(
                                        event.orderId(),
                                        event.userId(),
                                        amountToRefund,
                                        event.orderType()
                                );

                                return Mono.just(
                                        MessageBuilder.withPayload(cmd)
                                                .copyHeaders(message.getHeaders())
                                                .setHeader("eventType", "portfolio.refund")
                                                .build()
                                );
                            }))
                            .onErrorResume(e -> {
                                log.error("Erro ao processar rejeição da ordem: " + event.orderId(), e);
                                return Mono.empty();
                            });
                });
    }
}