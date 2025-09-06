package com.stockexchange.orderservice.controller;

import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.OrderRequest;
import com.stockexchange.orderservice.model.dto.OrderResponse;
import com.stockexchange.orderservice.service.OrderCreationService;
import com.stockexchange.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, 
                                                     @AuthenticationPrincipal Jwt principal) {
        UUID userId = UUID.fromString(principal.getSubject());
        OrderResponse orderResponse = orderCreationService.createOrder(orderRequest, userId);

        URI location = URI.create(String.format("/orders/%s", orderResponse.orderId()));

        return ResponseEntity.ok().location(location).body(orderResponse);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id,
                                                  @AuthenticationPrincipal Jwt principal) {
        UUID userId = UUID.fromString(principal.getSubject());
        OrderResponse orderResponse = orderService.getOrder(id, userId);
        if (orderResponse == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/recovery")
    public ResponseEntity<List<CreateOrderCommand>> recoverOrders() {
        var orderCommands = orderService.recoverOrders();
        return ResponseEntity.ok(orderCommands);
    }
    
}
