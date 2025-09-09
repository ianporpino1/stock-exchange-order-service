package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.dto.CreateOrderCommand;
import com.stockexchange.orderservice.model.dto.OrderRequest;
import com.stockexchange.orderservice.model.dto.OrderResponse;
import com.stockexchange.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class OrderCreationService {

    private final OrderProcessingService orderProcessingService;
    private final OrderRepository orderRepository;

    public OrderCreationService(OrderProcessingService orderProcessingService, OrderRepository orderRepository) {
        this.orderProcessingService = orderProcessingService;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, UUID userId) {
        UUID orderId = UUID.randomUUID();

        Order pendingOrder = new Order(orderId,userId,orderRequest.orderType(),orderRequest.quantity(),orderRequest.price(),orderRequest.symbol());
        orderRepository.save(pendingOrder);

        CreateOrderCommand command = new CreateOrderCommand(orderRequest, UUID.randomUUID(), orderId, userId);
//        logService.log(command);


        orderProcessingService.processOrder(command);
        return new OrderResponse(pendingOrder);
    }
}
