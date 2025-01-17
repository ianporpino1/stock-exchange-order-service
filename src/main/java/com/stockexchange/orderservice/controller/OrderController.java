package com.stockexchange.orderservice.controller;

import com.stockexchange.orderservice.controller.dto.OrderRequest;
import com.stockexchange.orderservice.controller.dto.OrderResponse;
import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    
    
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, 
                                                     @AuthenticationPrincipal Jwt principal) {
        //vai receber jwt com userId e role
        //OrderResponse response  = matchingEngine.matchOrder(orderRequest, user);
        System.out.println(principal.getSubject());
        return ResponseEntity.ok(new OrderResponse(new Order(orderRequest.symbol(),orderRequest.orderType(),orderRequest.quantity(),orderRequest.price())));
    }

//    @GetMapping
//    public ResponseEntity<List<OrderResponse>> getUserOrders(@AuthenticationPrincipal UserDetails user) {
//        //comunicacao com user microservice --> n vai precisar fazer isso, ja que jwt ja tem essa info
//        //List<OrderResponse> orders = orderService.getUserAuthenticatedOrders(user);
//        //return ResponseEntity.ok(orders);
//    }
}
