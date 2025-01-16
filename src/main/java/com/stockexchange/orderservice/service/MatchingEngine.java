package com.stockexchange.orderservice.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class MatchingEngine {
    public OrderBookService orderBookService;
    
    @Autowired
    private UserService userService;

    public MatchingEngine(OrderBookService orderBookService) {
        this.orderBookService = orderBookService;
    }

    public OrderResponse matchOrder(OrderRequest orderRequest, UserDetails user) {
        User currentUser = userService.getUserByUsername(user.getUsername());
        Order order = new Order(orderRequest.symbol(),orderRequest.orderType(),orderRequest.quantity(),orderRequest.price());

        currentUser.addOrder(order);

        return orderBookService.processOrder(order);
    }

}
