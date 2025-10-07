package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.*;
import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse getOrder(UUID orderId, UUID userId) {
        Order order = orderRepository.findOrderByOrderId(orderId);
        if (!order.getUserId().equals(userId)) {
            return null;
        }
        return new OrderResponse(order);
    }

    public List<CreateOrderCommand> recoverOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> new CreateOrderCommand(
                        new OrderRequest(order.getSymbol(),
                                order.getPrice(),
                                order.getTotalQuantity() - order.getExecutedQuantity(),
                                order.getType()),
                        UUID.randomUUID(),
                        order.getOrderId(),
                        order.getUserId()
                ))
                .toList();
    }

    public void saveAll(List<Order> orders) {
        orderRepository.saveAll(orders);
    }
}
