package com.stockexchange.orderservice.repository;

import com.stockexchange.orderservice.model.Order;
import com.stockexchange.orderservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
//    List<Order> findByUserUsername(String username);
    List<Order> findOrdersByUserId(UUID userId);

    Order findOrderByOrderId(UUID id);

    @Modifying
    @Query(value = """
    INSERT INTO trade_order (
        order_id, user_id, status, executed_quantity,
        total_quantity, price, symbol, type, created_at
    )
    VALUES (
        :orderId, :userId, :status, :executedQuantity,
        :totalQuantity, :price, :symbol, :type, :createdAt
    )
    ON CONFLICT (order_id) DO UPDATE
    SET status = EXCLUDED.status,
        executed_quantity = EXCLUDED.executed_quantity,
        total_quantity = EXCLUDED.total_quantity,
        price = EXCLUDED.price,
        symbol = EXCLUDED.symbol,
        type = EXCLUDED.type,
        created_at = EXCLUDED.created_at
    """, nativeQuery = true)
    void upsert(@Param("orderId") UUID orderId,
                @Param("userId") UUID userId,
                @Param("status") String status,
                @Param("executedQuantity") int executedQuantity,
                @Param("totalQuantity") int totalQuantity,
                @Param("price") BigDecimal price,
                @Param("symbol") String symbol,
                @Param("type") String type,
                @Param("createdAt") Instant createdAt);


    List<Order> findByStatusIn(Collection<OrderStatus> statuses);

    @Modifying
    @Query("UPDATE Order o SET o.status = :status, o.executedQuantity = :executedQty WHERE o.orderId = :id")
    void updateOrderFromMatch(@Param("id") UUID id,
                              @Param("status") OrderStatus status,
                              @Param("executedQty") int executedQty);
}
