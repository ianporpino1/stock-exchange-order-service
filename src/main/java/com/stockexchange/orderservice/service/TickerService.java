package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Ticker;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.model.dto.TradeResponse;
import com.stockexchange.orderservice.repository.TickerRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TickerService {

    private final TickerRepository tickerRepository;

    public TickerService(TickerRepository tickerRepository) {
        this.tickerRepository = tickerRepository;
    }

    public BigDecimal findLastPriceBySymbol(String symbol){
        return tickerRepository.findBySymbol(symbol).map(Ticker::getLastPrice)
                .orElseThrow(() -> new NoSuchElementException("No ticker found for symbol: " + symbol));
    }

    public void handleTickers(MatchResponse matchResponse) {
        for (TradeResponse trade : matchResponse.trades()) {
            updateLastPrice(trade.symbol(), trade.price(), trade.executedAt());
        }
    }


    public void updateLastPrice(String symbol, BigDecimal newPrice, Instant lastTradeTimestamp) {
        Optional<Ticker> optionalTicker = tickerRepository.findBySymbol(symbol);

        if (optionalTicker.isPresent()) {
            Ticker existingTicker = optionalTicker.get();
            existingTicker.setLastPrice(newPrice);
            existingTicker.setLastTradeTimestamp(lastTradeTimestamp);
            tickerRepository.save(existingTicker);
        } else {
            Ticker newTicker = new Ticker(symbol, newPrice, lastTradeTimestamp);
            tickerRepository.save(newTicker);
        }
    }
}
