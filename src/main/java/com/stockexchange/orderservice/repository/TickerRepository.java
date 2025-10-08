package com.stockexchange.orderservice.repository;

import com.stockexchange.orderservice.model.Ticker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TickerRepository extends CrudRepository<Ticker, String> {
    Optional<Ticker> findBySymbol(String symbol);
}
