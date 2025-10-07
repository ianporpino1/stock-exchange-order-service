package com.stockexchange.orderservice.service;

import com.stockexchange.orderservice.model.Trade;
import com.stockexchange.orderservice.model.dto.MatchResponse;
import com.stockexchange.orderservice.model.dto.TradeResponse;
import com.stockexchange.orderservice.repository.TradeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    public TradeService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Transactional
    public void handleTrade(MatchResponse matchResponse) {
        if(!matchResponse.trades().isEmpty()){
            List<Trade> trades = new ArrayList<>();
            for(TradeResponse tradeResponse: matchResponse.trades()){
                Trade trade = new Trade(
                        tradeResponse.tradeId(),
                        tradeResponse.buyOrderId(),
                        tradeResponse.sellOrderId(),
                        tradeResponse.buyerUserId(),
                        tradeResponse.sellerUserId(),
                        tradeResponse.symbol(),
                        tradeResponse.quantity(),
                        tradeResponse.price(),
                        tradeResponse.executedAt()
                );
                trades.add(trade);
            }
            tradeRepository.saveAll(trades);
        }
    }
}
