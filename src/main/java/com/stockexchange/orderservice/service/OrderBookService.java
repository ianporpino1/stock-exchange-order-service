package com.stockexchange.orderservice.service;


import com.stockexchange.orderservice.controller.dto.OrderResponse;
import com.stockexchange.orderservice.model.Execution;
import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.OrderBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderBookService {
    private final ConcurrentHashMap<String, OrderBook> orderBooks;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ExecutionService executionService;

    public OrderBookService() {
        this.orderBooks = new ConcurrentHashMap<>();
    }

    private OrderBook getOrderBook(String symbol) {
        return orderBooks.computeIfAbsent(symbol, k -> new OrderBook());
    }

    public OrderResponse processOrder(Order order) {
        OrderBook orderBook = getOrderBook(order.getSymbol());
        List<Execution> executions = orderBook.processOrder(order);
        
        if(executions.isEmpty()) {
            orderService.saveOrder(order);
            return new OrderResponse(
                    order.getId(),
                    order.getStatus(),
                    order.getExecutedQuantity(),
                    order.getTotalQuantity(),
                    order.getCreatedAt(),
                    order.getUser().getId()
            );
        }
        
        executionService.saveAllExecutions(executions);
        Set<Order> ordersToSave = new HashSet<>();
        ordersToSave.add(order);
        executions.forEach(execution -> {
            if (execution.getBuyOrder() != null) {
                ordersToSave.add(execution.getBuyOrder());
            }
            if (execution.getSellOrder() != null) {
                ordersToSave.add(execution.getSellOrder());
            }
        });
        orderService.saveAllOrders(ordersToSave);

        int executedQuantity = executions.stream()
                .filter(ex -> ex.getBuyOrder().equals(order) || ex.getSellOrder().equals(order))
                .mapToInt(Execution::getQuantity)
                .sum();
        
        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                executedQuantity,
                order.getTotalQuantity(),
                order.getCreatedAt(),
                order.getUser().getId()
        );
    }

}
