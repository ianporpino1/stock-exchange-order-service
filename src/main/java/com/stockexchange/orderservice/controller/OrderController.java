package com.stockexchange.orderservice.controller;

import com.stockexchange.orderservice.client.MatchingClient;
import com.stockexchange.orderservice.controller.dto.MatchRequest;
import com.stockexchange.orderservice.controller.dto.OrderRequest;
import com.stockexchange.orderservice.controller.dto.OrderResponse;
import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private MatchingClient matchingClient;
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, 
                                                     @AuthenticationPrincipal Jwt principal) {
        //1. sera q salvo no banco de dados antes de enviar pro match?
        
        System.out.println(principal.getClaims().get("roles"));
        OrderResponse orderResponse= matchingClient.match(new MatchRequest(orderRequest, UUID.fromString(principal.getSubject())));
        
        return ResponseEntity.ok(orderResponse);
    }

//    @GetMapping
//    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal UserDetails user) {
//        //comunicacao com user microservice --> n vai precisar fazer isso, ja que jwt ja tem essa info
//        //List<OrderResponse> orders = orderService.getUserAuthenticatedOrders(user);
//        //return ResponseEntity.ok(orders);
//    }
}
