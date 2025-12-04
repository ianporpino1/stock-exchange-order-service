package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.event.ReserveBalanceCommand;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderProcessingService {

    private final StreamBridge streamBridge;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(OrderProcessingService.class);

    public OrderProcessingService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public Mono<Void> processOrder(CreateOrderCommand command) {
        var message = MessageBuilder.withPayload(new ReserveBalanceCommand(command))
                .setHeader("eventType", "portfolio.balance").build();
        streamBridge.send("portfolioCommands-out-0", message);
        return Mono.empty();
    }
}
