package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.event.OrderCreatedEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderProcessingService {

    private final StreamBridge streamBridge;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderProcessingService.class);
    private static final String DESTINATION_HEADER ="spring.cloud.stream.sendto.destination";

    public OrderProcessingService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }


    public Mono<Void> processOrder(CreateOrderCommand command) {
        var message = MessageBuilder.withPayload(new OrderCreatedEvent(
                        command.orderId(),
                        command.userId(),
                        command.symbol(),
                        command.price(),
                        command.quantity(),
                        command.orderType(),
                        command.createdAt()))
                .setHeader(DESTINATION_HEADER, "orders.created")
                .build();
        streamBridge.send("orders.created", message);
        return Mono.empty();
    }
}
