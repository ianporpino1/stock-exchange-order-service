package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Trade;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.repository.TradeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }


    public Mono<Void> handleTrade(MatchResponse matchResponse) {
        if (matchResponse.trades().isEmpty()) {
            return Mono.empty();
        }

        List<Trade> trades = matchResponse.trades().stream()
                .map(tradeResponse -> new Trade(
                        tradeResponse.tradeId(),
                        tradeResponse.buyOrderId(),
                        tradeResponse.sellOrderId(),
                        tradeResponse.buyerUserId(),
                        tradeResponse.sellerUserId(),
                        tradeResponse.symbol(),
                        tradeResponse.quantity(),
                        tradeResponse.price(),
                        tradeResponse.executedAt()
                ))
                .toList();
        return tradeRepository.saveAll(trades).then();
    }
}
