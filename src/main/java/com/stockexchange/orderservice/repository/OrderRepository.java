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

//    @Modifying
//    @Query(value = """
//    INSERT INTO trade_order (
//        order_id, user_id, status, executed_quantity,
//        total_quantity, price, symbol, type, created_at
//    )
//    VALUES (
//        :orderId, :userId, :status, :executedQuantity,
//        :totalQuantity, :price, :symbol, :type, :createdAt
//    )
//    ON CONFLICT (order_id) DO UPDATE
//    SET status = EXCLUDED.status,
//        executed_quantity = EXCLUDED.executed_quantity,
//        total_quantity = EXCLUDED.total_quantity,
//        price = EXCLUDED.price,
//        symbol = EXCLUDED.symbol,
//        type = EXCLUDED.type,
//        created_at = EXCLUDED.created_at
//    """, nativeQuery = true)
//    void upsert(@Param("orderId") UUID orderId,
//                @Param("userId") UUID userId,
//                @Param("status") String status,
//                @Param("executedQuantity") int executedQuantity,
//                @Param("totalQuantity") int totalQuantity,
//                @Param("price") BigDecimal price,
//                @Param("symbol") String symbol,
//                @Param("type") String type,
//                @Param("createdAt") Instant createdAt);
//
//
//    List<Order> findByStatusIn(Collection<OrderStatus> statuses);

    @Modifying
    @Query("UPDATE trade_order SET status = :status, executed_quantity = :executedQty WHERE order_id = :id")
    Mono<Void> updateOrderFromMatch(@Param("id") UUID id,
                              @Param("status") OrderStatus status,
                              @Param("executedQty") int executedQty);
}
