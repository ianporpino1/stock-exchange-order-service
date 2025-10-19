package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Ticker;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.repository.TickerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.Instant;

@Service
public class TickerService {

    private final TickerRepository tickerRepository;

    public TickerService(TickerRepository tickerRepository) {
        this.tickerRepository = tickerRepository;
    }

    public Mono<BigDecimal> findLastPriceBySymbol(String symbol){
        return tickerRepository.findTickerBySymbol(symbol)
                .map(Ticker::getLastPrice);
    }

    public Mono<Void> updateLastPrice(String symbol, BigDecimal newPrice, Instant lastTradeTimestamp) {
        return tickerRepository.findTickerBySymbol(symbol)
                .flatMap(ticker -> {
                    ticker.setLastPrice(newPrice);
                    ticker.setLastTradeTimestamp(lastTradeTimestamp);
                    return tickerRepository.save(ticker);
                })
                .switchIfEmpty(Mono.defer(() -> tickerRepository.save(new Ticker(symbol,newPrice, lastTradeTimestamp))))
                .then();
    }

    public Mono<Void> handleTickers(MatchResponse matchResponse) {
        return Flux.fromIterable(matchResponse.trades())
                .flatMap(trade -> updateLastPrice(trade.symbol(), trade.price(), trade.executedAt()))
                .then();
    }
}