package com.stockexchange.orderservice.service;


import com.stockexchange.orderservice.controller.dto.OrderResponse;
import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

//    @Transactional
//    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
//    public List<OrderResponse> getUserAuthenticatedOrders(UserDetails userDetails) {
//        String username = userDetails.getUsername();
//        boolean isAdmin = userDetails.getAuthorities().stream()
//                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//        
//        List<Order> orders;
//        if (isAdmin) {
//            orders = orderRepository.findAll();
//        } else {
//            orders = orderRepository.findOrdersByUserId(username);
//        }
//        return orders.stream()
//                .map(OrderResponse::new)
//                .toList();
//    }

    public void saveAllOrders(Set<Order> ordersToSave) {
        orderRepository.saveAll(ordersToSave);
    }
}
