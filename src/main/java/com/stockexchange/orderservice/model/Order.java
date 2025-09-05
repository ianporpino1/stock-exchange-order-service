package com.stockexchange.orderservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "trade_order")
public class Order {
    @Id
    private UUID orderId;
    private String symbol;
    @Enumerated(EnumType.STRING)
    private OrderType type;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private BigDecimal price;
    private int executedQuantity;
    private int totalQuantity;
    private Instant createdAt;
    private UUID userId;

    public Order(UUID id, String symbol, OrderType type, OrderStatus status, BigDecimal price, int executedQuantity, int totalQuantity, Instant createdAt, UUID userId) {
        this.orderId = id;
        this.symbol = symbol;
        this.type = type;
        this.price = price;
        this.executedQuantity = executedQuantity;
        this.totalQuantity = totalQuantity;
        this.createdAt = createdAt;
        this.status = status;
        this.userId = userId;
    }

    public Order() {}

    public Order(UUID orderId, UUID userId, OrderType type, int quantity, BigDecimal price, String symbol) {
        this.orderId = orderId;
        this.userId = userId;
        this.type = type;
        this.executedQuantity = 0;
        this.totalQuantity = quantity;
        this.createdAt = Instant.now();
        this.status = OrderStatus.PENDING;
        this.price = price;
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public OrderType getType() {
        return type;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getExecutedQuantity() {
        return executedQuantity;
    }

    public void setExecutedQuantity(int executedQuantity) {
        this.executedQuantity = executedQuantity;
    }

    public UUID getOrderId() {
        return orderId;
    }
    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
