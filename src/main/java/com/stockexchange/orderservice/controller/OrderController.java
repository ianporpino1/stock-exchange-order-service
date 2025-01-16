package com.stockexchange.orderservice.controller;


import com.stockexchange.orderservice.controller.dto.OrderRequest;
import com.stockexchange.orderservice.controller.dto.OrderResponse;
import com.stockexchange.orderservice.service.MatchingEngine;
import com.stockexchange.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, @AuthenticationPrincipal UserDetails user) {

        OrderResponse response  = matchingEngine.matchOrder(orderRequest, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal UserDetails user) {
        //comunicacao com user microservice
        //List<OrderResponse> orders = orderService.getUserAuthenticatedOrders(user);
        //return ResponseEntity.ok(orders);
    }
}
