package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.event.OrderEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Mono;

@Service
public class OrderProcessingService {

    private final StreamBridge streamBridge;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderProcessingService.class);

    public OrderProcessingService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public Mono<Void> processOrder(CreateOrderCommand command) {
        var message = MessageBuilder.withPayload(new OrderEvent.OrderCreated(
                        command.orderId(),
                        command.userId(),
                        command.symbol(),
                        command.price(),
                        command.quantity(),
                        command.orderType(),
                        command.createdAt()))
                .setHeader("eventType", "order.created")
                .build();
        streamBridge.send("orderEvents-out-0", message);
        return Mono.empty();
    }
}
