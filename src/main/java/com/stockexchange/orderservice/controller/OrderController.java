package com.stockexchange.orderservice.controller;

import com.stockexchange.orderservice.controller.dto.OrderRequest;
import com.stockexchange.orderservice.controller.dto.OrderResponse;
import com.stockexchange.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, 
                                                     @AuthenticationPrincipal Jwt principal) {
        //1. sera q salvo no banco de dados antes de enviar pro match?
        UUID userId = UUID.fromString(principal.getSubject());
        OrderResponse orderResponse = orderService.createOrder(orderRequest, userId);
        return ResponseEntity.ok(orderResponse);
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
    
}
