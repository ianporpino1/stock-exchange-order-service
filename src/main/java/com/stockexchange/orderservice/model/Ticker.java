package com.stockexchange.orderservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

@Table(name = "ticker")
@Entity
public class Ticker {

    @Id
    private Long id;

    private String symbol;

    private BigDecimal lastPrice;

    private Instant lastTradeTimestamp;

    public Ticker(String symbol, BigDecimal lastPrice, Instant lastTradeTimestamp) {
        this.symbol = symbol;
        this.lastPrice = lastPrice;
        this.lastTradeTimestamp = lastTradeTimestamp;
    }

    public Ticker() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Instant getLastTradeTimestamp() {
        return lastTradeTimestamp;
    }

    public void setLastTradeTimestamp(Instant lastTradeTimestamp) {
        this.lastTradeTimestamp = lastTradeTimestamp;
    }
}