package com.stockexchange.orderservice.repository;

import com.stockexchange.orderservice.model.Ticker;
import jakarta.annotation.PostConstruct;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.Instant;

@Repository
public class TickerRepository {

    private final RedissonReactiveClient redissonReactiveClient;

    public TickerRepository(RedissonReactiveClient redissonReactiveClient) {
        this.redissonReactiveClient = redissonReactiveClient;
    }

    @PostConstruct
    private void init() {
        System.out.println("Inicializando tickers com chaves individuais...");
        Flux.just(
                        new Ticker("AAPL", BigDecimal.valueOf(200), Instant.now()),
                        new Ticker("MSFT", BigDecimal.valueOf(150), Instant.now()),
                        new Ticker("GOOGL", BigDecimal.valueOf(100), Instant.now())
                )
                .flatMap(this::save)
                .subscribe();
    }

    public Mono<Void> save(Ticker ticker) {
        String key = "/tickers/" + ticker.getSymbol();
        RBucketReactive<Ticker> bucket = redissonReactiveClient.getBucket(key);
        return bucket.set(ticker).then();
    }

    public Mono<Ticker> findTickerBySymbol(String symbol) {
        String key = "/tickers/" + symbol;
        RBucketReactive<Ticker> bucket = redissonReactiveClient.getBucket(key);
        return bucket.get();
    }
}