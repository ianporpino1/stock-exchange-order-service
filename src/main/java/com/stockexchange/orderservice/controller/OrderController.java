package com.stockexchange.orderservice.controller;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.OrderRequest;
import com.stockexchange.orderservice.model.dto.OrderResponse;
import com.stockexchange.orderservice.service.OrderCreationService;
import com.stockexchange.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderCreationService orderCreationService;
    private final OrderService orderService;

    public OrderController(OrderCreationService orderCreationService, OrderService orderService) {
        this.orderCreationService = orderCreationService;
        this.orderService = orderService;
    }

    @PostMapping
    public Mono<ResponseEntity<OrderResponse>> createOrder(@RequestBody Mono<OrderRequest> orderRequestMono,
                                                     @AuthenticationPrincipal Jwt principal) {
        UUID userId = UUID.fromString(principal.getSubject());
        return orderRequestMono
                .flatMap(orderRequest -> orderCreationService.createOrder(orderRequest, userId))
                .map(orderResponse -> {
                    URI location = URI.create(String.format("/orders/%s", orderResponse.orderId()));
                    return ResponseEntity.created(location).body(orderResponse);
                });
    }
    
    @GetMapping("/{id}")
    public Mono<OrderResponse> getOrder(@PathVariable UUID id,
                                        @AuthenticationPrincipal Jwt principal) {
        UUID userId = UUID.fromString(principal.getSubject());
        return orderService.getOrder(id, userId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found or access denied")));
    }

    @GetMapping(path = "/recovery", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CreateOrderCommand> recoverOrders() {
        return orderService.recoverOrders();
    }
    
}
