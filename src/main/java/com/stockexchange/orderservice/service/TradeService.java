package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Trade;
import com.stockexchange.orderservice.model.dto.TradeResponse;
import com.stockexchange.orderservice.repository.TradeRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }


    public Mono<Void> handleTrade(TradeResponse tradeResponse) {
        var trade = new Trade(
                        tradeResponse.tradeId(),
                        tradeResponse.buyOrderId(),
                        tradeResponse.sellOrderId(),
                        tradeResponse.buyerUserId(),
                        tradeResponse.sellerUserId(),
                        tradeResponse.symbol(),
                        tradeResponse.quantity(),
                        tradeResponse.price(),
                        tradeResponse.executedAt());

        return tradeRepository.save(trade).then();
    }
}
