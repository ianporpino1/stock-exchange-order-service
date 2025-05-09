package com.stockexchange.orderservice.repository;

import com.stockexchange.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    List<Order> findByUserUsername(String username);
    List<Order> findOrdersByUserId(UUID userId);

    Order findOrderById(UUID id);
}
