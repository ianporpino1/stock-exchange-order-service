package com.stockexchange.orderservice.repository;

import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.OrderStatus;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID> {
//    List<Order> findByUserUsername(String username);
    Flux<Order> findOrdersByUserId(UUID userId);

    Mono<Order> findOrderByOrderId(UUID id);

    @Modifying
    @Query("UPDATE trade_order SET status = :newStatus WHERE order_id = :id AND status = :expected")
    Mono<Void> updateStatus(
            @Param("id") UUID id,
            @Param("newStatus") OrderStatus newStatus,
            @Param("expected") OrderStatus expectedStatus
    );

    @Modifying
    @Query("UPDATE trade_order SET status = :status, executed_quantity = :executedQty WHERE order_id = :id")
    Mono<Void> updateOrderFromMatch(@Param("id") UUID id,
                              @Param("status") OrderStatus status,
                              @Param("executedQty") int executedQty);
}
