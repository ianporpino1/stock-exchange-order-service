package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.client.MatchingClient;
import com.stockexchange.orderservice.controller.dto.MatchRequest;
import com.stockexchange.orderservice.controller.dto.MatchResponse;
import com.stockexchange.orderservice.controller.dto.OrderRequest;
import com.stockexchange.orderservice.controller.dto.OrderResponse;
import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MatchingClient matchingClient;

    
    public OrderResponse createOrder(OrderRequest orderRequest, UUID userId) {
        //validar ordem
        //salvar no banco com status accepted
        //qual sera a abordagem que vai mostrar uma diferenca de desempenho maior,
        //retornar pro cliente sem esperar o matching ou esperar o matching e retornar a ordem
        MatchResponse matchResponse = matchingClient.match(
                new MatchRequest(orderRequest, userId));
        
        List<Order> orders = new ArrayList<>();
        
        OrderResponse responseForClient = null;
        List<OrderResponse> sortedOrders = matchResponse.orders().stream()
                .filter(orderResponse -> orderResponse.userId().equals(userId))
                .sorted(Comparator.comparing(OrderResponse::orderDate).reversed())
                .toList();
        for(OrderResponse orderResponse: sortedOrders){
            Order order = convertOrderResponseToOrder(orderResponse);
            orders.add(order);
            
            if (responseForClient == null) {
                responseForClient = orderResponse;
            }
        }
        orderRepository.saveAll(orders);
        
        return responseForClient;
    }

    private Order convertOrderResponseToOrder(OrderResponse orderResponse) {
        return new Order(
                orderResponse.orderId(),
                orderResponse.symbol(),
                orderResponse.orderType(),
                orderResponse.orderStatus(),
                orderResponse.price(),
                orderResponse.executedQuantity(),
                orderResponse.totalQuantity(), 
                orderResponse.orderDate(),
                orderResponse.userId()
               
        );
    }

    public OrderResponse getOrder(UUID orderId, UUID userId) {
        Order order = orderRepository.findOrderByOrderId(orderId);
        if(!order.getUserId().equals(userId)){
            return null;
        }
        return new OrderResponse(order);
    }
}
