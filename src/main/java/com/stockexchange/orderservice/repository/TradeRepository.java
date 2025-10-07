package com.stockexchange.orderservice.repository;

import com.stockexchange.orderservice.model.Trade;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TradeRepository extends ReactiveCrudRepository<Trade, UUID> {
}
