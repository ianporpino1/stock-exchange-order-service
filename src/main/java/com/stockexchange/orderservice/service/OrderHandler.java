package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.model.dto.OrderResponse;
import com.stockexchange.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHandler {
    private final OrderRepository orderRepository;

    public OrderHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public void handleOrders(MatchResponse matchResponse) {
        //39k orders: sem portfolio
//        Map<UUID, OrderResponse> latestOrderStates = matchResponse.orders().stream()
//                .collect(Collectors.toMap(
//                        OrderResponse::orderId, dto -> dto, (_, replacement) -> replacement
//                ));
//
//        if (!latestOrderStates.isEmpty()) {
//            List<UUID> orderIds = new ArrayList<>(latestOrderStates.keySet());
//
//            List<Order> existingOrders = orderRepository.findAllById(orderIds);
//
//            existingOrders.forEach(order -> {
//                OrderResponse dto = latestOrderStates.get(order.getOrderId());
//                order.setStatus(dto.orderStatus());
//                order.setExecutedQuantity(dto.executedQuantity());
//            });
//
//            orderRepository.saveAll(existingOrders);
//        }

        //50k - 42k orders: sem portfolio
//        for (Order order : ordersToSave) {
//            orderRepository.upsert(
//                    order.getOrderId(),
//                    order.getUserId(),
//                    order.getStatus().name(),
//                    order.getExecutedQuantity(),
//                    order.getTotalQuantity(),
//                    order.getPrice(),
//                    order.getSymbol(),
//                    order.getType().name(),
//                    order.getCreatedAt()
//            );
//
//        }

        for (OrderResponse orderDto : matchResponse.orders()) {
            orderRepository.updateOrderFromMatch(
                    orderDto.orderId(),
                    orderDto.orderStatus(),
                    orderDto.executedQuantity()
            );
        }
    }
    public Order convertOrderResponseToOrder(OrderResponse orderResponse) {
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
}
