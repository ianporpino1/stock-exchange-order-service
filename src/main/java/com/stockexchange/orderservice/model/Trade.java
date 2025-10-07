package com.stockexchange.orderservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table(name = "trade")
public class Trade implements Persistable<UUID> {
    @Id
    @Column("trade_id")
    private UUID tradeId;
    @Column("buy_order_id")
    private UUID buyOrderId;

    @Column("sell_order_id")
    private UUID sellOrderId;

    @Column("buyer_user_id")
    private UUID buyerUserId;

    @Column("seller_user_id")
    private UUID sellerUserId;

    @Column("symbol")
    private String symbol;
    private int quantity;
    private BigDecimal price;
    private Instant executedAt;

    @Transient
    private final boolean isNew;

    public Trade(UUID tradeId, UUID buyOrderId, UUID sellOrderId, UUID buyerUserId, UUID sellerUserId, String symbol, int quantity, BigDecimal price, Instant executedAt) {
        this.tradeId = tradeId;
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.buyerUserId = buyerUserId;
        this.sellerUserId = sellerUserId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.executedAt = executedAt;
        this.isNew = true;
    }

    public UUID getBuyOrderId() {
        return buyOrderId;
    }

    public void setBuyOrderId(UUID buyOrderId) {
        this.buyOrderId = buyOrderId;
    }

    public UUID getSellOrderId() {
        return sellOrderId;
    }

    public void setSellOrderId(UUID sellOrderId) {
        this.sellOrderId = sellOrderId;
    }

    public UUID getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(UUID buyerUserId) {
        this.buyerUserId = buyerUserId;
    }

    public UUID getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(UUID sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }

    public UUID getTradeId() {
        return tradeId;
    }

    public void setTradeId(UUID tradeId) {
        this.tradeId = tradeId;
    }

    @Override
    public UUID getId() {
        return this.tradeId;
    }

    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }
}