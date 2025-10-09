package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.model.dto.OrderResponse;
import com.stockexchange.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderHandler {
    private final OrderRepository orderRepository;

    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void handleOrders(MatchResponse matchResponse) {
        for (OrderResponse orderDto : matchResponse.orders()) {
            orderRepository.updateOrderFromMatch(
                    orderDto.orderId(),
                    orderDto.orderStatus(),
                    orderDto.executedQuantity()
            );
        }
    }
}
