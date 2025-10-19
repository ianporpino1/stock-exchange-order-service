package com.stockexchange.orderservice.repository;

import com.stockexchange.orderservice.model.Ticker;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TickerRepository extends ReactiveCrudRepository<Ticker, String> {
    Mono<Ticker> findTickerBySymbol(String symbol);
}
